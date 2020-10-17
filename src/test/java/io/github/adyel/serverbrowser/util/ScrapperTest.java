package io.github.adyel.serverbrowser.util;

import com.google.common.base.Stopwatch;
import io.github.adyel.serverbrowser.jpa.model.Movie;
import io.github.adyel.serverbrowser.web.scraper.Scraper;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@SpringBootTest
public class ScrapperTest {

  @Test
  public void scrapWithParse() {
    Scraper.of("http://ftp2.circleftp.net/FILE--SERVER/ftp2/English%20Movies/").scrap().stream()
        .map(Parser::of)
        .map(Parser::parse)
        .map(Movie::getFilename)
        .forEach(System.out::println);
  }

  @Test
  public void obtainList() {
    Scraper.of("http://ftp2.circleftp.net/FILE--SERVER/English%20Movies/2000/")
        .scrap()
        .forEach(System.out::println);
  }

  @Test
  public void reactiveFetch() {
    List<String> urlList = new ArrayList<>();

    StringBuilder currentURL =
        new StringBuilder("http://ftp2.circleftp.net/FILE--SERVER/English%20Movies/XXXX");

    for (int i = 1996; i < 2020; i++) {
      currentURL.replace(currentURL.length() - 4, currentURL.length(), i + "");
      urlList.add(currentURL.toString());
    }
    //    urlList.forEach(System.out::println);

    Stopwatch stopwatch = Stopwatch.createStarted();
    Flux.fromIterable(urlList)
        .subscribeOn(Schedulers.elastic())
        .map(Scraper::of)
        .flatMapIterable(Scraper::scrap)
        .map(Parser::of)
        .map(Parser::parse)
        .count()
        .subscribe(System.out::println,null, () -> System.out.println(Thread.currentThread().getName()));
    stopwatch.stop();
    System.out.println("Elapsed:" + stopwatch.elapsed(TimeUnit.MILLISECONDS));

    for (int i = 0; i < 7; i++){
      try {
        Thread.sleep(500);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      System.out.println("Doing Work ..." + i + " on " + Thread.currentThread().getName());
    }
  }

  @Test
  public void nonReactive() {
    List<String> urlList = new ArrayList<>();

    StringBuilder currentURL =
        new StringBuilder("http://ftp2.circleftp.net/FILE--SERVER/English%20Movies/XXXX");

    for (int i = 1996; i < 2020; i++) {
      currentURL.replace(currentURL.length() - 4, currentURL.length(), i + "");
      urlList.add(currentURL.toString());
    }

    int count = 0;

    Stopwatch stopwatch = Stopwatch.createStarted();
    for (String url : urlList) {
      List<String> movies = Scraper.of(url).scrap();
      for (String movie : movies) {
//        System.out.println(Parser.of(movie).parse().getFilename());
        count++;
      }
    }
    stopwatch.stop();

    System.out.println("Total Parsed: " + count);
    System.out.println("Elapsed:" + stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }
}
