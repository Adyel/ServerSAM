package util;

import model.json.Result;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

@Deprecated
public interface tmdbAPI {

    // https://api.themoviedb.org/3/search/movie?api_key=909af28891cff6bfe9ea2fea81fc5426&query=Oldboy&year=2013

    @GET("/{apiVer}/{operation}/movie")
    Call<Result> response (
            @Path("apiVer") int apiVer,
            @Path("operation") String operation,
            @Query("api_key") String apiKey,
            @Query("query") String movieName,
            @Query("year") int year

    );


}
