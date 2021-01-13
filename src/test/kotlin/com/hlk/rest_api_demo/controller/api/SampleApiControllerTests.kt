package com.hlk.rest_api_demo.controller.api

import com.hlk.rest_api_demo.controller.sample.api.SampleApiController
import com.hlk.rest_api_demo.model.CustomUserDetails
import com.hlk.rest_api_demo.model.User
import com.hlk.rest_api_demo.service.JwtTokenProvider
import com.hlk.rest_api_demo.service.UserService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.security.Principal
import javax.sql.DataSource

@ActiveProfiles("test")
@WebMvcTest(SampleApiController::class)
//@ContextConfiguration(classes = [SampleApiController::class, AppProps::class, JwtTokenProvider::class, UserService::class])
//@ContextConfiguration(classes = [SampleApiController::class, AppProps::class])
internal class SampleApiControllerTests {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var userService: UserService

    @MockkBean
    private lateinit var dataSource: DataSource

    private val username = "user_test"

    private val mockPrincipal = mockk<Principal> {
        every { name } returns username
    }

    @Test
    @DisplayName("Should response Unauthorized")
    fun shouldResponseErrorUnauthorized() {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/appProps").accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isUnauthorized)
            .andExpect(MockMvcResultMatchers.status().reason("Error: Unauthorized"))
    }

    @Test
    @DisplayName("Should response AppProps properties")
    fun shouldResponseAppPropsProperties() {
        val user = User(1, "username", "email", "123", "ROLE_USER")
//        userService.createUser(user)
        val jwtTokenProvider = mockk<JwtTokenProvider>()
        val token = "token-SHA256"
        every { jwtTokenProvider.generateToken(CustomUserDetails(user)) } returns token

        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/appProps").principal(mockPrincipal)
                .header("authorization", "Bearer $token")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
    }
}
