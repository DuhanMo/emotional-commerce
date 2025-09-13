package com.commerce.duhan.db.user

import com.commerce.duhan.core.domain.user.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserJpaRepository : JpaRepository<User, Long> {
    fun existsByLoginId(loginId: String): Boolean
}
