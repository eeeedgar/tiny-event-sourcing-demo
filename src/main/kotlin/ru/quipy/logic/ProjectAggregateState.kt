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

    lateinit var projectTags: MutableSet<UUID>
    lateinit var participants: MutableSet<UUID>
    lateinit var statuses: MutableSet<StatusEntity>

    override fun getId() = projectId

    // State transition functions which is represented by the class member function
    @StateTransitionFunc
    fun projectCreatedApply(event: ProjectCreatedEvent) {
        projectId = event.projectId
        projectTitle = event.title
        creatorId = event.authorId
        updatedAt = createdAt
        participants = event.participants
        statuses = event.statuses
    }

    @StateTransitionFunc
    fun tagCreatedApply(event: TagCreatedEvent) {
        projectTags.add(event.tagId)
        updatedAt = System.currentTimeMillis()
    }

    @StateTransitionFunc
    fun tagDeletedApply(event: TagDeletedEvent) {
        projectTags.remove(event.tagId)
        updatedAt = System.currentTimeMillis()
    }

    @StateTransitionFunc
    fun userAssignedApply(event: UserAssignedToProjectEvent) {
        participants.add(event.participantId)
        updatedAt = System.currentTimeMillis()
    }

    @StateTransitionFunc
    fun userRemoveApply(event: UserRemoveFromProjectEvent) {
        participants.remove(event.participantId)
        updatedAt = System.currentTimeMillis()
    }

    @StateTransitionFunc
    fun statusCreatedApply(event: StatusCreateEvent) {
        statuses.add(event.status)
        updatedAt = System.currentTimeMillis()
    }

    @StateTransitionFunc
    fun statusDeletedApply(event: StatusDeleteEvent) {
        statuses = statuses.filter { it.id != event.statusId }.toMutableSet()
        updatedAt = System.currentTimeMillis()
    }
}

data class TagEntity(
    val id: UUID = UUID.randomUUID(),
    val name: String
)
