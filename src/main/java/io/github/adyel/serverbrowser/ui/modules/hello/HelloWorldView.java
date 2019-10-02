package io.github.adyel.serverbrowser.ui.modules.hello;

import io.github.adyel.serverbrowser.jpa.controller.MovieController;
import io.github.adyel.serverbrowser.util.ApplicationContextUtils;
import io.github.adyel.serverbrowser.util.Parser;
import io.github.adyel.serverbrowser.web.scraper.Scraper;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import org.controlsfx.control.Notifications;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

class HelloWorldView extends BorderPane {

  private Label label = new Label("Nothing Here");

  private final MovieController movieController =
      ApplicationContextUtils.getApplicationContext().getBean(MovieController.class);

  HelloWorldView() {
    setCenter(label);

    Button btnClick = new Button("Update");
    Button btnFlick = new Button("Flick");
    Region spacer = new Region();

    HBox hBox = new HBox(btnClick, spacer, btnFlick);
    hBox.setSpacing(10);
    HBox.setHgrow(spacer, Priority.ALWAYS);

    ButtonBar buttonBar = new ButtonBar();
    buttonBar.getButtons().addAll(btnClick, btnFlick);

    setBottom(hBox);
    setPadding(new Insets(10));

    btnClick.setOnAction(
        event -> {
          label.setText("Clicked !!");
          updateMovieList();
        });

    btnFlick.setOnAction(
        event -> {
          label.setText("Flicked !!");
          movieController.deleteAll();
        });
  }

  private void updateMovieList() {
    List<String> urlList = new ArrayList<>();

    StringBuilder currentURL =
        new StringBuilder("http://ftp2.circleftp.net/FILE--SERVER/English%20Movies/XXXX");

    for (int i = 1996; i < 2020; i++) {
      currentURL.replace(currentURL.length() - 4, currentURL.length(), i + "");
      urlList.add(currentURL.toString());
    }

    Notifications.create()
        .title("Starting Update")
        .text("Fetching new List from the server")
        .showInformation();

    Notifications notifyUpdateComplete = Notifications.create();
    notifyUpdateComplete.title("Update Complete").text("Added all entries");

    Thread mainThread = Thread.currentThread();

    Flux.fromIterable(urlList)
        .subscribeOn(Schedulers.elastic())
        .map(Scraper::of)
        .flatMapIterable(Scraper::scrap)
        .map(Parser::of)
        .map(Parser::parse)
        .subscribe(movieController::save, Throwable::printStackTrace, () -> Platform.runLater(notifyUpdateComplete::showInformation));
  }
}