package org.careerseekers.csfileservice.services.kafka.consumers

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.careerseekers.csfileservice.dto.CloudFileSavingDto
import org.careerseekers.csfileservice.services.YandexDiskService
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Service

@Service
class CloudFileSavingKafkaConsumer(private val yandexDiskService: YandexDiskService) :
    CustomKafkaConsumer<String, CloudFileSavingDto> {

    @KafkaListener(
        topics = ["CLOUD_FILE_TRANSFER_TOPIC"],
        groupId = "cloudFileTransferTopicConsumer"
    )
    override fun receiveMessage(
        consumerRecord: ConsumerRecord<String, CloudFileSavingDto>,
        acknowledgment: Acknowledgment
    ) {
        val message = consumerRecord.value()

        yandexDiskService.uploadFileToDisk(
            filePath = message.filePath,
            filename = message.filename,
            fileType = message.fileType,
        ).subscribe()
        acknowledgment.acknowledge()
    }
}