package me.ex1tus.mancala.game.service.messaging.impl;

import me.ex1tus.mancala.game.model.Game;
import me.ex1tus.mancala.game.service.messaging.MessagingService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(value = "features.stomp.enabled", havingValue = "false")
public class NoopGameMessagingService implements MessagingService<Game> {

    @Override
    public Game publish(Game game) {
        return game;
    }
}
