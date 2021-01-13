package com.hlk.rest_api_demo

import com.hlk.rest_api_demo.model.User
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Primary
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = [RestApiDemoApplication::class])
@ComponentScan(basePackages = ["com.hlk.rest_api_demo"])
class RestApiDemoApplicationTests {

    @Test
    fun contextLoads() {
    }

}

//@TestConfiguration
//class SpringSecurityTestConfig {
//    @Bean
//    @Primary
//    fun userDetailsService() {
//        val user = User(1, username = "test", password = "123", role = "ROLE_USER")
//    }
//}
