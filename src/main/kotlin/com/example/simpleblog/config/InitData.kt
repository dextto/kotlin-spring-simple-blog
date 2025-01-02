package com.example.simpleblog.config

import com.example.simpleblog.domain.member.Member
import com.example.simpleblog.domain.member.MemberRepository
import com.example.simpleblog.domain.member.Role
import io.github.serpro69.kfaker.Faker
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.EventListener

@Configuration
class InitData(
    private val memberRepository: MemberRepository,
) {
    val faker = Faker()

    @EventListener(ApplicationReadyEvent::class)
    private fun init() {

        val member = Member(
            email = faker.internet.safeEmail(),
            password = "1234",
            role = Role.USER
        )
        memberRepository.save(member)
    }
}