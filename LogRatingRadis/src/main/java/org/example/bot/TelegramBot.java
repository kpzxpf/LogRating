package org.example.bot;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.example.DataBase;
import org.example.Parser;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;



public class TelegramBot extends TelegramLongPollingBot {
    private String login;
    private String password;
    private long chatId;
    private DataBase dataBase = new DataBase();
    private Parser parser = new Parser();
    public boolean messageIsLogin = false;
    public boolean messageIsPasswords = false;
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            this.chatId = update.getMessage().getChatId();
            String messageText = update.getMessage().getText();
            if ("/start".equals(messageText)) {
                sendMainMenu(chatId);
            } else if (!messageIsLogin && !messageIsPasswords) {
                switch (messageText) {
                    case "Регистрация":
                        if (!isUserAuthenticated(chatId)) {
                            sendMsg(chatId, "Введите логин:");
                            messageIsLogin = true;
                        } else {
                            sendMsg(chatId, "Вы уже зарегистрированы");
                        }
                        break;
                    case "Удалить аккаунт":
                        dataBase.deleteData(chatId);
                        sendMsg(chatId, "Успешно");
                        break;
                    case "Посмотреть оценки":
                        if (parser.parsing(chatId)) {
                                sendRating();
                        } else {
                                sendMsg(this.chatId, "Не вышло, попробуйте еще раз.");
                        }
                        break;
                    default:
                        this.sendMsg(this.chatId, "Неизвестная команда. Пожалуйста, используйте /start для начала.");
                }
            } else if (messageIsLogin) {
                login = messageText;
                messageIsLogin = false;
                messageIsPasswords = true;
                sendMsg(this.chatId, "Введите пароль:");
            } else if (messageIsPasswords) {
                password = messageText;
                messageIsPasswords = false;
                sendMsg(this.chatId, "Регистрация прошла успешно!");
                dataBase.insertInBD(chatId, login, password);
            }
        }

    }

    private void sendMainMenu(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Выберите действие:");
        message.setReplyMarkup(this.getMainMenuKeyboard());

        try {
            this.execute(message);
        } catch (TelegramApiException var5) {
            var5.printStackTrace();
        }

    }

    private ReplyKeyboardMarkup getMainMenuKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setOneTimeKeyboard(true);
        keyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> keyboard = new ArrayList();
        KeyboardRow row1 = new KeyboardRow();
        row1.add("Регистрация");
        row1.add("Удалить аккаунт");
        keyboard.add(row1);
        KeyboardRow row2 = new KeyboardRow();
        row2.add("Посмотреть оценки");
        keyboard.add(row2);
        keyboardMarkup.setKeyboard(keyboard);
        return keyboardMarkup;
    }

    private boolean isUserAuthenticated(Long chatId)  {
        return this.dataBase.CheckChatId(chatId);
    }

    private void sendRating() {
        List<String> logRating = this.parser.getLogRating();
        Iterator var2 = logRating.iterator();

        while(var2.hasNext()) {
            String line = (String)var2.next();
            int index = logRating.indexOf(line);
            if (index > 3 && index != 5 && index != 8 && index != 12 && index != 17) {
                this.sendMsg(this.chatId, line);
            }
        }

    }

    private void sendMsg(Long chatId, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId.toString());
        sendMessage.setText(message);

        try {
            this.execute(sendMessage);
        } catch (TelegramApiException var5) {
            var5.printStackTrace();
        }

    }

    public String getBotToken() {
        return "6891926075:AAE7tvJuDQmUHxPQlUoT2UadHpUsd5kWhGA";
    }

    public String getBotUsername() {
        return "rating767_bot";
    }
}