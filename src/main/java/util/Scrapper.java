package util;

import model.orm.MovieDetails;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.pmw.tinylog.Logger;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class Scrapper {

    public static int counter = 0;

    public static void main(String[] args) {
        final String baseURL = "http://ftp2.circleftp.net/FILE--SERVER/English%20Movies/";


        for (int year = 1996; year <= 2018; year++) {
            String finalURL = baseURL + "/" + year;
            scrap(finalURL);
        }

    }


    private static void scrap(String URL) {
        Document doc = null;

        for (int i = 0; i < 5; i++) {
            try {
                doc = Jsoup.connect(URL)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36")
                        .timeout(15000)
                        .get();
                break;
            } catch (IOException e) {
                Logger.warn("Retrying : " + (i + 1));
                try {
                    String result = java.net.URLDecoder.decode(URL, "UTF-8");
                    System.out.println(result);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                continue;
            }
        }

        Elements names = doc.getElementsByTag("a");

        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(MovieDetails.class)
                .buildSessionFactory();


        Session session = factory.openSession();
        session.beginTransaction();


        for (Element name : names) {
//            Logger.info(name.text());

            String[] movieData = ParseData.detectCleanMovienameAndYear(name.text());

            System.out.println(movieData[0] + " " + movieData[1]);


            if (movieData != null && movieData.length == 2 && !movieData[1].isBlank()){
                session.save(new MovieDetails(movieData[0],Integer.parseInt(movieData[1])) );
            }
        }

        session.getTransaction().commit();
        factory.close();
    }
}
