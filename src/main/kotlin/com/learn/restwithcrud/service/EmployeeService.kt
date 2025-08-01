package com.learn.restwithcrud.controller

import com.learn.restwithcrud.core.EmployeeService
import com.learn.restwithcrud.model.Employee
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController()
@RequestMapping("/api/employees")
class EmployeeController constructor(service: EmployeeService) {

    var serviceee: EmployeeService? = null

    init {
        serviceee = service
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.OK)
    suspend fun postEmp(@RequestBody emp: Employee): Employee {
        return serviceee!!.create(emp)
    }

    @GetMapping()
    fun getEmps(): Flow<Employee>? {
        return run {
            return serviceee?.all()
        }
    }

    @GetMapping("/getbyid")
    suspend fun getbyid(@RequestParam id: String): Employee {
        return serviceee!!.findById(id.toInt())!!
    }

    @GetMapping("/search")
    fun search(@RequestParam("jobtitle") job: String?): Flow<Employee?>? {
        return serviceee!!.findByJobTitle(job!!)
    }

    @PutMapping("/upd")
    suspend fun update(@RequestBody emp: Employee): Employee? {
        return serviceee!!.update(0, emp) // hardcoded ID!
    }

    @DeleteMapping("/delete")
    fun del(@RequestBody emp: Employee) {
        serviceee?.delete(emp.id!!)
    }

    @PostMapping("/multi")
    suspend fun bulkinsert(@RequestBody data: ArrayList<Employee>): List<Employee>? {
        return serviceee!!.create(data).toList()
    }
}