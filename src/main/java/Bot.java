import lombok.*;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Bot extends TelegramLongPollingBot {
    private static final Logger log = Logger.getLogger(Bot.class);
    private AnagramHandler anagramHandler;

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

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        UpdateLogger.log(update);
        receiveQueue.add(update);

//        Long chatId = update.getMessage().getChatId();
//        String inputText = update.getMessage().getText();

//        System.out.println(update.getMessage().getSticker());
//
//        SendSticker sendSticker = new SendSticker();
//        sendSticker.setChatId(chatId);
//        sendSticker.setSticker(Sticker.THUMB_UP_CAT.toString());
//        execute(sendSticker);

//        AnagramHandler anagramHandler = new AnagramHandler();
//
//        SendMessage sendMessage = new SendMessage(chatId, anagramHandler.getMessage());
//        sendMessage.enableHtml(true);
//        execute(sendMessage);
//
//        String request = anagramHandler.guess(update.getMessage().getText(), update.getMessage().getFrom());
//        if (request != null) {
//            execute(new SendMessage(chatId, request));
//        }
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
            anagramHandler = new AnagramHandler(this);
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