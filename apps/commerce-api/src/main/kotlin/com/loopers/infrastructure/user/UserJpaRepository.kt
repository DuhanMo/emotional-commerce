package com.loopers.infrastructure.user

import com.loopers.domain.user.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserJpaRepository : JpaRepository<User, Long> {
    fun findByLoginId(loginId: String): User?

    fun existsByLoginId(loginId: String): Boolean
}
