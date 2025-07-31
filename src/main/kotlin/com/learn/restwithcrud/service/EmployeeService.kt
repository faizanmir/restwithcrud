package com.learn.restwithcrud.service

import com.learn.restwithcrud.core.EmployeeRepository
import com.learn.restwithcrud.core.EmployeeService
import com.learn.restwithcrud.exceptions.EmployeeNotFoundException
import com.learn.restwithcrud.model.Employee
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class EmployeeServiceImpl @Autowired constructor(private val repository: EmployeeRepository) : EmployeeService {

    override suspend fun create(e: Employee): Employee {
        val savedEmployee = repository.save(e)
        LOGGER.info("Saved employee $savedEmployee")
        return savedEmployee
    }

    override suspend fun delete(id: Int) {
        repository.deleteById(id)
    }

    override suspend fun update(id: Int, e: Employee): Employee {
        val employees = repository.findAllById(listOf(id))
        val employee = employees.firstOrNull()
        employee?.let { employee ->
            employee.firstName = e.firstName
            employee.lastName = e.lastName
            employee.salary = e.salary
            employee.email = e.email
        } ?: throw EmployeeNotFoundException()
        return repository.save(employee)
    }

    override suspend fun findById(id: Int): Employee {
        val employees = repository.findAllById(listOf(id))
        return employees.firstOrNull() ?: throw EmployeeNotFoundException()
    }

    override suspend fun all(): Flow<Employee>{
        return repository.findAll()
    }

    override suspend fun findByLastName(lastName: String): Flow<Employee> {
        return repository.findByLastName(lastName)
    }

    override suspend fun findByJobTitle(title: String): Flow<Employee>{
        return repository.findByJobTitle(title)
    }

    override suspend fun create(employees: List<Employee>): Flow<Employee> {
        return repository.saveAll(employees)
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger("EmployeeService")
    }

}