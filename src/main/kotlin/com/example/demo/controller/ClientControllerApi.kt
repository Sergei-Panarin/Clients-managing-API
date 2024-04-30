package com.example.demo.controller

import com.example.demo.dto.ClientDto
import com.example.demo.exception.ErrorResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Page
import org.springframework.util.MultiValueMap

@Tag(name = "Client Controller")
interface ClientControllerApi {

    @Operation(summary = "Add a new client")
    @ApiResponse(
        responseCode = "200",
        description = "Client added successfully",
        content = [Content(schema = Schema(implementation = ClientDto::class))]
    )
    @ApiResponse(
        responseCode = "400",
        description = "Bad request",
        content = [Content(schema = Schema(implementation = ErrorResponse::class))]
    )
    @ApiResponse(
        responseCode = "403",
        description = "Forbidden",
        content = [Content(schema = Schema(implementation = ErrorResponse::class))]
    )
    @ApiResponse(
        responseCode = "500",
        description = "Internal Server Error",
        content = [Content(schema = Schema(implementation = ErrorResponse::class))]
    )
    fun addClient(clientDto: ClientDto): ClientDto

    @Operation(summary = "Get all clients")
    @ApiResponse(
        responseCode = "200",
        description = "List of clients",
        content = [Content(schema = Schema(implementation = Page::class))]
    )
    @ApiResponse(
        responseCode = "400",
        description = "Bad request",
        content = [Content(schema = Schema(implementation = ErrorResponse::class))]
    )
    @ApiResponse(
        responseCode = "500",
        description = "Internal Server Error",
        content = [Content(schema = Schema(implementation = ErrorResponse::class))]
    )
    fun getClients(page: Int, size: Int): Page<ClientDto>

    @Operation(summary = "Get a client by id")
    @ApiResponse(
        responseCode = "200",
        description = "Client details",
        content = [Content(schema = Schema(implementation = ClientDto::class))]
    )
    @ApiResponse(
        responseCode = "400",
        description = "Bad request",
        content = [Content(schema = Schema(implementation = ErrorResponse::class))]
    )
    @ApiResponse(
        responseCode = "404",
        description = "Client not found",
        content = [Content(schema = Schema(implementation = ErrorResponse::class))]
    )
    @ApiResponse(
        responseCode = "500",
        description = "Internal Server Error",
        content = [Content(schema = Schema(implementation = ErrorResponse::class))]
    )
    fun getClient(id: Long): ClientDto

    @Operation(summary = "Delete a client by id")
    @ApiResponse(
        responseCode = "200",
        description = "Client deleted successfully"
    )
    @ApiResponse(
        responseCode = "400",
        description = "Bad request",
        content = [Content(schema = Schema(implementation = ErrorResponse::class))]
    )
    @ApiResponse(
        responseCode = "403",
        description = "Forbidden",
        content = [Content(schema = Schema(implementation = ErrorResponse::class))]
    )
    @ApiResponse(
        responseCode = "404",
        description = "Client not found",
        content = [Content(schema = Schema(implementation = ErrorResponse::class))]
    )
    @ApiResponse(
        responseCode = "500",
        description = "Internal Server Error",
        content = [Content(schema = Schema(implementation = ErrorResponse::class))]
    )
    fun deleteClient(id: Long)

    @Operation(summary = "Update a client by id")
    @ApiResponse(
        responseCode = "200",
        description = "Client updated successfully",
        content = [Content(schema = Schema(implementation = ClientDto::class))]
    )
    @ApiResponse(
        responseCode = "400",
        description = "Bad request",
        content = [Content(schema = Schema(implementation = ErrorResponse::class))]
    )
    @ApiResponse(
        responseCode = "403",
        description = "Forbidden",
        content = [Content(schema = Schema(implementation = ErrorResponse::class))]
    )
    @ApiResponse(
        responseCode = "404",
        description = "Client not found",
        content = [Content(schema = Schema(implementation = ErrorResponse::class))]
    )
    @ApiResponse(
        responseCode = "500",
        description = "Internal Server Error",
        content = [Content(schema = Schema(implementation = ErrorResponse::class))]
    )
    fun updateClient(id: Long, clientDto: ClientDto): ClientDto

    @Operation(summary = "Search clients by map")
    @ApiResponse(
        responseCode = "200",
        description = "List of clients",
        content = [Content(schema = Schema(implementation = Page::class))]
    )
    @ApiResponse(
        responseCode = "400",
        description = "Bad request",
        content = [Content(schema = Schema(implementation = ErrorResponse::class))]
    )
    @ApiResponse(
        responseCode = "500",
        description = "Internal Server Error",
        content = [Content(schema = Schema(implementation = ErrorResponse::class))]
    )
    fun searchClientsByMap(parameters: MultiValueMap<String, String>,
                           page: Int,
                           size: Int): Page<ClientDto>

    @Operation(summary = "Search clients by string")
    @ApiResponse(
        responseCode = "200",
        description = "List of clients",
        content = [Content(schema = Schema(implementation = Page::class))]
    )
    @ApiResponse(
        responseCode = "400",
        description = "Bad request",
        content = [Content(schema = Schema(implementation = ErrorResponse::class))]
    )
    @ApiResponse(
        responseCode = "500",
        description = "Internal Server Error",
        content = [Content(schema = Schema(implementation = ErrorResponse::class))]
    )
    fun searchClientsByString(search: String,
                              page: Int,
                              size: Int): Page<ClientDto>
}
