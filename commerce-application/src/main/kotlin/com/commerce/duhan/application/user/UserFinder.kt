package com.commerce.duhan.application.user

import com.commerce.duhan.domain.user.User
import com.commerce.duhan.domain.user.UserRepository
import org.springframework.stereotype.Component

@Component
class UserFinder(
    private val userRepository: UserRepository,
) {
    fun getById(id: Long): User = userRepository.getById(id)
}
