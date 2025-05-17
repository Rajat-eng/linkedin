package com.codingshuttle.linkedin.posts_service.service;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration

public class KafkaTopicConfig {
    public static final String POST_CREATED_TOPIC = "post-created-topic";
    public static final String POST_LIKED_TOPIC = "post-liked-topic";
    @Bean
    public NewTopic postCreatedTopic() {
        return new NewTopic("post-created-topic", 3, (short) 1);
    }

    @Bean
    public NewTopic postLikedTopic() {
        return new NewTopic("post-liked-topic", 3, (short) 1);
    }
}
