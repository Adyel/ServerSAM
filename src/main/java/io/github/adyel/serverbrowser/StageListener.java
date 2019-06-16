package io.github.adyel.serverbrowser;

import com.dlsc.workbenchfx.Workbench;
import io.github.adyel.serverbrowser.ui.modules.hello.HelloWorld;
import io.github.adyel.serverbrowser.ui.modules.preference.PreferenceModule;
import io.github.adyel.serverbrowser.ui.modules.table.TableModule;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class StageListener implements ApplicationListener<StageReady> {

  @Override
  public void onApplicationEvent(StageReady event) {
    Stage stage = event.getStage();

//    String darkTheme = serverbrowser.class.getResource("darkTheme.css").toExternalForm();
//    System.out.println(darkTheme);

    stage.setTitle("serverbrowser");

    Workbench workbench = initWorkbench();
//    workbench.getStylesheets().add(darkTheme);

    stage.setScene(new Scene(workbench));
    stage.setWidth(600);
    stage.setHeight(800);
    stage.centerOnScreen();
    stage.show();
  }

  private Workbench initWorkbench() {
    return Workbench.builder(
        new HelloWorld(),
        new TableModule(),
        new PreferenceModule()
    ).build();
  }
}
