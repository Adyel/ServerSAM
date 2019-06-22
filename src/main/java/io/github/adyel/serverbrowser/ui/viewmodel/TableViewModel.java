package io.github.adyel.serverbrowser.ui.viewmodel;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TableViewModel {
    private int tmdbId;
    private String title;
    private Integer year;
    private double rating;
    private boolean subtitleStatus;
    private String director;

    public TableViewModel(String title, int year){
        this.title = title;
        this.year = year;
    }
}
