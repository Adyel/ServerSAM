package model.fx;

import model.orm.MovieDetails;

public class TableViewModel {

    private String title;
    private Integer year;
    private double rating;
    private boolean subtitleStatus;
    private String director;

    public TableViewModel(MovieDetails movieDetails) {
        this.title = movieDetails.getFileName();
        this.year = movieDetails.getYear();
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
}
