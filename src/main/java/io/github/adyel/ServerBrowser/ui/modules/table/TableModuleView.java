package io.github.adyel.ServerBrowser.ui.modules.table;

import io.github.adyel.ServerBrowser.ui.viewmodel.TableViewModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import org.controlsfx.control.CheckComboBox;

public class TableModuleView extends BorderPane {

  TableModuleView() {
    super();

    TextField textField = new TextField();
    textField.setPromptText("Search");
    CheckComboBox<String> checkComboBox = new CheckComboBox<>();
    checkComboBox.setTitle("Genre");
    Region spacer = new Region();

    HBox.setHgrow(spacer, Priority.ALWAYS);
    HBox.setMargin(textField, new Insets(5.0));
    HBox.setMargin(checkComboBox, new Insets(5.0));
    HBox hBox = new HBox(textField, spacer, checkComboBox);

    var tableView = initTable();

    setTop(hBox);
    setCenter(tableView);
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
