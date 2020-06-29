package utilities;

import app.Bot;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import repository.TaskRepository;

import java.util.*;

public class AnagramHandler implements Runnable {
    private static final Logger log = Logger.getLogger(AnagramHandler.class);

    private Map<Long, TaskHandler> commonMap = new LinkedHashMap<>();

    private static final String START_COMMAND = "/start";
    private static final String TASK_COMMAND = "/task";
    private static final String LOCAL_RATING_COMMAND = "/rating";
    private static final String OVERALL_RATING_COMMAND = "/overall";

    private Bot bot;

    public AnagramHandler(Bot bot) {
        System.out.println("вызов конструктора");
        this.bot = bot;
    }

    public void parseUpdate(Update update) throws TelegramApiException {

        long chatID = update.getMessage().getChatId();
        String message = update.getMessage().getText();
        String userName = update.getMessage().getFrom().getUserName();

        if (!TaskRepository.contains(chatID) || !commonMap.containsKey(chatID))
            commonMap.put(chatID, new TaskHandler(chatID));

        switch (message) {
            case START_COMMAND:
                System.out.println("start");
                bot.execute(new SendMessage(chatID, "Привет, " + userName).enableHtml(true));
                break;
            case TASK_COMMAND:
                System.out.println(commonMap.get(chatID).showTask());
                bot.execute(new SendMessage(chatID, commonMap.get(chatID).showTask()).enableHtml(true));
                break;
            case LOCAL_RATING_COMMAND:
                System.out.println("rating command");
                break;
            case OVERALL_RATING_COMMAND:
                System.out.println("overall rating");
            default:
                String guessed = commonMap.get(chatID).guess(update);
                if (guessed != null)
                    bot.execute(new SendMessage(chatID, guessed).enableHtml(true));
        }
    }

//    public void get() {
//        log.info(commonMap.size());
//        String nextWord = WordRepository.get();
//        commonMap.put(nextWord, anagramize(nextWord));
//        log.info(commonMap);
//    }
//
//    private void refresh() {
//        for (int i = commonMap.size(); i < 5; i++)
//            get();
//    }


    @Override
    public void run() {
        for (;;) {
            for (Object object = bot.receiveQueue.poll(); object != null; object = bot.receiveQueue.poll()) {
                log.info("New object for analyze in queue " + object.toString());
                try {
                    parseUpdate((Update) object);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                log.error("Catch interrupt. Exit", e);
                return;
            }
        }
    }
}
