package me.ex1tus.mancala.game.service.messaging.impl;

import lombok.RequiredArgsConstructor;
import me.ex1tus.mancala.game.config.property.StompMessagingProperties;
import me.ex1tus.mancala.game.model.Game;
import me.ex1tus.mancala.game.service.messaging.MessagingService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(value = "features.stomp.enabled", havingValue = "true")
public class StompGameMessagingService implements MessagingService<Game> {

    private final SimpMessagingTemplate simp;
    private final StompMessagingProperties properties;

    @Override
    public Game publish(Game game) {
        simp.convertAndSend(properties.getTopic() + game.id(), game);
        return game;
    }
}
