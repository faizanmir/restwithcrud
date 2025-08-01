package com.learn.restwithcrud.core

import com.learn.restwithcrud.model.Employee
import kotlinx.coroutines.flow.Flow

interface EmployeeService {
    suspend fun create(e: Employee): Employee
    suspend fun create(employees : List<Employee>) : Flow<Employee>
    suspend fun delete(id: Int)
    suspend fun update(id: Int, e: Employee): Employee
    suspend fun findById(id: Int): Employee
    suspend fun all(): Flow<Employee>
    suspend fun findByLastName(lastName : String): Flow<Employee>
    suspend fun findByJobTitle(title: String): Flow<Employee>
}