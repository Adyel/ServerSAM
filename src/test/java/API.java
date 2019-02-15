import com.uwetrottmann.tmdb2.Tmdb;
import com.uwetrottmann.tmdb2.entities.CastMember;
import com.uwetrottmann.tmdb2.entities.Credits;
import com.uwetrottmann.tmdb2.entities.CrewMember;
import com.uwetrottmann.tmdb2.entities.GenreResults;
import com.uwetrottmann.tmdb2.services.CreditsService;
import com.uwetrottmann.tmdb2.services.MoviesService;
import com.uwetrottmann.tmdb2.services.SearchService;
import model.orm.Genre;
import model.orm.MovieDetails;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class API {
    public static void main(String[] args) {
        Tmdb tmdb = new Tmdb("909af28891cff6bfe9ea2fea81fc5426");
        SearchService searchService = tmdb.searchService();
        CreditsService creditsService = tmdb.creditsService();
        MoviesService moviesService = tmdb.moviesService();
        Call<GenreResults> genreResults = tmdb.genreService().movie("English");




        // INFO: GET Genre and Save it to Database

        List<Genre> genreList = new ArrayList<>();

        try {
            Response<GenreResults> genreResultsResponse = genreResults.execute();
            genreList = genreResultsResponse.body().genres.stream().map(Genre::new).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        } catch (IOException e) {
            e.printStackTrace();
        }

        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Genre.class)
                .buildSessionFactory();


        Session session = factory.openSession();
        session.beginTransaction();

        for (Genre genre : genreList){
            session.save(genre);
        }

        genreList.clear();
        session.getTransaction().commit();
        factory.close();


//        // INFO: GET Cast Members
//        // ! First Need to fetch TMDB_MOVIE_ID to work
//
//        List<CrewMember> crewMemberList = new ArrayList<>();
//
//        try {
//            Response<Credits> creditsResponse = moviesService.credits(14160).execute();
//            crewMemberList = creditsResponse.body().crew;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//            CrewMember director = crewMemberList.stream().filter(e -> e.job.equals("Director")).findFirst().orElse(null);

    }
}
