package com.example.simpleblog.domain.comment

import com.linecorp.kotlinjdsl.support.spring.data.jpa.repository.KotlinJdslJpqlExecutor
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface CommentRepository : JpaRepository<Comment, Long>, CommentCustomRepository {
}

interface CommentCustomRepository {
    fun findComments(pageable: Pageable): List<Comment>
}

class CommentCustomRepositoryImpl(
    private val kotlinJdslJpqlExecutor: KotlinJdslJpqlExecutor,
) : CommentCustomRepository {
    override fun findComments(pageable: Pageable): List<Comment> {
        return kotlinJdslJpqlExecutor.findAll(pageable) {
            select(
                entity(Comment::class),
            ).from(
                entity(Comment::class),
            ).orderBy(
                path(Comment::id).desc(),
            )
        }.filterNotNull()
    }
}
