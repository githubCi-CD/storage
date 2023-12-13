package spring.storage

import lombok.Value
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.reactive.config.EnableWebFlux

@SpringBootApplication
class StorageApplication

fun main(args: Array<String>) {
    runApplication<StorageApplication>(*args)
}
