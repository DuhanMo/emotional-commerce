package com.loopers.domain.user

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class UserReader(
    private val userRepository: UserRepository,
) {
    @Transactional(readOnly = true)
    fun exist(userId: UserId): Boolean = userRepository.findByUserId(userId) != null
}
