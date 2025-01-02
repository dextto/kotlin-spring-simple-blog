package com.example.simpleblog.domain.comment

import com.example.simpleblog.domain.AuditingEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "Comment")
class Comment(
    title: String,
    content: String,
) : AuditingEntity() {

    @Column(name = "content", nullable = false)
    var content: String = content
        protected set

    
}