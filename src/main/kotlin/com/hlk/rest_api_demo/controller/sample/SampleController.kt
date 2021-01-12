package com.hlk.rest_api_demo.controller.sample

import com.hlk.rest_api_demo.config.AppProps
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.Serializable
import java.time.LocalDateTime
import java.util.*
import javax.servlet.http.HttpSession

@RequestMapping("/sample")
@RestController
class SampleController(
    private val appProps: AppProps,
    private val httpSession: HttpSession
) {
    @GetMapping("/appProps")
    fun appProps() = appProps

    @GetMapping("/uuid")
    fun uuid() = httpSession.getAttribute("sample-uuid").let {
        var v = it as? String
        if (v == null) {
            v = UUID.randomUUID().toString()
            httpSession.setAttribute("sample-uuid", v)
        }
        v
    }

    @GetMapping("/sessionTest")
    fun sessionTest() = httpSession.getAttribute("sample-session-test").let {
        var v = it as? Test
        if (v == null) {
            v = Test(1, "kimhuor", LocalDateTime.now())
            httpSession.setAttribute("sample-session-test", v)
        }
        v
    }

    class Test(
        var id: Int,
        var name: String,
        var date: LocalDateTime
    ): Serializable
}
