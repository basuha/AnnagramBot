package pojo;

import repository.TaskRepository;

import javax.persistence.*;

@Entity
@Table(name = "task", schema = "public")
public class Task {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy=GenerationType.SEQUENCE,
            generator = "task_id_Sequence")
    @SequenceGenerator(name = "task_id_Sequence",
            sequenceName = "task_sequence",
            allocationSize = 1)
    private int ID;

    @Column(name = "chat_id")
    private long chatID;

    @Column(name = "key")
    private String key;

    @Column(name = "value")
    private String value;

    @Column(name = "complexity")
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
