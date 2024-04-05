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
import ru.quipy.projections.ProjectRepository
import ru.quipy.projections.Task
import ru.quipy.projections.TaskRepository
import java.util.*

@RestController
@RequestMapping("/tasks")
class TaskController(
    val taskEsService: EventSourcingService<UUID, TaskAggregate, TaskAggregateState>,
    val taskRepository: TaskRepository,
    val projectRepository: ProjectRepository,
) {

    @PostMapping("/")
    fun createTask(@RequestParam title: String,
                   @RequestParam description: String,
                   @RequestParam projectId: UUID,
                   @RequestParam authorId: UUID
    ) : TaskCreatedEvent {

        if (!projectRepository.findById(projectId).get().participants.contains(authorId)) {
            throw IllegalArgumentException("Author is not in the project: $authorId")
        }

        return taskEsService.create {
            it.create(title = title,
                description = description,
                projectId = projectId,
                authorId = authorId
            ) }
    }

    @GetMapping()
    fun getTasks() : List<Task> {
        return taskRepository.findAll()
    }

    @GetMapping("/{taskId}")
    fun getTaskById(@PathVariable taskId: UUID) : Task {
        return taskRepository.findById(taskId).get()
    }


    @GetMapping("/project/{projectId}")
    fun getTasksByProject(@PathVariable projectId: UUID) : List<Task> {
        return taskRepository.findAll().filter { it.projectId == projectId }
    }

    @PatchMapping("/")
    fun updateTask(@RequestParam title: String,
                   @RequestParam description: String,
                   @RequestParam projectId: UUID,
                   @RequestParam authorId: UUID,
                   @RequestParam taskId: UUID,
                   @RequestParam status: TaskStatus,
    ) : TaskUpdatedEvent {

        if (!projectRepository.findById(projectId).get().participants.contains(authorId)) {
            throw IllegalArgumentException("Author is not in the project: $authorId")
        }

        return taskEsService.update(taskId) {
            it.update(taskId = taskId,
                projectId = projectId,
                title = title,
                description = description,
                authorId = authorId,
                status = status
            )}
    }

    @DeleteMapping("/")
    fun deleteTask(@RequestParam title: String,
                   @RequestParam description: String,
                   @RequestParam projectId: UUID,
                   @RequestParam authorId: UUID,
                   @RequestParam taskId: UUID,
    ) : TaskDeletedEvent {

        if (!projectRepository.findById(projectId).get().participants.contains(authorId)) {
            throw IllegalArgumentException("Author is not in the project: $authorId")
        }

        return taskEsService.update(taskId) {
            it.delete(taskId = taskId,
                projectId = projectId,
                authorId = authorId
            )}
    }

    @PostMapping("/tags")
    fun assignTag(@RequestParam tagId: UUID,
                  @RequestParam projectId: UUID,
                  @RequestParam authorId: UUID,
                  @RequestParam taskId: UUID,
                  ) : TagAssignedToTaskEvent {

        if (!projectRepository.findById(projectId).get().participants.contains(authorId)) {
            throw IllegalArgumentException("Author is not in the project: $authorId")
        }

        return taskEsService.update(taskId) {
            it.assignTag(
                taskId = taskId,
                projectId = projectId,
                authorId = authorId,
                tagId = tagId
            )
        }
    }

    @DeleteMapping("/tags")
    fun removeTag(@RequestParam tagId: UUID,
                  @RequestParam projectId: UUID,
                  @RequestParam authorId: UUID,
                  @RequestParam taskId: UUID,
    ) : TagRemovedFromTaskEvent {

        if (!projectRepository.findById(projectId).get().participants.contains(authorId)) {
            throw IllegalArgumentException("Author is not in the project: $authorId")
        }

        return taskEsService.update(taskId) {
            it.removeTag(
                taskId = taskId,
                projectId = projectId,
                authorId = authorId,
                tagId = tagId
            )
        }
    }

    @PostMapping("/performers")
    fun assignPerformer(@RequestParam performerId: UUID,
                  @RequestParam projectId: UUID,
                  @RequestParam authorId: UUID,
                  @RequestParam taskId: UUID,
    ) : UserAssignedToTaskEvent {

        if (!projectRepository.findById(projectId).get().participants.contains(authorId)) {
            throw IllegalArgumentException("Author is not in the project: $authorId")
        }

        return taskEsService.update(taskId) {
            it.assignPerformer(
                taskId = taskId,
                projectId = projectId,
                authorId = authorId,
                performerId = performerId
            )
        }
    }

    @DeleteMapping("/performers")
    fun removePerformer(@RequestParam performerId: UUID,
                  @RequestParam projectId: UUID,
                  @RequestParam authorId: UUID,
                  @RequestParam taskId: UUID,
    ) : UserRemovedFromTaskEvent {

        if (!projectRepository.findById(projectId).get().participants.contains(authorId)) {
            throw IllegalArgumentException("Author is not in the project: $authorId")
        }

        return taskEsService.update(taskId) {
            it.removePerformer(
                taskId = taskId,
                projectId = projectId,
                authorId = authorId,
                performerId = performerId
            )
        }
    }
}