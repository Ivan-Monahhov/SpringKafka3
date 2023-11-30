package com.example.springkafka

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.KafkaContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.utility.DockerImageName
import java.time.Duration
import java.util.concurrent.TimeUnit

@SpringBootTest
class SpringKafkaApplicationTests {

    companion object {
        @Container
        private val kafkaContainer =
            KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest")).withExposedPorts(2181, 9092, 9093)

        @DynamicPropertySource
        @JvmStatic
        fun registerDynamicProperties(registry: DynamicPropertyRegistry) {
            kafkaContainer.withStartupTimeout(Duration.ofMinutes(1))
            // have docker running or else
            kafkaContainer.start()
            val url = kafkaContainer.bootstrapServers
            val elasticUrlSupplier: () -> String = { url }
            registry.add("spring.kafka.producer.bootstrap-servers", elasticUrlSupplier)
            registry.add("spring.kafka.consumer.bootstrap-servers", elasticUrlSupplier)

            Assertions.assertTrue(kafkaContainer.isRunning)
        }
    }

    @Autowired
    lateinit var produce: Produce

    @Autowired
    lateinit var consumer: Consumer

    @Test
    fun contextLoads() {
        produce.runner()
        val messageConsumed: Boolean = consumer.latch.await(10, TimeUnit.SECONDS)
        Assertions.assertTrue(messageConsumed)
    }
}
