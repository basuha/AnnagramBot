import javax.annotation.processing.Generated;
import javax.persistence.*;
import java.util.Map;

@Entity
@Table(name = "task_master")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int taskID;

    private long chatID;

    private String taskKey;

    private String taskValue;

    public Task() {}

    public Task(long chatID, String taskKey, String taskValue) {
        this.chatID = chatID;
        this.taskKey = taskKey;
        this.taskValue = taskValue;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskID=" + taskID +
                ", chatID=" + chatID +
                ", taskKey='" + taskKey + '\'' +
                ", taskValue='" + taskValue + '\'' +
                '}';
    }

    public String getTaskKey() {
        return taskKey;
    }

    public String getTaskValue() {
        return taskValue;
    }
}
