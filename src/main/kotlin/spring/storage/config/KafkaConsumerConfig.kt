package spring.storage.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate
import reactor.kafka.receiver.ReceiverOptions
import spring.storage.stock.Stock
import spring.storage.stock.StockConsumeDto
import java.util.*

@Configuration
class KafkaConsumerConfig {

    @Bean
    fun receiverOptions(
        @Value("\${spring.kafka.topic}") topic: String, props: KafkaProperties
    ): ReceiverOptions<String, StockConsumeDto> = ReceiverOptions.create<String, StockConsumeDto>(props.buildConsumerProperties())
        .subscription(Collections.singletonList(topic))

    @Bean
    fun reactiveKafkaConsumerTemplate(
        receiverOptions: ReceiverOptions<String, StockConsumeDto>
    ): ReactiveKafkaConsumerTemplate<String, StockConsumeDto> =
        ReactiveKafkaConsumerTemplate(receiverOptions)
}