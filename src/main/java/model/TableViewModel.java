package model;

public class TableViewModel {

    String title;
    Integer year;
    //double rating;

    public TableViewModel(String title, int year) {
        this.title = title;
        this.year = year;
        //this.rating = rating;
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

//    public double getRating() {
//        return rating;
//    }
//
//    public void setRating(double rating) {
//        this.rating = rating;
//    }
}
