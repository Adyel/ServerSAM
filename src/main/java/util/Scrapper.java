package util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import org.hibernate.Session;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.pmw.tinylog.Logger;

public class Scrapper {

  public static void main(String[] args) {
    final String baseURL = "http://ftp2.circleftp.net/FILE--SERVER/English%20Movies/";

    for (int year = 1996; year <= 2018; year++) {
      String finalURL = baseURL + "/" + year;
      scrap(finalURL);
    }
  }

  private static void scrap(String url) {
    Document doc = null;

    for (int i = 0; i < 5; i++) {
      try {
        doc =
            Jsoup.connect(url)
                .userAgent(
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36")
                .timeout(15000)
                .get();
        break;
      } catch (IOException e) {
        Logger.warn("Retrying : " + (i + 1));
        try {
          String result = java.net.URLDecoder.decode(url, "UTF-8");
          Logger.info(result);
        } catch (UnsupportedEncodingException exception) {

          Logger.debug("Exception occurred: " + exception);
        }
      }
    }


    Elements names = doc.getElementsByTag("a");

    if (names != null) {

      ParseData parseData = new ParseData();

      for (Element name : names) {
        parseData.add(name.text());
      }

      Session session = HibernateConnMan.getSession();
      session.beginTransaction();
      parseData.getMovieDetailsList().forEach(session::save);
      session.getTransaction().commit();
    } else {
      Logger.warn("Scrapping Failed! Could not find any matching element");
    }
  }
}
