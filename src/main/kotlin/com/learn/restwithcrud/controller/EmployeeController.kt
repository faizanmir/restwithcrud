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
class EmployeeController(val employeeService: EmployeeService) {

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
    suspend fun getEmployeeById(@PathVariable("id") id: Int): Employee {
        val emp = employeeService.findById(id)
        if (emp != null) {
            return emp
        } else {
            throw ResponseStatusException(HttpStatus.NOT_FOUND)
        }
    }

    @GetMapping("/search")
    suspend fun searchByJobTitle(@RequestParam("jobTitle") jobTitle: String): Flow<Employee> {
        return employeeService.findByJobTitle(jobTitle)
    }

    @PutMapping("/{id}")
    suspend fun updateEmployee(@PathVariable id: Int, @RequestBody employee: Employee): Employee {
        return employeeService.update(id, employee)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    suspend fun deleteEmployee(@PathVariable("id") id: Int): Unit {
        employeeService.delete(id)
    }

    @PostMapping("/bulk")
    @ResponseStatus(HttpStatus.CREATED)
    suspend fun createMultipleEmployees(@RequestBody employees: List<Employee>): List<Employee> {
        val result = employeeService.create(employees)
        return result.toList()
    }
}