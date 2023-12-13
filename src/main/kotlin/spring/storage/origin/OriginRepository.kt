package spring.storage.origin

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono

interface OriginRepository : ReactiveCrudRepository<Origin, Long> {
    fun findOriginById(id: Long?): Mono<Origin>
}