package ru.quipy.logic

import ru.quipy.api.*
import ru.quipy.core.annotations.StateTransitionFunc
import ru.quipy.domain.AggregateState
import java.util.*

// Service's business logic
class ProjectAggregateState : AggregateState<UUID, ProjectAggregate> {
    private lateinit var projectId: UUID
    var createdAt: Long = System.currentTimeMillis()
    var updatedAt: Long = System.currentTimeMillis()

    lateinit var projectTitle: String
    lateinit var creatorId: UUID
    var tasks = mutableMapOf<UUID, TaskEntity>()
    var projectTags = mutableMapOf<UUID, TagEntity>()
    var participants = mutableMapOf<UUID, UserEntity>()

    override fun getId() = projectId

    // State transition functions which is represented by the class member function
    @StateTransitionFunc
    fun projectCreatedApply(event: ProjectCreatedEvent) {
        projectId = event.projectId
        projectTitle = event.title
        creatorId = event.creatorId
        updatedAt = createdAt
        participants[creatorId] = UserEntity(creatorId)
    }

    @StateTransitionFunc
    fun tagCreatedApply(event: TagCreatedEvent) {
        projectTags[event.tagId] = TagEntity(event.tagId, event.tagName)
        updatedAt = System.currentTimeMillis()
    }

    @StateTransitionFunc
    fun tagDeletedApply(event: TagDeletedEvent) {
        projectTags.remove(event.tagId)
        updatedAt = System.currentTimeMillis()
    }

    @StateTransitionFunc
    fun taskCreatedApply(event: TaskCreatedEvent) {
        tasks[event.taskId] = TaskEntity(event.taskId, event.title, event.description, TaskStatus.CREATED, mutableSetOf())
        updatedAt = System.currentTimeMillis()
    }

    @StateTransitionFunc
    fun taskUpdatedApply(event: TaskUpdatedEvent) {
        val task = tasks[event.taskId]
        if (task != null) {
            tasks[event.taskId] = TaskEntity(event.taskId, event.title, event.description, event.status, task.tagsAssigned)
        }
        updatedAt = System.currentTimeMillis()
    }

    @StateTransitionFunc
    fun taskDeletedApply(event: TaskDeletedEvent) {
        tasks.remove(event.taskId)
        updatedAt = System.currentTimeMillis()
    }

    @StateTransitionFunc
    fun userAssignedApply(event: UserAssignedToProjectEvent) {
        participants[event.userId] = UserEntity(event.userId)
        updatedAt = System.currentTimeMillis()
    }

    @StateTransitionFunc
    fun userRemoveApply(event: UserRemoveFromProjectEvent) {
        participants.remove(event.userId)
        updatedAt = System.currentTimeMillis()
    }
}

data class TaskEntity(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val description: String,
    val status: TaskStatus,
    val tagsAssigned: MutableSet<UUID>
)

data class TagEntity(
    val id: UUID = UUID.randomUUID(),
    val name: String
)

/**
 * Demonstrates that the transition functions might be representer by "extension" functions, not only class members functions
 */
@StateTransitionFunc
fun ProjectAggregateState.tagAssignedApply(event: TagAssignedToTaskEvent) {
    tasks[event.taskId]?.tagsAssigned?.add(event.tagId)
        ?: throw IllegalArgumentException("No such task: ${event.taskId}")
    updatedAt = System.currentTimeMillis()
}

@StateTransitionFunc
fun ProjectAggregateState.removeFromTaskApply(event: TagRemovedFromTask) {
    tasks[event.taskId]?.tagsAssigned?.remove(event.tagId)
        ?: throw IllegalArgumentException("No such task: ${event.taskId}")
    updatedAt = System.currentTimeMillis()
}
