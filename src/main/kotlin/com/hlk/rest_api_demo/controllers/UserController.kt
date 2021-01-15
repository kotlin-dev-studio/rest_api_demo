package com.hlk.rest_api_demo.controllers

import com.hlk.rest_api_demo.repository.UserRepository
import com.hlk.rest_api_demo.model.UserDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import kotlin.collections.HashMap

@RestController
@RequestMapping("/user")
class UserController(private val userRepository: UserRepository) {

    @GetMapping("/getAllUsers")
    fun getAllUsers(): HashMap<String, List<UserDao>> {
        return hashMapOf("results" to userRepository.findAll() as MutableList<UserDao>)
    }

    @GetMapping("/{id}")
    fun getUserById(@PathVariable(value = "id") userId: Long): ResponseEntity<UserDao> {
        return userRepository.findById(userId).map { user -> ResponseEntity.ok(user)
        }.orElse(ResponseEntity.notFound().build())
    }

    @Autowired
    private lateinit var bcryptEncoder: PasswordEncoder

    @PutMapping("{id}")
    fun updateUserById(
        @Valid @RequestBody newUser: UserDao,
        @PathVariable(value = "id") userId: Long
    ): ResponseEntity<UserDao>{
        return userRepository.findById(userId).map { exitsUser ->
             exitsUser.username = newUser.username
             exitsUser.password = bcryptEncoder.encode(newUser.password)
            ResponseEntity.ok().body(userRepository.save(exitsUser))
        }.orElse(ResponseEntity.notFound().build())
    }
}
