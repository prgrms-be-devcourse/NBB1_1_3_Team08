package org.prgrms.devconnect

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling // 스케줄러 활성화
@EnableAspectJAutoProxy
@EnableAsync(proxyTargetClass = true)
class DevconnectApplication

fun main(args: Array<String>) {
  runApplication<DevconnectApplication>(*args)
}
