package com.loopers.application.user

import com.loopers.domain.point.Point
import com.loopers.domain.point.PointWriteService
import com.loopers.domain.user.LoginId
import com.loopers.domain.user.UserReadService
import com.loopers.domain.user.UserRegisterCommand
import com.loopers.domain.user.UserWriteService
import com.loopers.support.error.CoreException
import com.loopers.support.error.ErrorType
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserFacade(
    private val userWriteService: UserWriteService,
    private val userReadService: UserReadService,
    private val pointWriteService: PointWriteService,
) {
    @Transactional
    fun register(command: UserRegisterCommand): UserOutput {
        if (userReadService.exist(command.loginId)) {
            throw CoreException(ErrorType.CONFLICT, "이미 존재하는 ID 입니다")
        }
        val user = userWriteService.write(command.toUser())
        pointWriteService.write(Point(user.id))

        return UserOutput.from(user)
    }

    fun getMe(loginId: LoginId): UserOutput =
        userReadService.findByLoginId(loginId)?.let { UserOutput.from(it) }
            ?: throw CoreException(ErrorType.NOT_FOUND, "존재하지 않는 회원입니다")
}
