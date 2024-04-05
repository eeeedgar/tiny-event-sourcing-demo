package ru.quipy.api

import ru.quipy.core.annotations.DomainEvent
import ru.quipy.domain.Event
import java.util.*

const val TAG_ASSIGNED_TO_TASK_EVENT = "TAG_ASSIGNED_TO_TASK_EVENT"
const val TAG_REMOVED_FROM_TASK_EVENT = "TAG_REMOVED_FROM_TASK_EVENT"

const val TASK_CREATED_EVENT = "TASK_CREATED_EVENT"
const val TASK_DELETED_EVENT = "TASK_DELETED_EVENT"
const val TASK_UPDATED_EVENT = "TASK_UPDATED_EVENT"

const val USER_ASSIGNED_TO_TASK_EVENT = "USER_ASSIGNED_TO_TASK_EVENT"
const val USER_REMOVED_FROM_TASK_EVENT = "USER_REMOVED_FROM_TASK_EVENT"




// just task
@DomainEvent(name = TASK_CREATED_EVENT)
class TaskCreatedEvent(
    val taskId: UUID = UUID.randomUUID(),
    val projectId: UUID,
    val authorId: UUID,

    val title: String,
    val description: String,
    val status: UUID,

    createdAt: Long = System.currentTimeMillis(),
    ) : Event<TaskAggregate>(
    name = TASK_CREATED_EVENT,
    createdAt = createdAt,
)

@DomainEvent(name = TASK_UPDATED_EVENT)
class TaskUpdatedEvent(
    val taskId: UUID,
    val projectId: UUID,
    val authorId: UUID,

    val title: String,
    val description: String,
    val status: UUID,

    createdAt: Long = System.currentTimeMillis(),
    ) : Event<TaskAggregate>(
    name = TASK_UPDATED_EVENT,
    createdAt = createdAt,
)

@DomainEvent(name = TASK_DELETED_EVENT)
class TaskDeletedEvent(
    val taskId: UUID,
    val projectId: UUID,
    val authorId: UUID,

    createdAt: Long = System.currentTimeMillis(),
) : Event<TaskAggregate>(
    name = TASK_DELETED_EVENT,
    createdAt = createdAt,
)




// tag
@DomainEvent(name = TAG_ASSIGNED_TO_TASK_EVENT)
class TagAssignedToTaskEvent(
    val taskId: UUID,
    val projectId: UUID,
    val authorId: UUID,

    val tagId: UUID,

    createdAt: Long = System.currentTimeMillis(),

    ) : Event<TaskAggregate>(
    name = TAG_ASSIGNED_TO_TASK_EVENT,
    createdAt = createdAt
)

@DomainEvent(name = TAG_REMOVED_FROM_TASK_EVENT)
class TagRemovedFromTaskEvent(
    val taskId: UUID,
    val projectId: UUID,
    val authorId: UUID,

    val tagId: UUID,

    createdAt: Long = System.currentTimeMillis(),
    ) : Event<TaskAggregate>(
    name = TAG_REMOVED_FROM_TASK_EVENT,
    createdAt = createdAt
)


// user
@DomainEvent(name = USER_ASSIGNED_TO_TASK_EVENT)
class UserAssignedToTaskEvent(
    val taskId: UUID,
    val projectId: UUID,
    val authorId: UUID,

    val performerId: UUID,

    createdAt: Long = System.currentTimeMillis(),

    ) : Event<TaskAggregate>(
    name = USER_ASSIGNED_TO_TASK_EVENT,
    createdAt = createdAt
)

@DomainEvent(name = USER_REMOVED_FROM_TASK_EVENT)
class UserRemovedFromTaskEvent(
    val taskId: UUID,
    val projectId: UUID,
    val authorId: UUID,

    val performerId: UUID,

    createdAt: Long = System.currentTimeMillis(),
    ) : Event<TaskAggregate>(
    name = USER_REMOVED_FROM_TASK_EVENT,
    createdAt = createdAt
)
