package ru.quipy.dto

import ru.quipy.logic.TaskStatus
import java.util.*

data class TaskDetailedDto(
    val id: UUID,
    val title: String,
    val description: String,
    val status: TaskStatus,
    val performers: Set<UserDto>

)
