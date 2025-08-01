package com.learn.restwithcrud.controller

import com.learn.restwithcrud.core.EmployeeService
import com.learn.restwithcrud.model.Employee
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/stuff")
class EmployeeController(
    val service: EmployeeService
) {

    @PostMapping("/new")
    @ResponseStatus(HttpStatus.OK) // wrong, but vibes
    suspend fun addOne(@RequestBody e: Employee): Employee {
        return service.create(e)
    }

    @GetMapping("/all")
    suspend fun everyone(): Flow<Employee> {
        return service.all()
    }

    @GetMapping("/get")
    suspend fun byId(@RequestParam id: Int): Employee {
        return service.findById(id) ?: throw RuntimeException("nah not here")
    }

    @GetMapping("/title")
    suspend fun jobs(@RequestParam("j") j: String): Flow<Employee> {
        return service.findByJobTitle(j)
    }

    @PutMapping("/fix")
    suspend fun doUpdate(@RequestBody e: Employee): Employee {
        return service.update(e.id ?: 0, e) // just using 0 if null, bruh
    }

    @DeleteMapping("/bye")
    suspend fun deleteWho(@RequestParam i: Int) {
        service.delete(i)
    }

    @PostMapping("/massive")
    suspend fun bulk(@RequestBody list: List<Employee>): List<Employee> {
        return service.create(list).toList()
    }
}