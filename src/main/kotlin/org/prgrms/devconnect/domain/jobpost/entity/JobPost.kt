package org.prgrms.devconnect.domain.jobpost.entity

import jakarta.persistence.*
import org.prgrms.devconnect.domain.jobpost.entity.constant.JobType
import org.prgrms.devconnect.domain.jobpost.entity.constant.Status
import java.time.LocalDateTime

@Entity
@Table(name = "job_post")
class JobPost(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "job_post_id")
        var jobPostId: Long? = null,

        @Column(name = "post_id")
        var postId: Long,

        @Column(name = "job_post_name")
        var jobPostName: String,

        @Column(name = "job_post_link")
        var jobPostLink: String,

        @Column(name = "company_name")
        var companyName: String,

        @Column(name = "company_link")
        var companyLink: String,

        @Column(name = "company_address")
        var companyAddress: String,

        @Column(name = "post_date")
        var postDate: LocalDateTime,

        @Column(name = "open_date")
        var openDate: LocalDateTime,

        @Column(name = "end_date")
        var endDate: LocalDateTime,

        @Column(name = "experience_level")
        var experienceLevel: String,

        @Column(name = "required_education")
        var requiredEducation: String,

        @Column(name = "salary")
        var salary: String,

        @Enumerated(EnumType.STRING)
        @Column(name = "job_type")
        var jobType: JobType,

        @Enumerated(EnumType.STRING)
        @Column(name = "status")
        var status: Status,

        @Column(name = "likes")
        var likes: Int = 0,

        @Column(name = "views")
        var views: Int = 0,

        jobPostTechStackMappings: List<JobPostTechStackMapping> = mutableListOf()
) {

    @OneToMany(mappedBy = "jobPost", cascade = [CascadeType.PERSIST])
    var jobPostTechStackMappings: MutableList<JobPostTechStackMapping> = ArrayList(jobPostTechStackMappings)

    // 연관관계 편의 메서드
    fun addTechStackMapping(mapping: JobPostTechStackMapping) {
        jobPostTechStackMappings.add(mapping)
    }

    // 상태 변경 메서드
    fun updateStatus(newStatus: Status) {
        this.status = newStatus
    }

    // 조회수 증가
    fun increaseViews() {
        this.views += 1
    }

    // 좋아요 수 증가
    fun increaseLikes() {
        this.likes += 1
    }

    // 좋아요 수 감소
    fun decreaseLikes() {
        if (likes > 0) this.likes -= 1
    }
}
