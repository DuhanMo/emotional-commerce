package com.commerce.duhan.core.application.user

import com.commerce.duhan.core.domain.user.*
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class UserCreator(
    private val userRepository: UserRepository
) {
    @Transactional
    fun create(command: CreateUserCommand): Long {
        require(!userRepository.existByLoginId(command.loginId)) { "이미 존재하는 로그인 아이디입니다." }

        return userRepository.save(
            User(
                loginId = command.loginId,
                email = command.email,
                birthDate = command.birthDate,
                gender = command.gender,
            )
        ).id
    }
}