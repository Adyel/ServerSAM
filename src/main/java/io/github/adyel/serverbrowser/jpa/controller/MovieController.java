package io.github.adyel.serverbrowser.jpa.controller;

import io.github.adyel.serverbrowser.jpa.model.Movie;
import io.github.adyel.serverbrowser.jpa.service.MovieService;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Controller;

@Controller
public class MovieController {

  private final MovieService movieService;

  public MovieController(MovieService movieService) {
    this.movieService = movieService;
  }

  public void sampleTest() {
    List<Movie> movies =
        Arrays.asList(
            new Movie("Up", 2009),
            new Movie("Alice in the wonderland", 2012),
            new Movie("Mad Max", 2014));

    movieService.saveAll(movies);

    List<Movie> movieList  = movieService.getAll();
    movieList.forEach(System.out::println);
  }
}
