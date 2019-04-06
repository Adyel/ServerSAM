package model.fx;

import model.orm.MovieDetails;

public class TableViewModel {

  private int id;
  private String title;
  private Integer year;
  private double rating;
  private boolean subtitleStatus;
  private String director;

  public TableViewModel(MovieDetails movieDetails) {

    id = movieDetails.getMovieID();
    title = movieDetails.getFileName();
    year = movieDetails.getYear();
    rating = movieDetails.getVoteAverage();
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Integer getYear() {
    return year;
  }

  public void setYear(Integer year) {
    this.year = year;
  }

  public double getRating() {
    return rating;
  }

  public void setRating(double rating) {
    this.rating = rating;
  }

  public boolean isSubtitleStatus() {
    return subtitleStatus;
  }

  public void setSubtitleStatus(boolean subtitleStatus) {
    this.subtitleStatus = subtitleStatus;
  }

  public String getDirector() {
    return director;
  }

  public void setDirector(String director) {
    this.director = director;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }
}
