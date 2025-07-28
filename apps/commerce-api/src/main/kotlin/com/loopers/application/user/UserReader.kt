package com.loopers.application.user

import com.loopers.domain.user.LoginId
import com.loopers.domain.user.User
import com.loopers.domain.user.UserRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class UserReader(
    private val userRepository: UserRepository,
) {
    @Transactional(readOnly = true)
    fun exist(loginId: LoginId): Boolean = userRepository.existsByLoginId(loginId)

    @Transactional(readOnly = true)
    fun findByLoginId(loginId: LoginId): User? = userRepository.findByLoginId(loginId)
}
