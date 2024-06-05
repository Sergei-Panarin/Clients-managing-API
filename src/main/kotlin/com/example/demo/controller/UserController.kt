package com.example.demo.controller

import com.example.demo.dto.UserDto
import com.example.demo.security.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@Tag(name = "User Management System")
@RestController
@RequestMapping("/users")
class UserController(private val userService: UserService) {

    @Operation(summary="Create a new user with role USER")
    @PostMapping("/user")
    fun createUser(@RequestBody userDto: UserDto): UserDto {
        return userService.createUser(userDto, "USER")
    }

    @Operation(summary="Create a new user with role ADMIN")
    @PostMapping("/admin")
    fun createAdmin(@RequestBody userDto: UserDto): UserDto {
        return userService.createUser(userDto, "ADMIN")
    }
}
