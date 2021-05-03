package com.example.authorizationauthentication.security.jwt

import com.example.authorizationauthentication.security.services.UserDetailsImpl
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.SignatureException
import lombok.Value
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.lang.IllegalArgumentException
import java.util.*

@Component
class JwtUtils(

    private val jwtSecret: String = "pakkaz",
    private val jwtExpirationMs: Int = 86400000
) {

    fun generateJwtToken(authentication: Authentication): String {
        val userPrincipal: UserDetailsImpl = authentication.principal as UserDetailsImpl
        return Jwts.builder()
            .setSubject((userPrincipal.username))
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + jwtExpirationMs))
            .signWith(SignatureAlgorithm.HS512,jwtSecret)
            .compact();

    }

    fun getUserNameFromJwtToken(token: String): String{
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).body.subject
    }

    fun validateJwtToken(authToken: String): Boolean{
        try{
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        }catch (e: SignatureException){
            val msg = e.message
            println("Invalid JWT signature: $msg" )
        } catch (e: MalformedJwtException){
            val msg = e.message
            println("Invalid JWT token: $msg")

        }catch (e: IllegalArgumentException){
            val msg = e.message
            println("JWT claims string is empty: $msg")
        }
        return false;
    }
}