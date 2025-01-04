package com.example.simpleblog.exception

import org.springframework.validation.BindingResult


class ErrorResponse(
    errorCode: ErrorCode,
    var errors: List<FieldError> = ArrayList(),
) {
    var code: String = errorCode.code
        private set

    var message: String = errorCode.message
        private set

    class FieldError private constructor(
        val field: String,
        val value: String,
        val reason: String?,
    ) {
        companion object {
            fun of(bindingResult: BindingResult) = bindingResult.fieldErrors.map { error ->
                FieldError(
                    field = error.field,
                    value = if (error.rejectedValue == null) "" else error.rejectedValue.toString(),
                    reason = error.defaultMessage,
                )
            }
        }
    }

    companion object {
        fun of(code: ErrorCode, bindingResult: BindingResult) = ErrorResponse(
            errorCode = code,
            errors = FieldError.of(bindingResult)
        )

        fun of(code: ErrorCode) = ErrorResponse(code)
    }

}