package com.loopers.application.user

import com.loopers.domain.user.UserCreateCommand
import com.loopers.domain.user.UserReader
import com.loopers.domain.user.UserWriter
import com.loopers.support.error.CoreException
import com.loopers.support.error.ErrorType
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userWriter: UserWriter,
    private val userReader: UserReader,
) {
    fun register(command: UserCreateCommand) {
        if (userReader.exist(command.uid)) {
            throw CoreException(ErrorType.CONFLICT, "이미 존재하는 ID 입니다")
        }
        userWriter.write(command.toUser())
    }
}
