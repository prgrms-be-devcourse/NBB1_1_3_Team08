package org.prgrms.devconnect.common.auth.redis

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive
import org.springframework.data.redis.core.index.Indexed


@RedisHash(value = "refreshToken", timeToLive = RefreshToken.TTL)
class RefreshToken(
    @Id
    val refreshToken: String,

    @Indexed
    val userEmail: String,

    @TimeToLive
    private val ttl: Long = TTL
) {

  companion object {
    const val TTL: Long = (60 * 60 * 24 * 14).toLong()
  }
}