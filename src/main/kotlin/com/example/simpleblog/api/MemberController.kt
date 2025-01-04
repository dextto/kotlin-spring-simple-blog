package com.example.simpleblog.api

import com.example.simpleblog.domain.member.Member
import com.example.simpleblog.domain.member.MemberRes
import com.example.simpleblog.domain.member.MemberSaveReq
import com.example.simpleblog.service.MemberService
import com.example.simpleblog.util.value.CmResDto
import jakarta.validation.Valid
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RequestMapping("/members")
@RestController
class MemberController(
    private val memberService: MemberService,
) {
    @GetMapping
    fun findAll(pageable: Pageable): CmResDto<*> {
        return CmResDto(
            HttpStatus.OK.toString(),
            "Found all members",
            memberService.findAll(pageable),
        )
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): CmResDto<MemberRes> {
        return CmResDto(
            HttpStatus.OK.toString(),
            "Found Member by id",
            memberService.findMemberById(id),
        )
    }

    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable id: Long): CmResDto<Unit> {
        return CmResDto(
            HttpStatus.NO_CONTENT.toString(),
            "Member Deleted",
            memberService.deleteMember(id),
        )
    }

    @PostMapping
    fun save(@Valid @RequestBody dto: MemberSaveReq): CmResDto<Member> {
        return CmResDto(
            HttpStatus.CREATED.toString(),
            "Member Created",
            memberService.saveMember(dto),
        )
    }
}
