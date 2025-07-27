package com.learn.restwithcrud.core

import com.learn.restwithcrud.model.Employee

interface EmployeeService {
    fun create(e: Employee): Employee
    fun create(employees : List<Employee>) : List<Employee>
    fun delete(id: Int)
    fun update(id: Int, e: Employee): Employee
    fun findById(id: Int): Employee
    fun all(): List<Employee>
    fun findByLastName(lastName : String): List<Employee>
    fun findByJobTitle(title: String):List<Employee>

}