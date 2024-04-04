//package ru.quipy.api
//
//import ru.quipy.core.annotations.DomainEvent
//import ru.quipy.domain.Event
//import ru.quipy.logic.TaskStatus
//import java.util.*
//
//
//// API
//@DomainEvent(name = TASK_CREATED_EVENT)
//class TaskCreatedEvent(
//    val taskId: UUID,
//    val projectId: UUID,
//    val creatorId: UUID,
//    val title: String,
//    val description: String,
//    createdAt: Long = System.currentTimeMillis(),
//) : Event<TaskAggregate>(
//    name = TASK_CREATED_EVENT,
//    createdAt = createdAt,
//)
//
//@DomainEvent(name = TASK_UPDATED_EVENT)
//class TaskUpdatedEvent(
//    val taskId: UUID,
//    val updaterId: UUID,
//    val title: String,
//    createdAt: Long = System.currentTimeMillis(),
//) : Event<TaskAggregate>(
//    name = TASK_UPDATED_EVENT,
//    createdAt = createdAt,
//)
//
//@DomainEvent(name = TASK_DELETED_EVENT)
//class TaskDeletedEvent(
//    val taskId: UUID,
//    val userId: UUID,
//    createdAt: Long = System.currentTimeMillis(),
//) : Event<TaskAggregate>(
//    name = TASK_DELETED_EVENT,
//    createdAt = createdAt,
//)