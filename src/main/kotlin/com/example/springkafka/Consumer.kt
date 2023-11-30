package com.example.springkafka

import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import java.util.concurrent.CountDownLatch




@Component
class Consumer {
    val latch = CountDownLatch(1)
    @KafkaListener(id = "myId", topics = ["topic1"])
    fun listen(value: Message?) {
        println(value)
        println(value!!.message)
        println(value!!.author)
        latch.countDown();
    }
}