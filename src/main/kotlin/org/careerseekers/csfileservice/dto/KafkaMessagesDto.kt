package org.careerseekers.csfileservice.dto

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.careerseekers.csfileservice.enums.KafkaFileTypes
import org.springframework.http.codec.multipart.FilePart

@Serializable
@Polymorphic
sealed class KafkaMessagesDto : DtoClass

@Serializable
@SerialName("CloudFileSavingDto")
data class CloudFileSavingDto(
    val filename: String,
    val type: KafkaFileTypes,
    val file: FilePart,
) : KafkaMessagesDto()