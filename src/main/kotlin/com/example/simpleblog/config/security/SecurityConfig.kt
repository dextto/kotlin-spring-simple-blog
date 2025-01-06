package com.example.simpleblog.config.security

import com.example.simpleblog.domain.member.MemberRepository
import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity(debug = false)
class SecurityConfig(
    private val authenticationConfiguration: AuthenticationConfiguration,
    private val objectMapper: ObjectMapper, // 이미 bean으로 등록되어 있음
    private val memberRepository: MemberRepository,
) {
    private val log = KotlinLogging.logger { }

    // NOTE: 일단 테스트로 모든 요청에 대해 인증을 무효화
//    @Bean
    fun webSecurityCustomizer(): WebSecurityCustomizer? {
        return WebSecurityCustomizer { web -> web.ignoring().anyRequest() }
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain? {
        http
            .csrf { it.disable() }
            .headers { headers -> headers.frameOptions { it.disable() } }
            .formLogin { it.disable() }
            .httpBasic { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }  // JWT를 사용하므로
            .cors { it.configurationSource(corsConfig()) }
            .addFilter(loginFilter())
            .addFilter(authenticationFilter())

        return http.build()
    }

    @Bean
    fun authenticationFilter(): CustomBasicAuthenticationFilter {
        return CustomBasicAuthenticationFilter(
            authenticationManager = authenticationManager(),
            memberRepository = memberRepository,
        )
    }

    @Bean
    fun authenticationManager(): AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }

    @Bean
    fun loginFilter(): CustomUsernameAuthenticationFilter {
        val authenticationFilter = CustomUsernameAuthenticationFilter(objectMapper)
        authenticationFilter.setAuthenticationManager(authenticationManager())

        return authenticationFilter
    }

    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
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
