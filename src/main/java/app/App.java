package app;

import org.apache.log4j.Logger;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class App {
    private static final Logger log = Logger.getLogger(App.class);
    private static final String BOT_ADMIN = "312541201";
    private static final String BOT_NAME = "AnnagramBot";
    private static final String BOT_TOKEN = "1268361427:AAEBI07yEkQ5PbDkczeq0V9stcZ0GmRe5-E";


    public static void main(String[] args) {
        log.info("app.App is starting...");
        ApiContextInitializer.init();
        Bot bot = new Bot(BOT_NAME, BOT_TOKEN);

        AnnagramBot annagramBot = new AnnagramBot(bot);

        bot.botConnect();

        Thread receiver = new Thread(annagramBot);
        receiver.setDaemon(true);
        receiver.setName("MsgReceiver");
        receiver.start();

        sendStartReport(bot);
    }

    private static void sendStartReport(Bot bot) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(BOT_ADMIN);
        sendMessage.setText("Запустился");
        bot.sendQueue.add(sendMessage);
    }

}

