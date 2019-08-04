package io.github.adyel.serverbrowser.jpa.model;

import io.github.adyel.serverbrowser.jpa.model.Movie.MovieId;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Lob;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "movie_details")
@IdClass(MovieId.class)
public class Movie {

//  @Id
//  @Column(name = "movie_id")
//  @GeneratedValue
//  private Long movieId;

  @Column(name = "tmdb_id")
  private int tmdbId;

  @Id
  @Column(name = "file_name")
  private String filename;

  @Column(name = "file_path")
  private String filepath;

  @Id
  @Column(name = "year", columnDefinition = "SMALLINT")
  private short year;

  @Column(name = "movie_original_title")
  private String originalTitle;

  @Column(name = "movie_lang")
  private String language;

  @Lob
  @Column(name = "movie_overview")
  private String overview;

  @Column(name = "movie_popularity")
  private Double popularity;

  @Column(name = "movie_poster")
  private String posterPath;

  @Column(name = "movie_releaseDate")
  private Date releaseDate;

  @Column(name = "movie_title")
  private String title;

  @Column(name = "movie_rating")
  private Double voteAverage;

//  @Column(name = "movie_genre")
//  private List<Genre> genres;

  @Column(name = "movie_media_type")
  private String mediaType;

  public Movie(String filename, int year){
    this.filename = filename;
    this.year = (short) year;
  }

  @Data
  static class MovieId implements Serializable{
    private String filename;
    private short year;
  }
}
