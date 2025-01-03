package com.example.simpleblog.domain.member

import com.linecorp.kotlinjdsl.support.spring.data.jpa.repository.KotlinJdslJpqlExecutor
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository : JpaRepository<Member, Long>, MemberCustomRepository {
}

interface MemberCustomRepository {
    fun findMembers(pageable: Pageable): List<Member>
}

class MemberCustomRepositoryImpl(
    private val kotlinJdslJpqlExecutor: KotlinJdslJpqlExecutor,
) : MemberCustomRepository {
    override fun findMembers(pageable: Pageable): List<Member> {
        return kotlinJdslJpqlExecutor.findAll(pageable) {
            select(
                entity(Member::class),
            ).from(
                entity(Member::class),
            ).orderBy(
                path(Member::id).desc(),
            )
        }.filterNotNull()
    }
}
