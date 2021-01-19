package com.hlk.rest_api_demo.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.hlk.rest_api_demo.config.JwtTokenUtil
import com.hlk.rest_api_demo.controllers.JwtAuthenticationController
import com.hlk.rest_api_demo.model.JwtRequest
import com.hlk.rest_api_demo.model.UserDao
import com.hlk.rest_api_demo.model.UserDto
import com.hlk.rest_api_demo.service.JwtUserDetailsService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.util.ArrayList

@ExtendWith(SpringExtension::class)
@WebMvcTest(JwtAuthenticationController::class)
@AutoConfigureMockMvc(addFilters = false)
internal class JwtAuthenticationControllerTest {

    @MockkBean
    lateinit var mockUserDetailsService: JwtUserDetailsService

    @MockkBean
    lateinit var mockAuthenticationManager: AuthenticationManager

    @MockkBean
    lateinit var mockJwtTokenUtil: JwtTokenUtil

    @Autowired
    private lateinit var mockMvc: MockMvc

    private val mapper = jacksonObjectMapper()

    @Test
    fun `when a new user register then response username`() {
        val usernameTest = "test_user"
        val passwordTest = "1234"

        val userDto = UserDto()
        userDto.username = usernameTest
        userDto.password = passwordTest

        val newUser = UserDao(1L, "test_user", "1234")

        every { mockUserDetailsService.save(any()) } returns newUser

        mockMvc.perform(
            MockMvcRequestBuilders.post("/register")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(userDto))
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(
                MockMvcResultMatchers.jsonPath("\$.username")
                    .value(usernameTest)
            ).andReturn()

        verify { mockUserDetailsService.save(any()) }
    }

    @Nested
    inner class Authenticate {
        private val usernameTest = "test_user"
        private val passwordTest = "1234"

        private val authenticationRequest = JwtRequest().apply {
            this.username = usernameTest
            this.password = passwordTest
        }

        @Test
        fun `when user is not registered`() {
            every { mockAuthenticationManager.authenticate(any()) } throws Exception("INVALID_CREDENTIALS")
            performAuthenticate(authenticationRequest)
                .andExpect(MockMvcResultMatchers.status().`is`(400))
                .andReturn()
        }

        @Test
        fun `when user is registered`() {
            every { mockUserDetailsService.loadUserByUsername(usernameTest) } returns User(
                usernameTest, passwordTest,
                ArrayList()
            )
            performAuthenticate(authenticationRequest)
//                .andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()

            every { mockUserDetailsService.loadUserByUsername(usernameTest) }
        }
    }

    private fun performAuthenticate(authenticationRequest: JwtRequest) =
        mockMvc.perform(
            MockMvcRequestBuilders.post("/authenticate")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(authenticationRequest))
                .accept(MediaType.APPLICATION_JSON)
        )
}
