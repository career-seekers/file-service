package org.careerseekers.csfileservice.services

import com.fasterxml.jackson.databind.JsonNode
import org.careerseekers.csfileservice.config.YandexDiskProperties
import org.careerseekers.csfileservice.entities.FilesStorage
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.core.io.buffer.DataBufferUtils
import org.springframework.http.codec.multipart.FilePart
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Service
class YandexDiskService(
    private val properties: YandexDiskProperties,
    @param:Qualifier("yandexDiskClient") private val webClient: WebClient,
) {

    fun uploadFileToDisk(file: FilePart, item: FilesStorage): Mono<Void> {
        return webClient.get()
            .uri("/v1/disk/resources/upload?path=/career-seekers-backups/docs/${item.storedFilename}&overwrite=true")
            .header("Authorization", properties.oauthToken)
            .retrieve()
            .bodyToMono(JsonNode::class.java)
            .map { it.get("href").asText() }
            .flatMap { uploadUrl ->
                DataBufferUtils.join(file.content())
                    .map { dataBuffer ->
                        val bytes = ByteArray(dataBuffer.readableByteCount())
                        dataBuffer.read(bytes)
                        DataBufferUtils.release(dataBuffer)
                        bytes
                    }
                    .flatMap { bytes ->
                        webClient.put()
                            .uri(uploadUrl)
                            .bodyValue(bytes)
                            .retrieve()
                            .bodyToMono(Void::class.java)
                    }
            }
    }
}