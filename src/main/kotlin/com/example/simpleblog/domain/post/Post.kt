package com.example.simpleblog.domain.post

import com.example.simpleblog.domain.AuditingEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "Post")
class Post(
    title: String,
    content: String,
) : AuditingEntity() {
    @Column(name = "title", nullable = false)
    var title: String = title
        protected set

    @Column(name = "content")
    var content: String = content
        protected set
}