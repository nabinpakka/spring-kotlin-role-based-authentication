package com.example.authorizationauthentication.repository

import com.example.authorizationauthentication.models.ERole
import com.example.authorizationauthentication.models.Role
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface RoleRepository: JpaRepository<Role, Long> {
    fun findByName(name: ERole): Role
}