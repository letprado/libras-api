package com.librasja.libras_api.messaging;

import com.librasja.libras_api.config.RabbitMqConfig;
import com.librasja.libras_api.dto.FeedbackEventDto;
import com.librasja.libras_api.dto.SessionEventDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publishSessionCreatedEvent(SessionEventDto event) {
        log.info("Publicando evento de sessão criada: {}", event.getSessionId());
        rabbitTemplate.convertAndSend(
                RabbitMqConfig.SESSION_EXCHANGE,
                RabbitMqConfig.SESSION_CREATED_ROUTING_KEY,
                event
        );
    }

    public void publishSessionFinishedEvent(SessionEventDto event) {
        log.info("Publicando evento de sessão finalizada: {}", event.getSessionId());
        rabbitTemplate.convertAndSend(
                RabbitMqConfig.SESSION_EXCHANGE,
                RabbitMqConfig.SESSION_FINISHED_ROUTING_KEY,
                event
        );
    }

    public void publishFeedbackCreatedEvent(FeedbackEventDto event) {
        log.info("Publicando evento de feedback criado: {}", event.getFeedbackId());
        rabbitTemplate.convertAndSend(
                RabbitMqConfig.SESSION_EXCHANGE,
                RabbitMqConfig.FEEDBACK_CREATED_ROUTING_KEY,
                event
        );
    }
}
