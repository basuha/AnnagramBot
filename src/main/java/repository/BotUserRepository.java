package repository;

import pojo.BotUser;
import java.util.ArrayList;
import java.util.List;

public class BotUserRepository extends AbstractRepository {

    public static void add(BotUser botUser) {
        openSession();
        session.beginTransaction();
        session.save(botUser);
        session.getTransaction().commit();
        closeSession();
    }

    public static List<BotUser> getByID(int userID) {
        openSession();
        query = session.createQuery("FROM BotUser WHERE userID = " + userID);
        List<BotUser> botUsers = new ArrayList<>(query.getResultList());
        closeSession();
        return botUsers;
    }

    public static List<BotUser> getByChatID(long chatID) {
        openSession();
        query = session.createQuery("FROM BotUser WHERE chatID = " + chatID);
        List<BotUser> botUsers = new ArrayList<>(query.getResultList());
        closeSession();
        return botUsers;
    }

    public static List<BotUser> getAll() {
        openSession();
        query = session.createQuery("FROM BotUser");
        List<BotUser> botUsers = new ArrayList<>(query.getResultList());
        closeSession();
        return botUsers;
    }

    public static long getSumScoresOfUser(int userID) {
        openSession();
        query = session.createQuery("SELECT sum(score) FROM BotUser " +
                " WHERE userID = " + userID);
        long sum = (long) query.getSingleResult();
        closeSession();
        return sum;
    }

    public static long getSumGuessedWordsOfUser(int userID) {
        openSession();
        query = session.createQuery("SELECT sum(guessCount) FROM BotUser " +
                " WHERE userID = " + userID);
        long sum = (long) query.getSingleResult();
        closeSession();
        return sum;
    }

    public static boolean isIntroduced(int userID, long chatID) {
        return !getByID(userID).isEmpty() && !getByChatID(chatID).isEmpty();
    }

    public static BotUser getBotUser(int userID, long chatID) {
        openSession();
        query = session.createQuery("FROM BotUser" +
                " WHERE userID = " + userID +
                " AND chatID = " + chatID);
        List<BotUser> botUsers = new ArrayList<>();
        botUsers.addAll(query.getResultList());
        closeSession();
        return botUsers.get(0);
    }

    public static int incrementGuessCount(BotUser botUser) {
        openSession();
        int newGuessCount = botUser.getGuessCount() + 1;
        session.beginTransaction();
        session.createQuery("UPDATE BotUser" +
                " SET guessCount = " + newGuessCount +
                " WHERE userID = " + botUser.getUserID() +
                " AND chatID = " + botUser.getChatID())
                .executeUpdate();
        session.getTransaction().commit();
        closeSession();
        return newGuessCount;
    }

    public static int incrementScore(BotUser botUser, int score) {
        openSession();
        int incrementedScore = botUser.getScore() + score;
        session.beginTransaction();
        session.createQuery("UPDATE BotUser" +
                " SET score = " + incrementedScore +
                " WHERE userID = " + botUser.getUserID() +
                " AND chatID = " + botUser.getChatID())
                .executeUpdate();
        session.getTransaction().commit();
        closeSession();
        return incrementedScore;
    }
}
