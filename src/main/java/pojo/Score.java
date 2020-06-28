package pojo;

import javax.persistence.*;

@Entity
@Table(name = "Score")
public class Score {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int entryID;

    private long chatID;

    private String userName;

    private long userID;

    private float score;

    private int guessCount;

    public Score() {}

    public Score(String userName, long userID, float score) {
        this.userName = userName;
        this.userID = userID;
        this.score = score;
    }

}
