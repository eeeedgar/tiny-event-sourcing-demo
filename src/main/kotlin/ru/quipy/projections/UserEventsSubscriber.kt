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

@Document("user-projection")
data class User(
    @Id
    var userId: UUID,
    val fullname: String,
    val nickname: String
)

@Repository
interface UserRepository : MongoRepository<User, UUID>

@Service
class UserEventsSubscriber(val userRepository: UserRepository) {
    val logger: Logger = LoggerFactory.getLogger(UserEventsSubscriber::class.java)

    @Autowired
    lateinit var subscriptionsManager: AggregateSubscriptionsManager

    @PostConstruct
    fun init() {
        subscriptionsManager.createSubscriber(UserAggregate::class, "some-meaningful-name-user") {
            `when`(UserCreatedEvent::class) { event ->
                userRepository.insert(User(event.userId, event.fullname, event.nickname))
            }
        }
    }
}
