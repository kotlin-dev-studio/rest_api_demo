package com.hlk.rest_api_demo.service

import com.hlk.rest_api_demo.model.CustomUserDetails
import io.jsonwebtoken.*
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.springframework.stereotype.Component
import java.io.IOException
import java.util.*
import javax.servlet.ServletException


@Component
class JwtTokenProvider {
    // Đoạn JWT_SECRET này là bí mật, chỉ có phía server biết
    val SECRET: String? = "SecretKeyToGenJWTs"
    val EXPIRATION_TIME: Int = 7200000 // 2 hours
    val REFRESH_TIME: Int = 864000 // 10 days
    val TOKEN_PREFIX = "Bearer "

    // Tạo ra jwt từ thông tin user
    @Throws(IOException::class, ServletException::class)
    fun generateToken(userDetails: CustomUserDetails): String {
        return Jwts.builder()
            .setSubject(userDetails.username)
            .setExpiration(Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .signWith(SignatureAlgorithm.HS512, SECRET)
            .compact()
    }

    // Lấy thông tin user từ jwt
    fun getUserIdFromJWT(token: String?): String? {
        var username = Jwts.parser()
            .setSigningKey(SECRET)
            .parseClaimsJws(token!!.replace(TOKEN_PREFIX, "")).body
        return username.subject
    }

    fun doGenerateRefreshToken(subject: CustomUserDetails): String? {
        return Jwts.builder().setSubject(subject.username).setIssuedAt(Date(System.currentTimeMillis()))
                .setExpiration(Date(System.currentTimeMillis() + REFRESH_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET).compact()
    }

    fun validateToken(authToken: String?): Boolean {
        val log: Log = LogFactory.getLog(javaClass)

        try {
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(authToken)
            return true
        } catch (ex: MalformedJwtException) {
            log.error("Invalid JWT token")
        } catch (ex: ExpiredJwtException) {
            log.error("Expired JWT token")
        } catch (ex: UnsupportedJwtException) {
            log.error("Unsupported JWT token")
        } catch (ex: IllegalArgumentException) {
            log.error("JWT claims string is empty.")
        }
        return false
    }
}
