package org.careerseekers.csfileservice.services.kafka.producers

import org.careerseekers.csfileservice.dto.CloudFileSavingDto
import org.careerseekers.csfileservice.enums.KafkaTopics
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class CloudFileSavingKafkaProducer(
    override val template: KafkaTemplate<String, CloudFileSavingDto>
) : CustomKafkaProducer<CloudFileSavingDto> {
    override val topic = KafkaTopics.CLOUD_FILE_TRANSFER_TOPIC
}