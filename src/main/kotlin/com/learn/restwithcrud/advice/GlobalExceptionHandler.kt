package com.learn.restwithcrud.advice

import com.learn.restwithcrud.exceptions.EmployeeNotFoundException
import org.springframework.http.HttpStatusCode
import org.springframework.web.ErrorResponse
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler
    fun handleEmployeeNotFoundException(e: EmployeeNotFoundException): ErrorResponse {
        return ErrorResponse.builder(e,
            HttpStatusCode.valueOf(404),
            "Employee not found").build()
    }
}