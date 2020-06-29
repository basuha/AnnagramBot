package repository;

import pojo.Score;

public class ScoreRepository extends AbstractRepository {

    private static int incrementGuessCount(Score score) {
        openSession();
        int newGuessCount = score.getGuessCount() + 1;
        session.beginTransaction();
        session.createQuery("UPDATE Task" +
                " SET complexity = " + newGuessCount +
                " WHERE ID = " + score.getID())
                .executeUpdate();
        session.getTransaction().commit();
        closeSession();
        return newGuessCount;
    }
}
