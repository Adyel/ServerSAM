package io.github.adyel.serverbrowser.jpa.service;

import io.github.adyel.serverbrowser.jpa.model.Movie;
import io.github.adyel.serverbrowser.jpa.repository.MovieRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MovieService {

  private final MovieRepository movieRepository;

  public MovieService(MovieRepository movieRepository) {
    this.movieRepository = movieRepository;
  }

  public List<Movie> getAll(){
    return movieRepository.findAll();
  }

  public void save(Movie movie){
    movieRepository.save(movie);
  }

  public void saveAll(List<Movie> movieList){
    movieRepository.saveAll(movieList);
  }
}
