package spring.storage.origin

import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
class OriginHandler(
    private val originRepository: OriginRepository
) {

    fun saveOrigin(request: ServerRequest): Mono<ServerResponse> =
        request.bodyToMono(Origin::class.java)
            .flatMap { origin ->
                originRepository.save(origin)
                    .flatMap { savedOrigin ->
                        ServerResponse.status(201).bodyValue(savedOrigin)
                    }
            }

    fun updatePrice(request: ServerRequest): Mono<ServerResponse> =
        request.bodyToMono(Origin::class.java)
            .flatMap { origin ->
                origin.id?.let {
                    originRepository.findById(origin.id)
                        .switchIfEmpty(Mono.error(IllegalArgumentException("origin id not found")))
                        .flatMap { findOrigin ->
                            originRepository.save(findOrigin.copy(price = origin.price))
                                .flatMap { savedOrigin ->
                                    ServerResponse.status(202).bodyValue(savedOrigin)
                                }
                        }
                } ?: return@flatMap Mono.error(IllegalArgumentException("origin id is null"))
            }.onErrorResume { error ->
                ServerResponse.badRequest().bodyValue("Error: ${error.message}")
            }
}