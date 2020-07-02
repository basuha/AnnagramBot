package app;

import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import pojo.BotUser;
import repository.TaskRepository;
import repository.BotUserRepository;
import utilities.ScoreHandler;
import utilities.TaskHandler;

import java.util.*;

public class AnnagramBot implements Runnable {
    private static final Logger log = Logger.getLogger(AnnagramBot.class);

    private Bot bot;

    private static final String INFO_COMMAND = "/info";
    private static final String TASK_COMMAND = "/task";
    private static final String LOCAL_RATING_COMMAND = "/rank";
    private static final String OVERALL_RATING_COMMAND = "/overall";
    private static final String HINT_COMMAND = "/hint";
    private static final String REMIX_COMMAND = "/remix";

    private Map<Long, TaskHandler> commonMap = new LinkedHashMap<>(); //key is chatID, value is task for specific chat

    private static final String BOT_AUTHOR = "Arkadiy Nadyrov";
    private static final String BOT_NAME = "@AnnagramBot";
    private static final String BOT_VERSION = "1.0.9";
    private static final String BOT_INFO = "<b>Привет!</b>\n\nЯ бот-генератор анаграм. " +
            "Я использую базу частоупотребимых существительных русского языка из около 1500 слов. \n\n" +
            "За каждое угаданное слово я начисляю пользователю определенное количество очков. " +
            "Чем дольше слово остается в списке, тем сложнее оно считается, " +
            "и тем больше можно получить очков за его отгадывание. \n\n" +
            "Для просмотра комманд введите \"<code>/</code>\".\n\n\n" +
            "<code>v" + BOT_VERSION + " | Написано на Java, хостинг - Heroku \n" +
            "by " + BOT_AUTHOR + "</code>";


    public AnnagramBot(Bot bot) {
        this.bot = bot;
    }

    public void parseUpdate(Update update) throws TelegramApiException { //TODO: multithreading

        long chatID = update.getMessage().getChatId();
        String message = update.getMessage().getText();
        int userID = update.getMessage().getFrom().getId();
        String userName = update.getMessage().getFrom().getUserName();

        log.info("Message from: " + userName
                + "[id: " + userID + "]"
                + " text: " + message
                + ". from chat: " + update.getMessage().getChat().getTitle());

        if (message == null || message.matches("\\d+")) return;  //if no text to parse then return

        if (!BotUserRepository.isIntroduced(userID,chatID))    //if user is absent in bot`s database
            BotUserRepository.add(new BotUser(userID,userName,chatID)); //add user into database

        if (!TaskRepository.contains(chatID) || !commonMap.containsKey(chatID)) //is any
            commonMap.put(chatID, new TaskHandler(chatID));

        switch (message) {

            case INFO_COMMAND + BOT_NAME, INFO_COMMAND
                    -> bot.execute(
                            new SendMessage(chatID, BOT_INFO).enableHtml(true));

            case TASK_COMMAND + BOT_NAME, TASK_COMMAND
                    -> bot.execute(
                            new SendMessage(chatID, commonMap.get(chatID).showTask()).enableHtml(true));

            case LOCAL_RATING_COMMAND + BOT_NAME, LOCAL_RATING_COMMAND
                    -> bot.execute(
                            new SendMessage(chatID,new ScoreHandler(chatID).showLocalScores(update)).enableHtml(true));

            case OVERALL_RATING_COMMAND + BOT_NAME, OVERALL_RATING_COMMAND
                    -> bot.execute(
                            new SendMessage(chatID,new ScoreHandler().showOverallScores()).enableHtml(true));

            default -> {
                String guessed = commonMap.get(chatID).guess(update);
                if (guessed != null)
                    bot.execute(new SendMessage(chatID, guessed).enableHtml(true));
            }
        }
    }

    @Override
    public void run() {
        for (;;) {
            for (Object object = bot.receiveQueue.poll(); object != null; object = bot.receiveQueue.poll()) {
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
