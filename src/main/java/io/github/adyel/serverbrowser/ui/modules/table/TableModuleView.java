package io.github.adyel.serverbrowser.ui.modules.table;

import io.github.adyel.serverbrowser.jpa.controller.MovieController;
import io.github.adyel.serverbrowser.ui.viewmodel.TableViewModel;
import io.github.adyel.serverbrowser.util.ApplicationContextUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import org.controlsfx.control.CheckComboBox;
import org.controlsfx.control.Rating;
import org.springframework.context.ApplicationContext;

class TableModuleView extends BorderPane {

  TableModuleView() {
    super();

    TextField searchField = new TextField();
    searchField.setPromptText("Search");
    CheckComboBox<String> checkComboBox = new CheckComboBox<>();
    checkComboBox.setTitle("Genre");
    Region hBoxSpacer = new Region();

    HBox.setHgrow(hBoxSpacer, Priority.ALWAYS);
    HBox.setMargin(searchField, new Insets(5.0));
    HBox.setMargin(checkComboBox, new Insets(5.0));
    HBox hBox = new HBox(searchField, hBoxSpacer, checkComboBox);

    Rating rating = setupRating();


    ImageView imageView = new ImageView();
    imageView.setImage(new Image("http://image.tmdb.org/t/p/w185/kqjL17yufvn9OVLyXYpvtyrFfak.jpg"));
    imageView.setPreserveRatio(true);

    Region gridPaneSpacer = new Region();

    GridPane gridPane = setupGridPane();
    gridPane.add(rating, 0, 1);
    gridPane.add(gridPaneSpacer, 1, 1);
    gridPane.add(imageView, 2, 1, 3, 3);


    ObservableList<TableViewModel> tableViewModelObservableList = initObservableList();
    TableView<TableViewModel> tableView = setupTableView();
    setTop(hBox);
    setCenter(tableView);
    setBottom(gridPane);

    var filteredList = new FilteredList<>(tableViewModelObservableList, e -> true);

    searchField
        .textProperty()
        .addListener(
            ((observable, oldValue, newValue) ->
                filteredList.setPredicate(
                    tableViewModel -> {
                      if (newValue == null || newValue.isEmpty()) {
                        return true;
                      }

                      if (tableViewModel
                          .getTitle()
                          .toLowerCase()
                          .contains(newValue.trim().toLowerCase())) {
                        return true;
                      } else {
                        return tableViewModel.getYear().toString().contains(newValue.trim());
                      }
                    })));

    SortedList<TableViewModel> sortedList = new SortedList<>(filteredList);
    sortedList.comparatorProperty().bind(tableView.comparatorProperty());
    tableView.setItems(sortedList);

//    );
  }

  private ObservableList<TableViewModel> initObservableList() {
    ObservableList<TableViewModel> observableList = FXCollections.observableArrayList();

    ApplicationContext applicationContext = ApplicationContextUtils.getApplicationContext();
    MovieController movieController = applicationContext.getBean(MovieController.class);

    observableList.addAll(movieController.getAllMovieInTableViewModel());

    return observableList;
  }

  private GridPane setupGridPane(){
    var gridPane = new GridPane();
    var gridPaneSpacer = new Region();
    gridPane.setPadding(new Insets(5.0));
    GridPane.setHgrow(gridPaneSpacer, Priority.ALWAYS);
    return gridPane;
  }

  private Rating setupRating(){
    var rating = new Rating();
    rating.setMax(10);
    rating.setPartialRating(true);
    return rating;
  }

  private TableView<TableViewModel> setupTableView(){
    var columnTitle = new TableColumn<TableViewModel, String>("Title");
    var columnYear = new TableColumn<TableViewModel, Integer>("Year");
    var columnRating = new TableColumn<TableViewModel, Double>("Rating");
    var columnDirector = new TableColumn<TableViewModel, String>("Director");

    columnTitle.setCellValueFactory(new PropertyValueFactory<>("Title"));
    columnYear.setCellValueFactory(new PropertyValueFactory<>("Year"));
    columnRating.setCellValueFactory(new PropertyValueFactory<>("Rating"));
    columnDirector.setCellValueFactory(new PropertyValueFactory<>("Director"));

    TableView<TableViewModel> tableView = new TableView<>();
    tableView.getColumns().addAll(columnTitle, columnYear, columnRating, columnDirector);

    return tableView;
  }
}
