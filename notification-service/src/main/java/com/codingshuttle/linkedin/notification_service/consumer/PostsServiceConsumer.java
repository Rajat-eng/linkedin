package com.codingshuttle.linkedin.notification_service.consumer;

import com.codingshuttle.linkedin.notification_service.clients.ConnectionsClient;
import com.codingshuttle.linkedin.notification_service.dto.PersonDto;
import com.codingshuttle.linkedin.notification_service.service.SendNotification;
import com.codingshuttle.linkedin.posts_service.event.PostCreatedEvent;
import com.codingshuttle.linkedin.posts_service.event.PostLikedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
@Slf4j
@RequiredArgsConstructor
public class PostsServiceConsumer {
    private final ConnectionsClient connectionsClient;
    private final SendNotification sendNotification;

    @KafkaListener(topics = "post-created-topic")
    public void consumePostCreatedEvent(PostCreatedEvent postCreatedEvent) {
        log.info("Received Post Created Event: {}", postCreatedEvent);
        List<PersonDto> connections = connectionsClient.getFirstDegreeConnections(postCreatedEvent.getCreatorId());
        for (PersonDto connection : connections) {
            sendNotification.send(connection.getUserId(),  "Your connection "+ postCreatedEvent.getCreatorId() +" has created" +
            " a post, Check it out");
        }
    }

    @KafkaListener(topics = "post-liked-topic")
    public void handlePostLiked(PostLikedEvent postLikedEvent) {
        log.info("Sending notifications: handlePostLiked: {}", postLikedEvent);
        String message = String.format("Your post, %d has been liked by %d", postLikedEvent.getPostId(),
                postLikedEvent.getLikedByUserId());

        sendNotification.send(postLikedEvent.getCreatorId(), message);
    }
}
