package com.example.authorizationauthentication.controllers

import com.example.authorizationauthentication.models.ERole
import com.example.authorizationauthentication.models.Role
import com.example.authorizationauthentication.models.User
import com.example.authorizationauthentication.payload.request.LoginRequest
import com.example.authorizationauthentication.payload.request.SignupRequest
import com.example.authorizationauthentication.payload.response.JwtResponse
import com.example.authorizationauthentication.payload.response.MessageResponse
import com.example.authorizationauthentication.repository.RoleRepository
import com.example.authorizationauthentication.repository.UserRepository
import com.example.authorizationauthentication.security.jwt.JwtUtils
import com.example.authorizationauthentication.security.services.UserDetailsImpl
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import java.util.*
import java.util.stream.Collectors
import javax.validation.Valid
import kotlin.collections.HashSet

@CrossOrigin(origins = ["*"],maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authenticationManager: AuthenticationManager,
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtils: JwtUtils,
) {
    @PostMapping("/signin")
    fun authenticateUser(@Valid @RequestBody loginRequest: LoginRequest): ResponseEntity<*>{
        val authentication: Authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(loginRequest.username,loginRequest.password)
        )
        //setting the context for current user
        SecurityContextHolder.getContext().authentication = authentication;
        //getting jwt token for current user
        val jwt:String = jwtUtils.generateJwtToken(authentication)

        val userDetails: UserDetailsImpl = authentication.principal as UserDetailsImpl
        val roles: List<String> = userDetails.authorities.stream()
            .map { item -> item.authority }
            .collect(Collectors.toList())

        return ResponseEntity.ok(
            JwtResponse(jwt
                ,userDetails.getId()
                ,userDetails.username
                ,userDetails.getEmail()
                ,roles)
        );
    }

    @PostMapping("/signup")
    fun registerUser(@Valid @RequestBody signupRequest: SignupRequest): ResponseEntity<*>{
        if(userRepository.existsByUsername(signupRequest.username)){
            return ResponseEntity.badRequest()
                .body( MessageResponse("Error: Username is already taken!"))
        }

        if(userRepository.existsByEmail(signupRequest.email)){
            return ResponseEntity.badRequest()
                .body( MessageResponse("Error: Email is already taken!"))
        }

        //create new User's account
        val strRoles: Set<String> = signupRequest.roles
        var roles: MutableSet<Role> = HashSet();
        if(strRoles ==null){
            roles.add(Role(ERole.ROLE_USER))
        }else{
            strRoles.forEach { role ->
                run {
                    if (role == "admin") {
                        roles.add(Role(ERole.ROLE_ADMIN))
                    }
                    else if (role == "mod"){
                        roles.add(Role(ERole.ROLE_MODERATOR))
                    }
                    else{
                        roles.add(Role(ERole.ROLE_USER))
                    }
                }
            }
        }


        val user = User(
            signupRequest.username,
            signupRequest.email,
            passwordEncoder.encode(signupRequest.password),
            roles
        );

        //saving to database
        userRepository.save(user)
        return ResponseEntity.ok(MessageResponse("User registered successfully!"))
    }
}