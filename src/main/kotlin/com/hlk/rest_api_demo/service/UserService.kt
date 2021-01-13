package com.hlk.rest_api_demo.service

import com.hlk.rest_api_demo.model.CustomUserDetails
import com.hlk.rest_api_demo.model.User
import com.hlk.rest_api_demo.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service


@Service
class UserService : UserDetailsService {
    private lateinit var userRepository: UserRepository

    override fun loadUserByUsername(username: String): UserDetails {
        // Kiểm tra xem user có tồn tại trong database không?
        val user = userRepository.findByUsername(username)!!
        return CustomUserDetails(user)
    }

    fun loadUserById(id: Long): UserDetails {
        val user = userRepository.findByIdOrNull(id)
        return CustomUserDetails(user!!)
    }

    fun createUser(user: User): User {
        userRepository.save(user)
        return user
    }
}
