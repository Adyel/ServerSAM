package main;

import com.github.wtekiela.opensub4j.api.OpenSubtitlesClient;
import com.github.wtekiela.opensub4j.impl.OpenSubtitlesClientImpl;
import com.github.wtekiela.opensub4j.response.MovieInfo;
import com.github.wtekiela.opensub4j.response.SubtitleFile;
import com.github.wtekiela.opensub4j.response.SubtitleInfo;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import model.fx.TableViewModel;
import model.orm.Genre;
import model.orm.MovieDetails;
import org.apache.xmlrpc.XmlRpcException;
import org.controlsfx.control.CheckComboBox;
import org.controlsfx.control.Rating;
import org.hibernate.Session;
import org.pmw.tinylog.Logger;
import ui.controller.Preference;
import util.HibernateConnMan;
import util.ParseData;
import util.TMDBservice;

public class Controller implements Initializable {

  @FXML Circle dbStatus;
  @FXML TextField searchField;
  @FXML CheckComboBox<String> checkComboBox;
  @FXML TextArea text;
  @FXML ImageView posterImage;
  @FXML Rating starRating;
  @FXML Button settingsButton;
  @FXML Button refreshButton;
  @FXML Button updateInfoButton;
  @FXML Button downloadSub;
  @FXML ProgressBar progressBar;
  @FXML private TableView<TableViewModel> table;
  @FXML private TableColumn<TableViewModel, String> title;
  @FXML private TableColumn<TableViewModel, Integer> year;
  @FXML private TableColumn<TableViewModel, Double> rating;
  @FXML private TableColumn<TableViewModel, String> director;
  @FXML private TableColumn<TableViewModel, Boolean> subtitle;
  private ObservableList<TableViewModel> tableViewModelObservableList =
      FXCollections.observableArrayList();
  private FilteredList<TableViewModel> tableViewModelFilteredList;

  private List<MovieDetails> movieDetailsList;

  private Session session = HibernateConnMan.getSession();

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    initializeColumns();
    loadDate();

    // INFO: Load Selected Genres from comboBox
    checkComboBox.getCheckModel().getCheckedItems().addListener(updateListOnChange());

    // INFO: Search Function
    tableViewModelFilteredList = new FilteredList<>(tableViewModelObservableList, e -> true);
    searchField.setOnKeyReleased(
        event -> {
          searchField
              .textProperty()
              .addListener(
                  (observable, oldValue, newValue) -> {
                    tableViewModelFilteredList.setPredicate(
                        (Predicate<? super TableViewModel>)
                            tableViewModel -> {
                              if (newValue == null || newValue.isEmpty()) {
                                return true;
                              } else if (tableViewModel
                                  .getTitle()
                                  .toLowerCase()
                                  .contains(newValue.toLowerCase())) {
                                return true;
                              } else return tableViewModel.getYear().toString().contains(newValue);
                            });
                  });

          SortedList<TableViewModel> sortedTableViewModel =
              new SortedList<>(tableViewModelFilteredList);
          sortedTableViewModel.comparatorProperty().bind(table.comparatorProperty());
          table.setItems(sortedTableViewModel);
        });
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
    session = HibernateConnMan.getSession(session);

    // INFO: Load all the movies
    movieDetailsList =
        session.createQuery("SELECT DISTINCT movie FROM MovieDetails movie").getResultList();
    List<Genre> genreList = session.createQuery("FROM Genre").getResultList();
    session.getTransaction().commit();

    // ! Load all suggestion to searchBox
    //        TextFields.bindAutoCompletion(searchField,
    // movieDetailsList.stream().map(MovieDetails::getFileName).collect(Collectors.toList()));

    // INFO: Add all genre to CheckComboBox
    ObservableList<String> genres =
        FXCollections.observableArrayList(
            genreList.stream().map(Genre::getName).collect(Collectors.toList()));
    checkComboBox.getItems().addAll(genres);

