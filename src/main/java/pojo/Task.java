package pojo;

import javax.persistence.*;

@Entity
public class Task{

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE,
            generator = "task_id_Sequence")
    @SequenceGenerator(name = "task_id_Sequence",
            sequenceName = "task_sequence",
            initialValue = 0,
            allocationSize = 1)
    private int ID;

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
        return "pojo.Task{" +
                "taskID=" + ID +
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
