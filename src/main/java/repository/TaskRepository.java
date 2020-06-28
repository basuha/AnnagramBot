package repository;

import pojo.Task;

import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

public class TaskRepository extends AbstractRepository {

    public static void add(Task task) {
        openSession();
        session.beginTransaction();
        session.save(task);
        session.getTransaction().commit();
        closeSession();
    }

    public static void delete(Task task) {
        openSession();
        Task task1 = session.get(Task.class,task.getID());
        session.beginTransaction();
        session.delete(task1);
        session.getTransaction().commit();
        closeSession();
    }

    public static List<Task> get(long chatID) {
        openSession();
        query = session.createQuery("FROM Task WHERE chatID = " + chatID);
        List<Task> tasks = new ArrayList<>();
        tasks.addAll(query.getResultList());
        closeSession();
        return tasks;
    }

    public static boolean contains(long chatID) {
        return !get(chatID).isEmpty();
    }
}
