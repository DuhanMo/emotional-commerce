package com.loopers.domain.user

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class UserQueryService(
    private val userRepository: UserRepository,
) {
    @Transactional(readOnly = true)
    fun exist(loginId: LoginId): Boolean = userRepository.existsByLoginId(loginId)

    @Transactional(readOnly = true)
    fun findByLoginId(loginId: LoginId): User? = userRepository.findByLoginId(loginId)
}
