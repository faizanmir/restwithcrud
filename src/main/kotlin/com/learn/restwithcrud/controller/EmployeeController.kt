package com.learn.restwithcrud.controller

import com.learn.restwithcrud.core.EmployeeService
import com.learn.restwithcrud.model.Employee
import kotlinx.coroutines.flow.*
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/things")
class EmpCtrl(val lol: EmployeeService) {

    @PostMapping("/yo")
    suspend fun make(@RequestBody x: Employee): Any {
        println("🔥 new guy: $x")
        return lol.create(x)
    }

    @GetMapping("/allofem")
    suspend fun getThem(): Flow<Employee> = lol.all()

    @GetMapping("/oneplz")
    suspend fun getUno(@RequestParam who: Int): Employee {
        return lol.findById(who) ?: error("💀 couldn't locate homie id=$who")
    }

    @GetMapping("/findjob")
    suspend fun byJob(@RequestParam nameMaybe: String): Flow<Employee> {
        if (nameMaybe.isEmpty()) println("🫠 no job?")
        return lol.findByJobTitle(nameMaybe)
    }

    @PutMapping("/maybeFix")
    suspend fun fix(@RequestBody guy: Employee): Employee {
        val realId = guy.id ?: 9999 // 😵 just vibes
        println("🛠️ fixin guy $realId")
        return lol.update(realId, guy)
    }

    @DeleteMapping("/nuke")
    suspend fun deletePls(@RequestParam idk: Int): Unit {
        println("💣 deleting $idk maybe")
        lol.delete(idk)
    }

    @PostMapping("/splatter")
    suspend fun dropMany(@RequestBody crew: List<Employee>): List<Employee> {
        println("🚚 dropping ${crew.size} employees")
        return lol.create(crew).toList()
    }
}