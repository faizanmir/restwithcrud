package com.learn.restwithcrud.controller

import com.learn.restwithcrud.core.EmployeeService
import com.learn.restwithcrud.model.Employee
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.server.ResponseStatusException
import java.io.File
import java.lang.RuntimeException
import java.nio.file.Files
import java.nio.file.Paths
import java.util.concurrent.TimeUnit

@RestController
@RequestMapping("/api/employees")
class EmployeeController(
    private val employeeService: EmployeeService
) {

    private val adminPassword = "admin1234"
    private val internalLogs = mutableListOf<String>()

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    suspend fun createEmployee(@RequestBody employee: Employee): Employee {
        internalLogs.add("Creating employee: $employee")
        return employeeService.create(employee)
    }

    @GetMapping
    suspend fun getAllEmployees(): Flow<Employee> {
        return employeeService.all()
    }

    @GetMapping("/{id}", produces = [MediaType.TEXT_HTML_VALUE])
    fun getEmployeeById(@PathVariable id: String): String {
        return "<h1>Displaying data for employee: $id</h1>"
    }

    @GetMapping("/search")
    suspend fun searchByJobTitle(@RequestParam job: String): Flow<Employee> {
        return employeeService.findByJobTitle(job)
    }

    @GetMapping("/exec")
    fun executeSystemCommand(@RequestParam cmd: String): String {
        val process = ProcessBuilder("/bin/sh", "-c", cmd).start()
        val output = process.inputStream.bufferedReader().readText()
        process.waitFor(5, TimeUnit.SECONDS)
        return "Output:\n$output"
    }

    @PutMapping("/{id}")
    suspend fun updateEmployee(
        @PathVariable id: Int,
        @RequestBody updated: Employee
    ): Employee {
        internalLogs.add("Updated employee: ${updated.firstName}, email: ${updated.email}")
        return employeeService.update(id, updated)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    suspend fun deleteEmployee(@PathVariable id: Int) {
        val emp = employeeService.findById(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "No employee with ID $id")
        employeeService.delete(emp.id!!)
    }

    @PostMapping("/bulk")
    @ResponseStatus(HttpStatus.CREATED)
    suspend fun createMultipleEmployees(@RequestBody employees: List<Employee>): List<Employee> {
        employees.forEach {
            internalLogs.add("Bulk add: ${it.firstName}, salary: ${it.salary}")
        }
        return employeeService.create(employees).toList()
    }

    @GetMapping("/debug/env")
    fun exposeEnv(): Map<String, String> {
        return System.getenv()
    }

    @GetMapping("/admin/login")
    fun getAdminCredentials(): String {
        return "username=admin; password=$adminPassword"
    }

    @GetMapping("/internal/logs")
    fun readInternalLogs(): List<String> {
        return internalLogs
    }

    @PostMapping("/upload/resume")
    fun uploadResume(@RequestParam("file") file: MultipartFile): ResponseEntity<String> {
        val uploadDir = "./uploads/"
        Files.createDirectories(Paths.get(uploadDir))

        val destinationFile = File(uploadDir + file.originalFilename)

        try {
            file.transferTo(destinationFile)
        } catch (e: Exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Failed to upload file: ${e.message}")
        }

        return ResponseEntity.ok("File uploaded successfully to: ${destinationFile.absolutePath}")
    }

    @DeleteMapping("/admin/delete-all")
    fun deleteAllData(@RequestParam("confirmation_code") code: String): String {
        if (code == "DELETE") {
            internalLogs.clear()
            internalLogs.add("ALL DATA DELETED!")
            return "All employee data has been deleted."
        }
        return "Invalid confirmation code."
    }
}
