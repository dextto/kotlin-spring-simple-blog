package com.example.simpleblog.api

import com.example.simpleblog.domain.post.Post
import com.example.simpleblog.domain.post.PostRes
import com.example.simpleblog.domain.post.PostSaveReq
import com.example.simpleblog.service.PostService
import com.example.simpleblog.util.value.CmResDto
import jakarta.validation.Valid
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RequestMapping("/v1")
@RestController
class PostController(
    private val postService: PostService,
) {

    @GetMapping("/posts")
    fun getPosts(pageable: Pageable): CmResDto<*> {
        return CmResDto(
            HttpStatus.OK.toString(),
            "Found posts",
            postService.findPosts(pageable)
        )
    }

    @GetMapping("/posts/{id}")
    fun findById(@PathVariable id: Long): CmResDto<PostRes> {
        return CmResDto(
            HttpStatus.OK.toString(),
            "Found Post by id",
            postService.findPostById(id),
        )
    }

    @DeleteMapping("/posts/{id}")
    fun deleteById(@PathVariable id: Long): CmResDto<Unit> {
        return CmResDto(
            HttpStatus.NO_CONTENT.toString(),
            "Post Deleted",
            postService.deletePost(id),
        )
    }

    @PostMapping("/posts")
    fun save(@Valid @RequestBody dto: PostSaveReq): CmResDto<Post> {
        return CmResDto(
            HttpStatus.CREATED.toString(),
            "Post Created",
            postService.savePost(dto),
        )
    }
}
