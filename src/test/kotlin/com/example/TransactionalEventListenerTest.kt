package com.example
import io.micronaut.runtime.EmbeddedApplication
import io.micronaut.test.extensions.kotest5.annotation.MicronautTest
import io.kotest.core.spec.style.StringSpec

@MicronautTest
class TransactionalEventListenerTest(private val service: Service): StringSpec({

    "trigger event" {
        service.createEntity()
    }
})
