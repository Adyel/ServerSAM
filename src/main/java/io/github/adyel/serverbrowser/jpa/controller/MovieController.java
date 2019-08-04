package io.github.adyel.serverbrowser.jpa.controller;

import io.github.adyel.serverbrowser.jpa.model.Movie;
import io.github.adyel.serverbrowser.jpa.service.MovieService;
import io.github.adyel.serverbrowser.ui.viewmodel.TableViewModel;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.stereotype.Controller;

@Controller
public class MovieController {

  private final MovieService movieService;

  public MovieController(MovieService movieService) {
    this.movieService = movieService;
  }

  public void sampleData() {
    List<Movie> movies =
        Arrays.asList(
            new Movie("Up", 2009),
            new Movie("Alice in the wonderland", 2012),
            new Movie("Mad Max", 2014),
            new Movie("3 Idiots!!!", 2008));

    movieService.saveAll(movies);

    List<Movie> movieList = movieService.getAll();
    movieList.forEach(System.out::println);
  }


  public void printAll(){
    movieService.getAll().forEach(System.out::println);
  }

  public void save(Movie movie){
    movieService.save(movie);
  }

  public ObservableList<TableViewModel> getAllMovieInTableViewModel() {
    return movieService.getAll().stream()
        .map(MovieController::movieToTableView)
        .collect(Collectors.toCollection(FXCollections::observableArrayList));
  }

  private static TableViewModel movieToTableView(Movie movie) {
    return new TableViewModel(movie.getFilename(), movie.getYear());
  }
}
