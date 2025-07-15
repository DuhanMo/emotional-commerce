package com.loopers.infrastructure.user

import com.loopers.domain.user.User
import com.loopers.domain.user.UserId
import com.loopers.domain.user.UserRepository
import org.springframework.stereotype.Repository

@Repository
class UserRepositoryImpl(
    private val userJpaRepository: UserJpaRepository,
) : UserRepository {
    override fun save(user: User): User = userJpaRepository.save(user)

    override fun findByUserId(userId: UserId): User? = userJpaRepository.findByUserId(userId.value)
}