    loadTable();
  }

  private ListChangeListener<String> updateListOnChange() {
    return change -> {
      Logger.info(checkComboBox.getCheckModel().getCheckedIndices());
      Logger.debug(checkComboBox.getCheckModel().getCheckedItems().toString());

      String query;

      if (checkComboBox.getCheckModel().getCheckedIndices().isEmpty()) {
        query = "FROM MovieDetails ";
      } else {
        query =
            "SELECT DISTINCT movie FROM MovieDetails movie JOIN movie.genres genre WHERE genre.name IN (";

        for (String genre : checkComboBox.getCheckModel().getCheckedItems()) {
          query = query + "'" + genre + "', ";
        }

        query = query.substring(0, query.length() - 2) + " )";
      }

      Logger.debug(query);

      session = HibernateConnMan.getSession(session);
      movieDetailsList = session.createQuery(query).getResultList();
      session.getTransaction().commit();

      loadTable();

      // ! Load suggestion
      //            TextFields.bindAutoCompletion(searchField,
      // movieDetailsList.stream().map(MovieDetails::getFileName).collect(Collectors.toList()));
    };
  }

  @FXML
  private void displaySelectedMovie(MouseEvent mouseEvent) {

    Object clickParent = mouseEvent.getPickResult().getIntersectedNode().getParent();
    boolean isTableRow = clickParent.getClass() == TableRow.class;
    // INFO: Had to do this because of "Anonymous Inner Class"
    // (https://stackoverflow.com/questions/1075207/what-is-the-1-in-class-file-names)
    boolean isTableColumn =
        Objects.equals(clickParent.getClass().getName(), "javafx.scene.control.TableColumn$1$1");

    Logger.debug(isTableRow);
    Logger.debug(isTableColumn);
    Logger.debug(clickParent);

    if (isTableRow || isTableColumn) {

      if (!session.isOpen()) {
        session = HibernateConnMan.getSession();
      }
      if (!session.getTransaction().isActive()) {
        session.beginTransaction();
      }

      int movieId = table.getSelectionModel().getSelectedItem().getId();
      MovieDetails movie =
          (MovieDetails)
              session
                  .createQuery("SELECT movie FROM MovieDetails movie WHERE movie.id = " + movieId)
                  .getSingleResult();

      // INFO: If Overview exists that means other info also exists
      if (movie.getOverview() != null) {

        // Display overview
        text.setVisible(true);
        text.setText(movie.getOverview());

        // load image from web and Display
        Image image = new Image("http://image.tmdb.org/t/p/w185" + movie.getPosterPath());
        posterImage.setImage(image);
        posterImage.setVisible(true);

        // Show star Rating
        starRating.setVisible(true);
        starRating.setRating(movie.getVoteAverage());
      } else {
        text.setVisible(false);
        posterImage.setVisible(false);
        starRating.setVisible(false);
      }
      session.close();
    }
  }

  @FXML
  private void buttonAction(ActionEvent event) {

    Button clickedButton = (Button) event.getSource();

    if (clickedButton.equals(settingsButton)) {
      try {
        Parent root = FXMLLoader.load(getClass().getResource("Preferences.fxml"));
        Stage stage = new Stage();
        stage.setTitle("ServerSAM");
        stage.setScene(new Scene(root));
        stage.setWidth(600);
        stage.setHeight(400);
        stage.centerOnScreen();
        stage.show();
      } catch (IOException e) {
        Logger.debug(e);
      }
    } else if (clickedButton.equals(refreshButton)) {
      String dirPath = Preference.preferences.get("dir_path", null);
      scanFolders(dirPath);
    } else if (clickedButton.equals(updateInfoButton)) {

      Thread thread =
          new Thread(
              () -> {
                TMDBservice tmdBservice = new TMDBservice();

                progressBar.setVisible(true);
                progressBar.setProgress(0.0);

                tmdBservice.loadGenre();
                progressBar.setProgress(0.2);

                tmdBservice.fetchData();
                progressBar.setProgress(0.7);

                tmdBservice.updateDatabase();
                progressBar.setProgress(0.9);
                loadDate();
                progressBar.setVisible(false);
              });

      thread.start();

    } else if (clickedButton.equals(downloadSub)) {

      Thread thread =
          new Thread(
              () -> {
                TableViewModel movieModel = table.getSelectionModel().getSelectedItem();
                downloadSubtitle(movieModel.getTitle(), movieModel.getYear());
              }
          );

      thread.start();
    }
  }

  private void downloadSubtitle(String movieName, int year) {

    try {
      URL serverUrl = new URL("https", "api.opensubtitles.org", 443, "/xml-rpc");
      OpenSubtitlesClient osClient = new OpenSubtitlesClientImpl(serverUrl);
      osClient.login("Adyel", "mvC1Re2soTcr", "en", "TemporaryUserAgent");

      String query = movieName + " (" + year + ")";

      MovieInfo movieInfo = osClient.searchMoviesOnImdb(query).get(0);
      int movieImdbId = movieInfo.getId();
      SubtitleInfo subtitleInfo = osClient.searchSubtitles("eng", "" + movieImdbId).get(0);
      SubtitleFile subtitleFile =
          osClient.downloadSubtitles(subtitleInfo.getSubtitleFileId()).get(0);

      Path path = Paths.get( System.getProperty("user.home") + System.getProperty("file.separator") + "Desktop" + System.getProperty("file.separator") + query + ".srt");
      try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
        writer.write(subtitleFile.getContentAsString("UTF-8"));
      } catch (IOException ex) {
        Logger.debug(ex);
      }

    } catch (MalformedURLException e) {
      Logger.debug(e);
    } catch (XmlRpcException e) {
      Logger.info(e);
    }
  }

  private void loadTable() {
    // INFO: Clear and load table
    tableViewModelObservableList.clear();
    tableViewModelObservableList =
        movieDetailsList.stream()
            .map(TableViewModel::new)
            .collect(Collectors.toCollection(FXCollections::observableArrayList));
    table.setItems(tableViewModelObservableList);

    // ! Load Search
    tableViewModelFilteredList = new FilteredList<>(tableViewModelObservableList, e -> true);
  }

  private void scanFolders(String dirPath) {
    Thread thread =
        new Thread(
            () -> {


              progressBar.setVisible(true);
              progressBar.setProgress(0.0);

              List<Path> paths = null;
              try {
                paths =
                    Files.list(Paths.get(dirPath))
                        .filter(Files::isDirectory)
                        .collect(Collectors.toList());
              } catch (IOException e) {
                Logger.debug("Error Occurred", e);
              }

              ParseData parseData = new ParseData();

              progressBar.setProgress(0.5);

              paths.stream()
                  .filter(Objects::nonNull)
                  .forEach(
                      path -> {
                        Logger.debug(path.getFileName());
                        parseData.add(path.toFile().getName());
                      });

              session = HibernateConnMan.getSession(session);
              parseData.getMovieDetailsList().forEach(session::save);
              session.getTransaction().commit();

              progressBar.setProgress(1.0);
              progressBar.setVisible(false);

              loadDate();
            });

    thread.start();
  }
}
