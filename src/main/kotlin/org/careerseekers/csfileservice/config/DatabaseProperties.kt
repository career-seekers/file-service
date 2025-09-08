package org.careerseekers.csfileservice.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "database")
class DatabaseProperties {
    lateinit var host: String
    lateinit var port: String
    lateinit var name: String
    lateinit var username: String
    lateinit var password: String
    lateinit var schema: String
}