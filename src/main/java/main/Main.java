package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import util.HibernateConnMan;

public class Main extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("MainWindow.fxml"));
    primaryStage.setTitle("ServerSAM");
    primaryStage.setScene(new Scene(root));
    primaryStage.show();
    primaryStage.setMinWidth(primaryStage.getWidth());
    primaryStage.setMinHeight(primaryStage.getHeight());
    primaryStage.setOnCloseRequest(event -> closeProgram());
  }

  private void closeProgram() {
    HibernateConnMan.closeFactory();
  }
}
