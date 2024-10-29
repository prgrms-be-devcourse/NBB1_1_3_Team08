package org.prgrms.devconnect.common.config

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisPassword
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.StringRedisSerializer


@Configuration
class RedisConfig(
    @Value("\${spring.data.redis.host}")
    private val host: String,

    @Value("\${spring.data.redis.port}")
    private val port: Int,

    @Value("\${spring.data.redis.password}")
    private val password: String,
) {
  private val log = LoggerFactory.getLogger(this::class.java)

  @Bean
  fun redisConnectionFactory(): RedisConnectionFactory {
    val config = RedisStandaloneConfiguration(host, port)
    config.password = RedisPassword.of(password)

    log.info("Connected to Redis at {}:{}", host, port)
    return LettuceConnectionFactory(config)
  }

  @Bean
  fun redisTemplate(): RedisTemplate<*, *> {
    val redisTemplate = RedisTemplate<ByteArray, ByteArray>()
    redisTemplate.keySerializer = StringRedisSerializer()
    redisTemplate.valueSerializer = StringRedisSerializer()
    redisTemplate.connectionFactory = redisConnectionFactory()
    return redisTemplate
  }
}