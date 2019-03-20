package main;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import model.fx.TableViewModel;
import model.orm.Genre;
import model.orm.MovieDetails;
import org.controlsfx.control.CheckComboBox;
import org.controlsfx.control.Rating;
import org.controlsfx.control.textfield.TextFields;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.pmw.tinylog.Logger;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Controller implements Initializable {

    @FXML
    Circle dbStatus;

    @FXML
    private TableView<TableViewModel> table;

    @FXML
    private TableColumn<TableViewModel, String> title;

    @FXML
    private TableColumn<TableViewModel, Integer> year;

    @FXML
    private TableColumn<TableViewModel, Double> rating;

    @FXML
    private TableColumn<TableViewModel, String> director;

    @FXML
    private TableColumn<TableViewModel, Boolean> subtitle;

    @FXML
    TextField searchField;

    @FXML
    CheckComboBox<String> checkComboBox;

    @FXML
    TextArea text;

    @FXML
    ImageView posterImage;

    @FXML
    Rating starRating;

    private ObservableList<TableViewModel> tableViewModelObservableList = FXCollections.observableArrayList();
    private List<MovieDetails> movieDetailsList;


    private static SessionFactory factory = new Configuration()
            .configure("hibernate.cfg.xml")
            .addAnnotatedClass(MovieDetails.class)
            .buildSessionFactory();


    private static Session session = factory.openSession();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        initializeColumns();
        loadDate();

        // INFO: Load Selected Genres from comboBox
        checkComboBox.getCheckModel().getCheckedItems().addListener(updateListOnChange());

        tableViewModelObservableList = movieDetailsList.stream().map(TableViewModel::new).collect(Collectors.toCollection(FXCollections::observableArrayList));


        // INFO: Search Function
        FilteredList<TableViewModel> tableViewModelFilteredList = new FilteredList<>(tableViewModelObservableList, e -> true);
        searchField.setOnKeyReleased(event -> {
            searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                tableViewModelFilteredList.setPredicate((Predicate<? super TableViewModel>) tableViewModel -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    } else if (tableViewModel.getTitle().toLowerCase().contains(newValue.toLowerCase())) {
                        return true;
                    } else return tableViewModel.getYear().toString().contains(newValue);


                });
            });

            SortedList<TableViewModel> sortedTableViewModel = new SortedList<>(tableViewModelFilteredList);
            sortedTableViewModel.comparatorProperty().bind(table.comparatorProperty());
            table.setItems(sortedTableViewModel);
        });

        table.setItems(tableViewModelObservableList);
    }


    private void initializeColumns() {
        // INFO: Config the Columns
        title.setCellValueFactory(new PropertyValueFactory<>("title"));
        year.setCellValueFactory(new PropertyValueFactory<>("year"));
        rating.setCellValueFactory(new PropertyValueFactory<>("rating"));
        director.setCellValueFactory(new PropertyValueFactory<>("director"));
        subtitle.setCellValueFactory(new PropertyValueFactory<>("subtitle"));
    }

    private void loadDate() {
        // INFO: Start Session
        session.beginTransaction();

        // INFO: Load all the movies
        movieDetailsList = session.createQuery("SELECT DISTINCT movie FROM MovieDetails movie").getResultList();
        List<Genre> genreList = session.createQuery("FROM Genre").getResultList();
        session.getTransaction().commit();


        // INFO: Load all suggestion to searchBox
        TextFields.bindAutoCompletion(searchField, movieDetailsList.stream().map(MovieDetails::getFileName).collect(Collectors.toList()));


        // INFO: Add all genre to CheckComboBox
        ObservableList<String> genres = FXCollections.observableArrayList(genreList.stream().map(Genre::getName).collect(Collectors.toList()));
        checkComboBox.getItems().addAll(genres);
    }


    private ListChangeListener<String> updateListOnChange(){
        return change -> {
            Logger.info(checkComboBox.getCheckModel().getCheckedIndices());
            Logger.debug(checkComboBox.getCheckModel().getCheckedItems().toString());

            String query;

            if (checkComboBox.getCheckModel().getCheckedIndices().isEmpty()) {
                query = "FROM MovieDetails ";
            } else {
                query = "SELECT DISTINCT movie FROM MovieDetails movie JOIN movie.genres genre WHERE genre.name IN (";

                for (String genre : checkComboBox.getCheckModel().getCheckedItems()) {
                    query = query + "'" + genre + "', ";
                }

                query = query.substring(0, query.length() - 2) + " )";
            }

            System.out.println(query);

            session.beginTransaction();
            movieDetailsList = session.createQuery(query).getResultList();
            session.getTransaction().commit();

            // INFO: Clear and load table
            tableViewModelObservableList.clear();
            tableViewModelObservableList = movieDetailsList.stream().map(TableViewModel::new).collect(Collectors.toCollection(FXCollections::observableArrayList));
            table.setItems(tableViewModelObservableList);

            //INFO : Load suggestion
            TextFields.bindAutoCompletion(searchField, movieDetailsList.stream().map(MovieDetails::getFileName).collect(Collectors.toList()));
        };
    }


    @FXML
    private void displaySelectedMovie(MouseEvent mouseEvent) {

        Object clickParent = mouseEvent.getPickResult().getIntersectedNode().getParent();
        boolean isTableRow = clickParent.getClass() == TableRow.class;
        boolean isTableColumn = Objects.equals(clickParent.getClass().getName(), "javafx.scene.control.TableColumn$1$1");   // INFO: Had to do this because of "Anonymous Inner Class" (https://stackoverflow.com/questions/1075207/what-is-the-1-in-class-file-names)
        Logger.debug(isTableRow);
        Logger.debug(isTableColumn);
        Logger.debug(clickParent);

        if (isTableRow || isTableColumn) {

            int movieId = table.getSelectionModel().getSelectedItem().getId();
            MovieDetails movie = (MovieDetails) session.createQuery("SELECT movie FROM MovieDetails movie WHERE movie.id = " + movieId).getSingleResult();

            // INFO: If Overview exists that means other info also exists
            if (movie.getOverview() != null) {

                // Display overview
                text.setText(movie.getOverview());

                // load image from web and Display
                Image image = new Image("http://image.tmdb.org/t/p/w185" + movie.getPosterPath());
                posterImage.setImage(image);

                // Show star Rating
                starRating.setVisible(true);
                starRating.setRating(movie.getVoteAverage());
            }
        }
    }

    @FXML
    void handleButtonAction(ActionEvent event) {
    }

    static void closeFactory() {
        factory.close();
    }
}
