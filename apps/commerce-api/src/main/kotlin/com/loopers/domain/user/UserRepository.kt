package com.loopers.domain.user

interface UserRepository {
    fun save(user: User): User

    fun existsByLoginId(loginId: LoginId): Boolean

    fun getByLoginId(loginId: LoginId): User
}
