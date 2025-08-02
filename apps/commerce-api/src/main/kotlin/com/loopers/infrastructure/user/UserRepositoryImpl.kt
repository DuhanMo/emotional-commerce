package com.loopers.infrastructure.user

import com.loopers.domain.user.LoginId
import com.loopers.domain.user.User
import com.loopers.domain.user.UserRepository
import com.loopers.support.error.CoreException
import com.loopers.support.error.ErrorType
import org.springframework.stereotype.Repository

@Repository
class UserRepositoryImpl(
    private val userJpaRepository: UserJpaRepository,
) : UserRepository {
    override fun save(user: User): User = userJpaRepository.save(user)

    override fun existsByLoginId(loginId: LoginId): Boolean = userJpaRepository.existsByLoginId(loginId.value)

    override fun getByLoginId(loginId: LoginId): User = userJpaRepository.findByLoginId(loginId.value)
        ?: throw CoreException(ErrorType.NOT_FOUND, "존재하지 않는 회원입니다")
}
