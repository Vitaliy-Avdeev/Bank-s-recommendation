package org.skypro.recommendService.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.skypro.recommendService.DTO.RecommendationObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TelegramMessageService {

    @Autowired
    private TelegramBot telegramBot;

    public void sendStartMessage(Long chatId) {
        telegramBot.execute(new SendMessage(chatId, "справка"));
    }

    public void sendUserNotFound(Long chatId) {
        telegramBot.execute(new SendMessage(chatId, "Пользователь не найден"));
    }

    public void sendRecommendations(Long chatId, String name,
                                    Optional<List<RecommendationObject>> invest500,
                                    Optional<List<RecommendationObject>> topSaving,
                                    Optional<List<RecommendationObject>> simpleCredit) {

        if (invest500.isEmpty() && topSaving.isEmpty() && simpleCredit.isEmpty()) {
            telegramBot.execute(new SendMessage(chatId, "Рекомендаций нет"));
            return;
        }

        StringBuilder message = new StringBuilder();
        message.append("Новые продукты для вас:\n").append(name).append(":\n");

        invest500.ifPresent(list -> list.forEach(obj ->
                message.append("Продукт: ").append(obj.getName()).append("\n")
                        .append(obj.getText()).append("\n\n")));

        topSaving.ifPresent(list -> list.forEach(obj ->
                message.append("Продукт: ").append(obj.getName()).append("\n")
                        .append(obj.getText()).append("\n\n")));

        simpleCredit.ifPresent(list -> list.forEach(obj ->
                message.append("Продукт: ").append(obj.getName()).append("\n")
                        .append(obj.getText()).append("\n\n")));

        telegramBot.execute(new SendMessage(chatId, message.toString()));
    }
}

