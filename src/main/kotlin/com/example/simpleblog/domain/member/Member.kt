package com.example.simpleblog.domain.member

import com.example.simpleblog.domain.AuditingEntity
import jakarta.persistence.*

@Entity
@Table
class Member(
    id: Long = 0,
    email: String,
    password: String,
    role: Role = Role.USER,
) : AuditingEntity(id) {
    @Column(name = "email", nullable = false)
    var email: String = email
        protected set

    @Column(name = "password", nullable = false)
    var password: String = password
        protected set

    @Enumerated(EnumType.STRING)
    var role: Role = role
        protected set

    override fun toString(): String {
        return "Member(email=$email, password=$password, role=$role)"
    }

    companion object {
        fun createFakeMember(memberId: Long): Member {
            return Member(memberId, "test@test.com", "1234")
        }
    }
}

fun Member.toDto(): MemberRes {
    return MemberRes(
        id = this.id,
        email = this.email,
        password = this.password,
        role = this.role,
    )
}

enum class Role {
    USER, ADMIN
}