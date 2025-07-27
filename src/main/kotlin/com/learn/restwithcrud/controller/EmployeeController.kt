package com.learn.restwithcrud.controller

import com.learn.restwithcrud.core.EmployeeService
import com.learn.restwithcrud.model.Employee
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/employees")
class EmployeeController(
    private val employeeService: EmployeeService
) {

    @PostMapping
    fun createEmployee(@RequestBody employee: Employee): ResponseEntity<Employee> {
        val saved = employeeService.create(employee)
        return ResponseEntity.status(HttpStatus.CREATED).body(saved)
    }

    @GetMapping
    fun getAllEmployees(
    ): List<Employee> {
        return employeeService.all()
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Int): ResponseEntity<Employee> {
        val employee = employeeService.findById(id)
        return ResponseEntity.ok(employee)
    }

    @GetMapping("/by-lastname/{lastName}")
    fun getByLastName(@PathVariable lastName: String): ResponseEntity<List<Employee>> {
        val employees = employeeService.findByLastName(lastName)
        return ResponseEntity.ok(employees)
    }

    @GetMapping("/by-jobtitle/{jobTitle}")
    fun getByJobTitle(@PathVariable jobTitle: String): ResponseEntity<List<Employee>> {
        val employees = employeeService.findByJobTitle(jobTitle)
        return ResponseEntity.ok(employees)
    }


    @PutMapping("/{id}")
    fun update(@PathVariable id: Int, @RequestBody updated: Employee): ResponseEntity<Employee> {
        val updatedEmployee = employeeService.update(id, updated)
        return ResponseEntity.ok(updatedEmployee)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Int): ResponseEntity<Void> {
        employeeService.delete(id)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/bulk")
    fun bulkInsert(@RequestBody employees: List<Employee>): ResponseEntity<List<Employee>> {
        val savedEmployees = employeeService.create(employees)
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEmployees)
    }
}