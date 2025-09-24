package org.careerseekers.csfileservice.config

import org.apache.kafka.clients.admin.NewTopic
import org.careerseekers.csfileservice.enums.KafkaTopics
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.TopicBuilder

@Configuration
class KafkaTopicsConfig {

    @Bean
    fun cloudFileTransferTopic(): NewTopic {
        return TopicBuilder
            .name(KafkaTopics.CLOUD_FILE_TRANSFER_TOPIC.name)
            .partitions(12)
            .replicas(3)
            .build()
    }
}