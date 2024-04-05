package ru.quipy.projections

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import ru.quipy.api.*
import ru.quipy.logic.TaskStatus
import ru.quipy.streams.AggregateSubscriptionsManager
import java.util.*
import javax.annotation.PostConstruct

@Document("task-projection")
data class Task(
    @Id
    var taskId: UUID,
    var projectId: UUID,
    var creatorId: UUID,
    var title: String,
    var description: String,
//    val tags: MutableSet<UUID>,
    var performers: MutableSet<UUID>,
    var status: TaskStatus,
)

@Repository
interface TaskRepository : MongoRepository<Task, UUID>

@Service
class TaskEventsSubscriber(val taskRepository: TaskRepository) {

    val logger: Logger = LoggerFactory.getLogger(TaskEventsSubscriber::class.java)

    @Autowired
    lateinit var subscriptionsManager: AggregateSubscriptionsManager

    @PostConstruct
    fun init() {
        subscriptionsManager.createSubscriber(TaskAggregate::class, "some-meaningful-name-task") {
            `when`(TaskCreatedEvent::class) { event ->
                taskRepository.insert(Task(event.taskId, event.projectId, event.authorId, event.title, event.description, mutableSetOf(), event.status))
            }

            `when`(TaskUpdatedEvent::class) { event ->
                val taskOpt = taskRepository.findById(event.taskId)
                if (taskOpt.isPresent) {
                    val task = taskOpt.get()
                    taskRepository.deleteById(task.taskId)
                    taskRepository.insert(Task(event.taskId, event.projectId, event.authorId, event.title, event.description, task.performers, event.status))
                }
            }

            `when`(TaskDeletedEvent::class) { event ->
                taskRepository.deleteById(event.taskId)
            }

            `when`(UserAssignedToTaskEvent::class) { event ->
                val taskOpt = taskRepository.findById(event.taskId)
                if (taskOpt.isPresent) {
                    val task = taskOpt.get()
                    task.performers.add(event.performerId)
                    taskRepository.deleteById(task.taskId)
                    taskRepository.insert(task)
                }
            }

            `when`(UserRemovedFromTaskEvent::class) { event ->
                val taskOpt = taskRepository.findById(event.taskId)
                if (taskOpt.isPresent) {
                    val task = taskOpt.get()
                    task.performers.remove(event.performerId)
                    taskRepository.insert(task)
                }
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