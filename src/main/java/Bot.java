import lombok.*;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class Bot extends TelegramLongPollingBot {
    private static final Logger log = Logger.getLogger(Bot.class);

    final int RECONNECT_PAUSE = 10000;

    @Setter
    @Getter
    String userName;
    @Setter
    @Getter
    String token;

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {

        Long chatId = update.getMessage().getChatId();
        String inputText = update.getMessage().getText();

        log.info("Receive new Update -> updateID: " + update.getUpdateId()
                + " Text: " + inputText
                + " From User: " + update.getMessage().getFrom().getUserName()
                + " From Chat: " + update.getMessage().getChat().getTitle());

        System.out.println(update.getMessage().getSticker());

        SendSticker sendSticker = new SendSticker();
        sendSticker.setChatId(chatId);
        sendSticker.setSticker(Sticker.THUMB_UP_CAT.toString());
        execute(sendSticker);

    }

    @Override
    public String getBotUsername() {
        return userName;
    }

    @Override
    public String getBotToken() {
        return token;
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