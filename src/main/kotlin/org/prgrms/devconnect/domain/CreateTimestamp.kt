package org.prgrms.devconnect.domain

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class CreateTimestamp {
  @CreatedDate
  @Column(name = "created_at", updatable = false)
  var createdAt: LocalDateTime? = null

}