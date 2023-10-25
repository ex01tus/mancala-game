package me.ex1tus.mancala.game.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("game")
public class GameProperties {

    private int pitsInRow;
    private int stonesInPit;
}
