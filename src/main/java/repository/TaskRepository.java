package repository;

import pojo.Task;

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
        session.beginTransaction();
        session.delete(task);
        session.getTransaction().commit();
        closeSession();
    }

    public static List<Task> getByChatID(long chatID) {
        openSession();
        query = session.createQuery("FROM Task WHERE chatID = " + chatID);
        List<Task> tasks = query.getResultList();
        closeSession();
        return tasks;
    }
}
