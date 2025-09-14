package com.commerce.duhan.core.domain.user

interface UserRepository {
    fun existByLoginId(loginId: LoginId): Boolean

    fun save(user: User): User

    fun getById(id: Long): User
}
