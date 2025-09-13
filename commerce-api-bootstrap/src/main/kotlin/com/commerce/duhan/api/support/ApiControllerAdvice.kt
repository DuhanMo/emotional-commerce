package com.commerce.duhan.api.support

import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.exc.InvalidFormatException
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingRequestHeaderException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.server.ServerWebInputException
import org.springframework.web.servlet.resource.NoResourceFoundException

@RestControllerAdvice
class ApiControllerAdvice {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @ExceptionHandler
    fun handleBadRequest(e: MethodArgumentTypeMismatchException): ResponseEntity<ApiResponse<*>> {
        logger.error("Exception : {}", e.message, e)
        val name = e.name
        val type = e.requiredType?.simpleName ?: "unknown"
        val value = e.value ?: "null"
        val message = "요청 파라미터 '$name' (타입: $type)의 값 '$value'이(가) 잘못되었습니다."
        return failureResponse(errorType = ErrorType.BAD_REQUEST, errorMessage = message)
    }

    @ExceptionHandler
    fun handleBadRequest(e: MissingServletRequestParameterException): ResponseEntity<ApiResponse<*>> {
        logger.error("Exception : {}", e.message, e)
        val name = e.parameterName
        val type = e.parameterType
        val message = "필수 요청 파라미터 '$name' (타입: $type)가 누락되었습니다."
        return failureResponse(errorType = ErrorType.BAD_REQUEST, errorMessage = message)
    }

    @ExceptionHandler
    fun handleBadRequest(e: MissingRequestHeaderException): ResponseEntity<ApiResponse<*>> {
        logger.error("Exception : {}", e.message, e)
        val header = e.headerName
        val message = "필수 요청 헤더 '$header'가 누락되었습니다."
        return failureResponse(errorType = ErrorType.BAD_REQUEST, errorMessage = message)
    }

    @ExceptionHandler
    fun handleValidationErrors(e: MethodArgumentNotValidException): ResponseEntity<ApiResponse<*>> {
        logger.error("Exception : {}", e.message, e)
        val errors = e.bindingResult.fieldErrors.map { fieldError ->
            "${fieldError.field}: ${fieldError.defaultMessage}"
        }
        val message = if (errors.isNotEmpty()) {
            "검증 실패: ${errors.joinToString(", ")}"
        } else {
            "요청 데이터 검증에 실패했습니다."
        }
        return failureResponse(errorType = ErrorType.BAD_REQUEST, errorMessage = message)
    }

    @ExceptionHandler
    fun handleBadRequest(e: HttpMessageNotReadableException): ResponseEntity<ApiResponse<*>> {
        logger.error("Exception : {}", e.message, e)
        val errorMessage = when (val rootCause = e.rootCause) {
            is InvalidFormatException -> {
                val fieldName = rootCause.path.joinToString(".") { it.fieldName ?: "?" }

                val valueIndicationMessage = when {
                    rootCause.targetType.isEnum -> {
                        val enumClass = rootCause.targetType
                        val enumValues = enumClass.enumConstants.joinToString(", ") { it.toString() }
                        "사용 가능한 값 : [$enumValues]"
                    }

                    else -> ""
                }

                val expectedType = rootCause.targetType.simpleName
                val value = rootCause.value

                "필드 '$fieldName'의 값 '$value'이(가) 예상 타입($expectedType)과 일치하지 않습니다. $valueIndicationMessage"
            }

            is MismatchedInputException -> {
                val fieldPath = rootCause.path.joinToString(".") { it.fieldName ?: "?" }
                "필수 필드 '$fieldPath'이(가) 누락되었습니다."
            }

            is JsonMappingException -> {
                val fieldPath = rootCause.path.joinToString(".") { it.fieldName ?: "?" }
                "필드 '$fieldPath'에서 JSON 매핑 오류가 발생했습니다: ${rootCause.originalMessage}"
            }

            else -> "요청 본문을 처리하는 중 오류가 발생했습니다. JSON 메세지 규격을 확인해주세요."
        }

        return failureResponse(errorType = ErrorType.BAD_REQUEST, errorMessage = errorMessage)
    }

    @ExceptionHandler
    fun handleBadRequest(e: ServerWebInputException): ResponseEntity<ApiResponse<*>> {
        logger.error("Exception : {}", e.message, e)
        fun extractMissingParameter(message: String): String {
            val regex = "'(.+?)'".toRegex()
            return regex.find(message)?.groupValues?.get(1) ?: ""
        }

        val missingParams = extractMissingParameter(e.reason ?: "")
        return if (missingParams.isNotEmpty()) {
            failureResponse(errorType = ErrorType.BAD_REQUEST, errorMessage = "필수 요청 값 \'$missingParams\'가 누락되었습니다.")
        } else {
            failureResponse(errorType = ErrorType.BAD_REQUEST)
        }
    }

    @ExceptionHandler(NoResourceFoundException::class, NoSuchElementException::class)
    fun handleNotFound(e: Throwable): ResponseEntity<ApiResponse<*>> {
        logger.error("Exception : {}", e.message, e)
        return failureResponse(errorType = ErrorType.NOT_FOUND, errorMessage = e.message)
    }

    @ExceptionHandler(IllegalArgumentException::class, IllegalStateException::class)
    fun handleBadRequest(e: Throwable): ResponseEntity<ApiResponse<*>> {
        logger.error("Exception : {}", e.message, e)
        return failureResponse(errorType = ErrorType.BAD_REQUEST, errorMessage = e.message)
    }

    @ExceptionHandler
    fun handle(e: Throwable): ResponseEntity<ApiResponse<*>> {
        logger.error("Exception : {}", e.message, e)
        val errorType = ErrorType.INTERNAL_ERROR
        return failureResponse(errorType = errorType)
    }

    private fun failureResponse(errorType: ErrorType, errorMessage: String? = null): ResponseEntity<ApiResponse<*>> =
        ResponseEntity(
            ApiResponse.fail(errorCode = errorType.code, errorMessage = errorMessage ?: errorType.message),
            errorType.status,
        )
}
