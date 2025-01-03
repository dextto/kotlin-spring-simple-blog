package com.example.simpleblog.service

import com.example.simpleblog.domain.post.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PostService(
    private val postRepository: PostRepository,
) {
    @Transactional(readOnly = true)
    fun findPosts(): List<PostRes> {
        return postRepository.findAll().map { it.toDto() }
    }

    @Transactional
    fun savePost(dto: PostSaveReq): Post {
        return postRepository.save(dto.toEntity())
    }

    @Transactional
    fun deletePost(id: Long) {
        postRepository.deleteById(id)
    }

    @Transactional
    fun findPostById(id: Long): PostRes {
        return postRepository.findById(id).orElseThrow().toDto()
    }
}