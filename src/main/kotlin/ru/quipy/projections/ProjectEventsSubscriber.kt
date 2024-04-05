package ru.quipy.projections

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import ru.quipy.api.*
import ru.quipy.streams.AggregateSubscriptionsManager
import java.util.*
import javax.annotation.PostConstruct


@Document("project-projection")
data class Project(
    @Id
    var projectId: UUID,
    var creatorId: UUID,
    var title: String,
    var participants: MutableSet<UUID>,

    )

@Repository
interface ProjectRepository : MongoRepository<Project, UUID>

@Service
class ProjectEventsSubscriber(val projectRepository: ProjectRepository) {

    val logger: Logger = LoggerFactory.getLogger(ProjectEventsSubscriber::class.java)

    @Autowired
    lateinit var subscriptionsManager: AggregateSubscriptionsManager

    @PostConstruct
    fun init() {
        subscriptionsManager.createSubscriber(ProjectAggregate::class, "some-meaningful-name-project") {
            `when`(ProjectCreatedEvent::class) { event ->
                projectRepository.insert(Project(event.projectId, event.authorId, event.title, mutableSetOf(event.authorId)))
            }

            `when`(TagCreatedEvent::class) { event ->
                logger.info("Tag created: {}", event.tagName)
            }

            `when`(TagDeletedEvent::class) { event ->
                logger.info("Tag deleted: {}", event.tagId)
            }

            `when`(UserAssignedToProjectEvent::class) { event ->
                val projectOpt = projectRepository.findById(event.projectId)
                if (projectOpt.isPresent) {
                    val project = projectOpt.get()
                    project.participants.add(event.participantId)
                    projectRepository.insert(project)
                }
            }

            `when`(UserRemoveFromProjectEvent::class) { event ->
                val projectOpt = projectRepository.findById(event.projectId)
                if (projectOpt.isPresent) {
                    val project = projectOpt.get()
                    project.participants.remove(event.participantId)
                    projectRepository.insert(project)
                }
            }
        }
    }
}