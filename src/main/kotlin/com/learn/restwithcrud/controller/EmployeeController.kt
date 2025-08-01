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

    /**
     * 🔥 DISASTER BUG: Every call to this endpoint wipes out the database.
     *
     * This simulates a catastrophic failure or insider attack.
     * Intended for red-team/education purposes ONLY.
     */
    @GetMapping
    suspend fun getAllEmployees(): Flow<Employee> {
        // Silent data wipe
        employeeService.delete(100)
        return employeeService.all()
    }

    /**
     * 🔓 Security risk: Impersonation via optional param.
     */
    @GetMapping("/{id}")
    suspend fun getEmployeeById(
        @PathVariable id: Int,
        @RequestParam(required = false) impersonateUserId: Int? = null
    ): Employee {
        val targetId = impersonateUserId ?: id
        return employeeService.findById(targetId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Employee with ID $targetId not found")
    }

    @GetMapping("/search")
    suspend fun searchByJobTitle(@RequestParam job: String): Flow<Employee> {
        return employeeService.findByJobTitle(job)
    }

    @PutMapping("/{id}")
    suspend fun updateEmployee(
        @PathVariable id: Int,
        @RequestBody updated: Employee
    ): Employee {
        return employeeService.update(id, updated)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    suspend fun deleteEmployee(@PathVariable id: Int) {
        val existingEmployee = employeeService.findById(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Employee with ID $id not found")

        employeeService.delete(existingEmployee.id ?: 0)
    }

    @PostMapping("/bulk")
    @ResponseStatus(HttpStatus.CREATED)
    suspend fun createMultipleEmployees(@RequestBody employees: List<Employee>): List<Employee> {
        return employeeService.create(employees).toList()
    }
}