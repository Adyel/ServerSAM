package io.github.adyel.serverbrowser.web.scraper;

import com.google.common.base.Charsets;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import lombok.Cleanup;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Scraper {

  private String baseURL;

  private Scraper(String url){
    this.baseURL = url;
  };

  public static Scraper of(String url){
    return new Scraper(url);
  }

  public List<String> scrap(){

    Elements movieElements = null;

    try {
      movieElements = Jsoup.connect(baseURL)
          .timeout(2000)
          .userAgent("Mozilla/5.0 (X11; Linux x86_64; rv:68.0) Gecko/20100101 Firefox/68.0")
          .get()
          .getElementsByTag("a");
    } catch (IOException e) {
      e.printStackTrace();
    }

    if (movieElements != null){
      return movieElements
          .stream()
          .skip(3)
          .map(Element::text)
          .collect(Collectors.toList());
    }

    return null;
  }




}