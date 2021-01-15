package com.hlk.rest_api_demo.service

import com.hlk.rest_api_demo.model.UserDao
import com.hlk.rest_api_demo.model.UserDto
import com.hlk.rest_api_demo.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.ArrayList

@Service
class JwtUserDetailsService : UserDetailsService {
    @Autowired
    private lateinit var userDao: UserRepository

    @Autowired
    private lateinit var bcryptEncoder: PasswordEncoder

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        val user: UserDao = userDao.findByUsername(username)
            ?: throw UsernameNotFoundException("User not found with username: $username")
        return User(
            user.username, user.password,
            ArrayList()
        )
    }

    fun save(user: UserDto): UserDao {
        val newUser = UserDao()
        newUser.username = user.username
        newUser.password = bcryptEncoder.encode(user.password)
        return userDao.save(newUser)
    }
}
