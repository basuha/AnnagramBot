package pojo;

import repository.BotUserRepository;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "user")
public class BotUser {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE,
            generator = "user_id_Sequence")
    @SequenceGenerator(name = "user_id_Sequence",
            sequenceName = "user_sequence",
            initialValue = 0,
            allocationSize = 1)
    private int ID;

    private int userID;

    private String userName;

    private int score;

    private int guessCount;

    private long chatID;

    public BotUser() {}

    public BotUser(int userID, String userName, long chatID) {
        this.userID = userID;
        this.userName = userName;
        this.chatID = chatID;
    }

    public void incrementScore(int score) {
        this.guessCount = BotUserRepository.incrementGuessCount(this);
        this.score = BotUserRepository.incrementScore(this,score);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BotUser botUser = (BotUser) o;

        return userID == botUser.userID;
    }

    @Override
    public int hashCode() {
        return userID;
    }

    public int getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }

    public int getScore() {
        return score;
    }

    public int getGuessCount() {
        return guessCount;
    }

    public long getChatID() {
        return chatID;
    }
}
