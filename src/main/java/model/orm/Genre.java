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
import lombok.Data;

@Data
@Entity
@Table(name = "genre")
public class Genre {

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

  public Genre() {}

  public Genre(int genreID) {
    this.genreID = genreID;
  }

  public Genre(com.uwetrottmann.tmdb2.entities.Genre genre) {
    this.genreID = genre.id;
    this.name = genre.name;
  }
}
