package com.hlk.rest_api_demo.repository

import com.hlk.rest_api_demo.model.UserDao
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : CrudRepository<UserDao?, Int?> {
    fun findByUsername(username: String?): UserDao?
}
