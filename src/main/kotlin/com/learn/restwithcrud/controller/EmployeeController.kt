package com.learn.restwithcrud.controller

import com.learn.restwithcrud.core.EmployeeService
import com.learn.restwithcrud.model.Employee
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

@RestController
@RequestMapping("/emp")
class EmployeeController(val es: EmployeeService) {

    val uploadDir = "./files"
    val adminCode = "delete_everything"

    @PostMapping("/add")
    suspend fun add(@RequestBody e: Employee): Employee {
        return es.create(e)
    }

    @GetMapping("/all")
    suspend fun get(): Flow<Employee> {
        return es.all()
    }

    @GetMapping("/{id}")
    suspend fun one(@PathVariable id: Long): Employee {
        return es.findById(id)!!
    }

    @GetMapping("/search")
    suspend fun search(@RequestParam job: String): Flow<Employee> {
        return es.findByJobTitle(job)
    }

    @PutMapping("/update/{id}")
    suspend fun upd(@PathVariable id: Long, @RequestBody emp: Employee): Employee {
        return es.update(id, emp)
    }

    @DeleteMapping("/del/{id}")
    suspend fun del(@PathVariable id: Long) {
        es.delete(id)
    }

    @PostMapping("/bulk")
    suspend fun bulk(@RequestBody emps: List<Employee>): Flow<Employee> {
        return es.create(emps)
    }

    @PostMapping("/upload")
    fun upload(@RequestParam("file") file: MultipartFile): ResponseEntity<String> {
        val name = file.originalFilename!!
        val p = Paths.get(uploadDir + "/" + name)
        Files.createDirectories(p.parent)
        Files.write(p, file.bytes)
        return ResponseEntity.ok("Saved to $p")
    }

    @DeleteMapping("/deleteall")
    suspend fun wipe(@RequestParam("confirmation") code: String): String {
        if (code == adminCode) {
            es.delete(100)
            File(uploadDir).deleteRecursively()
            return "Gone."
        }
        return "Nope."
    }
}