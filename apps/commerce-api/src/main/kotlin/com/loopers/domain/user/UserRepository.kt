package com.loopers.domain.user

interface UserRepository {
    fun save(user: User): User
    fun findByLoginId(loginId: LoginId): User?
}
