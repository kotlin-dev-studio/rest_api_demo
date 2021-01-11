package com.hlk.rest_api_demo.service

import com.hlk.rest_api_demo.model.CustomUserDetails
import com.hlk.rest_api_demo.model.User
import com.hlk.rest_api_demo.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service


@Service
class UserService(private val repository: UserRepository) : UserDetailsService {

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        val user: User = repository.findByUsername(username)
        return CustomUserDetails(user)
    }
}
