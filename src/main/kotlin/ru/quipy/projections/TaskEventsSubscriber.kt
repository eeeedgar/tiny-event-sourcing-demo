package ru.quipy.projections

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.quipy.api.*
import ru.quipy.streams.AggregateSubscriptionsManager
import javax.annotation.PostConstruct

@Service
class TaskEventsSubscriber {

    val logger: Logger = LoggerFactory.getLogger(TaskEventsSubscriber::class.java)

    @Autowired
    lateinit var subscriptionsManager: AggregateSubscriptionsManager

    @PostConstruct
    fun init() {
        subscriptionsManager.createSubscriber(TaskAggregate::class, "some-meaningful-name-task") {
            `when`(TaskCreatedEvent::class) { event ->
                logger.info("Task created: {}", event.title)
            }

            `when`(TaskUpdatedEvent::class) { event ->
                logger.info("Task updated: {} {} {}", event.title, event.description, event.status)
            }

            `when`(TaskDeletedEvent::class) { event ->
                logger.info("Task deleted: {}", event.taskId)
            }

            `when`(UserAssignedToTaskEvent::class) { event ->
                logger.info("User assigned to task: {} {}", event.taskId, event.performerId)
            }

            `when`(UserRemovedFromTaskEvent::class) { event ->
                logger.info("User removed from task: {} {}", event.taskId, event.performerId)
            }

            `when`(TagAssignedToTaskEvent::class) { event ->
                logger.info("Tag assigned to task: {} {}", event.taskId, event.tagId)
            }

            `when`(TagRemovedFromTaskEvent::class) { event ->
                logger.info("Tag removed from task: {} {}", event.taskId, event.tagId)
            }
        }
    }
}