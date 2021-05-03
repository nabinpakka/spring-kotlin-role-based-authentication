package com.example.authorizationauthentication.models

import javax.persistence.*

@Entity
@Table(name="roles")
class Role(
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    val name: ERole,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0
)