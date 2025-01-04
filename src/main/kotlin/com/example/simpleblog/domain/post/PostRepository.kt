package com.example.simpleblog.domain.post

import com.linecorp.kotlinjdsl.support.spring.data.jpa.repository.KotlinJdslJpqlExecutor
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface PostRepository : JpaRepository<Post, Long>, PostCustomRepository

interface PostCustomRepository {
    fun findPosts(pageable: Pageable): List<Post>
}

class PostCustomRepositoryImpl(
    private val kotlinJdslJpqlExecutor: KotlinJdslJpqlExecutor,
) : PostCustomRepository {
    override fun findPosts(pageable: Pageable): List<Post> {
        return kotlinJdslJpqlExecutor.findAll(pageable) {
            select(
                entity(Post::class),
            ).from(
                entity(Post::class),
                fetchJoin(Post::member),
            ).orderBy(
                path(Post::id).desc(),
            )
        }.filterNotNull()
    }
}
