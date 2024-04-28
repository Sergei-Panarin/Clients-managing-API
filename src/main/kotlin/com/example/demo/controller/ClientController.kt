package com.example.demo.controller

import com.example.demo.dto.ClientDto
import com.example.demo.mapper.ClientMapper
import com.example.demo.service.ClientService
import org.springframework.data.domain.Page
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/clients")
class ClientController(private val clientService: ClientService, private val clientMapper: ClientMapper) : ClientControllerApi {

    //private val log = LoggerFactory.getLogger(ClientController::class.java)

    @PostMapping("")
    override fun addClient(@RequestBody clientDto: ClientDto): ClientDto {
        //log.info("Adding client in ClientController: $clientDto")
        val client = clientMapper.toEntity(clientDto)
        return clientMapper.toDto(clientService.addClient(client))
    }

    @GetMapping("")
    override fun getClients(@RequestParam(defaultValue = "0") page: Int,
                   @RequestParam(defaultValue = "10") size: Int): Page<ClientDto> {
        return clientService.getClients(page, size).map { clientMapper.toDto(it) }
    }

    @GetMapping("/{id}")
    override fun getClient(@PathVariable id: Long): ClientDto = clientMapper.toDto(clientService.getClient(id))

    @DeleteMapping("/{id}")
    override fun deleteClient(@PathVariable id: Long) = clientService.deleteClient(id)

    @PutMapping("/{id}")
    override fun updateClient(@PathVariable id: Long, @RequestBody clientDto: ClientDto): ClientDto {
        val client = clientMapper.toEntity(clientDto)
        return clientMapper.toDto(clientService.updateClient(id, client))
    }

    @GetMapping("/search-map")
    override fun searchClientsByMap(@RequestParam parameters: MultiValueMap<String, String>,
                           @RequestParam(defaultValue = "0") page: Int,
                           @RequestParam(defaultValue = "10") size: Int): Page<ClientDto> {
        val searchMap = parameters.toSingleValueMap()
        val clients = clientService.searchClientsByMap(searchMap, page, size)
        return clients.map { clientMapper.toDto(it) }
    }

    @GetMapping("/search-string")
    override fun searchClientsByString(@RequestParam search: String,
                              @RequestParam(defaultValue = "0") page: Int,
                              @RequestParam(defaultValue = "10") size: Int): Page<ClientDto> {
        val clients = clientService.searchClientsByString(search, page, size)
        return clients.map { clientMapper.toDto(it) }
    }
}