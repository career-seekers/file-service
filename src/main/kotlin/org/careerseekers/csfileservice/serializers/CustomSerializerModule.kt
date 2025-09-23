package org.careerseekers.csfileservice.serializers

import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.careerseekers.csfileservice.dto.CloudFileSavingDto
import org.careerseekers.csfileservice.dto.KafkaMessagesDto

object CustomSerializerModule {
    val customSerializerModule = SerializersModule {
        polymorphic(KafkaMessagesDto::class) {
            subclass(CloudFileSavingDto::class, CloudFileSavingDto.serializer())
        }
    }

    val json = Json {
        serializersModule = customSerializerModule
        classDiscriminator = "type"
        ignoreUnknownKeys = true
    }
}