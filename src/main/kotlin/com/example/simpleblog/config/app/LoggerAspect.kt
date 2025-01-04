package com.example.simpleblog.config.app

import mu.KotlinLogging
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.annotation.Pointcut
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

/**
 * 전통적인 방식의 프록시 기반 스프링 AOP
 */

@Component
@Aspect
class LoggerAspect {
    val log = KotlinLogging.logger { }

    @Pointcut("execution(* com.example.simpleblog.api.*Controller.*(..))")
    private fun controllerCut() = Unit

    @Before("controllerCut()")
    fun controllerLoggerAdvice(joinpoint: JoinPoint) {
        val typeName = joinpoint.signature.declaringTypeName
        val methodName = joinpoint.signature.name

        val request = (RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes).request

        log.info {
            """
            request url: ${request.servletPath}
            type: $typeName
            methodName: $methodName
            """.trimIndent()
        }
    }
}