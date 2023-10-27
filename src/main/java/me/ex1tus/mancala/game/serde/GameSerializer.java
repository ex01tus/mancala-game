package me.ex1tus.mancala.game.serde;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import me.ex1tus.mancala.game.model.Game;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

@RequiredArgsConstructor
public class GameSerializer implements RedisSerializer<Game> {

    private final ObjectMapper objectMapper;

    @Override
    @SneakyThrows
    public byte[] serialize(Game game) throws SerializationException {
        return objectMapper.writeValueAsBytes(game);
    }

    @Override
    @SneakyThrows
    public Game deserialize(byte[] bytes) throws SerializationException {
        return bytes != null ? objectMapper.readValue(bytes, Game.class) : null;
    }
}
