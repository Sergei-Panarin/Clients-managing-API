package com.example.demo.service.impl

import com.example.demo.exception.ServiceError
import com.example.demo.exception.ServiceException
import com.example.demo.model.Client
import com.example.demo.repository.ClientRepository
import com.example.demo.service.ClientService
import com.example.demo.service.GenderizeService
import com.example.demo.specification.ClientSpecification
import com.example.demo.specification.ClientSpecificationAdvanced
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
        setGender(client)
        return repository.save(client)
    }

    @Transactional(readOnly = true)
    override fun getClients(page: Int, size: Int): Page<Client> {
        log.info("Getting all clients in ClientServiceImpl")
        return repository.findAll(PageRequest.of(page, size))
    }

    @Transactional(readOnly = true)
    override fun getClient(id: Long): Client {
        log.info("Getting client in ClientServiceImpl with id: $id")
        return repository.findById(id).orElseThrow {
            ServiceException(ServiceError.NOT_FOUND, "Client with id $id not found")
        }
    }

    @Transactional
    override fun deleteClient(id: Long) {
        log.info("Deleting client in ClientServiceImpl with id: $id")
        if (!repository.existsById(id)) {
            throw ServiceException(ServiceError.NOT_FOUND, "Client with id $id not found")
        }
        repository.deleteById(id)
    }

    @Transactional
    override fun updateClient(id: Long, client: Client): Client {
        val existingClient = repository.findById(id).orElseThrow {
            ServiceException(ServiceError.NOT_FOUND, "Client with id $id not found") }
        existingClient.firstName = client.firstName
        existingClient.lastName = client.lastName
        existingClient.email = client.email
        existingClient.job = client.job
        existingClient.position = client.position
        setGender(existingClient)
        log.info("Updating client in ClientServiceImpl with id: $id")
        return repository.save(existingClient)
    }

    @Transactional(readOnly = true)
    override fun searchClientsByMap(searchMap: Map<String, String>, page: Int, size: Int): Page<Client> {
        val specification = ClientSpecification(searchMap)
        log.info("Searching clients in ClientServiceImpl with searchMap: $searchMap")
        return repository.findAll(specification, PageRequest.of(page, size))
    }

    @Transactional(readOnly = true)
    override fun searchClientsByString(search: String, page: Int, size: Int): Page<Client> {
        val specification = ClientSpecificationAdvanced(search)
        log.info("Searching clients in ClientServiceImpl with search string: $search")
        return repository.findAll(specification, PageRequest.of(page, size))
    }

    private fun setGender(client: Client) {
        val genderizeResponse = genderizeService.getGender(client.firstName)
        if (genderizeResponse != null && genderizeResponse.probability >= 0.8) {
            client.gender = genderizeResponse.gender
        } else {
            throw ServiceException(ServiceError.INTERNAL_SERVER_ERROR, "Gender not detected")
        }
    }
}