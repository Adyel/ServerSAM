package util;

import com.google.gson.Gson;
import model.Movie;
import model.Result;
import org.pmw.tinylog.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class TMDBconnect {

    private static String baseUrl = "https://api.themoviedb.org/3/search/movie?api_key=";

    public TMDBconnect(String apiKey) {
        baseUrl = baseUrl + apiKey;
    }

    public Movie findMovie(String name, int year) {
        Gson gson = new Gson();
        String finalUrl = baseUrl + "&query=" + name + "&year=" + year;
        Movie movie = null;

        Logger.info(finalUrl);
        try {
            URL url = new URL(finalUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            BufferedReader jsonFile = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            //String testString = jsonFile.readLine();

            Result result = gson.fromJson(jsonFile.readLine(), Result.class);
            movie = result.getMovies().get(0);

            Logger.info("Name : " + movie.getTitle());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return movie;
    }
}