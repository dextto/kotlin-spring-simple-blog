package com.example.simpleblog.domain.member

import com.example.simpleblog.domain.AuditingEntity
import jakarta.persistence.*

@Entity
@Table
class Member(
    email: String,
    password: String,
    role: Role,
) : AuditingEntity() {
    @Column(name = "email", nullable = false)
    var email: String = email
        protected set

    @Column(name = "password", nullable = false)
    var password: String = password
        protected set

    @Enumerated(EnumType.STRING)
    var role: Role = role
        protected set

}

enum class Role {
    USER, ADMIN
}