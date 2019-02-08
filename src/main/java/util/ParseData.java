package util;

import model.orm.MovieDetails;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ParseData {

    private String name;

    private int year;

    private String quality;

    private boolean validData = false;

    public static Set<String> hash_check = new HashSet<>();
    ArrayList<MovieDetails> movieDetailsArrayList = new ArrayList<MovieDetails>();


    public ParseData(){};

    @Deprecated
    public ParseData(String _fullTitle) {
        if (_fullTitle.contains("(") && _fullTitle.contains(")")) {
            String[] splitted = _fullTitle.split("[\\(\\)]");
            name = splitted[0].trim();

            try {
                year = Integer.parseInt(splitted[1].trim());
            } catch (NumberFormatException e) {
                year = Integer.parseInt(splitted[3]);
            }
            try {
                quality = splitted[2].trim().split(" ")[0].replaceAll("[\\[\\]]", "");

            } catch (ArrayIndexOutOfBoundsException e) {
                quality = "";
            }
            validData = true;
        }
    }


    public void setFileName(String _fullTitle){
        if (_fullTitle.contains("(") && _fullTitle.contains(")")) {
            String[] splitted = _fullTitle.split("[\\(\\)]");
            name = splitted[0].trim();

            try {
                year = Integer.parseInt(splitted[1].trim());
            } catch (NumberFormatException e) {
                year = Integer.parseInt(splitted[3]);
            }

            try {
                quality = splitted[2].trim().split(" ")[0].replaceAll("[\\[\\]]", "");

            } catch (ArrayIndexOutOfBoundsException e) {
                quality = "";
            }
            validData = true;
        }
    }


    public void insertToList(){

        if (validData){
            if ( hash_check.add(name+year) ){
                movieDetailsArrayList.add( new MovieDetails(name, year) );
            }
        }
    }

    public ArrayList<MovieDetails> getMovieDetails(){
        return movieDetailsArrayList;
    }



    public String getName() {
        return name;
    }

    public int getYear() {
        return year;
    }

    public String getQuality() {
        return quality;
    }

    public boolean getDataValidity() {
        return validData;
    }
}
