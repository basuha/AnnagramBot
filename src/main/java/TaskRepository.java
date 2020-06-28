import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.Query;
import javax.transaction.Transactional;

public class TaskRepository {
    private static SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
    private static Session session;
    private static Query query;

    public static void add(Task task) {
        session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(task);
        session.getTransaction().commit();
        session.close();
    }

    public static Task getByChatID(long chatID) {
        query = session.createQuery("SELECT Task WHERE chatID = " + chatID);
        return (Task) query.getSingleResult();
    }

    public static void delete(Task task) {
        session.close();
    }
}
