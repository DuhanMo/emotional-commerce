package com.loopers.domain.user

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserQueryService(
    private val userRepository: UserRepository,
) {
    @Transactional(readOnly = true)
    fun exist(loginId: LoginId): Boolean = userRepository.existsByLoginId(loginId)

    @Transactional(readOnly = true)
    fun getByLoginId(loginId: LoginId): User = userRepository.getByLoginId(loginId)
}
