package com.commerce.duhan.application.user

import com.commerce.duhan.application.point.PointCreator
import com.commerce.duhan.application.point.PointFinder
import org.springframework.stereotype.Service

@Service
class UserApplicationService(
    private val userCreator: UserCreator,
    private val pointWriter: PointCreator,
    private val userFinder: UserFinder,
    private val pointFinder: PointFinder,
) {
    fun create(command: CreateUserCommand): UserResult {
        val user = userCreator.create(command)
        pointWriter.create(user.id)
        return UserResult.from(user)
    }
    fun getMe(userId: Long): GetMeResult {
        val user = userFinder.getById(userId)
        val point = pointFinder.getByUserId(user.id)
        return GetMeResult.of(user, point)
    }
}
