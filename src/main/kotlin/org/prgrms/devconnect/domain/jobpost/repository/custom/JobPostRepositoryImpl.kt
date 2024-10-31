package org.prgrms.devconnect.domain.jobpost.repository.custom

import com.querydsl.jpa.impl.JPAQueryFactory
import org.prgrms.devconnect.api.controller.jobpost.dto.response.JobPostInfoResponseDto
import org.prgrms.devconnect.api.controller.techstack.dto.response.TechStackResponseDto
import org.prgrms.devconnect.domain.jobpost.entity.QJobPost
import org.prgrms.devconnect.domain.jobpost.entity.QJobPostTechStackMapping
import org.prgrms.devconnect.domain.jobpost.entity.constant.Status
import org.prgrms.devconnect.domain.member.entity.Member
import org.prgrms.devconnect.domain.member.entity.MemberTechStackMapping
import org.prgrms.devconnect.domain.techstack.entity.QTechStack
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.support.PageableExecutionUtils

class JobPostRepositoryImpl(
        private val queryFactory: JPAQueryFactory
) : JobPostRepositoryCustom {

    override fun findAllRecruiting(pageable: Pageable): Page<JobPostInfoResponseDto> {
        val jobPost = QJobPost.jobPost

        val content = queryFactory
                .selectFrom(jobPost)
                .where(jobPost.status.eq(Status.RECRUITING))
                .offset(pageable.offset)
                .limit(pageable.pageSize.toLong())
                .fetch()
                .map { post ->
                    JobPostInfoResponseDto(
                            jobPostId = post.jobPostId!!,
                            postId = post.postId!!,
                            jobPostName = post.jobPostName,
                            companyName = post.companyName,
                            companyAddress = post.companyAddress,
                            companyLink = post.companyLink,
                            jobType = post.jobType,
                            status = post.status,
                            postDate = post.postDate,
                            openDate = post.openDate,
                            endDate = post.endDate,
                            salary = post.salary,
                            likes = post.likes,
                            views = post.views,
                            techStacks = post.jobPostTechStackMappings.map { mapping ->
                                TechStackResponseDto.from(mapping.techStack)
                            }
                    )
                }

        val countQuery = queryFactory
                .selectFrom(jobPost)
                .where(jobPost.status.eq(Status.RECRUITING))

        return PageableExecutionUtils.getPage(content, pageable) { countQuery.fetchCount() }
    }

    override fun findAllByTechStackName(techStackName: String, pageable: Pageable): Page<JobPostInfoResponseDto> {
        val jobPost = QJobPost.jobPost
        val mapping = QJobPostTechStackMapping.jobPostTechStackMapping
        val techStack = QTechStack.techStack

        val content = queryFactory
                .selectFrom(jobPost)
                .join(jobPost.jobPostTechStackMappings, mapping)
                .join(mapping.techStack, techStack)
                .where(techStack.name.eq(techStackName).and(jobPost.status.eq(Status.RECRUITING)))
                .offset(pageable.offset)
                .limit(pageable.pageSize.toLong())
                .fetch()
                .map { post ->
                    JobPostInfoResponseDto(
                            jobPostId = post.jobPostId!!,
                            postId = post.postId!!,
                            jobPostName = post.jobPostName,
                            companyName = post.companyName,
                            companyAddress = post.companyAddress,
                            companyLink = post.companyLink,
                            jobType = post.jobType,
                            status = post.status,
                            postDate = post.postDate,
                            openDate = post.openDate,
                            endDate = post.endDate,
                            salary = post.salary,
                            likes = post.likes,
                            views = post.views,
                            techStacks = post.jobPostTechStackMappings.map { mapping ->
                                TechStackResponseDto.from(mapping.techStack)
                            }
                    )
                }

        val countQuery = queryFactory
                .selectFrom(jobPost)
                .join(jobPost.jobPostTechStackMappings, mapping)
                .join(mapping.techStack, techStack)
                .where(techStack.name.eq(techStackName).and(jobPost.status.eq(Status.RECRUITING)))

        return PageableExecutionUtils.getPage(content, pageable) { countQuery.fetchCount() }
    }

    override fun findAllByTechStackJobCode(jobCode: String, pageable: Pageable): Page<JobPostInfoResponseDto> {
        val jobPost = QJobPost.jobPost
        val mapping = QJobPostTechStackMapping.jobPostTechStackMapping
        val techStack = QTechStack.techStack

        val content = queryFactory
                .selectFrom(jobPost)
                .join(jobPost.jobPostTechStackMappings, mapping)
                .join(mapping.techStack, techStack)
                .where(techStack.code.eq(jobCode).and(jobPost.status.eq(Status.RECRUITING)))
                .offset(pageable.offset)
                .limit(pageable.pageSize.toLong())
                .fetch()
                .map { post ->
                    JobPostInfoResponseDto(
                            jobPostId = post.jobPostId!!,
                            postId = post.postId!!,
                            jobPostName = post.jobPostName,
                            companyName = post.companyName,
                            companyAddress = post.companyAddress,
                            companyLink = post.companyLink,
                            jobType = post.jobType,
                            status = post.status,
                            postDate = post.postDate,
                            openDate = post.openDate,
                            endDate = post.endDate,
                            salary = post.salary,
                            likes = post.likes,
                            views = post.views,
                            techStacks = post.jobPostTechStackMappings.map { mapping ->
                                TechStackResponseDto.from(mapping.techStack)
                            }
                    )
                }

        val countQuery = queryFactory
                .selectFrom(jobPost)
                .join(jobPost.jobPostTechStackMappings, mapping)
                .join(mapping.techStack, techStack)
                .where(techStack.code.eq(jobCode).and(jobPost.status.eq(Status.RECRUITING)))

        return PageableExecutionUtils.getPage(content, pageable) { countQuery.fetchCount() }
    }

    override fun findAllByJobPostNameContaining(keyword: String, pageable: Pageable): Page<JobPostInfoResponseDto> {
        val jobPost = QJobPost.jobPost

        val content = queryFactory
                .selectFrom(jobPost)
                .where(jobPost.jobPostName.contains(keyword).and(jobPost.status.eq(Status.RECRUITING)))
                .offset(pageable.offset)
                .limit(pageable.pageSize.toLong())
                .fetch()
                .map { post ->
                    JobPostInfoResponseDto(
                            jobPostId = post.jobPostId!!,
                            postId = post.postId!!,
                            jobPostName = post.jobPostName,
                            companyName = post.companyName,
                            companyAddress = post.companyAddress,
                            companyLink = post.companyLink,
                            jobType = post.jobType,
                            status = post.status,
                            postDate = post.postDate,
                            openDate = post.openDate,
                            endDate = post.endDate,
                            salary = post.salary,
                            likes = post.likes,
                            views = post.views,
                            techStacks = post.jobPostTechStackMappings.map { mapping ->
                                TechStackResponseDto.from(mapping.techStack)
                            }
                    )
                }

        val countQuery = queryFactory
                .selectFrom(jobPost)
                .where(jobPost.jobPostName.contains(keyword).and(jobPost.status.eq(Status.RECRUITING)))

        return PageableExecutionUtils.getPage(content, pageable) { countQuery.fetchCount() }
    }

    override fun findAllByMemberInterests(member: Member, pageable: Pageable): Page<JobPostInfoResponseDto> {
        val jobPost = QJobPost.jobPost
        val jobPostMapping = QJobPostTechStackMapping.jobPostTechStackMapping
        val techStack = QTechStack.techStack

        val memberTechStacks = member.memberTechStacks.map(MemberTechStackMapping::techStack)

        val content = queryFactory
                .selectFrom(jobPost)
                .join(jobPost.jobPostTechStackMappings, jobPostMapping)
                .join(jobPostMapping.techStack, techStack)
                .where(techStack.`in`(memberTechStacks).and(jobPost.status.eq(Status.RECRUITING)))
                .offset(pageable.offset)
                .limit(pageable.pageSize.toLong())
                .fetch()
                .map { post ->
                    JobPostInfoResponseDto(
                            jobPostId = post.jobPostId!!,
                            postId = post.postId!!,
                            jobPostName = post.jobPostName,
                            companyName = post.companyName,
                            companyAddress = post.companyAddress,
                            companyLink = post.companyLink,
                            jobType = post.jobType,
                            status = post.status,
                            postDate = post.postDate,
                            openDate = post.openDate,
                            endDate = post.endDate,
                            salary = post.salary,
                            likes = post.likes,
                            views = post.views,
                            techStacks = post.jobPostTechStackMappings.map { mapping ->
                                TechStackResponseDto.from(mapping.techStack)
                            }
                    )
                }

        val countQuery = queryFactory
                .selectFrom(jobPost)
                .join(jobPost.jobPostTechStackMappings, jobPostMapping)
                .join(jobPostMapping.techStack, techStack)
                .where(techStack.`in`(memberTechStacks).and(jobPost.status.eq(Status.RECRUITING)))

        return PageableExecutionUtils.getPage(content, pageable) { countQuery.fetchCount() }
    }
}
