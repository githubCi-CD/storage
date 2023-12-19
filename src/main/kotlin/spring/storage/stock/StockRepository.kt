package spring.storage.stock

import io.micrometer.tracing.TraceContext
import org.slf4j.LoggerFactory
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

private val log = LoggerFactory.getLogger(StockRepository::class.java)

@Repository
interface StockRepository : ReactiveCrudRepository<Stock, Long> {

    fun findOriginsByFactoryId(factoryId: Long?): Flux<Stock>

    fun findOriginByOriginIdAndFactoryId(originId: Long, factoryId: Long): Mono<Stock>
}