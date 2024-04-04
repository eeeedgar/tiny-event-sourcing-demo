package ru.quipy.api

import ru.quipy.core.annotations.DomainEvent
import ru.quipy.domain.Event
import ru.quipy.logic.TaskStatus
import ru.quipy.logic.UserEntity
import java.util.*

const val PROJECT_CREATED_EVENT = "PROJECT_CREATED_EVENT"

const val TAG_CREATED_EVENT = "TAG_CREATED_EVENT"
const val TAG_DELETED_EVENT = "TAG_DELETED_EVENT"
const val TAG_ASSIGNED_TO_TASK_EVENT = "TAG_ASSIGNED_TO_TASK_EVENT"

const val TASK_CREATED_EVENT = "TASK_CREATED_EVENT"
const val TASK_DELETED_EVENT = "TASK_DELETED_EVENT"
const val TASK_UPDATED_EVENT = "TASK_UPDATED_EVENT"

const val USER_ASSIGNED_TO_PROJECT_EVENT = "USER_ASSIGNED_TO_PROJECT_EVENT"
const val USER_REMOVE_FROM_PROJECT_EVENT = "USER_REMOVE_FROM_PROJECT_EVENT"

// API
@DomainEvent(name = PROJECT_CREATED_EVENT)
class ProjectCreatedEvent(
    val projectId: UUID,
    val title: String,
    val creatorId: UUID,
    createdAt: Long = System.currentTimeMillis(),
) : Event<ProjectAggregate>(
    name = PROJECT_CREATED_EVENT,
    createdAt = createdAt,
)

@DomainEvent(name = TAG_CREATED_EVENT)
class TagCreatedEvent(
    val tagId: UUID,
    val tagName: String,
    createdAt: Long = System.currentTimeMillis(),
) : Event<ProjectAggregate>(
    name = TAG_CREATED_EVENT,
    createdAt = createdAt,
)

@DomainEvent(name = TAG_DELETED_EVENT)
class TagDeletedEvent(
    val tagId: UUID,
    createdAt: Long = System.currentTimeMillis(),
) : Event<ProjectAggregate>(
    name = TAG_DELETED_EVENT,
    createdAt = createdAt,
)

@DomainEvent(name = TASK_CREATED_EVENT)
class TaskCreatedEvent(
    val taskId: UUID,
    val projectId: UUID,
    val creatorId: UUID,
    val title: String,
    val description: String,
    createdAt: Long = System.currentTimeMillis(),
) : Event<ProjectAggregate>(
    name = TASK_CREATED_EVENT,
    createdAt = createdAt,
)

@DomainEvent(name = TASK_UPDATED_EVENT)
class TaskUpdatedEvent(
    val taskId: UUID,
    val userId: UUID,
    val title: String,
    val description: String,
    val status: TaskStatus,
    createdAt: Long = System.currentTimeMillis(),
) : Event<ProjectAggregate>(
    name = TASK_UPDATED_EVENT,
    createdAt = createdAt,
)

@DomainEvent(name = TASK_DELETED_EVENT)
class TaskDeletedEvent(
    val taskId: UUID,
    val userId: UUID,
    createdAt: Long = System.currentTimeMillis(),
) : Event<ProjectAggregate>(
    name = TASK_DELETED_EVENT,
    createdAt = createdAt,
)

@DomainEvent(name = TAG_ASSIGNED_TO_TASK_EVENT)
class TagAssignedToTaskEvent(
    val taskId: UUID,
    val tagId: UUID,
    createdAt: Long = System.currentTimeMillis(),
) : Event<ProjectAggregate>(
    name = TAG_ASSIGNED_TO_TASK_EVENT,
    createdAt = createdAt
)

@DomainEvent(name = USER_ASSIGNED_TO_PROJECT_EVENT)
class UserAssignedToProjectEvent(
    val userId: UUID,
    createdAt: Long = System.currentTimeMillis(),
) : Event<ProjectAggregate>(
    name = USER_ASSIGNED_TO_PROJECT_EVENT,
    createdAt = createdAt
)

@DomainEvent(name = USER_REMOVE_FROM_PROJECT_EVENT)
class UserRemoveFromProjectEvent(
    val userId: UUID,
    createdAt: Long = System.currentTimeMillis(),
) : Event<ProjectAggregate>(
    name = USER_REMOVE_FROM_PROJECT_EVENT,
    createdAt = createdAt
)
