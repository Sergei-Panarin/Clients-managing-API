package com.example.demo.it

import com.example.demo.dto.ClientDto
import com.example.demo.getClient
import com.example.demo.getClientDto
import com.example.demo.getValidGenderizeResponse
import com.example.demo.repository.ClientRepository
import com.example.demo.service.GenderizeService
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ClientControllerTest {

    companion object {
        @Container
        val postgreSQLContainer: PostgreSQLContainer<*> = PostgreSQLContainer<Nothing>("postgres:15.1")
            .apply {
                withDatabaseName("test")
                withUsername("test")
                withPassword("test")
            }

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl)
            registry.add("spring.datasource.username", postgreSQLContainer::getUsername)
            registry.add("spring.datasource.password", postgreSQLContainer::getPassword)
            registry.add("spring.flyway.schemas") { "public" }
        }
    }

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var clientRepository: ClientRepository

    @Autowired
    private lateinit var genderService: GenderizeService

    @BeforeEach
    fun setup() {
        val client = getClient()
        clientRepository.save(client)
    }

    @Test
    @WithMockUser(username = "test", roles = ["USER"])
    fun `get clients should return list of clients`() {
        val clientDto = getClientDto()
        val clients = listOf(clientDto)

        val result = mockMvc.perform(get("/clients")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andReturn()

        val typeRef = object : TypeReference<RestResponsePage<ClientDto>>() {}
        val response: RestResponsePage<ClientDto> =
            jacksonObjectMapper().readValue(result.response.contentAsString, typeRef)
        val content = response.content

        assertThat(clients).isEqualTo(content)
    }

    @Test
    fun `get clients without authentication should return unauthorized`() {
        mockMvc.perform(get("/clients")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized)
    }

    @Test
    @WithMockUser(username = "test", roles = ["USER"])
    fun `add client with ROLE_USER should return access denied`() {
        val clientDto = getClientDto()
        val clientJson = objectMapper.writeValueAsString(clientDto)

        mockMvc.perform(post("/clients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(clientJson))
            .andExpect(status().isForbidden)
    }

    @Test
    @WithMockUser(username = "test", roles = ["ADMIN"])
    fun `add client with ROLE_ADMIN should return added client`() {
        genderService = mock(GenderizeService::class.java)
        `when`(genderService.getGender(anyString())).thenReturn(getValidGenderizeResponse())
        val clientDto = getClientDto()
        val clientJson = objectMapper.writeValueAsString(clientDto)

        mockMvc.perform(post("/clients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(clientJson))
            .andExpect(status().isOk)
            .andExpect(content().json(clientJson))
    }

    @Test
    @WithMockUser(username = "test", roles = ["USER"])
    fun `get client should return client with given id`() {
        val clientDto = getClientDto()
        val clientJson = objectMapper.writeValueAsString(clientDto)

        mockMvc.perform(get("/clients/1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(content().json(clientJson))
    }

    @Test
    @WithMockUser(username = "test", roles = ["USER"])
    fun `delete client with ROLE_USER should access denied`() {

        mockMvc.perform(delete("/clients/1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden)
    }

    @Test
    @WithMockUser(username = "test", roles = ["ADMIN"])
    fun `delete client with ROLE_ADMIN should delete client with given id`() {
        mockMvc.perform(get("/clients/1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)

        mockMvc.perform(delete("/clients/1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)

        mockMvc.perform(get("/clients/1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound)
    }
}