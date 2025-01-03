package com.example.simpleblog.api

import com.example.simpleblog.service.MemberService
import com.example.simpleblog.util.value.CmResDto
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class MemberController(
    private val memberService: MemberService,
) {
    @GetMapping("members")
    fun findAll(pageable: Pageable): CmResDto<*> {
        return CmResDto(
            HttpStatus.OK.toString(),
            "Found all members",
            memberService.findAll(pageable),
        )
    }
}