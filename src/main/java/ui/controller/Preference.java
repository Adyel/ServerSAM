package ui.controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class Preference implements Initializable {

  public static final Preferences preferences = Preferences.userNodeForPackage(Preference.class);

  @FXML BorderPane borderPane;

  @FXML Button apply;

  @FXML Button cancel;

  @FXML Button folderSelector;

  @FXML TextField dirPath;

  private File file = null;

  @Override
  public void initialize(URL location, ResourceBundle resources) {}

  @FXML
  private void buttonAction(Event event) {

    Button clickedButton = (Button) event.getSource();

    final DirectoryChooser directoryChooser = new DirectoryChooser();
    Stage stage = (Stage) borderPane.getScene().getWindow();

    if (clickedButton.equals(folderSelector)) {
      file = directoryChooser.showDialog(stage);
      dirPath.setText(file.getAbsolutePath());
    } else if (clickedButton.equals(apply) && file != null) {
      preferences.put("dir_path", file.getAbsolutePath());
    } else if (clickedButton.isCancelButton()) {
      stage.hide();
    }
  }
}
