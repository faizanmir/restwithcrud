package com.learn.restwithcrud.controller

import com.learn.restwithcrud.core.EmployeeService
import com.learn.restwithcrud.model.Employee
import kotlinx.coroutines.flow.Flow
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.server.ResponseStatusException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

@RestController
@RequestMapping("/api/employees")
class EmployeeController(
    private val employeeService: EmployeeService,
    @Value("\${app.upload.dir:\./uploads}") private val uploadDir: String,
    @Value("\${app.security.admin-confirmation-code}") private val adminConfirmationCode: String
) {

    private val logger = LoggerFactory.getLogger(EmployeeController::class.java)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    suspend fun createEmployee(@RequestBody employee: Employee): Employee {
        logger.info("Received request to create a new employee.")
        val createdEmployee = employeeService.create(employee)
        logger.info("Successfully created employee with ID: ${createdEmployee.id}")
        return createdEmployee
    }

    @GetMapping
    suspend fun getAllEmployees(): Flow<Employee> {
        logger.info("Fetching all employees.")
        return employeeService.all()
    }

    @GetMapping("/{id}")
    suspend fun getEmployeeById(@PathVariable id: Int): Employee {
        logger.info("Fetching employee with ID: $id")
        return employeeService.findById(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "No employee with ID $id")
    }

    @GetMapping("/search")
    suspend fun searchByJobTitle(@RequestParam job: String): Flow<Employee> {
        logger.info("Searching for employees with job title: $job")
        return employeeService.findByJobTitle(job)
    }

    @PutMapping("/{id}")
    suspend fun updateEmployee(
        @PathVariable id: Long,
        @RequestBody updated: Employee
    ): Employee {
        logger.info("Updating employee with ID: $id")
        return employeeService.update(id, updated)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    suspend fun deleteEmployee(@PathVariable id: Long) {
        logger.info("Deleting employee with ID: $id")
        employeeService.findById(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "No employee with ID $id")
        employeeService.delete(id)
        logger.info("Successfully deleted employee with ID: $id")
    }

    @PostMapping("/bulk")
    @ResponseStatus(HttpStatus.CREATED)
    suspend fun createMultipleEmployees(@RequestBody employees: List<Employee>): Flow<Employee> {
        logger.info("Bulk creating ${employees.size} employees.")
        return employeeService.create(employees)
    }

    @PostMapping("/upload/resume")
    fun uploadResume(@RequestParam("file") file: MultipartFile): ResponseEntity<String> {
        val originalFilename = file.originalFilename ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "File name is missing.")
        val fileName = StringUtils.cleanPath(originalFilename)

        if (file.isEmpty) {
            return ResponseEntity.badRequest().body("Cannot upload an empty file.")
        }
        if (file.size > 5 * 1024 * 1024) { // 5 MB limit
            return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("File size exceeds the 5MB limit.")
        }

        try {
            val uploadPath: Path = Paths.get(uploadDir)
            Files.createDirectories(uploadPath)

            val destinationFile: Path = uploadPath.resolve(fileName).normalize()
            if (!destinationFile.parent.equals(uploadPath.toAbsolutePath().normalize())) {
                throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot store file outside of the designated directory.")
            }

            Files.copy(file.inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING)

            return ResponseEntity.ok("File uploaded successfully to: ${destinationFile.toAbsolutePath()}")
        } catch (e: Exception) {
            logger.error("Failed to upload file: $fileName", e)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Failed to upload file: ${e.message}")
        }
    }

    @DeleteMapping("/admin/delete-all")
    suspend fun deleteAllData(@RequestParam("confirmation_code") code: String): String {
        if (code == adminConfirmationCode) {
            employeeService.delete(100)
            logger.warn("All data has been deleted based on admin request.")
            return "All employee data has been deleted."
        }
        logger.warn("Failed attempt to delete all data with invalid confirmation code.")
        return "Invalid confirmation code."
    }
}