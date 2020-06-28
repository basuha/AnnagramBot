package utilities;

import com.google.common.base.Joiner;
import org.apache.commons.lang3.StringUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import pojo.Task;
import repository.TaskRepository;
import repository.WordRepository;

import javax.persistence.*;
import java.util.*;

public class TaskHandler {

    private static final int TASK_LIMIT = 5;

    private static final String NEXT_LINE = "\n";
    private static final String MESSAGE = "Угадайте следующие слова: " + NEXT_LINE;
    private static final String OP_B_TAG = "<b>";
    private static final String CL_B_TAG = "</b>";
    private static final String NAME_TAG = "@";

    private List<Task> tasks = new LinkedList<>();

    protected long chatID;

    public TaskHandler(long chatID) {
        this.chatID = chatID;
        refreshTaskInChat();
    }

    public List<Task> getTasks() {
        if (tasks.size() < TASK_LIMIT)
            refreshTaskInChat();
        return tasks;
    }

    public String showTask() {
        StringBuilder message = new StringBuilder(MESSAGE + OP_B_TAG);

        for (Task t : getTasks())
            message.append(t.getValue())
                    .append(NEXT_LINE);
        message.append(CL_B_TAG);

        return message.toString();
    }

    public String guess(String message, String userName) {
        for (String w : message.toLowerCase()
                .replaceAll("([?!:;,.])", "")
                .replaceAll("([\\-`_])", " ")
                .split(" ")) {
            for (Task t : tasks) {
                if (t.getKey().equals(w)) {
                    String guessed = remove(t);
                    return OP_B_TAG + guessed + CL_B_TAG + " - угадано! " +
                            "Ответ: " + OP_B_TAG + w.toUpperCase() + CL_B_TAG + ". " + //TODO: words, which not guessed gradually changes color from white to red
                            "Победитель " + OP_B_TAG + NAME_TAG + userName + CL_B_TAG; //TODO: if chat.getType() == private dont show name
                }
            }
        }
        return null;
    }

    private String remove(Task task) {
        tasks.remove(task);
        TaskRepository.delete(task);
        refreshTaskInChat();
        return task.getValue();
    }

    private void refreshTaskInChat() {
        System.out.println("hi");
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
