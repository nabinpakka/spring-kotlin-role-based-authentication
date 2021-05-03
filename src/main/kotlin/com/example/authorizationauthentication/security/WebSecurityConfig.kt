package com.example.authorizationauthentication.security

import com.example.authorizationauthentication.security.jwt.AuthEntryPointJwt
import com.example.authorizationauthentication.security.jwt.AuthTokenFilter
import com.example.authorizationauthentication.security.services.UserDetailsServiceImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.lang.Exception
import kotlin.jvm.Throws

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
    //securedEnable=true,
    //jsr250Enabled = true,
    prePostEnabled = true
)
class WebSecurityConfig(
    private val userDetailsService:UserDetailsServiceImpl,
    private val unauthorizedHandler: AuthEntryPointJwt

): WebSecurityConfigurerAdapter() {

    @Bean
    fun authenticationJwtTokenFilter(): AuthTokenFilter{
        return AuthTokenFilter();
    }

    @Throws(Exception::class)
    override fun configure(auth: AuthenticationManagerBuilder?){
        if (auth != null) {
            auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder())
        }
    }

    @Bean
    @Throws(Exception::class)
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean();
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder{
        return BCryptPasswordEncoder();
    }


    override fun configure(http: HttpSecurity) {
        http.cors().and().csrf().disable()
            .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            .authorizeRequests().antMatchers("/api/auth/**").permitAll()
            .antMatchers("/api/test/**").permitAll()


        http.addFilterBefore(authenticationJwtTokenFilter(),UsernamePasswordAuthenticationFilter::class.java)


    }
}