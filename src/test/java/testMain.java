import model.json.Movie;
import model.json.Result;
import okhttp3.Dispatcher;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import util.tmdbAPI;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;

public class testMain {

    public static void main(String[] args) {

//        TMDBconnect tmdb = new TMDBconnect("909af28891cff6bfe9ea2fea81fc5426");
//        Movie movie = tmdb.findMovie("Oldboy", 2013);
//
//        System.out.println(movie.getTitle() + "(" + movie.getVoteAverage() + ") - " + movie.getOverview());
//        System.out.println(movie.getGenreIds());


        Dispatcher dispatcher = new Dispatcher(Executors.newFixedThreadPool(20));
        dispatcher.setMaxRequests(20);

        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl("https://api.themoviedb.org")
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

        util.tmdbAPI apiInterface = retrofit.create(tmdbAPI.class);

        // https://api.themoviedb.org/3/search/movie?api_key=909af28891cff6bfe9ea2fea81fc5426&query=Oldboy&year=2013

        Call<Result> result = apiInterface.response(3,"search","909af28891cff6bfe9ea2fea81fc5426","Oldboy", 2003);


//        result.enqueue(new Callback<Result>() {
//            @Override
//            public void onResponse(Call<Result> call, retrofit2.Result<Result> response) {
//                Result responseList = response.body();
//                List<Movie> movieList = responseList.getMovies();
//                System.out.println(movieList.get(0).getGenreIds());
//
//
//            }
//
//            @Override
//            public void onFailure(Call<Result> call, Throwable t) {
//                t.printStackTrace();
//
//            }
//        });


        try {
           retrofit2.Response response = result.execute();
           Result reply = (Result) response.body();
           List<Movie> movieList = reply.getMovies();
           Movie firstMovie = movieList.get(0);

            System.out.println(firstMovie.getReleaseDate());

//           Connection connection = DBConnect.getConnection();
//           Statement statement = connection.createStatement();
//           statement.executeUpdate("UPDATE Movie_List SET Rating = " + firstMovie.getVoteAverage() + ", Overview = '" + firstMovie.getOverview() + "' WHERE ID = 2292");

        } catch (IOException e) {
            e.printStackTrace();
        }
//        catch (SQLException e) {
//            e.printStackTrace();
//        }
    }
}
