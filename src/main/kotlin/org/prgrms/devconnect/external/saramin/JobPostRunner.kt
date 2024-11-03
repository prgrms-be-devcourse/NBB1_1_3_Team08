package org.prgrms.devconnect.external.saramin

import org.prgrms.devconnect.api.service.jobpost.JobPostCommandService
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class JobPostRunner(
        private val jobPostCommandService: JobPostCommandService
) : ApplicationRunner {

    // 애플리케이션 실행 시 호출되는 메서드
    @Throws(Exception::class)
    override fun run(args: ApplicationArguments?) {
//         jobPostCommandService.saveJobPosts()
    }
}
