package com.loopers.domain.point

import com.loopers.domain.user.LoginId

interface PointRepository {
    fun save(point: Point): Point

    fun findByUserLoginId(loginId: LoginId): Point?
}
