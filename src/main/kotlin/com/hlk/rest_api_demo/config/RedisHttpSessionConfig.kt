package com.hlk.rest_api_demo.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.session.Session
import org.springframework.session.SessionRepository
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession
import org.springframework.session.data.redis.config.annotation.web.http.RedisHttpSessionConfiguration
import org.springframework.session.web.http.SessionRepositoryFilter

@Primary
@Configuration
@EnableRedisHttpSession
class RedisHttpSessionConfig : RedisHttpSessionConfiguration() {

    @Autowired
    var redisTemplate: RedisTemplate<Any?, Any?>? = null

    @Bean
    override fun <S : Session?> springSessionRepositoryFilter(sessionRepository: SessionRepository<S>): SessionRepositoryFilter<out Session> {
        return super.springSessionRepositoryFilter(
                SafeDeserializationRepository(
                        sessionRepository,
                        redisTemplate!!
                )
        )
    }

    @Bean
    fun jedisConnectionFactory(): JedisConnectionFactory? {
        return JedisConnectionFactory()
    }
}
