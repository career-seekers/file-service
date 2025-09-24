package org.careerseekers.csfileservice.services

import com.fasterxml.jackson.databind.JsonNode
import org.careerseekers.csfileservice.config.YandexDiskProperties
import org.careerseekers.csfileservice.enums.KafkaFileTypes
import org.careerseekers.csfileservice.enums.KafkaFileTypes.Companion.getAlias
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import java.nio.file.Files
import java.nio.file.Paths

@Service
class YandexDiskService(
    private val properties: YandexDiskProperties,
    @param:Qualifier("yandexDiskClient") private val webClient: WebClient,
) {

    fun uploadFileToDisk(filePath: String, filename: String, fileType: KafkaFileTypes): Mono<Void> {
        return Mono.fromCallable { Files.readAllBytes(Paths.get(filePath)) }
            .subscribeOn(Schedulers.boundedElastic())
            .flatMap { fileBytes ->
                webClient.get()
                    .uri("/v1/disk/resources/upload?path=/career-seekers-backups/${fileType.getAlias()}/$filename&overwrite=true")
                    .header("Authorization", properties.oauthToken)
                    .retrieve()
                    .bodyToMono(JsonNode::class.java)
                    .map { it.get("href").asText() }
                    .flatMap { uploadUrl ->
                        webClient.put()
                            .uri(uploadUrl)
                            .bodyValue(fileBytes)
                            .retrieve()
                            .bodyToMono(Void::class.java)
                    }
            }
    }
}