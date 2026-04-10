package com.librasja.libras_api.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    public static final String SESSION_EXCHANGE = "libras.sessions.exchange";
    public static final String SESSION_CREATED_QUEUE = "libras.sessions.created.queue";
    public static final String SESSION_FINISHED_QUEUE = "libras.sessions.finished.queue";
    public static final String SESSION_CREATED_ROUTING_KEY = "session.created";
    public static final String SESSION_FINISHED_ROUTING_KEY = "session.finished";
    public static final String FEEDBACK_CREATED_QUEUE = "libras.feedbacks.created.queue";
    public static final String FEEDBACK_CREATED_ROUTING_KEY = "feedback.created";

    @Bean
    public TopicExchange sessionExchange() {
        return new TopicExchange(SESSION_EXCHANGE, true, false);
    }

    @Bean
    public Queue sessionCreatedQueue() {
        return new Queue(SESSION_CREATED_QUEUE, true);
    }

    @Bean
    public Queue sessionFinishedQueue() {
        return new Queue(SESSION_FINISHED_QUEUE, true);
    }

    @Bean
    public Queue feedbackCreatedQueue() {
        return new Queue(FEEDBACK_CREATED_QUEUE, true);
    }

    @Bean
    public Binding sessionCreatedBinding(Queue sessionCreatedQueue, TopicExchange sessionExchange) {
        return BindingBuilder.bind(sessionCreatedQueue)
                .to(sessionExchange)
                .with(SESSION_CREATED_ROUTING_KEY);
    }

    @Bean
    public Binding sessionFinishedBinding(Queue sessionFinishedQueue, TopicExchange sessionExchange) {
        return BindingBuilder.bind(sessionFinishedQueue)
                .to(sessionExchange)
                .with(SESSION_FINISHED_ROUTING_KEY);
    }

    @Bean
    public Binding feedbackCreatedBinding(Queue feedbackCreatedQueue, TopicExchange sessionExchange) {
        return BindingBuilder.bind(feedbackCreatedQueue)
                .to(sessionExchange)
                .with(FEEDBACK_CREATED_ROUTING_KEY);
    }
}
