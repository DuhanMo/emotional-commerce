package com.loopers.infrastructure.user

import com.loopers.domain.user.User
import com.loopers.domain.user.LoginId
import com.loopers.domain.user.UserRepository
import org.springframework.stereotype.Repository

@Repository
class UserRepositoryImpl(
    private val userJpaRepository: UserJpaRepository,
) : UserRepository {
    override fun save(user: User): User = userJpaRepository.save(user)

    override fun findByLoginId(loginId: LoginId): User? = userJpaRepository.findByLoginId(loginId.value)

    override fun existsByLoginId(loginId: LoginId): Boolean = userJpaRepository.existsByLoginId(loginId.value)
}
