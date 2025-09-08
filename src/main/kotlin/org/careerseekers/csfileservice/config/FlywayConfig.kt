package org.careerseekers.csfileservice.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration
class FlywayConfig(private val databaseProperties: DatabaseProperties) {

    @Bean
    fun dataSource(): DataSource {
        val config = HikariConfig().apply {
            jdbcUrl =
                "jdbc:postgresql://${databaseProperties.host}:${databaseProperties.port}/${databaseProperties.name}"
            username = databaseProperties.username
            password = databaseProperties.password
            schema = databaseProperties.schema
        }
        return HikariDataSource(config)
    }

    @Bean
    fun flyway(dataSource: DataSource): Flyway {
        val logger = LoggerFactory.getLogger(FlywayConfig::class.java)
        logger.info("Flyway data source: {}", dataSource.javaClass.simpleName)

        val flyway = Flyway.configure()
            .dataSource(dataSource)
            .baselineOnMigrate(true)
            .load()

        flyway.migrate()

        logger.debug("=== Applied migrations ===")
        for (info in flyway.info().applied()) {
            logger.debug("{} - {}", info.version, info.description)
        }

        return flyway
    }
}
