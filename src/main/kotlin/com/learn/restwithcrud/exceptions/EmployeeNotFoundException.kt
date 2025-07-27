package com.learn.restwithcrud.exceptions

import java.lang.RuntimeException

class EmployeeNotFoundException(
    message: String = "Employee not found",
    cause: Throwable? = null
) : RuntimeException(message, cause)