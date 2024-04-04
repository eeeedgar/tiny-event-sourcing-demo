package ru.quipy.projections

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import ru.quipy.api.*
import ru.quipy.logic.TaskStatus
import ru.quipy.streams.AggregateSubscriptionsManager
import java.util.*
import javax.annotation.PostConstruct
//
//@Component
//class TaskProjection (
//    private val taskRepository: TaskRepository,
//    private val subscriptionsManager: AggregateSubscriptionsManager
//){
//    private val logger = LoggerFactory.getLogger(TaskProjection::class.java)
//
//    @PostConstruct
//    fun init() {
//        subscriptionsManager.createSubscriber(ProjectAggregate::class, "users::user-projection") {
//            `when`(TaskCreatedEvent::class) { event ->
//                withContext(Dispatchers.IO) {
//                    taskRepository.save(Task(event.taskId, event.projectId, event.authorId, event.title, event.description, mutableSetOf(), TaskStatus.CREATED))
//                }
//                logger.info("Update task projection, create task ${event.taskId}")
//            }
//            `when`(TaskUpdatedEvent::class) { event ->
//                withContext(Dispatchers.IO) {
//                    val task = taskRepository.deleteById(event.taskId)
//                    taskRepository.insert(Task(event.taskId, (task as Task).projectId, (task as Task).creatorId, event.title, event.description, (task as Task).tags, event.status))
//                }
//                logger.info("Update task projection, update task ${event.taskId}")
//            }
//            `when`(TaskDeletedEvent::class) { event ->
//                withContext(Dispatchers.IO) {
//                    val task = taskRepository.deleteById(event.taskId)
//                }
//                logger.info("Update task projection, delete task ${event.taskId}")
//            }
//        }
//    }
//}

@Document("task-projection")
data class Task(
    @Id
    var taskId: UUID,
    var projectId: UUID,
    var creatorId: UUID,
    val title: String,
    val description: String,
    val tags: MutableSet<String>,
    val status: TaskStatus,
)

@Repository
interface TaskRepository : MongoRepository<Task, UUID>