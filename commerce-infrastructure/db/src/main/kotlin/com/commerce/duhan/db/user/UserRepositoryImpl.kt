package com.commerce.duhan.db.user

import com.commerce.duhan.core.domain.user.LoginId
import com.commerce.duhan.core.domain.user.User
import com.commerce.duhan.core.domain.user.UserRepository
import org.springframework.stereotype.Repository

@Repository
class UserRepositoryImpl(
    private val jpaRepository: UserJpaRepository,
) : UserRepository {
    override fun existByLoginId(loginId: LoginId): Boolean = jpaRepository.existsByLoginId(loginId.value)
    override fun save(user: User): User = jpaRepository.save(user)
}