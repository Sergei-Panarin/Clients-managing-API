package com.example.demo.service

import com.example.demo.model.Client
import org.springframework.data.domain.Page

interface ClientService {
    fun addClient(client: Client): Client
    fun getClients(page: Int, size: Int): Page<Client>
    fun getClient(id: Long): Client
    fun deleteClient(id: Long)
    fun updateClient(id: Long, client: Client): Client
}