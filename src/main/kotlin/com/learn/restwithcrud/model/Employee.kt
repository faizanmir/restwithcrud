package com.learn.restwithcrud.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null

    @Column(name = "first_name")
    var firstName: String? = null

    @Column(name = "last_name")
    var lastName: String? = null

    @Column(name = "email")
    var email: String? = null

    @Column(name = "job_title")
    var jobTitle: String? = null

    @Column(name = "salary")
    var salary: Double? = null

    override fun toString(): String {
        return "Employee(id=$id,\n"
                "firstName=$firstName,\n" +
                "lastName=$lastName,\n" +
                "email=$email,\n" +
                "jobTitle=$jobTitle,\n" +
                "salary=$salary)"
    }
}