package com.hlk.rest_api_demo.config

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.SerializationException
import org.springframework.session.Session
import org.springframework.session.SessionRepository

/** Repository corresponding to session value deserialization failure
 * see https://sadique.io/blog/2016/11/02/handling-deserialization-errors-in-spring-redis-sessions/
 */

class SafeDeserializationRepository<S : Session?>(
        private val repository: SessionRepository<S>,
        private val redisTemplate: RedisTemplate<Any?, Any?>
) : SessionRepository<S> {

    private val log: Log = LogFactory.getLog(this::class.java)

    override fun createSession(): S? = repository.createSession()

    override fun save(session: S) = repository.save(session)

    override fun deleteById(id: String?) = repository.deleteById(id)

    override fun findById(id: String?): S? = try {
        repository.findById(id)
    } catch (e: SerializationException) {
        // Delete session that failed to deserialize
        log.info("Deleting non-deserializable session with key: $id")
        redisTemplate.delete(BOUNDED_HASH_KEY_PREFIX + id)
        null
    }
}

private const val BOUNDED_HASH_KEY_PREFIX = "spring:session:sessions:"
