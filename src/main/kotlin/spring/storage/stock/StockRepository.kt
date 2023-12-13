package spring.storage.stock

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository
interface StockRepository : ReactiveCrudRepository<Stock, Long> {

    fun findOriginsByFactoryId(factoryId: Long?): Flux<Stock>
}