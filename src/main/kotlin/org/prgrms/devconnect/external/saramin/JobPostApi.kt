package org.prgrms.devconnect.external.saramin

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.prgrms.devconnect.api.service.jobpost.JobPostQueryService
import org.prgrms.devconnect.api.service.techstack.TechStackQueryService
import org.prgrms.devconnect.domain.jobpost.entity.JobPost
import org.prgrms.devconnect.domain.jobpost.entity.JobPostTechStackMapping
import org.prgrms.devconnect.domain.jobpost.entity.constant.JobType
import org.prgrms.devconnect.domain.jobpost.entity.constant.Status
import org.prgrms.devconnect.domain.techstack.entity.TechStack
import org.prgrms.devconnect.external.saramin.dto.response.CompanyHtmlResponse
import org.prgrms.devconnect.external.saramin.dto.response.JobSearchResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@Component
class JobPostApi(
  private val jobPostQueryService: JobPostQueryService,
  private val techStackQueryService: TechStackQueryService
) {

  @Value("\${api.key}")
  lateinit var accessKey: String

  final var total: Int = 0
    private set

  fun fetchJobPosts(start: Int): List<JobPost> {
    val jobPosts = mutableListOf<JobPost>()
    val allTechStacks = techStackQueryService.getAll()

    val techStackMap = allTechStacks.associateBy { it.name }

    try {
      val encodedKeyword = URLEncoder.encode("", StandardCharsets.UTF_8)
      val apiURL =
        "https://oapi.saramin.co.kr/job-search?access-key=$accessKey&job_cd=2232+84+92+291+277+213+235+236+302+272+87+195&job_mid_cd=2&start=$start&count=100"
      val response = xmlParser(apiURL)
      val xmlMapper = XmlMapper()
      val jobSearchResponse =
        xmlMapper.readValue(response.toString(), JobSearchResponse::class.java)

      jobSearchResponse.jobs?.jobList?.forEach { job ->
        val postId = job.id
        if (!jobPostQueryService.isJobPostByPostId(postId)) {
          val companyPageLink = extractCompanyInfoFromXml(response.toString(), postId)
          val companyInfo = extractCompanyInfoFromHtml(companyPageLink)

          val jobPost = JobPost(
            postId = postId,
            jobPostName = job.position?.title ?: "",
            jobPostLink = job.url ?: "",
            companyName = job.company?.name ?: "",
            companyLink = companyInfo.homepage,
            companyAddress = companyInfo.address,
            postDate = convertLocalDateTime(job.postingTimestamp),
            openDate = convertLocalDateTime(job.openingTimestamp),
            endDate = convertLocalDateTime(job.expirationTimestamp),
            experienceLevel = job.position?.experienceLevel ?: "",
            requiredEducation = job.position?.requiredEducation ?: "",
            salary = job.salary ?: "",
            jobType = convertJobType(job.position?.jobType ?: ""),
            status = Status.RECRUITING,
            likes = 0,
            views = 0
          )

          val techStacks =
            saveTechStackByJobCode(techStackMap, job.position?.jobCode) // JobCode에 따라 TechStack 리스트 생성

          techStacks.forEach { techStack ->
            val mapping = JobPostTechStackMapping(jobPost = jobPost, techStack = techStack)
            jobPost.addTechStackMapping(mapping) // 매핑 추가
          }

          jobPosts.add(jobPost) // jobPost 리스트에 추가
        }
      }
    } catch (e: Exception) {
      println(e)
    }
    return jobPosts
  }

  fun getTotal(start: Int): Int {
    return try {
      val encodedKeyword = URLEncoder.encode("", StandardCharsets.UTF_8)
      val apiURL =
        "https://oapi.saramin.co.kr/job-search?access-key=$accessKey&job_cd=2232+84+92+291+277+213+235+236+302+272+87+195&job_mid_cd=2&start=$start&count=1"
      val response = xmlParser(apiURL)
      val xmlMapper = XmlMapper()
      val jobSearchResponse =
        xmlMapper.readValue(response.toString(), JobSearchResponse::class.java)

      total = jobSearchResponse.jobs?.total ?: 0 // jobs가 null일 경우 0을 반환
      total
    } catch (e: Exception) {
      println(e)
      0
    }
  }


  private fun xmlParser(apiURL: String): StringBuilder {
    val url = URL(apiURL)
    val con = url.openConnection() as HttpURLConnection
    con.requestMethod = "GET"
    con.setRequestProperty("Accept", "application/xml")

    val br = if (con.responseCode == 200) BufferedReader(InputStreamReader(con.inputStream))
    else BufferedReader(InputStreamReader(con.errorStream))

    return StringBuilder().apply {
      br.useLines { lines -> lines.forEach { append(it) } }
    }
  }

  private fun extractCompanyInfoFromHtml(companyPageLink: String?): CompanyHtmlResponse {
    var homepage: String? = null
    var address: String? = null

    try {
      if (companyPageLink != null) {
        val companyPage = Jsoup.connect(companyPageLink)
          .timeout(10000)
          .followRedirects(true)
          .get()
        homepage = companyPage.selectFirst("dd.desc a[target=_blank]")?.attr("href")
        address =
          companyPage.selectFirst("div.company_details_group dt.tit:contains(주소) + dd.desc p.ellipsis")
            ?.attr("title")?.trim()
      }
    } catch (e: Exception) {
      println(e)
    }

    return CompanyHtmlResponse(homepage ?: "", address ?: "")
  }

  private fun extractCompanyInfoFromXml(xmlResponse: String, postId: Long): String? {
    val document: Document = Jsoup.parse(xmlResponse, "", org.jsoup.parser.Parser.xmlParser())
    val jobElement = document.selectFirst("job:has(id:contains($postId))")
    return jobElement?.selectFirst("company > name")?.attr("href")
  }

  private fun convertLocalDateTime(timestamp: Long): LocalDateTime =
    LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.systemDefault())

  private fun convertJobType(jobTypeCode: String): JobType =
    when (jobTypeCode.split(",").first().trim()) {
      "정규직" -> JobType.REGULAR
      "인턴" -> JobType.CONTRACT
      else -> JobType.IRREGULAR
    }

  fun saveTechStackByJobCode(techStackMap: Map<String, TechStack>, jobCode: String?): List<TechStack> {
    return jobCode?.split(",")?.mapNotNull { techStackMap[it.trim()] } ?: emptyList()
  }
}
