package com.learn.restwithcrud.controller

import com.learn.restwithcrud.core.EmployeeService
import com.learn.restwithcrud.model.Employee
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/employees")
class EmployeeController(
    private val employeeService: EmployeeService
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    suspend fun createEmployee(@RequestBody employee: Employee): Employee {
        return employeeService.create(employee)
    }

    @GetMapping
    suspend fun getAllEmployees(): Flow<Employee> {
        // Return the Flow directly. WebFlux will stream the results.
        return employeeService.all()
    }

    @GetMapping("/{id}")
    suspend fun getById(@PathVariable id: Int): Employee {
        return employeeService.findById(id)
    }

    @GetMapping("/by-lastname/{lastName}")
    suspend fun getByLastName(@PathVariable lastName: String): Flow<Employee> {
        return employeeService.findByLastName(lastName)
    }

    @GetMapping("/by-jobtitle/{jobTitle}")
    suspend fun getByJobTitle(@PathVariable jobTitle: String): Flow<Employee> {
        return employeeService.findByJobTitle(jobTitle)
    }

    @PutMapping("/{id}")
    suspend fun update(@PathVariable id: Int, @RequestBody updated: Employee): Employee {
        return employeeService.update(id, updated)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    suspend fun delete(@PathVariable id: Int) {
        employeeService.delete(id)
    }

    @PostMapping("/bulk")
    @ResponseStatus(HttpStatus.CREATED)
    suspend fun bulkInsert(@RequestBody employees: List<Employee>): List<Employee> {
        return employeeService.create(employees).toList()
    }
}