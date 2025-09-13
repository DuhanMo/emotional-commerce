package com.commerce.duhan.core.application.user

import com.commerce.duhan.core.application.point.PointCreator
import org.springframework.stereotype.Service

@Service
class UserApplicationService(
    private val userCreator: UserCreator,
    private val pointWriter: PointCreator,
) {
    fun create(command: CreateUserCommand): CreateUserResult {
        val user = userCreator.create(command)
        pointWriter.create(user.id)
        return CreateUserResult.from(user)
    }
}
