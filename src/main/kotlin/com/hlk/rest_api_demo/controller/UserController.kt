package com.hlk.rest_api_demo.controller

import com.hlk.rest_api_demo.model.CustomUserDetails
import com.hlk.rest_api_demo.model.OauthAccessToken
import com.hlk.rest_api_demo.model.User
import com.hlk.rest_api_demo.repository.AccessTokenRepository
import com.hlk.rest_api_demo.repository.UserRepository
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import com.hlk.rest_api_demo.service.JwtTokenProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController


@RestController
class UserController {
    @Autowired
    lateinit var authenticationManager: AuthenticationManager

    @Autowired
    lateinit var tokenProvider: JwtTokenProvider

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var accessTokenRepository: AccessTokenRepository

    @PostMapping("/login")
    fun authenticateUser(@RequestBody user: User): OauthAccessToken {

        // Xác thực từ username và password.
        val authentication: Authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                        user.username,
                        user.password
                )
        )

        // Nếu không xảy ra exception tức là thông tin hợp lệ
        // Set thông tin authentication vào Security Context
        SecurityContextHolder.getContext().authentication = authentication

        // Trả về jwt cho người dùng.
        @Suppress("NAME_SHADOWING") val user = authentication.principal as CustomUserDetails
        val jwt = tokenProvider.generateToken(user)
        val refresh = tokenProvider.doGenerateRefreshToken(user)!!
        return accessTokenRepository.save(
                OauthAccessToken(
                        access_token = jwt,
                        refresh_token = refresh,
                        expires_in = tokenProvider.EXPIRATION_TIME,
                        resource_owner = user.getId(),
                        token_type = tokenProvider.TOKEN_PREFIX
                )
        )
    }

    @PostMapping("/signup")
    fun signUp(@RequestBody user: User) {
        val bCryptPasswordEncoder = BCryptPasswordEncoder()
        user.password = bCryptPasswordEncoder.encode(user.password)
        userRepository.save(User(username = user.username, password = user.password, role = user.role))
    }
}
