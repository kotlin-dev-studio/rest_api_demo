package com.hlk.rest_api_demo.controller

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest
@ContextConfiguration(classes = [HomeController::class])
class HomeControllerTests(
        @Autowired
        val mockMvc: MockMvc
) {

    @Test
    @DisplayName("Should response json status Living")
    fun shouldResponseStatusLiving() {
        mockMvc.perform(MockMvcRequestBuilders.get("/").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("\$.status").value("Living"))
    }

    @Nested
    inner class ResultResponse {
        @Test
        fun `When not provide request param type`() {
            mockMvc.perform(MockMvcRequestBuilders.get("/resultResponse").accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest)
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.jsonPath("\$.result").value(false))
                    .andExpect(
                            MockMvcResultMatchers.jsonPath("\$.message")
                                    .value("Please provide type result with success or failure")
                    )
        }

        @Test
        fun `When request param type is success`() {
            mockMvc.perform(MockMvcRequestBuilders.get("/resultResponse?type=success").accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk)
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.jsonPath("\$.result").value(true))
                    .andExpect(
                            MockMvcResultMatchers.jsonPath("\$.message")
                                    .value("Success message!")
                    )
        }

        @Test
        fun `When request param type is failure`() {
            mockMvc.perform(MockMvcRequestBuilders.get("/resultResponse?type=failure").accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity)
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.jsonPath("\$.result").value(false))
                    .andExpect(
                            MockMvcResultMatchers.jsonPath("\$.message")
                                    .value("Failure message!")
                    )
        }
    }

}
//https://phauer.com/2018/best-practices-unit-testing-kotlin/
