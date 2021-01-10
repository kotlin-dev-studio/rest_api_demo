package com.hlk.rest_api_demo.controller

import com.hlk.rest_api_demo.model.ResultRes
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class HomeController {
    @GetMapping("/")
    fun home() : ResponseEntity<Home> = ResponseEntity.status(HttpStatus.OK).body(Home.Living)

    @GetMapping("/resultResponse")
    fun resultResponse(@RequestParam(required=false) type: String?): ResponseEntity<ResultRes> {
        return when(type) {
            "success" -> ResponseEntity.status(HttpStatus.OK).body(ResultRes.success("Success message!"))
            "failure" -> ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(ResultRes.failure("Failure message!"))
            else -> {
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ResultRes.failure("Please provide type result with success or failure")
                )
            }
        }
    }
}

data class Home(val status: String) {
    companion object {
        val Living = Home("Living")
    }
}