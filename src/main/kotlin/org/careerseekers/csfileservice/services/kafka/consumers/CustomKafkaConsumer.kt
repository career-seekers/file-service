package org.careerseekers.csfileservice.services.kafka.consumers

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.support.Acknowledgment

interface CustomKafkaConsumer<T, K> {
    fun receiveMessage(consumerRecord: ConsumerRecord<T, K>, acknowledgment: Acknowledgment): Any
}