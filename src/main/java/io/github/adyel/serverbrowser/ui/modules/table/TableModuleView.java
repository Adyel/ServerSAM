package io.github.adyel.serverbrowser.ui.modules.table;

import io.github.adyel.serverbrowser.ui.viewmodel.TableViewModel;
import java.util.function.Predicate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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

    Rating rating = new Rating();
    rating.setMax(10);
    rating.setPartialRating(true);

    ImageView imageView = new ImageView();
    imageView.setImage(new Image("http://image.tmdb.org/t/p/w185/kqjL17yufvn9OVLyXYpvtyrFfak.jpg"));
    imageView.setPreserveRatio(true);

    Region gridPaneSpacer = new Region();

    GridPane gridPane = new GridPane();
    gridPane.setPadding(new Insets(5.0));
    GridPane.setHgrow(gridPaneSpacer, Priority.ALWAYS);

    gridPane.add(rating,0,1);
    gridPane.add(gridPaneSpacer,1,1);
    gridPane.add(imageView,2,1,3,3);


    var tableView = initTable();

    setTop(hBox);
    setCenter(tableView);
    setBottom(gridPane);

    // Event Implementation

    ObservableList<TableViewModel> tableViewModelObservableList =
        FXCollections.observableArrayList();

    FilteredList<TableViewModel> tableViewModelFilteredList =
        new FilteredList<>(tableViewModelObservableList, e -> true);

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
        });
  }

  private TableView initTable() {

    TableView<TableViewModel> tableView = new TableView<>();

    var columnTitle = new TableColumn<TableViewModel, String>("Title");
    var columnYear = new TableColumn<TableViewModel, Integer>("Year");
    var columnRating = new TableColumn<TableViewModel, Double>("Rating");
    var columnDirector = new TableColumn<TableViewModel, String>("Director");

    columnTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
    columnYear.setCellValueFactory(new PropertyValueFactory<>("year"));
    columnRating.setCellValueFactory(new PropertyValueFactory<>("rating"));
    columnDirector.setCellValueFactory(new PropertyValueFactory<>("director"));


//    Todo: Remove after testing
    ObservableList<TableViewModel> list =
        FXCollections.observableArrayList(
            new TableViewModel("Up", 2019),
            new TableViewModel("Fighting Club", 2011),
            new TableViewModel("Avengers", 2017));

    tableView.setItems(list);
    tableView.getColumns().addAll(columnTitle, columnYear, columnRating, columnDirector);
    return tableView;
  }
}
