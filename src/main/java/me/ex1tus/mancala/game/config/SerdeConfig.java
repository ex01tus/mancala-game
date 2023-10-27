package me.ex1tus.mancala.game.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.ex1tus.mancala.game.serde.GameSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(value = "features.redis.enabled", havingValue = "true")
public class SerdeConfig {

    @Bean
    public GameSerializer gameSerializer() {
        return new GameSerializer(new ObjectMapper());
    }
}
