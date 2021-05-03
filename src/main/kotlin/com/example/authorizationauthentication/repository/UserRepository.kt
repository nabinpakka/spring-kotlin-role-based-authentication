package com.example.authorizationauthentication.repository

import com.example.authorizationauthentication.models.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository: JpaRepository<User, Long> {
    fun findByUsername(username:String): User;
    fun existsByUsername(username:String): Boolean;
    fun existsByEmail(username: String): Boolean;
}