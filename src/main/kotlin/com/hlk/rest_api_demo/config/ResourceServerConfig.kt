package com.hlk.rest_api_demo.config

import com.hlk.rest_api_demo.common.Role
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter
import com.hlk.rest_api_demo.common.security.hasRole

@Configuration
@EnableResourceServer
class ResourceServerConfig : ResourceServerConfigurerAdapter() {
    override fun configure(http: HttpSecurity) {
        http
            .csrf().disable()
            .formLogin().disable()
            .anonymous().disable()
            .authorizeRequests()
            .antMatchers("/sample/**").permitAll()
            .antMatchers("/api/authen/**").authenticated()
            .antMatchers("/api/admin/**").hasRole(Role.ADMIN)
            .antMatchers("/api/user/**").hasRole(Role.USER)

    }
}
