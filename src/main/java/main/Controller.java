package main;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import util.DBConnect;

import java.net.URL;
import java.sql.Connection;
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


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        this.dbStatus.setStroke(Color.BLACK);



        if (connection == null) {
            this.dbStatus.setFill(Color.rgb(244, 91, 59));
        } else {
            this.dbStatus.setFill(Color.rgb(107, 244, 66));
        }
    }
}
