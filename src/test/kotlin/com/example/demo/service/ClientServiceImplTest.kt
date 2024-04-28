package com.example.demo.service

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
    fun `when authorized client expect getClients return list Of Clients`() {
        val clients = listOf(
            Client(id = 1, firstName = "Mick", lastName = "Jagger", email = "m.jagger@test.com", job = "Singer", position = "Vocalist", gender = "male"),
            Client(id = 2, firstName = "Freddie", lastName = "Mercury", email = "f.mercury@test.com", job = "Singer", position = "Vocalist", gender = "male"),
            Client(id = 3, firstName = "David", lastName = "Bowie", email = "d.bowie@test.com", job = "Singer", position = "Vocalist", gender = "male"),
            Client(id = 4, firstName = "Axel", lastName = "Rose", email = "a.rose@test.com", job = "Singer", position = "Vocalist", gender = "male"),
            Client(id = 5, firstName = "James", lastName = "Hetfield", email = "j.hetfield@test.com", job = "Singer", position = "Vocalist", gender = "male")
        )
        val pageRequest = PageRequest.of(0, 10)
        `when`(clientRepository.findAll(pageRequest)).thenReturn(PageImpl(clients))

        val result: Page<Client> = clientService.getClients(0, 10)

        assertEquals(clients.size, result.content.size)
        verify(clientRepository).findAll(pageRequest)
    }

    @Test
    fun testGetClient() {
        val client = Client(id = 1,firstName = "Axel", lastName = "Rose", email = "a.rose@test.com", job = "Singer", position = "Vocalist", gender = "male")
        `when`(clientRepository.findById(1L)).thenReturn(Optional.of(client))

        val result: Client = clientService.getClient(1L)

        assertEquals(client, result)
        verify(clientRepository).findById(1L)
    }

    @Test
    fun testGetClientNotFound() {
        `when`(clientRepository.findById(1L)).thenReturn(Optional.empty())

        assertThrows(Exception::class.java) { clientService.getClient(1L) }
        verify(clientRepository).findById(1L)
    }
}