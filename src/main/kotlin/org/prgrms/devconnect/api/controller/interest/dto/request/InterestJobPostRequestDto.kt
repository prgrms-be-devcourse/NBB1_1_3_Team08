package org.prgrms.devconnect.api.controller.interest.dto.request

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull
import org.prgrms.devconnect.domain.interest.entity.InterestJobPost
import org.prgrms.devconnect.domain.jobpost.entity.JobPost
import org.prgrms.devconnect.domain.member.entity.Member

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
@Schema(description = "관심 채용 공고 등록 요청 정보")
class InterestJobPostRequestDto(
  @field:NotNull(message = "채용공고 ID는 필수입니다.")
  @Schema(description = "채용공고 ID", example = "1")
  val jobPostId: Long
) {

  fun toEntity(member: Member, jobPost: JobPost): InterestJobPost {
    return InterestJobPost(
      member = member,
      jobPost = jobPost
    )
  }

}
