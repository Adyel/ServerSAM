package model.orm;

import javax.persistence.*;

@Entity
@Table(name = "movie_list")
public class MovieDetails {

    public MovieDetails(){};


    public MovieDetails(String fileName, int year) {
//        this.tmdbId = tmdbId;
        this.fileName = fileName;
        this.year = year;
    }


    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
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

    @Column(name = "overview")
    private String overView;

    @Column(name = "rating")
    private Double voteAverage;

    @Column(name = "popularity")
    private Double popularity;

    @Column(name = "language")
    private String originalLanguage;

    @Column(name = "release_date")
    private String releaseDate;

}
