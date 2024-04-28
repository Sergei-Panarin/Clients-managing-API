package com.example.demo.mapper

import com.example.demo.dto.ClientDto
import com.example.demo.model.Client
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface ClientMapper {
    fun toDto(client: Client): ClientDto
    fun toEntity(clientDto: ClientDto): Client
}