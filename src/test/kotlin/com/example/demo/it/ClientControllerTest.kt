package com.example.demo.it

import com.example.demo.dto.ClientDto
import com.example.demo.getClientDto
import com.example.demo.mapper.ClientMapper
import com.example.demo.repository.ClientRepository
import com.example.demo.service.ClientService
import com.example.demo.wireMockResponse
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.junit5.WireMockTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest
@WireMockTest(httpPort = 8081)
@ActiveProfiles("test")
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
    private lateinit var clientMapper: ClientMapper
    @Autowired
    private lateinit var clientService: ClientService

    @BeforeEach
    fun setup() {
        WireMock.stubFor(WireMock.get(WireMock.urlMatching("/\\?name=.*"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody(wireMockResponse)))
    }

    @AfterEach
    fun tearDown() {
        clientRepository.deleteAll()
    }

    @Test
    @WithMockUser(username = "test", roles = ["USER"])
    fun `get clients should return list of clients`() {
        val clientDto = getClientDto()
        val client = clientMapper.toEntity(clientDto)
        val savedClient = clientService.addClient(client)
        val clients = listOf(clientMapper.toDto(savedClient))

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
    @WithMockUser(username = "test", roles = ["ADMIN"])
    fun `add client with ROLE_ADMIN should return added client`() {
        val clientDto = getClientDto().apply { firstName = "test" }
        val clientJson = objectMapper.writeValueAsString(clientDto)

        mockMvc.perform(post("/clients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(clientJson))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.firstName").value(clientDto.firstName))
            .andExpect(jsonPath("$.lastName").value(clientDto.lastName))
            .andExpect(jsonPath("$.email").value(clientDto.email))
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
    fun `add client with ROLE_ADMIN should return bad request when client data is invalid`() {
        val clientDto = getClientDto().apply { email = "invalid email" }
        val clientJson = objectMapper.writeValueAsString(clientDto)

        mockMvc.perform(post("/clients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(clientJson))
            .andExpect(status().isBadRequest)
    }

    @Test
    @WithMockUser(username = "test", roles = ["ADMIN"])
    fun `add client with ROLE_ADMIN should throw exception when gender service response is delayed`() {
        val clientDto = getClientDto()
        val clientJson = objectMapper.writeValueAsString(clientDto)

        WireMock.stubFor(WireMock.get(WireMock.urlMatching("/\\?name=.*"))
            .willReturn(aResponse()
                .withFixedDelay(4000)
                .withHeader("Content-Type", "application/json")
                .withBody(wireMockResponse)))

        val result = mockMvc.perform(post("/clients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(clientJson))

        result.andExpect(status().isInternalServerError)
    }

    @Test
    @WithMockUser(username = "test", roles = ["USER"])
    fun `get client should return client with given id`() {
        val clientDto = getClientDto()
        val client = clientMapper.toEntity(clientDto)
        val savedClient = clientService.addClient(client)
        val clientJson = objectMapper.writeValueAsString(clientMapper.toDto(savedClient))

        mockMvc.perform(get("/clients/${savedClient.id}")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(content().json(clientJson))
    }

    @Test
    @WithMockUser(username = "test", roles = ["USER"])
    fun `get client should return not found when client does not exist`() {
        mockMvc.perform(get("/clients/999")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound)
    }

    @Test
    @WithMockUser(username = "test", roles = ["ADMIN"])
    fun `update client with ROLE_ADMIN should return updated client`() {

        val clientDto = getClientDto()
        val client = clientMapper.toEntity(clientDto)
        val savedClient = clientService.addClient(client)

        val updatedClientDto = clientDto.copy(firstName = "updatedName", lastName = "updatedLastName")
        val clientJson = objectMapper.writeValueAsString(updatedClientDto)

        mockMvc.perform(put("/clients/${savedClient.id}")
            .contentType(MediaType.APPLICATION_JSON)
            .content(clientJson))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.firstName").value(updatedClientDto.firstName))
            .andExpect(jsonPath("$.lastName").value(updatedClientDto.lastName))
    }

    @Test
    @WithMockUser(username = "test", roles = ["ADMIN"])
    fun `update client with ROLE_ADMIN should return not found when client does not exist`() {
        val clientDto = getClientDto()
        val clientJson = objectMapper.writeValueAsString(clientDto)

        mockMvc.perform(put("/clients/999")
            .contentType(MediaType.APPLICATION_JSON)
            .content(clientJson))
            .andExpect(status().isNotFound)
    }

    @Test
    @WithMockUser(username = "test", roles = ["ADMIN"])
    fun `delete client with ROLE_ADMIN should delete client with given id`() {
        val clientDto = getClientDto()
        val client = clientMapper.toEntity(clientDto)
        val savedClient = clientService.addClient(client)

        mockMvc.perform(delete("/clients/${savedClient.id}")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)

        mockMvc.perform(get("/clients/${savedClient.id}")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound)
    }

    @Test
    @WithMockUser(username = "test", roles = ["USER"])
    fun `delete client with ROLE_USER should access denied`() {

        mockMvc.perform(delete("/clients/1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden)
    }

    @Test
    @WithMockUser(username = "test", roles = ["USER"])
    fun `search clients by map should return list of clients`() {
        val clientDto = getClientDto()
        val client = clientMapper.toEntity(clientDto)
        val savedClient = clientService.addClient(client)
        val clients = listOf(clientMapper.toDto(savedClient))

        val result = mockMvc.perform(get("/clients/search-map")
            .param("firstName", clientDto.firstName)
            .param("lastName", clientDto.lastName)
            .param("email", clientDto.email)
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
    @WithMockUser(username = "test", roles = ["USER"])
    fun `search clients by string should return list of clients`() {
        val clientDto = getClientDto()
        val client = clientMapper.toEntity(clientDto)
        val savedClient = clientService.addClient(client)
        val clients = listOf(clientMapper.toDto(savedClient))

        val result = mockMvc.perform(get("/clients/search-string")
            .param("search", clientDto.firstName)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andReturn()

        val typeRef = object : TypeReference<RestResponsePage<ClientDto>>() {}
        val response: RestResponsePage<ClientDto> =
            jacksonObjectMapper().readValue(result.response.contentAsString, typeRef)
        val content = response.content

        assertThat(clients).isEqualTo(content)
    }
}