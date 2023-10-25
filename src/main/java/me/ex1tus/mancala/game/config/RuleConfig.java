package me.ex1tus.mancala.game.config;

import me.ex1tus.mancala.game.rule.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RuleConfig {

    @Bean
    public GameRule rules() {
        return new StoneDistributionRule(
                new StoneCapturingRule(
                        new TurnSwitchRule(
                                new GameOverRule())));
    }
}
