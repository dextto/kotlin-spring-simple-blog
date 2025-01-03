package com.example.simpleblog.api

import com.example.simpleblog.service.PostService
import com.example.simpleblog.util.value.CmResDto
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class PostController(
    private val postService: PostService,
) {

    @GetMapping("posts")
    fun getPosts(): CmResDto<*> {
        return CmResDto(
            HttpStatus.OK.toString(),
            "Found posts",
            postService.findPosts()
        )
    }
}
