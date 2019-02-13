import com.uwetrottmann.tmdb2.Tmdb;
import com.uwetrottmann.tmdb2.entities.GenreResults;
import com.uwetrottmann.tmdb2.services.SearchService;
import model.orm.Genre;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class API {
    public static void main(String[] args) {
        Tmdb tmdb = new Tmdb("909af28891cff6bfe9ea2fea81fc5426");
        SearchService searchService = tmdb.searchService();
        Call<GenreResults> genreResults = tmdb.genreService().movie("English");


        List<Genre> genreList = new ArrayList<>();

        try {
            Response<GenreResults> genreResultsResponse = genreResults.execute();
            genreList = genreResultsResponse.body().genres.stream().map(Genre::new).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
