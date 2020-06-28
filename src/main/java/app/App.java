package app;

import org.apache.log4j.Logger;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import utilities.AnagramHandler;

public class App {
    private static final Logger log = Logger.getLogger(App.class);
    private static final String BOT_ADMIN = "312541201";

    public static void main(String[] args) {
        log.info("app.App is starting...");
        ApiContextInitializer.init();
        Bot annagramBot = new Bot("AnnagramBot", "1268361427:AAEBI07yEkQ5PbDkczeq0V9stcZ0GmRe5-E");

        AnagramHandler anagramHandler = new AnagramHandler(annagramBot);

        annagramBot.botConnect();

        Thread receiver = new Thread(anagramHandler);
        receiver.setDaemon(true);
        receiver.setName("MsgReceiver");
        receiver.start();

        sendStartReport(annagramBot);
    }

    private static void sendStartReport(Bot bot) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(BOT_ADMIN);
        sendMessage.setText("Запустился");
        bot.sendQueue.add(sendMessage);
    }

}

