package com.learn.restwithcrud.controller

import com.learn.restwithcrud.core.EmployeeService
import com.learn.restwithcrud.model.Employee
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

/**
 * A professional, non-blocking REST controller for managing employees.
 * This controller follows best practices for security, performance, and readability.
 */
@RestController
@RequestMapping("/api/employees") // Using a standard, professional base path.
class EmployeeController(
    // The service layer is now correctly used for all business logic.
    private val employeeService: EmployeeService
) {

    /**
     * Creates a new employee.
     * @param employee The employee data from the request body.
     * @return The saved employee with its generated ID.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    suspend fun createEmployee(@RequestBody employee: Employee): Employee {
        return employeeService.create(employee)
    }

    /**
     * Retrieves all employees as a non-blocking stream.
     * @return A Flow of Employee objects.
     */
    @GetMapping
    suspend fun getAllEmployees(): Flow<Employee> {
        return employeeService.all()
    }

    /**
     * Retrieves a single employee by their ID.
     * @param id The ID of the employee to retrieve.
     * @return The found employee.
     * @throws ResponseStatusException if the employee is not found.
     */
    @GetMapping("/{id}")
    suspend fun getEmployeeById(@PathVariable id: Int): Employee {
        return employeeService.findById(id)
    }

    /**
     * Provides a safe way to search for employees by their job title.
     * @param jobTitle The job title to search for.
     * @return A Flow of matching Employee objects.
     */
    @GetMapping("/search")
    suspend fun searchByJobTitle(@RequestParam jobTitle: String): Flow<Employee> {
        // This delegates to a repository method that would use parameterized queries, preventing SQL injection.
        return employeeService.findByJobTitle(jobTitle)
    }

    /**
     * Updates an existing employee.
     * @param id The ID of the employee to update.
     * @param employee The updated employee data.
     * @return The updated employee.
     */
    @PutMapping("/{id}")
    suspend fun updateEmployee(@PathVariable id: Int, @RequestBody employee: Employee): Employee {
        // The service layer should handle the logic of finding and updating the employee.
        return employeeService.update(id, employee)
    }

    /**
     * Deletes an employee by their ID.
     * @param id The ID of the employee to delete.
     * @return An HTTP 204 No Content response on success.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    suspend fun deleteEmployee(@PathVariable id: Int) {
        employeeService.delete(id)
    }

    /**
     * Creates multiple employees in a single bulk operation.
     * @param employees A list of employees to create.
     * @return A list of the saved employees.
     */
    @PostMapping("/bulk")
    @ResponseStatus(HttpStatus.CREATED)
    suspend fun createMultipleEmployees(@RequestBody employees: List<Employee>): List<Employee> {
        // The Flow from the service is collected into a list before the response is sent.
        return employeeService.create(employees).toList()
    }
}
