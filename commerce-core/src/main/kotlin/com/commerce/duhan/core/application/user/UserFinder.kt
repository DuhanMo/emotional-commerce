package com.commerce.duhan.core.application.user

import com.commerce.duhan.core.domain.user.User
import com.commerce.duhan.core.domain.user.UserRepository
import org.springframework.stereotype.Component

@Component
class UserFinder(
    private val userRepository: UserRepository,
) {
    fun getById(id: Long): User = userRepository.getById(id)
}
