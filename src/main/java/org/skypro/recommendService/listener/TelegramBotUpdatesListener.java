package org.skypro.recommendService.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import jakarta.annotation.PostConstruct;
import org.skypro.recommendService.DTO.RecommendationObject;
import org.skypro.recommendService.repository.RecommendationsRepository;
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
                telegramBot.execute(new SendMessage(chat_id, "справка"));
            } else if(textMessage.contains("/recommend")){

                String userId = textMessage.split(" ")[1];
                String name;
                if(userId != null){
                    name = recommendationsRepository.getUserNameById(UUID.fromString(userId));
                    if(name == null){
                        telegramBot.execute(new SendMessage(chat_id, "Пользователь не найден"));
                    }

                    StringBuilder message = new StringBuilder();
                    message.append("Новые продукты для вас:\n").append(name).append(":\n");

                    Optional<List<RecommendationObject>> invest500 = recommendationsRepository.
                            checkInvest500(UUID.fromString(userId));
                    Optional<List<RecommendationObject>> topSaving = recommendationsRepository.
                            checkTopSaving(UUID.fromString(userId));
                    Optional<List<RecommendationObject>> simpleCredit = recommendationsRepository.
                            checkSimpleCredit(UUID.fromString(userId));
                    if(invest500.isEmpty() && topSaving.isEmpty() && simpleCredit.isEmpty()){
                        telegramBot.execute(new SendMessage(chat_id, "Рекомендаций нет"));
                    }


                    invest500.ifPresent(list -> {
                        for(RecommendationObject object : list) {
                            message.append("Продукт: ").append(object.getName()).append("\n").
                                    append(object.getText()).append("\n\n");
                        }
                    });

                    topSaving.ifPresent(list -> {
                        for(RecommendationObject object : list) {
                            message.append("Продукт: ").append(object.getName()).append("\n").
                                    append(object.getText()).append("\n\n");
                        }
                    });

                    simpleCredit.ifPresent(list -> {
                        for(RecommendationObject object : list) {
                            message.append("Продукт: ").append(object.getName()).append("\n").
                                    append(object.getText()).append("\n\n");
                        }
                    });

                    telegramBot.execute(new SendMessage(chat_id, message.toString()));
                }
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}
