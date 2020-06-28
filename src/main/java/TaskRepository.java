import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

public class TaskRepository {
    private static SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
    private static Session session;
    private static Query query;

    private static void openSession() {
        session = sessionFactory.openSession();
    }

    private static void closeSession() {
        if (session != null) session.close();
    }

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
