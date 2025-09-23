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
    val fileBytes: ByteArray,
    val filename: String,
    val type: KafkaFileTypes,
) : KafkaMessagesDto() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CloudFileSavingDto

        if (!fileBytes.contentEquals(other.fileBytes)) return false
        if (filename != other.filename) return false
        if (type != other.type) return false

        return true
    }

    override fun hashCode(): Int {
        var result = fileBytes.contentHashCode()
        result = 31 * result + filename.hashCode()
        result = 31 * result + type.hashCode()
        return result
    }
}