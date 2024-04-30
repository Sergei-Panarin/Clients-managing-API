package com.example.demo.controller

import com.example.demo.dto.UserDto
import com.example.demo.security.UserService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(private val userService: UserService) {

    @PostMapping("/user")
    fun createUser(@RequestBody userDto: UserDto): UserDto {
        return userService.createUser(userDto, "USER")
    }

    @PostMapping("/admin")
    fun createAdmin(@RequestBody userDto: UserDto): UserDto {
        return userService.createUser(userDto, "ADMIN")
    }
}