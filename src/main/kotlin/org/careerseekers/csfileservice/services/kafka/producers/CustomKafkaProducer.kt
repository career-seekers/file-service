package org.careerseekers.csfileservice.services.kafka.producers

import org.careerseekers.csfileservice.dto.KafkaMessagesDto
import org.careerseekers.csfileservice.enums.KafkaTopics
import org.springframework.kafka.core.KafkaTemplate

interface CustomKafkaProducer<T : KafkaMessagesDto> {
    val topic: KafkaTopics
    val template: KafkaTemplate<String, T>

    fun sendMessage(message: T) {
        template.send(topic.name, message)
    }
}