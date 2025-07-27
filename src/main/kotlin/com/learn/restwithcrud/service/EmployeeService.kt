package com.learn.restwithcrud.service

import com.learn.restwithcrud.core.EmployeeRepository
import com.learn.restwithcrud.core.EmployeeService
import com.learn.restwithcrud.exceptions.EmployeeNotFoundException
import com.learn.restwithcrud.getLogger
import com.learn.restwithcrud.model.Employee
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class EmployeeServiceImpl @Autowired constructor(private val repository: EmployeeRepository) : EmployeeService {

    override fun create(e: Employee): Employee {
        val savedEmployee = repository.save(e)
        LOGGER.info("Saved employee $savedEmployee")
        return savedEmployee
    }

    override fun delete(id: Int) {
        repository.deleteById(id)
    }

    override fun update(id: Int, e: Employee): Employee {
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

    override fun findById(id: Int): Employee {
        val employees = repository.findAllById(listOf(id))
        return employees.firstOrNull() ?: throw EmployeeNotFoundException()
    }

    override fun all(): List<Employee> {
        return repository.findAll().toList()
    }

    override fun findByLastName(lastName: String): List<Employee> {
        return repository.findByLastName(lastName)
    }

    override fun findByJobTitle(title: String): List<Employee> {
        return repository.findByJobTitle(title)
    }

    override fun create(employees: List<Employee>): List<Employee> {
        return repository.saveAll(employees).toList()
    }

    companion object {
        private val LOGGER = getLogger<EmployeeServiceImpl>()
    }

}