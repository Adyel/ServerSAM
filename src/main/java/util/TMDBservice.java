package util;

import com.uwetrottmann.tmdb2.Tmdb;
import com.uwetrottmann.tmdb2.entities.BaseMovie;
import com.uwetrottmann.tmdb2.entities.GenreResults;
import com.uwetrottmann.tmdb2.entities.MovieResultsPage;
import com.uwetrottmann.tmdb2.services.SearchService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import model.orm.Genre;
import model.orm.MovieDetails;
import org.hibernate.Session;
import org.pmw.tinylog.Logger;
import retrofit2.Call;
import retrofit2.Response;

public class TMDBservice {

  private static final String API_KEY = "909af28891cff6bfe9ea2fea81fc5426";
  private final Tmdb tmdb = new Tmdb(API_KEY);
  private Session session = HibernateConnMan.getSession();
  private List<MovieDetails> movieDetailsList;

  private void loadDatabase() {
    session = HibernateConnMan.getSession();
    session.beginTransaction();
    movieDetailsList = session.createQuery("SELECT movie FROM MovieDetails movie").getResultList();
  }

  private void updateDetails(MovieDetails movie, BaseMovie baseMovie) {

    movie.setTmdbId(baseMovie.id);
    movie.setTitle(baseMovie.title);
    movie.setOverview(baseMovie.overview);
    movie.setVoteAverage(baseMovie.vote_average);
    movie.setPopularity(baseMovie.popularity);
    movie.setOriginalLanguage(baseMovie.original_language);
    movie.setReleaseDate(baseMovie.release_date);
    movie.setPosterPath(baseMovie.poster_path);

    for (int genreId : baseMovie.genre_ids) {
      movie.getGenres().add(new Genre(genreId));
    }
  }

  public void fetchData() {

    loadDatabase();

    SearchService searchService = tmdb.searchService();

    for (MovieDetails movie : movieDetailsList) {
      BaseMovie baseMovie = null;

      try {
        Call<MovieResultsPage> movieResultsPage =
            searchService.movie(movie.getFileName(), 1, "en", false, movie.getYear(), null, null);
        Response<MovieResultsPage> movieResultsResponse = movieResultsPage.execute();

        if (!movieResultsResponse.body().results.isEmpty()) {
          baseMovie = movieResultsResponse.body().results.get(0);
          updateDetails(movie, baseMovie);
        }
      } catch (IOException e) {
        Logger.debug(e);
      }
    }
  }

  public void updateDatabase() {
    movieDetailsList.forEach(session::save);
    session.getTransaction().commit();
  }

  public void loadGenre() {

    Call<GenreResults> genreResults = tmdb.genreService().movie("English");

    session = HibernateConnMan.getSession();
    session.beginTransaction();

    // INFO: GET Genre and Save it to Database

    List<Genre> genreList = new ArrayList<>();

    try {
      Response<GenreResults> genreResultsResponse = genreResults.execute();
      genreList =
          genreResultsResponse.body().genres.stream()
              .map(Genre::new)
              .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    } catch (IOException e) {
      Logger.info("IOException ", e);
    }

    for (Genre genre : genreList) {
      session.save(genre);
    }

    session.getTransaction().commit();
  }
}
