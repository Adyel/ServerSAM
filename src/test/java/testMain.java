import model.Movie;
import model.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import util.tmdbAPI;

import java.util.List;

public class testMain {

    public static void main(String[] args) {

//        TMDBconnect tmdb = new TMDBconnect("909af28891cff6bfe9ea2fea81fc5426");
//        Movie movie = tmdb.findMovie("Oldboy", 2013);
//
//        System.out.println(movie.getTitle() + "(" + movie.getVoteAverage() + ") - " + movie.getOverview());
//        System.out.println(movie.getGenreIds());


        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl("https://api.themoviedb.org")
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

        util.tmdbAPI apiInterface = retrofit.create(tmdbAPI.class);

        // https://api.themoviedb.org/3/search/movie?api_key=909af28891cff6bfe9ea2fea81fc5426&query=Oldboy&year=2013

        Call<Response> result = apiInterface.response(3,"search","909af28891cff6bfe9ea2fea81fc5426","Oldboy", 2013);


        result.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                Response responseList = response.body();
                List<Movie> movieList = responseList.getMovies();
                System.out.println(movieList.get(0).getGenreIds());


            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                t.printStackTrace();

            }
        });
    }
}
