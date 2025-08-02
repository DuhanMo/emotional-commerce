package com.loopers.application.user

import com.loopers.domain.user.LoginId
import com.loopers.domain.user.UserQueryService
import com.loopers.domain.user.UserRegisterCommand
import com.loopers.domain.user.UserService
import com.loopers.support.error.CoreException
import com.loopers.support.error.ErrorType
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserFacade(
    private val userService: UserService,
    private val userQueryService: UserQueryService,
) {
    @Transactional
    fun register(command: UserRegisterCommand): UserOutput {
        if (userQueryService.exist(command.loginId)) {
            throw CoreException(ErrorType.CONFLICT, "이미 존재하는 ID 입니다")
        }

        val user = userService.createUser(command.toUser())
        return UserOutput.from(user)
    }

    fun getMe(loginId: LoginId): UserOutput = userQueryService.getByLoginId(loginId).let { UserOutput.from(it) }
}
