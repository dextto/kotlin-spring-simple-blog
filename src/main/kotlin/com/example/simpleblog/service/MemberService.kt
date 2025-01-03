package com.example.simpleblog.service

import com.example.simpleblog.domain.member.MemberRepository
import com.example.simpleblog.domain.member.MemberRes
import com.example.simpleblog.domain.member.toDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberService(
    private val memberRepository: MemberRepository,
) {
    @Transactional(readOnly = true)
    fun findAll(): List<MemberRes> =
        memberRepository
            .findAll()
            .map { it.toDto() }
}