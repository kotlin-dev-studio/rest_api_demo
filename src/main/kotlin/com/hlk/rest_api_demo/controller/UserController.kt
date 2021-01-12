package com.hlk.rest_api_demo.controller

import com.hlk.rest_api_demo.common.security.jwt.JwtTokenProvider
import com.hlk.rest_api_demo.model.CustomUserDetails
import com.hlk.rest_api_demo.model.OauthAccessToken
import com.hlk.rest_api_demo.model.User
import com.hlk.rest_api_demo.payload.MessageResponse
import com.hlk.rest_api_demo.repository.AccessTokenRepository
import com.hlk.rest_api_demo.repository.UserRepository
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(private val authenticationManager: AuthenticationManager,
                     private val tokenProvider: JwtTokenProvider?,
                     private val userRepository: UserRepository,
                     private val accessTokenRepository: AccessTokenRepository) {
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
        val jwt = tokenProvider!!.generateToken(user)
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
    fun signUp(@RequestBody user: User): ResponseEntity<MessageResponse> {
        if (userRepository.existsByUsername(user.username)!!) {
            return ResponseEntity
                    .badRequest()
                    .body(MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(user.email)!!) {
            return ResponseEntity
                    .badRequest()
                    .body(MessageResponse("Error: Email is already in use!"));
        }

        val bCryptPasswordEncoder = BCryptPasswordEncoder()
        user.password = bCryptPasswordEncoder.encode(user.password)
        userRepository.save(User(username = user.username, password = user.password, role = user.role, email = user.email))

        return ResponseEntity.ok(MessageResponse("User registered successfully!"));
    }
}
