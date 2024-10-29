package org.prgrms.devconnect.common.auth.redis

import org.springframework.data.repository.CrudRepository


interface RefreshTokenRepository : CrudRepository<RefreshToken, Long> {

  fun findByRefreshToken(refreshToken: String): RefreshToken?

  fun findByUserEmail(userEmail: String): RefreshToken?
}