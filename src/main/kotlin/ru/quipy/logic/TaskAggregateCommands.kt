//package ru.quipy.logic
//
//import ru.quipy.api.TaskCreatedEvent
////import ru.quipy.api.TaskDeletedEvent
//import ru.quipy.api.TaskUpdatedEvent
//import java.util.*
//
//
//fun TaskAggregateState.create(
//    id: UUID,
//    projectId: UUID,
//    creatorId: UUID,
//    title: String,
//    description: String
//): TaskCreatedEvent {
//    return TaskCreatedEvent(
//        taskId = id,
//        projectId = projectId,
//        creatorId = creatorId,
//        title = title,
//        description = description,
//        createdAt = System.currentTimeMillis()
//    )
//}
//
//fun TaskAggregateState.updateTask(
//    id: UUID,
//    updaterId: UUID,
//    title: String,
//): TaskUpdatedEvent {
//    return TaskUpdatedEvent(
//        taskId = id,
//        updaterId = updaterId,
//        title = title,
//        createdAt = System.currentTimeMillis()
//    )
//}
//
////fun TaskAggregateState.delete(
////    projectId: UUID,
////    id: UUID,
////    userId: UUID,
////): TaskDeletedEvent {
////    return TaskDeletedEvent(
////        projectId = projectId,
////        taskId = id,
////        userId = userId,
////        createdAt = System.currentTimeMillis()
////    )
////}