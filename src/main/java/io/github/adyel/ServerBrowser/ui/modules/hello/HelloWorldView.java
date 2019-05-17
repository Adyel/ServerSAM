package io.github.adyel.ServerBrowser.ui.modules.hello;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

class HelloWorldView extends BorderPane {

    private Label label = new Label("Nothing Here");

    ButtonBar buttonBar = new ButtonBar();

    HelloWorldView() {
        setCenter(label);

        Button btnClick = new Button("Click");
        Button btnFlick = new Button("Flick");
        Region spacer = new Region();


        HBox hBox = new HBox(btnClick,spacer,btnFlick);
        hBox.setSpacing(10);
        HBox.setHgrow(spacer, Priority.ALWAYS);

        buttonBar.getButtons().addAll(btnClick,btnFlick);

        setBottom(hBox);
        setPadding( new Insets(10));

        btnClick.setOnAction(event -> {
            label.setText("Clicked !!");
        });

        btnFlick.setOnAction(event -> {
            label.setText("Flicked !!");
        });
    }
}
