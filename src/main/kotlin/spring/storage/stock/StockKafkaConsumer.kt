package spring.storage.stock

import brave.Tracer
import brave.Tracing
import brave.handler.MutableSpan
import brave.propagation.CurrentTraceContext
import io.micrometer.core.ipc.http.OkHttpSender
import org.springframework.boot.CommandLineRunner
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate
import org.springframework.stereotype.Component
import reactor.kotlin.core.publisher.toMono
import zipkin2.reporter.brave.ZipkinSpanHandler


@Component
class StockKafkaConsumer(
    private val reactiveKafkaConsumerTemplate: ReactiveKafkaConsumerTemplate<String, StockConsumeDto>,
    private val stockHandler: StockHandler,
    private val tracer: Tracer,
) : CommandLineRunner {
    fun consumeStock() =
        reactiveKafkaConsumerTemplate
            .receiveAutoAck()
            .flatMap { consumeRecord ->
                val context = tracer.nextSpan()
                val span = context.name("kafka-consumer").remoteServiceName("kafka").start()
                stockHandler.saveStock(consumeRecord.value().toMono(), context.context())
                    .doOnNext { span.finish() }
            }

    override fun run(vararg args: String?) {
        consumeStock().subscribe()
    }
}