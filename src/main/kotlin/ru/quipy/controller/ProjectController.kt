package ru.quipy.controller

import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.quipy.api.*
import ru.quipy.core.EventSourcingService
import ru.quipy.logic.*
import java.util.*

@RestController
@RequestMapping("/projects")
class ProjectController(
    val projectEsService: EventSourcingService<UUID, ProjectAggregate, ProjectAggregateState>,
) {

    @PostMapping("/")
    fun createProject(@RequestParam projectTitle: String, @RequestParam creatorId: UUID) : ProjectCreatedEvent {
        return projectEsService.create { it.create(UUID.randomUUID(), projectTitle, creatorId) }
    }

    @GetMapping("/{projectId}")
    fun getProject(@PathVariable projectId: UUID) : ProjectAggregateState? {
        return projectEsService.getState(projectId)
    }

    @PostMapping("/{projectId}/participants/{userId}")
    fun assignUser(@PathVariable projectId: UUID, @PathVariable userId: UUID, @RequestParam authorId: UUID) : UserAssignedToProjectEvent? {
        return projectEsService.update(projectId){ it.assignUserToProject(userId = userId, authorId = authorId) }
    }

    @DeleteMapping("/{projectId}/participants/{userId}")
    fun removeUser(@PathVariable projectId: UUID, @PathVariable userId: UUID, @RequestParam authorId: UUID) : UserRemoveFromProjectEvent? {
        return projectEsService.update(projectId){ it.removeUserFromProject(userId = userId, authorId = authorId) }
    }

    @PostMapping("/{projectId}/tasks")
    fun createTask(@PathVariable projectId: UUID, @RequestParam taskName: String, @RequestParam description: String, @RequestParam userId: UUID) : TaskCreatedEvent? {
        return projectEsService.update(projectId) {
            it.addTask(name = taskName, creatorId = userId, description = description)
        }
    }

    @PatchMapping("/{projectId}/tasks")
    fun updateTask(@PathVariable projectId: UUID, @RequestParam taskId: UUID, @RequestParam taskName: String, @RequestParam description: String, @RequestParam status: TaskStatus, @RequestParam userId: UUID) : TaskUpdatedEvent? {
        return projectEsService.update(projectId) {
            it.updateTask(taskId = taskId, userId = userId, taskName, description, status)
        }
    }

    @DeleteMapping("/{projectId}/tasks/{taskId}")
    fun deleteTask(@PathVariable projectId: UUID, @PathVariable taskId: UUID, @RequestParam userId: UUID) : TaskDeletedEvent? {
        return projectEsService.update(projectId) {
            it.deleteTask(taskId = taskId, userId = userId)
        }
    }
}