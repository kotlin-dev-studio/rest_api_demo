package com.hlk.rest_api_demo.controllers

import com.hlk.rest_api_demo.config.JwtTokenUtil
import com.hlk.rest_api_demo.model.JwtRequest
import com.hlk.rest_api_demo.model.JwtResponse
import com.hlk.rest_api_demo.model.UserDto
import com.hlk.rest_api_demo.service.JwtUserDetailsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin
class JwtAuthenticationController {
    @Autowired
    private lateinit var authenticationManager: AuthenticationManager

    @Autowired
    private lateinit var jwtTokenUtil: JwtTokenUtil

    @Autowired
    private lateinit var userDetailsService: JwtUserDetailsService

    @RequestMapping(value = ["/authenticate"], method = [RequestMethod.POST])
    @Throws(Exception::class)
    fun createAuthenticationToken(@RequestBody authenticationRequest: JwtRequest): ResponseEntity<*> {
        authenticate(authenticationRequest.username ?:  "", authenticationRequest.password ?: "")
        val userDetails = userDetailsService.loadUserByUsername(authenticationRequest.username ?: "")
        val token = jwtTokenUtil.generateToken(userDetails)
        return ResponseEntity.ok(JwtResponse(token))
    }

    @RequestMapping(value = ["/register"], method = [RequestMethod.POST])
    @Throws(Exception::class)
    fun saveUser(@RequestBody user: UserDto): ResponseEntity<*> {
        return ResponseEntity.ok(userDetailsService.save(user))
    }

    @Throws(Exception::class)
    private fun authenticate(username: String, password: String) {
        try {
            authenticationManager.authenticate(UsernamePasswordAuthenticationToken(username, password))
        } catch (e: DisabledException) {
            throw Exception("USER_DISABLED", e)
        } catch (e: BadCredentialsException) {
            throw Exception("INVALID_CREDENTIALS", e)
        }
    }
}
