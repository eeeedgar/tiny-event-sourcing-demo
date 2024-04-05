package ru.quipy.logic

import ru.quipy.api.*
import ru.quipy.core.annotations.StateTransitionFunc
import ru.quipy.domain.AggregateState
import java.util.UUID
class TaskAggregateState : AggregateState<UUID, TaskAggregate>{
    private lateinit var taskId: UUID

    var createdAt: Long = System.currentTimeMillis()
    var updatedAt: Long = System.currentTimeMillis()

    lateinit var projectId: UUID

    lateinit var title: String
    lateinit var description: String
    lateinit var status: UUID

    var performers = mutableSetOf<UUID>()

    var tags = mutableSetOf<UUID>()

    var active = true
    override fun getId() = taskId

    @StateTransitionFunc
    fun createTask(event: TaskCreatedEvent){
        taskId = event.taskId
        projectId = event.projectId

        title = event.title
        description = event.description
        status = event.status
    }

    @StateTransitionFunc
    fun updateTask(event: TaskUpdatedEvent){
        title = event.title
        description = event.description
        status = event.status

        updatedAt = event.createdAt
    }

    @StateTransitionFunc
    fun deleteTask(event: TaskDeletedEvent){
        active = false
        updatedAt = event.createdAt
    }

    @StateTransitionFunc
    fun assignP(event: UserAssignedToTaskEvent){
        performers.add(event.performerId)
        updatedAt = event.createdAt
    }

    @StateTransitionFunc
    fun removeP(event: UserRemovedFromTaskEvent){
        performers.remove(event.performerId)
        updatedAt = event.createdAt
    }

    @StateTransitionFunc
    fun assignT(event: TagAssignedToTaskEvent){
        tags.add(event.tagId)
        updatedAt = event.createdAt
    }

    @StateTransitionFunc
    fun removeT(event: TagRemovedFromTaskEvent){
        performers.remove(event.tagId)
        updatedAt = event.createdAt
    }
}

data class StatusEntity(
    val id: UUID = UUID.randomUUID(),
    var name: String,
    var hex: String,
)
//enum class TaskStatus {
//    CREATED, IN_PROGRESS, DONE
//}
