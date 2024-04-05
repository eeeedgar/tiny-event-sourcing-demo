package ru.quipy.dto

import ru.quipy.logic.StatusEntity
import java.util.*

data class TaskDetailedDto(
    val id: UUID,
    val title: String,
    val description: String,
    val status: StatusEntity,
    val performers: Set<UserDto>

)
