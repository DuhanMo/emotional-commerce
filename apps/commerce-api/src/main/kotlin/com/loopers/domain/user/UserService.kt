package com.loopers.domain.user

import com.loopers.domain.point.Point
import com.loopers.domain.point.PointRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
    private val pointRepository: PointRepository,
) {
    @Transactional
    fun createUser(user: User): User = userRepository.save(user).also {
        pointRepository.save(Point(user.id))
    }
}
