package com.example.demo.security

import com.example.demo.exception.ServiceError
import com.example.demo.exception.ServiceException
import com.example.demo.repository.UserRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl(private val userRepository: UserRepository) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsername(username)
            ?: throw ServiceException(ServiceError.NOT_FOUND, "User not found with username: $username")

        return User.withUsername(user.username)
            .password(user.password)
            .roles(user.roles)
            .build()
    }
}