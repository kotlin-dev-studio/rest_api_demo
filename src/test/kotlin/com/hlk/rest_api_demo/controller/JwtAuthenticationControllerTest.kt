package com.hlk.rest_api_demo.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.hlk.rest_api_demo.config.JwtTokenUtil
import com.hlk.rest_api_demo.controllers.JwtAuthenticationController
import com.hlk.rest_api_demo.model.UserDao
import com.hlk.rest_api_demo.model.UserDto
import com.hlk.rest_api_demo.service.JwtUserDetailsService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@ExtendWith(SpringExtension::class)
@WebMvcTest(JwtAuthenticationController::class)
@AutoConfigureMockMvc(addFilters = false)
internal class JwtAuthenticationControllerTest {

    @MockkBean(relaxed = true)
    lateinit var mockUserDetailsService: JwtUserDetailsService

    @MockkBean
    lateinit var mockJwtTokenUtil: JwtTokenUtil

    @Autowired
    private lateinit var mockMvc: MockMvc

    private val mapper = jacksonObjectMapper()

    @Test
    fun `when a new user register then response username`() {
        val username = "test_user"
        val password = "1234"

        val userDto = UserDto()
        userDto.username = username
        userDto.password = password

        val newUser = UserDao(1L, "test_user", "1234")


        every { mockUserDetailsService.save(userDto) } returns newUser

        mockMvc.perform(
            MockMvcRequestBuilders.post("/register")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapper.writeValueAsString(userDto))
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(
                MockMvcResultMatchers.jsonPath("\$.username")
                    .value(username)
            ).andReturn()

        verify { mockUserDetailsService.save(userDto) }
    }
}
