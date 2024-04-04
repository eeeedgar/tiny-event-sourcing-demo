package ru.quipy.projections

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import ru.quipy.api.*
import ru.quipy.streams.AggregateSubscriptionsManager
import java.util.*
import javax.annotation.PostConstruct

@Component
class ProjectProjection (
    private val projectRepository: ProjectRepository,
    private val subscriptionsManager: AggregateSubscriptionsManager
){
    private val logger = LoggerFactory.getLogger(TaskProjection::class.java)

    @PostConstruct
    fun init() {
        subscriptionsManager.createSubscriber(ProjectAggregate::class, "users::user-projection") {
            `when`(ProjectCreatedEvent::class) { event ->
                withContext(Dispatchers.IO) {
                    projectRepository.save(Project(event.projectId, event.creatorId, event.title))
                }
                logger.info("Update project projection, create project ${event.projectId}")
            }
        }
    }
}

@Document("project-projection")
data class Project(
    @Id
    var projectId: UUID,
    var creatorId: UUID,
    val title: String,
)

@Repository
interface ProjectRepository : MongoRepository<Project, UUID>