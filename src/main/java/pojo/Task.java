package pojo;

import repository.TaskRepository;

import javax.persistence.*;

@Entity
public class Task {

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

    private int complexity = 1;

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

    public void incrementComplexity() {
        complexity = TaskRepository.complexityInc(this);
    }



    //TODO: setters for debug only. remove before release
    public void setID(int ID) {
        this.ID = ID;
    }

    public void setChatID(long chatID) {
        this.chatID = chatID;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setComplexity(int complexity) {
        this.complexity = complexity;
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

    public int getComplexity() {
        return complexity;
    }
}
