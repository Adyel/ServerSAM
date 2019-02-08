package main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import model.fx.TableViewModel;
import util.DBConnect;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Controller implements Initializable {



    Connection connection;
    {
        try {
            connection = DBConnect.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    Circle dbStatus;

    @FXML
    private TableView<TableViewModel> table;
    @FXML
    private TableColumn<TableViewModel, String> title;
    @FXML
    private TableColumn<TableViewModel, Integer> year;

    ObservableList<TableViewModel> list = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

//        this.dbStatus.setFill(Color.rgb(244, 91, 59));

        // This is Database connection LED

        this.dbStatus.setStroke(Color.BLACK);

        if (connection == null) {
            this.dbStatus.setFill(Color.rgb(244, 91, 59));
        } else {
            this.dbStatus.setFill(Color.rgb(107, 244, 66));
        }



        title.setCellValueFactory(new PropertyValueFactory<>("title"));
        year.setCellValueFactory(new PropertyValueFactory<>("year"));

        try {
            Connection connection = DBConnect.getConnection();
            ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM Movie_List");

            while (resultSet.next()){
                list.add(new TableViewModel(resultSet.getString("Movie_Name"), resultSet.getInt("Year")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        table.setItems(list);




    }
}
