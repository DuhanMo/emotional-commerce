package com.loopers.domain.user

import com.loopers.support.error.CoreException
import com.loopers.support.error.ErrorType
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class UserReader(
    private val userRepository: UserRepository,
) {
    @Transactional(readOnly = true)
    fun exist(loginId: LoginId): Boolean = userRepository.findByLoginId(loginId) != null

    @Transactional(readOnly = true)
    fun find(loginId: LoginId): User? = userRepository.findByLoginId(loginId)

    @Transactional(readOnly = true)
    fun getLoginId(loginId: LoginId): User = userRepository.findByLoginId(loginId)
        ?: throw CoreException(ErrorType.NOT_FOUND, "존재하지 않는 회원입니다")
}
