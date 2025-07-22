package org.skypro.recommendService.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import jakarta.annotation.PostConstruct;
import org.skypro.recommendService.DTO.RecommendationObject;
import org.skypro.recommendService.repository.RecommendationsRepository;
import org.skypro.recommendService.service.TelegramMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    @Autowired
    private RecommendationsRepository recommendationsRepository;

    @Autowired
    private TelegramMessageService telegramMessageService;

    @Autowired
    private TelegramBot telegramBot;

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            String textMessage = update.message().text();
            Long chat_id = update.message().chat().id();

            if (textMessage.equals("/start")) {
                telegramMessageService.sendStartMessage(chat_id);
            } else if (textMessage.contains("/recommend")) {
                String userId = textMessage.split(" ")[1];
                if (userId != null) {
                    String name = recommendationsRepository.getUserNameById(UUID.fromString(userId));
                    if (name == null) {
                        telegramMessageService.sendUserNotFound(chat_id);
                    } else {
                        var invest500 = recommendationsRepository.checkInvest500(UUID.fromString(userId));
                        var topSaving = recommendationsRepository.checkTopSaving(UUID.fromString(userId));
                        var simpleCredit = recommendationsRepository.checkSimpleCredit(UUID.fromString(userId));
                        telegramMessageService.sendRecommendations(chat_id, name, invest500, topSaving, simpleCredit);
                    }
                }
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}

