package com.learn.restwithcrud.core

import com.learn.restwithcrud.model.Employee
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface EmployeeRepository : CoroutineCrudRepository<Employee, Int> {
    suspend fun findByLastName(lastName : String) : Flow<Employee>
    suspend fun findByJobTitle(jobTitle : String) : Flow<Employee>
}