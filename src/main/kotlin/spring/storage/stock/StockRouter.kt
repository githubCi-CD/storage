package spring.storage.stock

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router

@Configuration
class StockRouter(
    private val stockHandler: StockHandler
) {
    @Bean
    fun routerFunction(): RouterFunction<ServerResponse> = router {
        "/api/v1/storage".nest {
            GET("", stockHandler::findByFactoryId)
        }
    }
}