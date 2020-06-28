package utilities;

import app.Bot;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import pojo.TaskHandler;
import repository.WordRepository;

import java.util.*;

public class AnagramHandler implements Runnable {
    private static final Logger log = Logger.getLogger(AnagramHandler.class);

    private Map<Long, TaskHandler> task = new LinkedHashMap<>();

    private static final String NEXT_LINE = "\n";
    private static final String MESSAGE = "Угадайте следующие слова: " + NEXT_LINE;
    private static final String OP_B_TAG = "<b>";
    private static final String CL_B_TAG = "</b>";
    private static final String NAME_TAG = "@";

    private static final String START_COMMAND = "/start";
    private static final String TASK_COMMAND = "/task";
    private static final String LOCAL_RATING_COMMAND = "/rating";
    private static final String OVERALL_RATING_COMMAND = "/overall";

    private String chatID;
    private String inputText;

    private Bot bot;

    public AnagramHandler(Bot bot) {
        System.out.println("вызов конструктора");
        this.bot = bot;
        refresh();
    }

    public void parseUpdate(Update update) throws TelegramApiException {
        switch (update.getMessage().getText()) {
            case START_COMMAND:
                System.out.println("start");
                break;
            case TASK_COMMAND:
                showTask(update.getMessage().getChatId());
                break;
            case LOCAL_RATING_COMMAND:
                System.out.println("rating command");
                break;
            case OVERALL_RATING_COMMAND:
                System.out.println("overall rating");
            default:
                String guessed = guess(update.getMessage().getText(), update.getMessage().getFrom());
                if (guessed != null)
                    bot.execute(new SendMessage(update.getMessage().getChatId(),guessed).enableHtml(true));
        }
    }

    private void showTask(long chatID) throws TelegramApiException {
        StringBuilder message = new StringBuilder(MESSAGE + OP_B_TAG);

        for (Map.Entry<Long, TaskHandler> m : task.entrySet())
            message.append(m.getValue())
                    .append(NEXT_LINE);
        message.append(CL_B_TAG);

        bot.execute(new SendMessage(chatID, message.toString()).enableHtml(true));
    }

    public void get() {
        log.info(task.size());
        String nextWord = WordRepository.get();
        task.put(nextWord, anagramize(nextWord));
        log.info(task);
    }

    public String guess(String message, User user) {
        for (String w : message.toLowerCase()
                .replaceAll("([?!:;,.])", "")
                .replaceAll("([\\-`_])", " ")
                .split(" ")) {
            if (task.containsKey(w)) {
                String guessed = task.remove(w);
                return OP_B_TAG + guessed + CL_B_TAG + " - угадано! " +
                        "Ответ: " + OP_B_TAG + w.toUpperCase() + CL_B_TAG + ". " + //TODO: words, which not guessed gradually changes color from white to red
                        "Победитель " + OP_B_TAG + NAME_TAG + user.getUserName() + CL_B_TAG; //TODO: if chat.getType() == private dont show name
            }
        }
        return null;
    }

    private void refresh() {
        for (int i = task.size(); i < 5; i++)
            get();
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
