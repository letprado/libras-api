package com.librasja.libras_api.messaging;

import com.librasja.libras_api.config.RabbitMqConfig;
import com.librasja.libras_api.dto.FeedbackEventDto;
import com.librasja.libras_api.dto.SessionEventDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventPublisher {

    private final ObjectProvider<RabbitTemplate> rabbitTemplateProvider;

    @Value("${app.rabbitmq.enabled:false}")
    private boolean rabbitMqEnabled;

    public void publishSessionCreatedEvent(SessionEventDto event) {
        RabbitTemplate rabbitTemplate = getRabbitTemplate();
        if (rabbitTemplate == null) {
            return;
        }

        log.info("Publicando evento de sessão criada: {}", event.getSessionId());
        rabbitTemplate.convertAndSend(
                RabbitMqConfig.SESSION_EXCHANGE,
                RabbitMqConfig.SESSION_CREATED_ROUTING_KEY,
                event
        );
    }

    public void publishSessionFinishedEvent(SessionEventDto event) {
        RabbitTemplate rabbitTemplate = getRabbitTemplate();
        if (rabbitTemplate == null) {
            return;
        }

        log.info("Publicando evento de sessão finalizada: {}", event.getSessionId());
        rabbitTemplate.convertAndSend(
                RabbitMqConfig.SESSION_EXCHANGE,
                RabbitMqConfig.SESSION_FINISHED_ROUTING_KEY,
                event
        );
    }

    public void publishFeedbackCreatedEvent(FeedbackEventDto event) {
        RabbitTemplate rabbitTemplate = getRabbitTemplate();
        if (rabbitTemplate == null) {
            return;
        }

        log.info("Publicando evento de feedback criado: {}", event.getFeedbackId());
        rabbitTemplate.convertAndSend(
                RabbitMqConfig.SESSION_EXCHANGE,
                RabbitMqConfig.FEEDBACK_CREATED_ROUTING_KEY,
                event
        );
    }

    private RabbitTemplate getRabbitTemplate() {
        if (!rabbitMqEnabled) {
            log.info("RabbitMQ desativado temporariamente. Evento não será publicado.");
            return null;
        }

        RabbitTemplate rabbitTemplate = rabbitTemplateProvider.getIfAvailable();
        if (rabbitTemplate == null) {
            log.warn("RabbitTemplate não está disponível. Evento não será publicado.");
        }

        return rabbitTemplate;
    }
}
