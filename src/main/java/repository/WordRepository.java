package repository;

import pojo.Word;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

public class WordRepository extends AbstractRepository {

    private static int maxID() {
        query = session.createQuery("SELECT max(ID) FROM Word");
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
        query = session.createQuery("FROM Word WHERE ID = " + new Random().nextInt(maxID()));
        Word word = (Word) query.getSingleResult();
        closeSession();
        return word.toString();
    }

    public static void fillBase() {
        try {
            BufferedReader reader = Files.newBufferedReader(Paths.get("rus_comm_noun.txt"));
            String temp;
            do {
                temp = reader.readLine();
                if (temp == null) break;
                add(temp);
            } while (true);
        } catch (IOException e) {
            System.out.println("файл не найден");
        }
    }
}
