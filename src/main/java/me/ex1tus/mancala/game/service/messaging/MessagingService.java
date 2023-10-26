package me.ex1tus.mancala.game.service.messaging;

public interface MessagingService<T> {

    T publish(T message);
}
