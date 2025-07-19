package com.loopers.domain.point

import com.loopers.domain.user.LoginId
import com.loopers.domain.user.UserRepository
import com.loopers.support.error.CoreException
import com.loopers.support.error.ErrorType
import org.springframework.stereotype.Component

@Component
class PointReader(
    private val userRepository: UserRepository,
    private val pointRepository: PointRepository,
) {
    fun getByLoginId(loginId: LoginId): Point = findByLoginId(loginId)
        ?: throw CoreException(ErrorType.NOT_FOUND, "존재하지 않는 포인트입니다")

    fun findByLoginId(loginId: LoginId): Point? =
        userRepository.findByLoginId(loginId)?.let { user ->
            pointRepository.findByUserId(user.id)
        }
}
