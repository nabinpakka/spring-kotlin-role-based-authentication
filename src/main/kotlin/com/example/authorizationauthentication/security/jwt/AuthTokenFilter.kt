package com.example.authorizationauthentication.security.jwt

import com.example.authorizationauthentication.security.services.UserDetailsServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import kotlin.jvm.Throws

class AuthTokenFilter() : OncePerRequestFilter() {

    @Autowired
    lateinit var jwtUtils:JwtUtils;

    @Autowired
    lateinit var userDetailsService:UserDetailsServiceImpl

    @Throws(ServletException::class,IOException::class)
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filter: FilterChain) {

        try {
            val jwt: String = parseJwt(request)
            if( jwt.isNotBlank() && jwtUtils.validateJwtToken(jwt)){
                val username: String = jwtUtils.getUserNameFromJwtToken(jwt)

                val userDetails: UserDetails = userDetailsService.loadUserByUsername(username)
                val authentication: UsernamePasswordAuthenticationToken =
                    UsernamePasswordAuthenticationToken(userDetails,null,userDetails.authorities)
                authentication.details = WebAuthenticationDetailsSource().buildDetails(request)

                SecurityContextHolder.getContext().authentication=authentication;

            }

        }catch (e: Exception){
            println("Cannot set user authentication: ${e.message}")
        }
        filter.doFilter(request,response)
    }

    private fun parseJwt(request: HttpServletRequest): String {
        val headerAuth: String = request.getHeader("Authorization")

        if(headerAuth.isNotBlank() && headerAuth.startsWith("Bearer ")){
            return headerAuth.substring(7,headerAuth.length)
        }
        return "";
    }
}