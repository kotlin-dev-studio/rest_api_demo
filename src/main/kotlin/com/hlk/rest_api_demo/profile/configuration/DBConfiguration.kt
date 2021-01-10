package com.hlk.rest_api_demo.profile.configuration

import com.hlk.rest_api_demo.config.AppProps
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
@ConfigurationProperties("spring.datasource")
class DBConfiguration(
    private val appProps: AppProps
) {
    var url: String? = null
    var username: String? = null
    var password: String? = null

    @Profile("dev")
    @Bean
    fun devDatabaseConnection(): String {
        println("DB connection for DEV - MySQL - ${appProps.message}")
        println(url)
        println(username)
        println(password)
        return "DB connection for DEV - MySQL"
    }

    @Profile("test")
    @Bean
    fun testDatabaseConnection(): String {
        println("DB Connection to - MySQL - ${appProps.message}")
        println(url)
        return "DB Connection to RDS_TEST - Low Cost Instance"
    }

    @Profile("staging")
    @Bean
    fun stagingDatabaseConnection(): String {
        println("${appProps.version}")
        println("DB connection for staging - MySQL - ${appProps.message}")
        println(url)
        println(username)
        println(password)
        return "DB connection for staging - MySQL"
    }

    @Profile("prod")
    @Bean
    fun prodDatabaseConnection(): String {
        println("DB Connection to RDS_PROD - High Performance Instance")
        println(url)
        return "DB Connection to RDS_PROD - High Performance Instance"
    }
}