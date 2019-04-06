package model.orm;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "movie_list")
public class MovieDetails {

  public MovieDetails() {}

  public MovieDetails(String fileName, int year) {
    this.fileName = fileName;
    this.year = year;
  }

  @Id
  @Column(name = "movie_id")
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int movieID;

  @Column(name = "tmdb_id")
  private int tmdbId;

  @Column(name = "file_name")
  private String fileName;

  @Column(name = "year")
  private int year;

  @Column(name = "qulaity")
  private String quality;

  @Column(name = "title")
  private String title;

  @Lob
  @Column(name = "overview")
  private String overview;

  @Column(name = "rating", columnDefinition = "Decimal(4,2) default '0.0'")
  private Double voteAverage = 0.0;

  @Column(name = "popularity")
  private Double popularity;

  @Column(name = "language")
  private String originalLanguage;

  @Column(name = "poster_path")
  private String posterPath;

  @Column(name = "release_date")
  @Temporal(TemporalType.DATE)
  private Date releaseDate;

  @ManyToMany(
      fetch = FetchType.EAGER,
      cascade = {
        CascadeType.PERSIST, CascadeType.MERGE,
        CascadeType.DETACH, CascadeType.REFRESH
      })
  @JoinTable(
      name = "movie_genre",
      joinColumns = @JoinColumn(name = "movie_id"),
      inverseJoinColumns = @JoinColumn(name = "genre_id"))
  private List<Genre> genres;

  // INFO: Make life easier

  public void addGenre(Genre genre) {
    if (genres == null) {
      genres = new ArrayList<>();
    }
    genres.add(genre);
  }

  // INFO: Getter & Setter

  public int getTmdbId() {
    return tmdbId;
  }

  public void setTmdbId(int tmdbId) {
    this.tmdbId = tmdbId;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public int getYear() {
    return year;
  }

  public void setYear(int year) {
    this.year = year;
  }

  public String getQuality() {
    return quality;
  }

  public void setQuality(String quality) {
    this.quality = quality;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Double getVoteAverage() {
    return voteAverage;
  }

  public void setVoteAverage(Double voteAverage) {
    this.voteAverage = voteAverage;
  }

  public Double getPopularity() {
    return popularity;
  }

  public void setPopularity(Double popularity) {
    this.popularity = popularity;
  }

  public String getOriginalLanguage() {
    return originalLanguage;
  }

  public void setOriginalLanguage(String originalLanguage) {
    this.originalLanguage = originalLanguage;
  }

  public Date getReleaseDate() {
    return releaseDate;
  }

  public void setReleaseDate(Date releaseDate) {
    this.releaseDate = releaseDate;
  }

  public int getMovieID() {
    return movieID;
  }

  public void setMovieID(int movieID) {
    this.movieID = movieID;
  }

  public String getOverview() {
    return overview;
  }

  public void setOverview(String overview) {
    this.overview = overview;
  }

  public String getPosterPath() {
    return posterPath;
  }

  public void setPosterPath(String posterPath) {
    this.posterPath = posterPath;
  }

  public List<Genre> getGenres() {
    return genres;
  }

  public void setGenres(List<Genre> genres) {
    this.genres = genres;
  }
}
