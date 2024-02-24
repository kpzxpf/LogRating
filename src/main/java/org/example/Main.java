package org.example;

import org.example.bot.TelegramBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsApi telegramBot = new TelegramBotsApi(DefaultBotSession.class);
        try {
            telegramBot.registerBot(new TelegramBot());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}