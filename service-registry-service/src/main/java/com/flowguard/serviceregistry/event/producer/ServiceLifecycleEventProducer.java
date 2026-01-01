package com.flowguard.serviceregistry.event.producer;

import com.flowguard.serviceregistry.event.model.ServiceLifecycleEventV1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ServiceLifecycleEventProducer {

    private static final Logger log =
            LoggerFactory.getLogger(ServiceLifecycleEventProducer.class);

    private static final String TOPIC = "service.lifecycle.v1";

    private final KafkaTemplate<String, ServiceLifecycleEventV1> kafkaTemplate;

    public ServiceLifecycleEventProducer(
            KafkaTemplate<String, ServiceLifecycleEventV1> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(ServiceLifecycleEventV1 event) {

        kafkaTemplate.send(TOPIC, event.getEventId().toString(), event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error(
                                "Failed to publish service lifecycle event. eventId={}",
                                event.getEventId(),
                                ex
                        );
                    } else {
                        log.info(
                                "Service lifecycle event published. topic={}, partition={}, offset={}, eventId={}",
                                result.getRecordMetadata().topic(),
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset(),
                                event.getEventId()
                        );
                    }
                });
    }
}
