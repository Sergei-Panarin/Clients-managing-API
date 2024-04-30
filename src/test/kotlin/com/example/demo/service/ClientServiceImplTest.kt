package com.example.demo.service

import com.example.demo.exception.ServiceException
import com.example.demo.getClient
import com.example.demo.getInvalidGenderizeResponse
import com.example.demo.getListOfClients
import com.example.demo.getValidGenderizeResponse
import com.example.demo.model.Client
import com.example.demo.repository.ClientRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.util.*

@ExtendWith(MockitoExtension::class)
class ClientServiceImplTest {

    @Mock
    private lateinit var clientRepository: ClientRepository
    @Mock
    private lateinit var genderizeService: GenderizeService
    @InjectMocks
    private lateinit var clientService: ClientServiceImpl

    @Test
    fun `getClients should return list of clients`() {
        val clients = getListOfClients()
        val pageRequest = PageRequest.of(0, 10)
        `when`(clientRepository.findAll(pageRequest)).thenReturn(PageImpl(clients))

        val result: Page<Client> = clientService.getClients(0, 10)

        assertEquals(clients.size, result.content.size)
        verify(clientRepository).findAll(pageRequest)
    }

    @Test
    fun `getClient should return client when client exists`() {
        val client = getClient()
        `when`(clientRepository.findById(1L)).thenReturn(Optional.of(client))

        val result: Client = clientService.getClient(1L)

        assertEquals(client, result)
        verify(clientRepository).findById(1L)
    }

    @Test
    fun `getClient should throw exception when client does not exist`() {
        `when`(clientRepository.findById(1L)).thenReturn(Optional.empty())

        assertThrows(Exception::class.java) { clientService.getClient(1L) }
        verify(clientRepository).findById(1L)
    }

    @Test
    fun `addClient should add new client and return it`() {
        val client = getClient()
        val genderizeResponse = getValidGenderizeResponse()
        `when`(genderizeService.getGender(client.firstName)).thenReturn(genderizeResponse)
        `when`(clientRepository.save(any())).thenReturn(client)

        val result: Client = clientService.addClient(client)

        assertEquals(client, result)
        verify(clientRepository).save(client)
    }

    @Test
    fun `addClient with gender's probability less 0,8 should throw ServiceException`() {
        val client = getClient()
        val genderizeResponse = getInvalidGenderizeResponse()
        `when`(genderizeService.getGender(client.firstName)).thenReturn(genderizeResponse)

        assertThrows(ServiceException::class.java) { clientService.addClient(client) }
    }

    @Test
    fun `updateClient should update existing client and return it`() {
        val client = getClient()
        `when`(clientRepository.findById(client.id!!)).thenReturn(Optional.of(client))
        `when`(clientRepository.save(any())).thenReturn(client)

        val result: Client = clientService.updateClient(client.id!!, client)

        assertEquals(client, result)
        verify(clientRepository).save(client)
    }

    @Test
    fun `deleteClient should delete client with given id`() {
        val clientId = 1L
        `when`(clientRepository.existsById(clientId)).thenReturn(true)

        clientService.deleteClient(clientId)

        verify(clientRepository).deleteById(clientId)
    }

    @Test
    fun `searchClientsByMap should return list of clients`() {
        val client = getClient()
        val pageRequest = PageRequest.of(0, 10)
        val searchMap = mapOf("firstName" to "John", "lastName" to "Doe")
        `when`(clientRepository.findAll(any(), eq(pageRequest))).thenReturn(PageImpl(listOf(client)))

        val result: Page<Client> = clientService.searchClientsByMap(searchMap, 0, 10)

        assertEquals(client, result.content.get(0))
        verify(clientRepository).findAll(any(), eq(pageRequest))
    }

    @Test
    fun `searchClientsByString should return list of clients`() {
        val client = getClient()
        val pageRequest = PageRequest.of(0, 10)
        val searchString = "John Doe"
        `when`(clientRepository.findAll(any(), eq(pageRequest))).thenReturn(PageImpl(listOf(client)))

        val result: Page<Client> = clientService.searchClientsByString(searchString, 0, 10)

        assertEquals(client, result.content.get(0))
        verify(clientRepository).findAll(any(), eq(pageRequest))
    }
}