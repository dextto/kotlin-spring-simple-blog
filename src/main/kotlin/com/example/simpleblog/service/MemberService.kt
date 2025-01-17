package com.example.simpleblog.service

import com.example.simpleblog.domain.member.*
import com.example.simpleblog.exception.MemberNotFoundException
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberService(
    private val memberRepository: MemberRepository,
) {
    @Transactional(readOnly = true)
    fun findAll(pageable: Pageable): List<MemberRes> =
        memberRepository
            .findMembers(pageable)
            .map { it.toDto() }

    @Transactional
    fun saveMember(dto: LoginDto): Member {
        return memberRepository.save(dto.toEntity())
    }

    @Transactional
    fun deleteMember(id: Long) {
        memberRepository.deleteById(id)
    }

    @Transactional
    fun findMemberById(id: Long): MemberRes {
        return memberRepository.findById(id).orElseThrow { throw MemberNotFoundException(id.toString()) }.toDto()
    }
}