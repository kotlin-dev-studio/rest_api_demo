package com.hlk.rest_api_demo.repository

import com.hlk.rest_api_demo.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User?, Long?> {
    fun findByUsername(username: String?): User?

    fun existsByUsername(username: String?): Boolean?

    fun existsByEmail(email: String?): Boolean?
}
