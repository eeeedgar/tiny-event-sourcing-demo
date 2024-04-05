package ru.quipy.api

import ru.quipy.core.annotations.DomainEvent
import ru.quipy.domain.Event
import java.util.*

const val PROJECT_CREATED_EVENT = "PROJECT_CREATED_EVENT"

const val TAG_CREATED_EVENT = "TAG_CREATED_EVENT"
const val TAG_DELETED_EVENT = "TAG_DELETED_EVENT"

const val USER_ASSIGNED_TO_PROJECT_EVENT = "USER_ASSIGNED_TO_PROJECT_EVENT"
const val USER_REMOVE_FROM_PROJECT_EVENT = "USER_REMOVE_FROM_PROJECT_EVENT"

// API
@DomainEvent(name = PROJECT_CREATED_EVENT)
class ProjectCreatedEvent(
    val projectId: UUID = UUID.randomUUID(),
    val authorId: UUID,

    val title: String,
    val participants: MutableSet<UUID> = mutableSetOf(authorId),

    createdAt: Long = System.currentTimeMillis(),
) : Event<ProjectAggregate>(
    name = PROJECT_CREATED_EVENT,
    createdAt = createdAt,
)

@DomainEvent(name = TAG_CREATED_EVENT)
class TagCreatedEvent(
    val projectId: UUID,
    val authorId: UUID,

    val tagId: UUID = UUID.randomUUID(),
    val tagName: String,

    createdAt: Long = System.currentTimeMillis(),
) : Event<ProjectAggregate>(
    name = TAG_CREATED_EVENT,
    createdAt = createdAt,
)

@DomainEvent(name = TAG_DELETED_EVENT)
class TagDeletedEvent(
    val projectId: UUID,
    val authorId: UUID,

    val tagId: UUID,

    createdAt: Long = System.currentTimeMillis(),
) : Event<ProjectAggregate>(
    name = TAG_DELETED_EVENT,
    createdAt = createdAt,
)


@DomainEvent(name = USER_ASSIGNED_TO_PROJECT_EVENT)
class UserAssignedToProjectEvent(
    val projectId: UUID,
    val authorId: UUID,

    val participantId: UUID,

    createdAt: Long = System.currentTimeMillis(),
) : Event<ProjectAggregate>(
    name = USER_ASSIGNED_TO_PROJECT_EVENT,
    createdAt = createdAt
)

@DomainEvent(name = USER_REMOVE_FROM_PROJECT_EVENT)
class UserRemoveFromProjectEvent(
    val projectId: UUID,
    val authorId: UUID,

    val participantId: UUID,

    createdAt: Long = System.currentTimeMillis(),
) : Event<ProjectAggregate>(
    name = USER_REMOVE_FROM_PROJECT_EVENT,
    createdAt = createdAt
)
