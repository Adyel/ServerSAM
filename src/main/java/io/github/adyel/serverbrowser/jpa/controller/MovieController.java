package io.github.adyel.serverbrowser.jpa.controller;

import io.github.adyel.serverbrowser.jpa.model.Movie;
import io.github.adyel.serverbrowser.jpa.service.MovieService;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MovieController {

  @Autowired
  MovieService movieService;

  public void sampleTest() {
    List<Movie> movies =
        Arrays.asList(
            new Movie("Up", 2009),
            new Movie("Alice in the wonderland", 2012),
            new Movie("Mad Max", 2014));

    movieService.saveAll(movies);
  }
}
