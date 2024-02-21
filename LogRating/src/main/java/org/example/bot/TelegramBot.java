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

    public TelegramBot() throws SQLException {
    }

    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            this.chatId = update.getMessage().getChatId();
            String messageText = update.getMessage().getText();
            if ("/start".equals(messageText)) {
                this.sendMainMenu(this.chatId);
            } else if (!this.messageIsLogin && !this.messageIsPasswords) {
                switch (messageText) {
                    case "Регистрация":
                        try {
                            if (!this.isUserAuthenticated(this.chatId)) {
                                this.sendMsg(this.chatId, "Введите логин:");
                                this.messageIsLogin = true;
                            } else {
                                this.sendMsg(this.chatId, "Вы уже зарегистрированы");
                            }
                            break;
                        } catch (SQLException var7) {
                            throw new RuntimeException(var7);
                        }
                    case "Удалить аккаунт":
                        try {
                            dataBase.deleteData(chatId);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        sendMsg(chatId, "Успешно");
                        break;
                    case "Посмотреть оценки":
                        try {
                            if (this.parser.parsing(this.chatId)) {
                                this.sendRating();
                            } else {
                                this.sendMsg(this.chatId, "Не вышло, попробуйте еще раз.");
                            }
                            break;
                        } catch (SQLException var6) {
                            throw new RuntimeException(var6);
                        }
                    default:
                        this.sendMsg(this.chatId, "Неизвестная команда. Пожалуйста, используйте /start для начала.");
                }
            } else if (this.messageIsLogin) {
                this.login = messageText;
                this.messageIsLogin = false;
                this.messageIsPasswords = true;
                this.sendMsg(this.chatId, "Введите пароль:");
            } else if (this.messageIsPasswords) {
                this.password = messageText;
                this.messageIsPasswords = false;
                this.sendMsg(this.chatId, "Регистрация прошла успешно!");

                try {
                    this.dataBase.insertInBD(this.chatId, this.login, this.password);
                } catch (SQLException var8) {
                    throw new RuntimeException(var8);
                }
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

    private boolean isUserAuthenticated(Long chatId) throws SQLException {
        return this.dataBase.CheckChatId(chatId);
    }

    private void sendRating() throws SQLException {
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
