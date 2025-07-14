package com.loopers.domain.user

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class UserWriter(
    private val userRepository: UserRepository,
) {
    @Transactional
    fun write(user: User) {
        userRepository.save(user)
    }
}
