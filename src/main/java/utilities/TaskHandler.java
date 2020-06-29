package utilities;

import com.google.common.base.Joiner;
import org.apache.commons.lang3.StringUtils;
import org.telegram.telegrambots.meta.api.objects.Update;
import pojo.Task;
import repository.TaskRepository;
import repository.WordRepository;

import java.util.*;

public class TaskHandler {

    private static final int TASK_LIMIT = 10;

    private static final String NEXT_LINE = "\n";
    private static final String MESSAGE = "Угадайте следующие слова: " + NEXT_LINE;
    private static final String OP_B_TAG = "<b>";
    private static final String CL_B_TAG = "</b>";
    private static final String OP_CODE_TAG = "<code>";
    private static final String CL_CODE_TAG = "</code>";
    private static final String NAME_TAG = "@";

    private List<Task> tasks = new LinkedList<>();

    protected long chatID;

    public TaskHandler(long chatID) {
        this.chatID = chatID;
        refreshTaskForChat();
    }

    public List<Task> getTasks() {
        if (tasks.size() < TASK_LIMIT)
            refreshTaskForChat();
        return tasks;
    }

    public String showTask() {
        StringBuilder message = new StringBuilder(MESSAGE + OP_B_TAG);

        for (Task t : getTasks())
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
        String message = update.getMessage().getText();
        String userName = update.getMessage().getFrom().getUserName();
        boolean isGroupChat = update.getMessage().getChat().isGroupChat();

        StringBuilder outputMessage = new StringBuilder();
        for (String w : message.toLowerCase()
                .replaceAll("([?!:;,.])", "")
                .replaceAll("([\\-`_])", " ")
                .split(" ")) {
            for (Task t : tasks) {
                if (t.getKey().equals(w)) {
                    String guessed = t.getValue();
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
                            .append(NEXT_LINE) //TODO: add overall points
                            .append(NEXT_LINE)
                            .append(showTask());

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
        for (int i = TaskRepository.get(chatID).size(); i < TASK_LIMIT; i++) { //TODO: new word should add on the place of deleted word
            String word = WordRepository.get();
            TaskRepository.add(new Task(
                    chatID,
                    word,
                    anagramize(word)));
        }
        tasks.clear();
        tasks.addAll(TaskRepository.get(chatID));
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
