package pojo;

import com.google.common.base.Joiner;
import org.apache.commons.lang3.StringUtils;
import repository.TaskRepository;
import repository.WordRepository;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class TaskHandler {

    private static final int TASK_LIMIT = 5;

    private List<Task> tasks = new LinkedList<>();

    protected long chatID;

    public TaskHandler(long chatID) {
        this.chatID = chatID;
    }

    public void refreshTaskInChat() {
        for (int i = TaskRepository.get(chatID).size(); i < TASK_LIMIT; i++) {
            String word = WordRepository.get();
            TaskRepository.add(new Task(
                    chatID,
                    word,
                    anagramize(word)));
        }
        tasks.addAll(TaskRepository.get(chatID));
    }

    public List<Task> getTasks() {
        if (tasks.size() < TASK_LIMIT)
            refreshTaskInChat();
        return tasks;
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
