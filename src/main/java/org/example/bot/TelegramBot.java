package org.example.bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TelegramBot extends TelegramLongPollingBot {

    private Map<Long, String> logins = new HashMap<>();
    private Map<Long, String> passwords = new HashMap<>();
    public boolean messageIsLogin = false;
    public boolean messageIsPasswords = false;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Long chatId = update.getMessage().getChatId();
            String messageText = update.getMessage().getText();
            if ("/start".equals(messageText)) {
                sendMainMenu(chatId);
            }
            else if(messageIsLogin || messageIsPasswords) {
                if (messageIsLogin) {
                    logins.put(chatId, messageText);
                    messageIsLogin = false;
                    messageIsPasswords = true;
                    sendMsg(chatId, "Введите пароль:");
                } else if (messageIsPasswords) {
                    passwords.put(chatId, messageText);
                    messageIsPasswords = false;
                    sendMsg(chatId, "Регистрация прошла успешно!");
                }
            }else{
                switch (messageText) {
                    case "Регистрация":
                        if (!isUserAuthenticated(chatId)) {
                            sendMsg(chatId, "Введите логин:");
                            messageIsLogin = true;
                        }else{
                            sendMsg(chatId, "Вы уже зарегистрированы");
                        }
                        break;
                    case "Изменить данные":
                        sendMsg(chatId, "Введите логин:");
                        messageIsLogin = true;
                        break;
                    case "Посмотреть оценки":

                        break;
                    default:
                        sendMsg(chatId, "Неизвестная команда. Пожалуйста, используйте /start для начала.");
                        break;
                }
            }
        }
    }
    private void sendMainMenu(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Выберите действие:");
        message.setReplyMarkup(getMainMenuKeyboard());

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    private ReplyKeyboardMarkup getMainMenuKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setOneTimeKeyboard(true);
        keyboardMarkup.setResizeKeyboard(true);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        row1.add("Регистрация");
        row1.add("Изменить данные");
        keyboard.add(row1);

        KeyboardRow row2 = new KeyboardRow();
        row2.add("Посмотреть оценки");
        keyboard.add(row2);
        keyboardMarkup.setKeyboard(keyboard);

        return keyboardMarkup;
    }
    private boolean isUserAuthenticated(Long chatId) {
        return logins.containsKey(chatId) && passwords.containsKey(chatId);
    }


    private void sendMsg(Long chatId, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId.toString());
        sendMessage.setText(message);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    public String getBotToken(){
        return "6891926075:AAE7tvJuDQmUHxPQlUoT2UadHpUsd5kWhGA";
    }
    @Override
    public String getBotUsername() {
        return "rating767_bot";
    }
}
