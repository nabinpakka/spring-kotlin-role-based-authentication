package com.example.authorizationauthentication.models

import javax.persistence.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Entity
@Table( name = "users")
class User(
    @NotBlank
    @Size(max=20)
    val username: String,

    @NotBlank
    @Size(max= 50)
    @Email
    val email:String,

    @NotBlank
    @Size(max=120)
    val password: String,

    @ManyToMany(fetch = FetchType.LAZY,cascade = [CascadeType.ALL])
    @JoinTable(
        name = "user_roles",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "role_id")]
    )
    val roles: Set<Role> = HashSet(),

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id : Long=0
)