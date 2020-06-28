package pojo;

import javax.persistence.*;

@Entity
@Table(name = "Score")
public class Score {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE,
            generator = "score_id_Sequence")
    @SequenceGenerator(name = "score_id_Sequence",
            sequenceName = "score_sequence",
            initialValue = 0,
            allocationSize = 1)
    private int ID;

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
