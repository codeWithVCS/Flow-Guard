package com.flowguard.changeservice.event.producer;

import com.flowguard.changeservice.event.model.ChangeRecordedEventV1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ChangeRecordedEventProducer {

    private static final Logger log =
            LoggerFactory.getLogger(ChangeRecordedEventProducer.class);

    private final KafkaTemplate<String, ChangeRecordedEventV1> kafkaTemplate;
    private final String topicName;

    public ChangeRecordedEventProducer(
            KafkaTemplate<String, ChangeRecordedEventV1> kafkaTemplate,
            @Value("${app.kafka.topics.change-recorded}") String topicName
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicName = topicName;
    }

    public void publish(String key, ChangeRecordedEventV1 event) {

        try {
            kafkaTemplate.send(topicName, key, event);
            log.info(
                    "Published ChangeRecordedEventV1 to topic={}, key={}, eventId={}",
                    topicName,
                    key,
                    event.getEventId()
            );
        } catch (Exception ex) {
            log.error(
                    "Failed to publish ChangeRecordedEventV1, key={}, eventId={}",
                    key,
                    event.getEventId(),
                    ex
            );
        }
    }
}
