package spring.storage

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import reactor.core.publisher.Hooks

@SpringBootApplication
class StorageApplication

fun main(args: Array<String>) {
    Hooks.enableAutomaticContextPropagation()
    Hooks.enableContextLossTracking()
    runApplication<StorageApplication>(*args)
}
