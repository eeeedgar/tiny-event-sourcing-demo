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
import ru.quipy.projections.Project
import ru.quipy.projections.ProjectRepository
import java.util.*

@RestController
@RequestMapping("/projects")
class ProjectController(
    val projectEsService: EventSourcingService<UUID, ProjectAggregate, ProjectAggregateState>,
    val projectRepository: ProjectRepository
) {

    @PostMapping("/")
    fun createProject(@RequestParam projectTitle: String, @RequestParam authorId: UUID) : ProjectCreatedEvent {
        return projectEsService.create { it.create(title = projectTitle, authorId = authorId) }
    }

    @GetMapping
    fun getProjects() : List<Project> {
        return projectRepository.findAll()
    }

    @PostMapping("/{projectId}/participants/{userId}")
    fun assignUser(@PathVariable projectId: UUID, @PathVariable userId: UUID, @RequestParam authorId: UUID) : UserAssignedToProjectEvent? {
        return projectEsService.update(projectId){ it.assignUserToProject(participantId = userId, authorId = authorId) }
    }

    @DeleteMapping("/{projectId}/participants/{participantId}")
    fun removeUser(@PathVariable projectId: UUID, @PathVariable participantId: UUID, @RequestParam authorId: UUID) : UserRemoveFromProjectEvent? {
        return projectEsService.update(projectId){ it.removeUserFromProject(participantId = participantId, authorId = authorId) }
    }

//    @PostMapping("/{projectId}/tags")
//    fun createTag(@PathVariable projectId: UUID, @RequestParam tag: String, @RequestParam authorId: UUID) : TagCreatedEvent? {
//        return projectEsService.update(projectId) {
//            it.createTag(tag = tag, authorId = authorId)
//        }
//    }

    @DeleteMapping("/{projectId}/tags")
    fun removeTag(@PathVariable projectId: UUID, @RequestParam tagId: UUID, @RequestParam authorId: UUID) : TagDeletedEvent? {
        return projectEsService.update(projectId) {
            it.deleteTag(tagId = tagId, authorId = authorId)
        }
    }
}