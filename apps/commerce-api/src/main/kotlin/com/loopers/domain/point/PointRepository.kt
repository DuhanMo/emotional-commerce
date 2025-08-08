package com.loopers.domain.point

import com.loopers.domain.user.LoginId

interface PointRepository {
    fun save(point: Point): Point

    fun getByUserLoginId(loginId: LoginId): Point

    fun getByUserId(userId: Long): Point

    fun getByUserIdWithLock(userId: Long): Point
}
