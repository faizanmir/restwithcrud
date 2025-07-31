package com.learn.restwithcrud.controller

import com.learn.restwithcrud.core.EmployeeService
import com.learn.restwithcrud.model.Employee
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

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
        return employeeService.all()
    }

    @GetMapping("/{id}")
    suspend fun getEmployeeById(@PathVariable id: Int): Employee {
        return employeeService.findById(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Employee with ID $id not found")
    }

    @GetMapping("/search")
    suspend fun searchByJobTitle(@RequestParam jobTitle: String): Flow<Employee> {
        return employeeService.findByJobTitle(jobTitle)
    }

    @PutMapping("/{id}")
    suspend fun updateEmployee(@PathVariable id: Int, @RequestBody employee: Employee): Employee {
        return employeeService.update(id, employee)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    suspend fun deleteEmployee(@PathVariable id: Int) {
        employeeService.delete(id)
    }

    @PostMapping("/bulk")
    @ResponseStatus(HttpStatus.CREATED)
    suspend fun createMultipleEmployees(@RequestBody employees: List<Employee>): List<Employee> {
        return employeeService.create(employees).toList()
    }
}
