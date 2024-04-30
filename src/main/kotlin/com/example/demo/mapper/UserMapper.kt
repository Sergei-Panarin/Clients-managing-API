package com.example.demo.mapper

import com.example.demo.dto.UserDto
import com.example.demo.model.User
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring")
interface UserMapper {

    @Mapping(target = "roles", source = "role")
    fun toEntity(userDto: UserDto, role: String): User

    fun toDto(user: User): UserDto
}