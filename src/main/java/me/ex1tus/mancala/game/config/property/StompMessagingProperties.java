package me.ex1tus.mancala.game.config.property;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("features.stomp")
@ConditionalOnProperty(value = "features.stomp.enabled", havingValue = "true")
public class StompMessagingProperties {

    private String topic;
}
