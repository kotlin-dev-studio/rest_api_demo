package com.hlk.rest_api_demo.controller.sample.api.user

import com.hlk.rest_api_demo.model.ResultDataRes
import com.hlk.rest_api_demo.model.User
import com.hlk.rest_api_demo.repository.UserRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/user")
@RestController
class SampleUserApiController(private val userRepository: UserRepository) {
    @GetMapping("/getUser")
    fun getUser() : ResponseEntity<User> =
        ResponseEntity.ok(userRepository.findByUsername("anh1"))
}
