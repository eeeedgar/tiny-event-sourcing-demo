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

    var projectTags = mutableMapOf<UUID, TagEntity>()
    var participants = mutableMapOf<UUID, UserEntity>()

    override fun getId() = projectId

    // State transition functions which is represented by the class member function
    @StateTransitionFunc
    fun projectCreatedApply(event: ProjectCreatedEvent) {
        projectId = event.projectId
        projectTitle = event.title
        creatorId = event.authorId
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
    fun userAssignedApply(event: UserAssignedToProjectEvent) {
        participants[event.participantId] = UserEntity(event.participantId)
        updatedAt = System.currentTimeMillis()
    }

    @StateTransitionFunc
    fun userRemoveApply(event: UserRemoveFromProjectEvent) {
        participants.remove(event.participantId)
        updatedAt = System.currentTimeMillis()
    }
}

data class TagEntity(
    val id: UUID = UUID.randomUUID(),
    val name: String
)
