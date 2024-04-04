package ru.quipy.logic

import ru.quipy.api.*
import java.util.*


// Commands : takes something -> returns event
// Here the commands are represented by extension functions, but also can be the class member functions

fun ProjectAggregateState.create(id: UUID, title: String, creatorId: UUID): ProjectCreatedEvent {
    return ProjectCreatedEvent(
        projectId = id,
        title = title,
        creatorId = creatorId,
    )
}

fun ProjectAggregateState.addTask(name: String, creatorId: UUID, description: String): TaskCreatedEvent {
    return TaskCreatedEvent(projectId = this.getId(), taskId = UUID.randomUUID(), title = name, creatorId = creatorId, description = description)
}

fun ProjectAggregateState.updateTask(taskId: UUID, userId: UUID, taskName: String, description: String, status: TaskStatus): TaskUpdatedEvent {
    if (tasks[taskId] == null) {
        throw IllegalArgumentException("Task does not exist in project: $taskId")
    }
    return TaskUpdatedEvent(projectId = this.getId(), taskId = taskId, userId = userId, title = taskName, description = description, status = status)
}

fun ProjectAggregateState.deleteTask(taskId: UUID, userId: UUID): TaskDeletedEvent {
    if (tasks[taskId] == null) {
        throw IllegalArgumentException("Task does not exist in project: $taskId")
    }
    return TaskDeletedEvent(projectId = this.getId(), taskId = taskId, userId = userId)
}

fun ProjectAggregateState.createTag(name: String): TagCreatedEvent {
    if (projectTags.values.any { it.name == name }) {
        throw IllegalArgumentException("Tag already exists: $name")
    }
    return TagCreatedEvent(projectId = this.getId(), tagId = UUID.randomUUID(), tagName = name)
}

fun ProjectAggregateState.assignTagToTask(tagId: UUID, taskId: UUID): TagAssignedToTaskEvent {
    if (!projectTags.containsKey(tagId)) {
        throw IllegalArgumentException("Tag doesn't exists: $tagId")
    }

    if (!tasks.containsKey(taskId)) {
        throw IllegalArgumentException("Task doesn't exists: $taskId")
    }

    return TagAssignedToTaskEvent(projectId = this.getId(), tagId = tagId, taskId = taskId)
}

fun ProjectAggregateState.assignUserToProject(userId: UUID): UserAssignedToProjectEvent {
    return UserAssignedToProjectEvent(projectId = this.getId(), userId = userId)
}

fun ProjectAggregateState.removeUserFromProject(userId: UUID): UserRemoveFromProjectEvent {
    return UserRemoveFromProjectEvent(projectId = this.getId(), userId = userId)
}
