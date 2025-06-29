package org.esc.csfileservice.config

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
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DatabaseConfig {

    @Value("\${database.host}")
    private lateinit var databaseHost: String

    @Value("\${database.port}")
    private var databasePort: Int = 5432

    @Value("\${database.name}")
    private lateinit var databaseName: String

    @Value("\${database.username}")
    private lateinit var databaseUsername: String

    @Value("\${database.password}")
    private lateinit var databasePassword: String

    @Value("\${database.schema}")
    private lateinit var databaseSchema: String

    @Bean
    fun connectionFactory(): ConnectionFactory {
        val options = ConnectionFactoryOptions.builder()
            .option(DRIVER, "postgresql")
            .option(HOST, databaseHost)
            .option(PORT, databasePort)
            .option(DATABASE, databaseName)
            .option(USER, databaseUsername)
            .option(PASSWORD, databasePassword)
            .option(Option.valueOf("schema"), databaseSchema)
            .build()

        return ConnectionFactories.get(options)
    }
}