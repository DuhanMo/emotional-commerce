package com.loopers.application.user

import com.loopers.domain.user.UserReader
import com.loopers.domain.user.UserRegisterCommand
import com.loopers.domain.user.UserWriter
import com.loopers.support.error.CoreException
import com.loopers.support.error.ErrorType
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userWriter: UserWriter,
    private val userReader: UserReader,
) {
    fun register(command: UserRegisterCommand): UserOutput {
        if (userReader.exist(command.userId)) {
            throw CoreException(ErrorType.CONFLICT, "이미 존재하는 ID 입니다")
        }
        return UserOutput.from(userWriter.write(command.toUser()))
    }
}
