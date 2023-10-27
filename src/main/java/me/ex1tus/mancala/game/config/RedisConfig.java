package me.ex1tus.mancala.game.config;

import me.ex1tus.mancala.game.model.Game;
import me.ex1tus.mancala.game.serde.GameSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@ConditionalOnProperty(value = "features.redis.enabled", havingValue = "true")
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Game> redisTemplate(
            RedisConnectionFactory connectionFactory,
            GameSerializer gameSerializer) {
        RedisTemplate<String, Game> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(gameSerializer);

        return template;
    }
}
