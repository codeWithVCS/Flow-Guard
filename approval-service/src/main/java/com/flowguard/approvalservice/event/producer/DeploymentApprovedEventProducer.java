package com.flowguard.approvalservice.event.producer;

import com.flowguard.approvalservice.event.model.DeploymentApprovedEventV1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class DeploymentApprovedEventProducer {

    private static final Logger log =
            LoggerFactory.getLogger(DeploymentApprovedEventProducer.class);

    private final KafkaTemplate<String, DeploymentApprovedEventV1> kafkaTemplate;
    private final String topicName;

    public DeploymentApprovedEventProducer(
            KafkaTemplate<String, DeploymentApprovedEventV1> kafkaTemplate,
            @Value("${app.kafka.topics.deployment-approved}") String topicName
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicName = topicName;
    }

    public void publish(DeploymentApprovedEventV1 event) {
        kafkaTemplate.send(
                topicName,
                event.getDeploymentId().toString(),
                event
        );

        log.info(
                "DeploymentApprovedEvent published | deploymentId={} | eventId={}",
                event.getDeploymentId(),
                event.getEventId()
        );
    }
}
