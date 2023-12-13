package spring.storage.origin

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router

@Configuration
class OriginRouter(
    private val originHandler: OriginHandler
) {
    @Bean
    fun originRouterFunction(): RouterFunction<ServerResponse> = router {
        "/api/v1/origin".nest {
            GET("/{id}",originHandler::findOriginById)
            POST("", originHandler::saveOrigin)
            PATCH("", originHandler::updatePrice)
        }
    }
}