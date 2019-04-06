package model.orm;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "genre")
public class Genre {

  public Genre() {}

  public Genre(int genreID) {
    this.genreID = genreID;
  }

  public Genre(com.uwetrottmann.tmdb2.entities.Genre genre) {
    this.genreID = genre.id;
    this.name = genre.name;
  }

  @Id
  @Column(name = "genre_id")
  private int genreID;

  @Column(name = "genre_name")
  private String name;

  @ManyToMany(
      fetch = FetchType.LAZY,
      cascade = {
        CascadeType.PERSIST, CascadeType.MERGE,
        CascadeType.DETACH, CascadeType.REFRESH
      })
  @JoinTable(
      name = "movie_genre",
      joinColumns = @JoinColumn(name = "genre_id"),
      inverseJoinColumns = @JoinColumn(name = "movie_id"))
  private List<MovieDetails> movies;

  // INFO: Getters & Setters

  public int getGenreID() {
    return genreID;
  }

  public void setGenreID(int genreID) {
    this.genreID = genreID;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<MovieDetails> getMovies() {
    return movies;
  }

  public void setMovies(List<MovieDetails> movies) {
    this.movies = movies;
  }
}
