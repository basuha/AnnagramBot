package repository;

import pojo.Task;
import pojo.Word;

import javax.persistence.Query;
import java.util.Random;

public class WordRepository extends AbstractRepository {

    private static int maxID() {
        openSession();
        query = session.createQuery("SELECT max(wordID) FROM Word");
        int maxID = (int) query.getSingleResult();
        closeSession();
        return maxID;
    }

    public static void add(Word word) {
        openSession();
        session.beginTransaction();
        session.save(word);
        session.getTransaction().commit();
        closeSession();
    }

    public static Word get() {
        openSession();
        query = session.createQuery("FROM Word WHERE wordID = " + new Random().nextInt(maxID()));
        Word word = (Word) query.getSingleResult();
        closeSession();
        return null;
    }
}
