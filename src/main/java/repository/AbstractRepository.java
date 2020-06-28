package repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.Query;

public abstract class AbstractRepository {

    protected static SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
    protected static Session session;
    protected static Query query;

    protected static void openSession() {
        session = sessionFactory.openSession();
    }

    protected static void closeSession() {
        if (session != null) session.close();
    }
}
