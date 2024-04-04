package ru.quipy.logic

import ru.quipy.api.*
import java.util.*


// Commands : takes something -> returns event
// Here the commands are represented by extension functions, but also can be the class member functions

fun ProjectAggregateState.create(title: String, authorId: UUID): ProjectCreatedEvent {
    return ProjectCreatedEvent(
        title = title,
        authorId = authorId,
    )
}

fun ProjectAggregateState.createTag(tag: String, authorId: UUID): TagCreatedEvent {
    if (projectTags.values.any { it.name == tag }) {
        throw IllegalArgumentException("Tag already exists: $tag")
    }
    return TagCreatedEvent(projectId = getId(), authorId = authorId, tagName = tag)
}

fun ProjectAggregateState.deleteTag(tagId: UUID, authorId: UUID): TagDeletedEvent {
    if (!projectTags.containsKey(tagId)) {
        throw IllegalArgumentException("Tag does not exist in project: $tagId")
    }
    return TagDeletedEvent(projectId = getId(), authorId =  authorId, tagId = tagId)
}

fun ProjectAggregateState.assignUserToProject(participantId: UUID, authorId: UUID): UserAssignedToProjectEvent {
    if (!participants.containsKey(authorId)) {
        throw IllegalArgumentException("Author is not in the project: $authorId")
    }
    if (participants.containsKey(participantId)) {
        throw IllegalArgumentException("User is already in the project: $participantId")
    }
    return UserAssignedToProjectEvent(projectId = getId(), authorId = authorId, participantId = participantId)
}

fun ProjectAggregateState.removeUserFromProject(participantId: UUID, authorId: UUID): UserRemoveFromProjectEvent {
    if (!participants.containsKey(authorId)) {
        throw IllegalArgumentException("Author is not in the project: $authorId")
    }
    if (!participants.containsKey(participantId)) {
        throw IllegalArgumentException("User is not in the project: $participantId")
    }
    return UserRemoveFromProjectEvent(projectId = getId(), authorId = authorId, participantId = participantId)
}
