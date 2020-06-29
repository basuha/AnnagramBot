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

    //TODO: setters are debug purposes only
    public void setID(int ID) {
        this.ID = ID;
    }

    public void setChatID(long chatID) {
        this.chatID = chatID;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public void setGuessCount(int guessCount) {
        this.guessCount = guessCount;
    }



    public int getID() {
        return ID;
    }

    public long getChatID() {
        return chatID;
    }

    public String getUserName() {
        return userName;
    }

    public long getUserID() {
        return userID;
    }

    public float getScore() {
        return score;
    }

    public int getGuessCount() {
        return guessCount;
    }
}
