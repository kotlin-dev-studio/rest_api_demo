package com.hlk.rest_api_demo.config

import com.hlk.rest_api_demo.common.Role
import com.hlk.rest_api_demo.common.security.hasRole
import com.hlk.rest_api_demo.common.security.jwt.AuthEntryPointJwt
import com.hlk.rest_api_demo.common.security.jwt.JwtAuthenticationFilter
import com.hlk.rest_api_demo.common.security.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository
import javax.sql.DataSource

@Configuration
@EnableWebSecurity
internal class WebSecurityConfig(
        private val userService: UserService,
        private val unauthorizedHandler: AuthEntryPointJwt
) : WebSecurityConfigurerAdapter() {
    @Autowired
    private val dataSource: DataSource? = null

    override fun configure(webSecurity: WebSecurity) {
        webSecurity.ignoring().antMatchers(
                "/sample/**",
                "/signup",
                "/login",
                "/swagger-ui/",
                "/swagger-ui/{springfox,swagger-ui}.*",
                "/configuration/**",
                "/swagger-resources/**",
                "/v2/api-docs"
        )
    }

    @Throws(Exception::class)
    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder())
    }

    @Bean
    @Throws(java.lang.Exception::class)
    override fun authenticationManagerBean(): AuthenticationManager? {
        return super.authenticationManagerBean()
    }

    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http.csrf().disable()
                .formLogin().disable()
                .anonymous().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/signup").permitAll()
                .antMatchers("/swagger-ui/").permitAll()
                .antMatchers("/api/user/**").hasRole(Role.USER)
                .antMatchers("/api/admin/**").hasRole(Role.ADMIN)
                .anyRequest().authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter::class.java)
    }

    @Bean
    fun persistentTokenRepository(): PersistentTokenRepository? {
        val db = JdbcTokenRepositoryImpl()
        db.setDataSource(dataSource!!)
        return db
    }

    @Bean
    fun jwtAuthenticationFilter(): JwtAuthenticationFilter? {
        return JwtAuthenticationFilter()
    }
}
