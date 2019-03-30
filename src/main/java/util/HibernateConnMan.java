package util;

import model.orm.Genre;
import model.orm.MovieDetails;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateConnMan {

  private static SessionFactory factory;

  static {
    factory =
        new Configuration()
            .configure("hibernate.cfg.xml")
            .addAnnotatedClass(MovieDetails.class)
            .addAnnotatedClass(Genre.class)
            .buildSessionFactory();
  }

  private HibernateConnMan() {}

  public static Session getSession() {
    return factory.getCurrentSession();
  }

  public static Session getSession(Session session) {

    if (session.isOpen() && session.getTransaction().isActive()) {
      return session;
    }

    session = getSession();

    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }

    return session;
  }

  public static void closeFactory() {
    getSession().close();
    factory.close();
  }
}
