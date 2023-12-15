package spring.storage.config

import io.r2dbc.spi.ConnectionFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.r2dbc.connection.R2dbcTransactionManager
import org.springframework.transaction.reactive.TransactionalOperator

@Configuration
class R2dbcTransactionConfig {

    @Bean
    fun r2dbcTransactionalOperator(
        connectionFactory: ConnectionFactory
    ): TransactionalOperator =
        TransactionalOperator.create(R2dbcTransactionManager(connectionFactory))

}