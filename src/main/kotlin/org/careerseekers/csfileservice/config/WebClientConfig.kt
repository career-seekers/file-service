package org.careerseekers.csfileservice.config

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfig(private val properties: YandexDiskProperties) {

    @Bean
    @Qualifier("yandexDiskClient")
    fun webClient(): WebClient = WebClient.create(properties.baseUrl)
}