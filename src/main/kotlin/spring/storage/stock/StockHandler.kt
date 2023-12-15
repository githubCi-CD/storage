package spring.storage.stock

import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import reactor.kotlin.core.publisher.toMono
import spring.storage.origin.OriginRepository
import kotlin.jvm.optionals.getOrNull

@Component
class StockHandler(
    private val stockRepository: StockRepository,
    private val originRepository: OriginRepository
) {

    fun findByFactoryId(request: ServerRequest): Mono<ServerResponse> {
        val factoryId = request.queryParam("factoryId").getOrNull()?.toLong()
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
            .body(
                stockRepository.findOriginsByFactoryId(factoryId = factoryId)
                    .flatMap { stock ->
                        originRepository.findOriginById(stock.originId)
                            .map { origin -> stock.copy(origin = origin) }
                    }, Stock::class.java
            )
    }

    @Transactional
    fun saveStock(stockConsumeDto: Mono<StockConsumeDto>): Mono<Stock> =
        stockConsumeDto.flatMap { dto ->
            val modifiedStock = Stock.fromKafkaDto(dto)
            modifiedStock.originId?.let { originId ->
                stockRepository.findOriginByOriginIdAndFactoryId(
                    originId = originId,
                    factoryId = modifiedStock.factoryId
                )
                    .flatMap { savedStock ->
                        stockRepository.save(
                            savedStock.combine(modifiedStock)
                        ).toMono()
                    }
            } ?: originRepository.findAll()
                .flatMap { origin ->
                    stockRepository.save(
                        modifiedStock.copy(
                            originId = origin.id
                        )
                    )
                }.toMono()
        }
}