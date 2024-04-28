package com.example.demo.service

import com.example.demo.model.Client
import com.example.demo.repository.ClientRepository
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ClientServiceImpl(private val repository: ClientRepository,
                        private val genderizeService: GenderizeService) : ClientService {

    private val log = LoggerFactory.getLogger(ClientServiceImpl::class.java)

    @Transactional
    override fun addClient(client: Client): Client {
        log.info("Adding client in ClientServiceImpl: $client")
        val genderizeResponse = genderizeService.getGender(client.firstName)
        if (genderizeResponse != null && genderizeResponse.probability >= 0.8) {
            client.gender = genderizeResponse.gender
            return repository.save(client)
        } else {
            throw Exception("Gender not detected")
        }
    }

    @Transactional(readOnly = true)
    override fun getClients(page: Int, size: Int): Page<Client> {
        log.info("Getting all clients in ClientServiceImpl")
        return repository.findAll(PageRequest.of(page, size))
    }

    @Transactional(readOnly = true)
    override fun getClient(id: Long): Client {
        log.info("Getting client in ClientServiceImpl with id: $id")
        return repository.findById(id).orElse(null)
    }

    @Transactional
    override fun deleteClient(id: Long) {
        log.info("Deleting client in ClientServiceImpl with id: $id")
        repository.deleteById(id)
    }

    @Transactional
    override fun updateClient(id: Long, client: Client): Client {
        val existingClient = repository.findById(id).orElseThrow { Exception("Client not found") }
        existingClient.firstName = client.firstName
        existingClient.lastName = client.lastName
        existingClient.email = client.email
        existingClient.job = client.job
        existingClient.position = client.position
        existingClient.gender = client.gender
        log.info("Updating client in ClientServiceImpl with id: $id")
        return repository.save(existingClient)
    }
}