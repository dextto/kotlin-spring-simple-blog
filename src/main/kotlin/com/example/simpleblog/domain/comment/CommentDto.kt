package com.example.simpleblog.domain.comment

import com.example.simpleblog.domain.member.Member
import com.example.simpleblog.domain.post.Post
import jakarta.persistence.*

data class CommentSaveReq(
    val memberId: Long,
    val content: String,
    val postId: Long,
)

fun CommentSaveReq.toEntity(post: Post) = Comment(
    content = this.content,
    post = post,
    member = Member.createFakeMember(this.memberId),
)