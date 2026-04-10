package com.librasja.libras_api.messaging;

import com.librasja.libras_api.config.RabbitMqConfig;
import com.librasja.libras_api.dto.FeedbackEventDto;
import com.librasja.libras_api.dto.SessionEventDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@ConditionalOnProperty(name = "app.rabbitmq.enabled", havingValue = "true")
public class EventConsumer {

    @RabbitListener(queues = RabbitMqConfig.SESSION_CREATED_QUEUE)
    public void handleSessionCreatedEvent(SessionEventDto event) {
        log.info("Evento de sessão criada recebido: sessionId={}, requesterId={}, interpreterId={}",
                event.getSessionId(), event.getRequesterId(), event.getInterpreterId());
    }

    @RabbitListener(queues = RabbitMqConfig.SESSION_FINISHED_QUEUE)
    public void handleSessionFinishedEvent(SessionEventDto event) {
        log.info("Evento de sessão finalizada recebido: sessionId={}, status={}",
                event.getSessionId(), event.getStatus());
    }

    @RabbitListener(queues = RabbitMqConfig.FEEDBACK_CREATED_QUEUE)
    public void handleFeedbackCreatedEvent(FeedbackEventDto event) {
        log.info("Evento de feedback criado recebido: feedbackId={}, sessionId={}, rating={}",
                event.getFeedbackId(), event.getSessionId(), event.getRating());
    }
}
