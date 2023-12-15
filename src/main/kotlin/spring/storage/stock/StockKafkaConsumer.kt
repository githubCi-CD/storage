package spring.storage.stock

import org.springframework.boot.CommandLineRunner
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate
import org.springframework.stereotype.Component
import reactor.core.scheduler.Schedulers
import reactor.kotlin.core.publisher.toMono

@Component
class StockKafkaConsumer(
    private val reactiveKafkaConsumerTemplate: ReactiveKafkaConsumerTemplate<String, StockConsumeDto>,
    private val stockHandler: StockHandler
): CommandLineRunner {

    fun consumeStock() =
        reactiveKafkaConsumerTemplate
            .receiveAutoAck()
            .flatMap{ consumeRecord ->
                stockHandler.saveStock(consumeRecord.value().toMono())
            }

    override fun run(vararg args: String?) {
        consumeStock().subscribe()
    }
}