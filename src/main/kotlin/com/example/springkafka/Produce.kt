package com.example.springkafka

import org.apache.kafka.clients.admin.NewTopic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class Produce {
    @Autowired
    lateinit var template: KafkaTemplate<String?, Message?>
    val message: Message = Message(1,"Will it send object","me")
    @Bean
    fun topic() = NewTopic("topic1", 10, 1)

    @Scheduled(cron = "0/10 * * ? * *")
    fun runner() =
        template.send("topic1", message)
}
