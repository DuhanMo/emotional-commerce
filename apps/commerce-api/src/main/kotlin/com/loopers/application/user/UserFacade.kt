package com.loopers.application.user

import com.loopers.domain.user.LoginId
import com.loopers.domain.user.User
import com.loopers.domain.user.UserQueryService
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
    fun register(input: UserRegisterInput): UserOutput {
        if (userQueryService.exist(input.loginId)) {
            throw CoreException(ErrorType.CONFLICT, "이미 존재하는 ID 입니다")
        }
        val user = User(
            loginId = input.loginId,
            email = input.email,
            birthDate = input.birthDate,
            gender = input.gender,
        )
        return UserOutput.from(userService.createUser(user))
    }

    fun getMe(loginId: LoginId): UserOutput = userQueryService.getByLoginId(loginId).let { UserOutput.from(it) }
}
