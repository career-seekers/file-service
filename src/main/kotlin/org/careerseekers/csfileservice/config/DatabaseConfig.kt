package org.careerseekers.csfileservice.config

import io.r2dbc.spi.ConnectionFactories
import io.r2dbc.spi.ConnectionFactory
import io.r2dbc.spi.ConnectionFactoryOptions
import io.r2dbc.spi.ConnectionFactoryOptions.DATABASE
import io.r2dbc.spi.ConnectionFactoryOptions.DRIVER
import io.r2dbc.spi.ConnectionFactoryOptions.HOST
import io.r2dbc.spi.ConnectionFactoryOptions.PASSWORD
import io.r2dbc.spi.ConnectionFactoryOptions.PORT
import io.r2dbc.spi.ConnectionFactoryOptions.USER
import io.r2dbc.spi.Option
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DatabaseConfig(private val databaseProperties: DatabaseProperties) {

    @Bean
    fun connectionFactory(): ConnectionFactory {
        val options = ConnectionFactoryOptions.builder()
            .option(DRIVER, "postgresql")
            .option(HOST, databaseProperties.host)
            .option(PORT, databaseProperties.port.toInt())
            .option(DATABASE, databaseProperties.name)
            .option(USER, databaseProperties.username)
            .option(PASSWORD, databaseProperties.password)
            .option(Option.valueOf("schema"), databaseProperties.schema)
            .build()

        return ConnectionFactories.get(options)
    }
}