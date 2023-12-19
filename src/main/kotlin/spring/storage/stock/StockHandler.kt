package spring.storage.stock

import brave.Tracer
import brave.propagation.TraceContext
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.transaction.reactive.TransactionalOperator
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import spring.storage.origin.OriginRepository
import kotlin.jvm.optionals.getOrNull

@Component
class StockHandler(
    private val stockRepository: StockRepository,
    private val originRepository: OriginRepository,
    private val transactionalOperator: TransactionalOperator,
    private val tracer: Tracer,
) {
    private val log = LoggerFactory.getLogger(StockHandler::class.java)

    fun findByFactoryId(request: ServerRequest): Mono<ServerResponse> {
        val factoryId = request.queryParam("factoryId").getOrNull()?.toLong()
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
            .body(
                originRepository.findAll()
                    .flatMap { origin ->
                        stockRepository.findOriginByOriginIdAndFactoryId(origin.id ?: 0, factoryId ?: 0)
                            .map { stock -> stock.copy(origin = origin) }
                            .switchIfEmpty(Stock (
                                factoryId = factoryId ?: 0,
                                origin = origin
                            ).toMono())
                    }, Stock::class.java
            )
    }

    fun saveStock(stockConsumeDto: Mono<StockConsumeDto>, context: TraceContext): Mono<Stock> =
        stockConsumeDto.flatMap { dto ->
            val modifiedStock = Stock.fromKafkaDto(dto)
            val nextSpan = tracer.newChild(context)
                .name("saveStock")
                .start()
            log.info("modifed stock : $dto")
            modifiedStock.originId?.let { originId ->
                transactionalOperator.execute {
                    stockRepository.findOriginByOriginIdAndFactoryId(
                        originId = originId,
                        factoryId = modifiedStock.factoryId
                    )
                        .flatMap { savedStock ->
                            stockRepository.save(
                                savedStock.combine(modifiedStock)
                            )
                        }
                }.single()
                    .doOnNext { nextSpan.finish() }
            } ?: transactionalOperator.execute {
                    originRepository.findAll()
                        .flatMap { origin ->
                            stockRepository.save(
                                modifiedStock.copy(
                                    originId = origin.id
                                )
                            )
                        }
                }.last()
                .doOnNext { nextSpan.finish() }
        }
}