package com.learn.restwithcrud.core

import com.learn.restwithcrud.model.Employee
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface EmployeeRepository : CrudRepository<Employee, Int> {
    fun findByLastName(lastName : String) : List<Employee>
    fun findByJobTitle(jobTitle : String) : List<Employee>
}