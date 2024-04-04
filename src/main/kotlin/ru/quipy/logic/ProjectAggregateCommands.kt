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
    if (!participants.containsKey(creatorId)) {
        throw IllegalArgumentException("User is not in the project: $creatorId")
    }
    return TaskCreatedEvent(projectId = this.getId(), taskId = UUID.randomUUID(), title = name, creatorId = creatorId, description = description)
}

fun ProjectAggregateState.updateTask(taskId: UUID, userId: UUID, taskName: String, description: String, status: TaskStatus): TaskUpdatedEvent {
    if (!participants.containsKey(userId)) {
        throw IllegalArgumentException("User is not in the project: $userId")
    }
    if (!tasks.containsKey(taskId)) {
        throw IllegalArgumentException("Task does not exist in project: $taskId")
    }
    return TaskUpdatedEvent(taskId = taskId, userId = userId, title = taskName, description = description, status = status)
}

fun ProjectAggregateState.deleteTask(taskId: UUID, userId: UUID): TaskDeletedEvent {
    if (!participants.containsKey(userId)) {
        throw IllegalArgumentException("User is not in the project: $userId")
    }
    if (tasks[taskId] == null) {
        throw IllegalArgumentException("Task does not exist in project: $taskId")
    }
    return TaskDeletedEvent(taskId = taskId, userId = userId)
}

fun ProjectAggregateState.createTag(name: String): TagCreatedEvent {
    if (projectTags.values.any { it.name == name }) {
        throw IllegalArgumentException("Tag already exists: $name")
    }
    return TagCreatedEvent(tagId = UUID.randomUUID(), tagName = name)
}

fun ProjectAggregateState.deleteTag(tagId: UUID): TagDeletedEvent {
    if (!projectTags.containsKey(tagId)) {
        throw IllegalArgumentException("Tag does not exist in project: $tagId")
    }
    return TagDeletedEvent(tagId)
}

fun ProjectAggregateState.assignTagToTask(tagId: UUID, taskId: UUID): TagAssignedToTaskEvent {
    if (!projectTags.containsKey(tagId)) {
        throw IllegalArgumentException("Tag doesn't exists: $tagId")
    }

    if (!tasks.containsKey(taskId)) {
        throw IllegalArgumentException("Task doesn't exists: $taskId")
    }

    return TagAssignedToTaskEvent(tagId = tagId, taskId = taskId)
}

fun ProjectAggregateState.removeTagFromTask(tagId: UUID, taskId: UUID): TagRemovedFromTask {
    if (!projectTags.containsKey(tagId)) {
        throw IllegalArgumentException("Tag doesn't exists: $tagId")
    }

    val task = tasks[taskId] ?: throw IllegalArgumentException("Task doesn't exists: $taskId")

    if (task.tagsAssigned.none { it == tagId }) {
        throw IllegalArgumentException("Tag is not assigned to task: tagId $tagId, taskId $taskId")
    }

    return TagRemovedFromTask(tagId = tagId, taskId = taskId)
}

fun ProjectAggregateState.assignUserToProject(userId: UUID, authorId: UUID): UserAssignedToProjectEvent {
    if (!participants.containsKey(authorId)) {
        throw IllegalArgumentException("Author is not in the project: $authorId")
    }
    if (participants.containsKey(userId)) {
        throw IllegalArgumentException("User is already in the project: $userId")
    }
    return UserAssignedToProjectEvent(userId = userId)
}

fun ProjectAggregateState.removeUserFromProject(userId: UUID, authorId: UUID): UserRemoveFromProjectEvent {
    if (!participants.containsKey(authorId)) {
        throw IllegalArgumentException("Author is not in the project: $authorId")
    }
    if (!participants.containsKey(userId)) {
        throw IllegalArgumentException("User is not in the project: $userId")
    }
    return UserRemoveFromProjectEvent(userId = userId)
}
