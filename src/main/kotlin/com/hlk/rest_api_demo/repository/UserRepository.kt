package com.hlk.rest_api_demo.repository

import com.hlk.rest_api_demo.model.UserDao
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<UserDao, Long> {
    fun findByUsername(username: String?): UserDao
}
