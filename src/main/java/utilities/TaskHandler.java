package utilities;

import app.App;
import com.google.common.base.Joiner;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.objects.Update;
import pojo.BotUser;
import pojo.Task;
import repository.BotUserRepository;
import repository.TaskRepository;
import repository.WordRepository;

import java.util.*;

public class TaskHandler extends AbstractHandler {

    private static final Logger log = Logger.getLogger(App.class);

    private static final int TASK_LIMIT = 10;

    private static final String PARSE_REGEX = "([?!:;,.)(])";
    private static final String SPLIT_REGEX = "([\\-`_])";

    private static final String MESSAGE = "Угадайте следующие слова: " + NEXT_LINE;

    private List<Task> tasks = new LinkedList<>();

    public TaskHandler(long chatID) {
        super(chatID);
        refreshTaskForChat();
    }

    public String showTask() {

        StringBuilder message = new StringBuilder(MESSAGE + OP_B_TAG);

        refreshTaskForChat();

        tasks.sort((o1, o2) -> Integer.compare(o2.getComplexity(), o1.getComplexity()));

        for (Task t : tasks)
            message.append(OP_CODE_TAG)
                    .append("(+")
                    .append(t.getComplexity())
                    .append(") ")
                    .append(CL_CODE_TAG)
                    .append(t.getValue())
                    .append(" ")
                    .append(NEXT_LINE);
        message.append(CL_B_TAG);

        return message.toString();
    }

    public String guess(Update update) {

        initUpdate(update);

        BotUser botUser = BotUserRepository.getBotUser(userID,chatID);

        StringBuilder outputMessage = new StringBuilder();
        for (String w : message.toLowerCase()
                .replaceAll(PARSE_REGEX, "")
                .replaceAll(SPLIT_REGEX, " ")
                .split(" ")) {
            for (Task t : tasks) {
                if (t.getKey().equals(w)) {
                    String guessed = t.getValue();

                    log.info("Guessed! " + guessed
                            + "+" + t.getComplexity()
                            + " points" + "| by "
                            + userName + "[" + userID + "]");

                    remove(t);
                    incrementAllTasks();
                    refreshTaskForChat();

                    outputMessage.append(OP_B_TAG).append(guessed).append(CL_B_TAG).append(" - угадано! ")
                            .append("Ответ: ").append(OP_B_TAG).append(w.toUpperCase()).append(CL_B_TAG).append(". ")
                            .append(NEXT_LINE);

                    if (isGroupChat)
                        outputMessage.append("Победитель ")
                                .append(OP_B_TAG)
                                .append(NAME_TAG)
                                .append(userName)
                                .append(CL_B_TAG)
                                .append(NEXT_LINE);

                    outputMessage.append(OP_CODE_TAG).append("+").append(t.getComplexity()).append(CL_CODE_TAG)
                            .append(NEXT_LINE)
                            .append(NEXT_LINE)
                            .append(showTask());

                    botUser.incrementScore(t.getComplexity());

                    return outputMessage.toString();
                }
            }
        }
        return null;
    }

    private void incrementAllTasks() {
        for (var t : tasks)
            t.incrementComplexity();
    }

    private void remove(Task task) {
        tasks.remove(task);
        TaskRepository.delete(task);
    }

    private void refreshTaskForChat() {
        if (tasks.size() < TASK_LIMIT) {
            List<Task> currentTasks = TaskRepository.get(chatID);
            for (int i = currentTasks.size(); i < TASK_LIMIT; i++) {
                String word = WordRepository.get();
                TaskRepository.add(new Task(
                        chatID,
                        word,
                        anagramize(word)));
            }
            tasks.clear();
            tasks.addAll(currentTasks);
        }
    }

    private String anagramize(String word) {
        List<Character> anagram = new ArrayList<>();
        String request;

        for (int i = 0; i < word.length(); i++)
            anagram.add(null);

        do {
            for (Character c : word.toLowerCase().toCharArray()) {
                int index;
                do {
                    index = new Random().nextInt(word.length());
                } while (anagram.get(index) != null);
                anagram.set(index, c);
            }
            request = Joiner.on("").join(anagram);
        } while (request.equals(word));

        return StringUtils.capitalize(request);
    }

}
