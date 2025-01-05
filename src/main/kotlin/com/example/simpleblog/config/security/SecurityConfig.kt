package com.example.simpleblog.config.security

import mu.KotlinLogging
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
class SecurityConfig {
    private val log = KotlinLogging.logger { }

    // NOTE: 일단 테스트로 모든 요청에 대해 인증을 무효화
    @Bean
    fun webSecurityCustomizer(): WebSecurityCustomizer? {
        return WebSecurityCustomizer { web -> web.ignoring().anyRequest() }
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain? {
        // TODO: fix deprecated
        // http
        //     .csrf().disable()
        //     .headers().frameOptions().disable()
        //     .and()
        //     .formLogin().disable()
        //     .httpBasic().disable()
        //     .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // JWT를 사용하므로
        //     .and()
        //     .cors().configurationSource(corsConfig())

        http
            .csrf { it.disable() }
            .headers { headers -> headers.frameOptions { it.disable() } }
            .formLogin { it.disable() }
            .httpBasic { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }  // JWT를 사용하므로
            .cors { it.configurationSource(corsConfig()) }

        return http.build()
    }

    @Bean
    fun corsConfig(): CorsConfigurationSource {
        val config = CorsConfiguration()
        config.allowCredentials = true
        config.addAllowedOriginPattern("*")
        config.addAllowedMethod("*")
        config.addAllowedHeader("*")
        config.addExposedHeader("authorization")
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", config)

        return source
    }
}
