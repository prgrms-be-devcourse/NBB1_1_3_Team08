package org.prgrms.devconnect.common.config

import com.querydsl.jpa.JPQLTemplates
import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class QueryDslConfig (
    @PersistenceContext
    private val em:EntityManager
) {
    @Bean
    fun jpaQueryFactory() :JPAQueryFactory = JPAQueryFactory(JPQLTemplates.DEFAULT, em)
}