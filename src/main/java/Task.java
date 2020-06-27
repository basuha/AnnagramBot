import javax.persistence.*;
import java.util.Map;

@Entity
@Table(name = "task_master")
public class Task {

    @Id
    private int taskID;

    private long chatID;

    @OneToMany(cascade = CascadeType.ALL)
    private Map<String,String> task;

    public Task(long chatID, String task) {
    }
}
