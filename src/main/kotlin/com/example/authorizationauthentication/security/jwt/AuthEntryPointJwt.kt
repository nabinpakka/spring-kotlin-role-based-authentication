package com.example.authorizationauthentication.security.jwt

import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import java.io.IOException
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import kotlin.jvm.Throws

@Component
class AuthEntryPointJwt: AuthenticationEntryPoint {
    @Throws(IOException::class, ServletException::class)
    override fun commence(request: HttpServletRequest?, response: HttpServletResponse?, authException: AuthenticationException?) {
        //SC_UNAUTHORIZED is 401 status code
        println("Unauthorized error: ${authException?.message}")
        response?.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Error: Unauthorized")
    }
}