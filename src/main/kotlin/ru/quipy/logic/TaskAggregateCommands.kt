package ru.quipy.logic

import ru.quipy.api.*
import java.util.*


// Commands : takes something -> returns event
// Here the commands are represented by extension functions, but also can be the class member functions

fun TaskAggregateState.create(projectId: UUID, title: String, description: String, authorId: UUID, statusId: UUID): TaskCreatedEvent {
    return TaskCreatedEvent(
        projectId = projectId,
        title = title,
        description = description,
        authorId = authorId,
        status = statusId
    )
}

fun TaskAggregateState.update(taskId: UUID, projectId: UUID, title: String, description: String, authorId: UUID, statusId: UUID): TaskUpdatedEvent {
    return TaskUpdatedEvent(
        taskId = taskId,
        projectId = projectId,
        title = title,
        description = description,
        authorId = authorId,
        status = statusId,
    )
}

fun TaskAggregateState.delete(taskId: UUID, projectId: UUID, authorId: UUID): TaskDeletedEvent {
    return TaskDeletedEvent(
        taskId = taskId,
        projectId = projectId,
        authorId = authorId,
    )
}

fun TaskAggregateState.assignTag(taskId: UUID, projectId: UUID, authorId: UUID, tagId: UUID): TagAssignedToTaskEvent {
    return TagAssignedToTaskEvent(
        taskId = taskId,
        projectId = projectId,
        authorId = authorId,
        tagId = tagId,
    )
}

fun TaskAggregateState.removeTag(taskId: UUID, projectId: UUID, authorId: UUID, tagId: UUID): TagRemovedFromTaskEvent {
    return TagRemovedFromTaskEvent(
        taskId = taskId,
        projectId = projectId,
        authorId = authorId,
        tagId = tagId,
    )
}

fun TaskAggregateState.assignPerformer(taskId: UUID, projectId: UUID, authorId: UUID, performerId: UUID): UserAssignedToTaskEvent {
    return UserAssignedToTaskEvent(
        taskId = taskId,
        projectId = projectId,
        authorId = authorId,
        performerId = performerId,
    )
}

fun TaskAggregateState.removePerformer(taskId: UUID, projectId: UUID, authorId: UUID, performerId: UUID): UserRemovedFromTaskEvent {
    return UserRemovedFromTaskEvent(
        taskId = taskId,
        projectId = projectId,
        authorId = authorId,
        performerId = performerId,
    )
}