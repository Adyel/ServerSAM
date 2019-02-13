package model.orm;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "genre")
public class Genre {


    public Genre(){}

    public Genre(com.uwetrottmann.tmdb2.entities.Genre genre){
        this.genreID = genre.id;
        this.name = genre.name;
    }

    @Id
    @Column(name = "genre_id")
    private int genreID;

    @Column(name = "genre_name")
    private String name;
}