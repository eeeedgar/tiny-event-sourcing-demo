package ru.quipy.projections

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.quipy.api.*
import ru.quipy.streams.AggregateSubscriptionsManager
import javax.annotation.PostConstruct

@Service
class ProjectEventsSubscriber {

    val logger: Logger = LoggerFactory.getLogger(ProjectEventsSubscriber::class.java)

    @Autowired
    lateinit var subscriptionsManager: AggregateSubscriptionsManager

    @PostConstruct
    fun init() {
        subscriptionsManager.createSubscriber(ProjectAggregate::class, "some-meaningful-name-project") {
            `when`(ProjectCreatedEvent::class) { event ->
                logger.info("Project created: {}", event.title)
            }

            `when`(TagCreatedEvent::class) { event ->
                logger.info("Tag created: {}", event.tagName)
            }

            `when`(TagDeletedEvent::class) { event ->
                logger.info("Tag deleted: {}", event.tagId)
            }

            `when`(UserAssignedToProjectEvent::class) { event ->
                logger.info("User assigned to project: {}", event.participantId)
            }

            `when`(UserRemoveFromProjectEvent::class) { event ->
                logger.info("User removed from project: {}", event.participantId)
            }
        }
    }
}