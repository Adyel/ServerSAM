import model.orm.MovieDetails;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class H2hibernate {
    public static void main(String[] args){

        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(MovieDetails.class)
                .buildSessionFactory();

        Session session = factory.getCurrentSession();

        MovieDetails movieDetails = new MovieDetails("New",2017);

        try {
            session.beginTransaction();
            session.save(movieDetails);
            session.getTransaction().commit();
        } finally {
            factory.close();
        }
    }
}
