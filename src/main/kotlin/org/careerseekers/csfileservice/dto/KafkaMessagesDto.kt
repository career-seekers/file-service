package org.careerseekers.csfileservice.dto

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.careerseekers.csfileservice.enums.KafkaFileTypes

@Serializable
@Polymorphic
sealed class KafkaMessagesDto : DtoClass

@Serializable
@SerialName("CloudFileSavingDto")
data class CloudFileSavingDto(
    val filePath: String,
    val filename: String,
    val fileType: KafkaFileTypes,
) : KafkaMessagesDto()