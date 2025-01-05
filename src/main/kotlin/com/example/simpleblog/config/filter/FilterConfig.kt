package com.example.simpleblog.config.filter

import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Configuration

@Configuration
class FilterConfig {
    /**
     * TODO: 필터 시큐리티 적용
     */

//    @Bean
    fun registerAuthenticationFilter(): FilterRegistrationBean<AuthenticationFilter?> {
        val bean = FilterRegistrationBean(AuthenticationFilter())
        bean.addUrlPatterns("/members/*")
        bean.order = 0

        return bean
    }
}