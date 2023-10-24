package com.example

import io.micronaut.context.event.ApplicationEventPublisher
import io.micronaut.transaction.annotation.TransactionalEventListener
import jakarta.inject.Singleton
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import kotlin.jvm.optionals.getOrNull

@Singleton
open class Service(
    private val repository: Repository,
    private val eventPublisher: ApplicationEventPublisher<Event>
) {

    private val logger = LoggerFactory.getLogger(Service::class.java)

    @Transactional
    open fun createEntity() {
        val entity = repository.save(Entity())
        inner(entity)
        logger.info("Created Entity before committing transaction")
    }

    @Transactional(Transactional.TxType.MANDATORY)
    open fun inner(entity: Entity) {
        val innerEntity = repository.save(Entity())
        logger.info("Publishing event")
        eventPublisher.publishEvent(Event(entity.id, innerEntity.id))
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    @TransactionalEventListener(TransactionalEventListener.TransactionPhase.AFTER_COMMIT)
    open fun afterCommit(event: Event) {
        logger.info("Consuming event {}", event)
        val outerEntity = repository.findById(event.outerId).get()
        val innerEntity = repository.findById(event.innerId).get()
        logger.info("OuterEntity is ${outerEntity.id}, ${outerEntity.createdAt}")
        logger.info("InnerEntity is ${innerEntity.id}, ${innerEntity.createdAt}")
        logger.info("Consumed event {}", event)
    }
}


data class Event(val outerId: Long?, val innerId: Long?)
