package com.commerce.duhan.db.user

import com.commerce.duhan.domain.user.LoginId
import com.commerce.duhan.domain.user.User
import com.commerce.duhan.domain.user.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class UserRepositoryImpl(
    private val jpaRepository: UserJpaRepository,
) : UserRepository {
    override fun existByLoginId(loginId: LoginId): Boolean = jpaRepository.existsByLoginId(loginId.value)

    override fun save(user: User): User = jpaRepository.save(user)

    override fun getById(id: Long): User = jpaRepository.findByIdOrNull(id)
        ?: throw NoSuchElementException("유저를 찾을 수 없습니다. id: $id")
}
