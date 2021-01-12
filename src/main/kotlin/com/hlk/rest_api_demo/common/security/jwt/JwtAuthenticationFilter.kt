package com.hlk.rest_api_demo.common.security.jwt

import com.hlk.rest_api_demo.common.security.service.UserService
import io.jsonwebtoken.ExpiredJwtException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtAuthenticationFilter : OncePerRequestFilter() {
    @Autowired
    private val tokenProvider: JwtTokenProvider? = null

    @Autowired
    val customUserDetailsService: UserService? = null

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
            request: HttpServletRequest,
            response: HttpServletResponse, filterChain: FilterChain
    ) {
        try {
            // Lấy jwt từ request
            val jwt = getJwtFromRequest(request)
            if (StringUtils.hasText(jwt) && tokenProvider!!.validateToken(jwt)) {
                // Lấy username user từ chuỗi jwt
                val username = tokenProvider.getUserIdFromJWT(jwt)
                // Lấy thông tin người dùng từ id
                val userDetails: UserDetails = customUserDetailsService!!.loadUserByUsername(username!!)
                // Nếu người dùng hợp lệ, set thông tin cho Seturity Context
                val authentication = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
                authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = authentication
            } else {
                println("Cannot set the Security Context")
            }
        } catch (ex: ExpiredJwtException) {

            var isRefreshToken: String = request.getHeader("isRefreshToken")
            var requestURL: String = request.requestURL.toString()
            // allow for Refresh Token creation if following conditions are true.
            if (isRefreshToken != null && isRefreshToken == "true" && requestURL.contains("refreshtoken")) {
                allowForRefreshToken(ex, request)
            } else
                request.setAttribute("exception", ex)

        } catch (ex: BadCredentialsException) {
            request.setAttribute("exception", ex)
        } catch (ex: Exception) {
            println(ex)
        }
        filterChain.doFilter(request, response)
    }

    private fun allowForRefreshToken(ex: ExpiredJwtException, request: HttpServletRequest) {

        // create a UsernamePasswordAuthenticationToken with null values.
        val usernamePasswordAuthenticationToken = UsernamePasswordAuthenticationToken(
                null, null, null)
        // After setting the Authentication in the context, we specify
        // that the current user is authenticated. So it passes the
        // Spring Security Configurations successfully.
        SecurityContextHolder.getContext().authentication = usernamePasswordAuthenticationToken
        // Set the claims so that in controller we will be using it to create
        // new JWT
        request.setAttribute("claims", ex.claims)
    }

    private fun getJwtFromRequest(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader("Authorization")
        // Kiểm tra xem header Authorization có chứa thông tin jwt không
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null
    }
}
