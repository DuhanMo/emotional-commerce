package com.loopers.domain.user

interface UserRepository {
    fun save(user: User): User
    fun findByUid(uid: String): User?
}
