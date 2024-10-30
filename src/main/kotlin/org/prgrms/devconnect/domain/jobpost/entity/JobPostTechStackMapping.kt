package org.prgrms.devconnect.domain.jobpost.entity


import jakarta.persistence.*
import org.prgrms.devconnect.domain.techstack.entity.TechStack

@Entity
@Table(name = "job_post_tech_stack_mapping")
class JobPostTechStackMapping(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        var id: Long? = null,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "job_post_id", nullable = false)
        var jobPost: JobPost,

        @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST])
        @JoinColumn(name = "tech_stack_id", nullable = false)
        var techStack: TechStack
)
