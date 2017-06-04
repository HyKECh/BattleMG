import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateFactory {
    private static SessionFactory ourSessionFactory = null;

    static {
        try {
            Configuration configuration = new Configuration();
            configuration.configure();
            ourSessionFactory = configuration.buildSessionFactory();
        } catch (Throwable ex) {
            if (ourSessionFactory != null) ourSessionFactory.close();
            throw new ExceptionInInitializerError(ex);

        }
    }

    static Session getSession() throws HibernateException {
        return ourSessionFactory.openSession();
    }

    static void close() {
        ourSessionFactory.close();
    }
}