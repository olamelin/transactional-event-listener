package com.example

import io.micronaut.data.annotation.DateCreated
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import java.time.Instant

@Entity
class Entity {
    @Id
    @GeneratedValue
    var id: Long? = null

    @DateCreated
    var createdAt: Instant? = null
}
