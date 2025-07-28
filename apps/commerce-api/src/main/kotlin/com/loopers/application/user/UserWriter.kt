package com.loopers.application.user

import com.loopers.domain.user.User
import com.loopers.domain.user.UserRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class UserWriter(
    private val userRepository: UserRepository,
) {
    @Transactional
    fun write(user: User): User = userRepository.save(user)
}
