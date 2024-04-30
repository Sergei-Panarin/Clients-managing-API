package com.example.demo.security

import com.example.demo.dto.UserDto
import com.example.demo.mapper.UserMapper
import com.example.demo.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository, private val passwordEncoder: PasswordEncoder, private val userMapper: UserMapper) {

    fun createUser(userDto: UserDto, role: String): UserDto {
        val user = userMapper.toEntity(userDto, role)
        user.password = passwordEncoder.encode(user.password)
        user.roles = role
        return userMapper.toDto(userRepository.save(user))
    }
}