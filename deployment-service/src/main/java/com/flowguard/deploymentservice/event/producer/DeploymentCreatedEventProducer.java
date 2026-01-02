package com.flowguard.deploymentservice.event.producer;

import com.flowguard.deploymentservice.event.model.DeploymentCreatedEventV1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class DeploymentCreatedEventProducer {

    private static final Logger log =
            LoggerFactory.getLogger(DeploymentCreatedEventProducer.class);

    private final KafkaTemplate<String, DeploymentCreatedEventV1> kafkaTemplate;
    private final String topic;

    public DeploymentCreatedEventProducer(
            KafkaTemplate<String, DeploymentCreatedEventV1> kafkaTemplate,
            @Value("${app.kafka.topics.deployment-created}") String topic
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    public void publish(DeploymentCreatedEventV1 event) {
        try {
            kafkaTemplate.send(
                    topic,
                    event.getDeploymentId().toString(), // message key
                    event
            );
            log.info(
                    "Published DeploymentCreatedEventV1 for deploymentId={}",
                    event.getDeploymentId()
            );
        } catch (Exception ex) {
            log.error(
                    "Failed to publish DeploymentCreatedEventV1 for deploymentId={}",
                    event.getDeploymentId(),
                    ex
            );
        }
    }
}
