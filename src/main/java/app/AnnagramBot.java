package app;

import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import repository.TaskRepository;
import utilities.TaskHandler;

import java.util.*;

public class AnnagramBot implements Runnable {
    private static final Logger log = Logger.getLogger(AnnagramBot.class);

    private Bot bot;

    private static final String INFO_COMMAND = "/info";
    private static final String TASK_COMMAND = "/task";
    private static final String LOCAL_RATING_COMMAND = "/rating";
    private static final String OVERALL_RATING_COMMAND = "/overall";
    private static final String HINT_COMMAND = "/hint";

    private static final String BOT_NAME = "@AnnagramBot";


    private Map<Long, TaskHandler> commonMap = new LinkedHashMap<>();

    private static final String BOT_INFO = "<b>Привет!</b>\n\nЯ бот-генератор анаграм. " +
            "Я использую базу частоупотребимых существительных русского языка из около 1700 слов. \n\n" +
            "За каждое угаданное слово я начисляю пользователю определенное количество очков. " +
            "Чем дольше слово остается в списке, тем сложнее оно считается, " +
            "и тем больше можно получить очков за его отгадывание. \n\n" +
            "Если слово очень сложное можно воспользоваться подсказкой. " +
            "Для этого есть команда <code>/hint</code> <i>номер слова</>. " +
            "Она показывает первые три буквы слова, и обнуляет счетчик сложности (до 1). \n\n" +
            "Для просмотра всех комманд введите символ <code>/</code>\n\n\n" +
            "<code>Написано на Java, хостинг - Heroku</code>";


    public AnnagramBot(Bot bot) {
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
            case INFO_COMMAND + BOT_NAME, INFO_COMMAND -> bot.execute(new SendMessage(chatID, BOT_INFO).enableHtml(true));
            case TASK_COMMAND + BOT_NAME, TASK_COMMAND -> bot.execute(new SendMessage(chatID, commonMap.get(chatID).showTask()).enableHtml(true));
            case LOCAL_RATING_COMMAND + BOT_NAME, LOCAL_RATING_COMMAND -> System.out.println("rating command");
            case OVERALL_RATING_COMMAND + BOT_NAME, OVERALL_RATING_COMMAND -> System.out.println("overall rating");
            case HINT_COMMAND + BOT_NAME, HINT_COMMAND -> System.out.println("hint");
        }

        String guessed = commonMap.get(chatID).guess(update);
        if (guessed != null)
            bot.execute(new SendMessage(chatID, guessed).enableHtml(true));
    }

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
