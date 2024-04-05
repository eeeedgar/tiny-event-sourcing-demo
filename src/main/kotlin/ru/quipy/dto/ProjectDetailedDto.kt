package ru.quipy.dto

import ru.quipy.logic.StatusEntity
import java.util.*

data class ProjectDetailedDto(
    val id: UUID,
    val title: String,
    val participants: Set<UserDto>,
    val tasks: Set<TaskDetailedDto>,
    val statuses: Set<StatusEntity>,
    )
