package org.prgrms.devconnect.domain

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class Timestamp : CreateTimestamp() {
  @LastModifiedDate
  @Column(name = "updated_at")
  var updatedAt: LocalDateTime? = null
}