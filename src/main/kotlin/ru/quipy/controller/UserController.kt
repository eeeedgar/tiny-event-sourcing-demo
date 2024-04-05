package ru.quipy.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.quipy.api.*
import ru.quipy.core.EventSourcingService
import ru.quipy.logic.UserAggregateState
import ru.quipy.logic.create
import ru.quipy.projections.ProjectRepository
import ru.quipy.projections.User
import ru.quipy.projections.UserRepository
import java.util.*

@RestController
@RequestMapping("/users")
class UserController(
    val userEsService: EventSourcingService<UUID, UserAggregate, UserAggregateState>,
    val userRepository: UserRepository,
    val projectRepository: ProjectRepository,
) {
    @PostMapping
    fun createUser(
        @RequestParam nickname: String,
        @RequestParam fullname: String,
        @RequestParam password: String
    ): UserCreatedEvent {
        return userEsService.create {
            it.create(
                UUID.randomUUID(),
                nickname,
                fullname,
                password
            )
        }
    }

    @GetMapping
    fun getUsers(): List<User> {
        return userRepository.findAll()
    }

    @GetMapping("/{userId}")
    fun getUserById(@PathVariable userId: UUID): User {
        return userRepository.findById(userId).get()
    }

    @GetMapping("/search/{substring}")
    fun getUsersByNicknameSubstring(@PathVariable substring: String): List<User> {
        return userRepository.findAll().filter { it.nickname.contains(substring) }
    }

    @GetMapping("/project/{projectId}")
    fun getUsersByProject(@PathVariable projectId: UUID): List<User> {
        val project = projectRepository.findById(projectId)
        return userRepository.findAll().filter { project.get().participants.contains(it.userId) }
    }
}