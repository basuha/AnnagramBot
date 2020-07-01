package app;

import org.apache.log4j.Logger;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Bot extends TelegramLongPollingBot {
    private static final Logger log = Logger.getLogger(Bot.class);

    final int RECONNECT_PAUSE = 10000;

    public final Queue<Object> sendQueue = new ConcurrentLinkedQueue<>();
    public final Queue<Object> receiveQueue = new ConcurrentLinkedQueue<>();

    public Bot() {
    }

    public Bot(String userName, String token) {
        this.userName = userName;
        this.token = token;
    }

    String userName;
    String token;

    @Override
    public void onUpdateReceived(Update update) {
        log.info("Receive new Update -> updateID: " + update.getUpdateId()
                + " Text: " + update.getMessage().getText()
                + " From User: " + update.getMessage().getFrom().getUserName()
                + " From Chat: " + update.getMessage().getChat().getTitle()
                + " ChatID: " + update.getMessage().getChat().getId());

        receiveQueue.add(update);
    }

    @Override
    public String getBotUsername() {
        return userName;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void botConnect() {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(this);
            log.info("TelegramAPI started. Look for messages");
        } catch (TelegramApiRequestException e) {
            log.error("Cant Connect. Pause " + RECONNECT_PAUSE / 1000 + "sec and try again. Error: " + e.getMessage());
            try {
                Thread.sleep(RECONNECT_PAUSE);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
                return;
            }
            botConnect();
        }
    }
}