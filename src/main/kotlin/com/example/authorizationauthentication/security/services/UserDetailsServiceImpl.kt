package com.example.authorizationauthentication.security.services

import com.example.authorizationauthentication.models.User
import com.example.authorizationauthentication.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import kotlin.jvm.Throws

@Service
class UserDetailsServiceImpl(val userRepository: UserRepository): UserDetailsService {
    @Transactional
    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        val user: User= (userRepository.findByUsername(username)
            ?:throw UsernameNotFoundException("User not Found with username: $username ")) as User
        return UserDetailsImpl.build(user)
    }
}
