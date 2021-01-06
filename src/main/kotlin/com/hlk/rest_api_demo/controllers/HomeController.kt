package com.hlk.rest_api_demo.controllers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HomeController {
    @GetMapping("/")
    fun home() : ResponseEntity<String> = ResponseEntity.ok().body("Home")
}
