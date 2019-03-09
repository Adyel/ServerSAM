import com.uwetrottmann.tmdb2.Tmdb;
import com.uwetrottmann.tmdb2.entities.BaseMovie;
import com.uwetrottmann.tmdb2.entities.MovieResultsPage;
import com.uwetrottmann.tmdb2.services.SearchService;
import model.orm.Genre;
import model.orm.MovieDetails;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;

public class UpdateMovie {
    public static void main(String[] args) {

        Tmdb tmdb = new Tmdb("909af28891cff6bfe9ea2fea81fc5426");
        SearchService searchService = tmdb.searchService();

        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(MovieDetails.class)
                .buildSessionFactory();

        Session session = factory.openSession();
        session.beginTransaction();

        List<MovieDetails> movieDetailsList = session.createQuery("SELECT movie FROM MovieDetails movie").getResultList();

        int couter = 50;

        for (MovieDetails movie : movieDetailsList){

            BaseMovie baseMovie = null;

            try {
                Call<MovieResultsPage> movieResultsPage = searchService.movie(movie.getFileName(),1,"en",false, movie.getYear(),null,null);
                Response<MovieResultsPage> movieResultsResponse =  movieResultsPage.execute();

                if (movieResultsResponse.body().results.isEmpty()){
                    continue;
                }else {
                    baseMovie = movieResultsResponse.body().results.get(0);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            updateDetails(movie, baseMovie);
            session.save(movie);
            couter--;
            if (couter == 0){
                break;
            }
        }

        session.getTransaction().commit();
        factory.close();
    }

    private static void updateDetails(MovieDetails movie, BaseMovie baseMovie){
        movie.setTmdbId(baseMovie.id);
        movie.setTitle(baseMovie.title);
        movie.setOverview(baseMovie.overview);
        movie.setVoteAverage(baseMovie.vote_average);
        movie.setPopularity(baseMovie.popularity);
        movie.setOriginalLanguage(baseMovie.original_language);
        movie.setReleaseDate(baseMovie.release_date);
        movie.setPosterPath(baseMovie.poster_path);

        for (int genreId : baseMovie.genre_ids){
            movie.getGenres().add(new Genre(genreId));
        }
    }

}
