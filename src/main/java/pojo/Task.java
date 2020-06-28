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

    private String key;

    private String value;

    public Task() {}

    public Task(long chatID, String key, String value) {
        this.chatID = chatID;
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        return "pojo.Task{" +
                "taskID=" + ID +
                ", chatID=" + chatID +
                ", taskKey='" + key + '\'' +
                ", taskValue='" + value + '\'' +
                '}';
    }

    public int getID() {
        return ID;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
