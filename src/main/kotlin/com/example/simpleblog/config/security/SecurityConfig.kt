package com.example.simpleblog.config.security

import com.example.simpleblog.domain.member.MemberRepository
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import mu.KotlinLogging
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
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
//    @Bean  // 적용 안함
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
            .exceptionHandling {
                it.accessDeniedHandler(CustomAccessDeniedHandler())
                it.authenticationEntryPoint(CustomAuthenticationEntryPoint())
            }
            .authorizeHttpRequests { it.requestMatchers("/**").authenticated() }

        return http.build()
    }


    class CustomAuthenticationEntryPoint : AuthenticationEntryPoint {
        private val log = KotlinLogging.logger { }

        override fun commence(
            request: HttpServletRequest?,
            response: HttpServletResponse?,
            authException: org.springframework.security.core.AuthenticationException?,
        ) {
            log.info { "authentication required!!!" }  // TODO: 왜 여기를 2번 타는지 확인
//            response?.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.reasonPhrase)
            response?.sendError(HttpServletResponse.SC_UNAUTHORIZED)
        }
    }

    class CustomFailureHandler : AuthenticationFailureHandler {
        private val log = KotlinLogging.logger { }

        override fun onAuthenticationFailure(
            request: HttpServletRequest?,
            response: HttpServletResponse?,
            exception: AuthenticationException?,
        ) {
            log.info { "로그인 실패!!" }

            response?.sendError(HttpServletResponse.SC_FORBIDDEN, "인증 실패")
        }
    }

    class CustomSuccessHandler : AuthenticationSuccessHandler {
        private val log = KotlinLogging.logger { }

        override fun onAuthenticationSuccess(
            request: HttpServletRequest?,
            response: HttpServletResponse?,
            authentication: Authentication?,
        ) {
            log.info { "로그인 성공" }
        }

    }

    class CustomAccessDeniedHandler : AccessDeniedHandler {
        private val log = KotlinLogging.logger { }

        override fun handle(
            request: HttpServletRequest?,
            response: HttpServletResponse?,
            accessDeniedException: AccessDeniedException?,
        ) {
            log.info { "access denied!!!" }

            response?.sendError(HttpServletResponse.SC_UNAUTHORIZED)
        }
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
        authenticationFilter.setFilterProcessesUrl("/login")
        authenticationFilter.setAuthenticationFailureHandler(CustomFailureHandler())
        authenticationFilter.setAuthenticationSuccessHandler(CustomSuccessHandler())

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
