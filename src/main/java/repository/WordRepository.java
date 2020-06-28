package repository;

import pojo.Task;
import pojo.Word;

import javax.persistence.Query;
import java.util.Random;

public class WordRepository extends AbstractRepository {

    private static int maxID() {
        query = session.createQuery("SELECT max(wordID) FROM Word");
        return (int) query.getSingleResult();
    }

    public static void add(String word) {
        openSession();
        session.beginTransaction();
        session.save(new Word(word));
        session.getTransaction().commit();
        closeSession();
    }

    public static String get() {
        openSession();
        query = session.createQuery("FROM Word WHERE wordID = " + new Random().nextInt(maxID()));
        Word word = (Word) query.getSingleResult();
        closeSession();
        return word.toString();
    }
}
