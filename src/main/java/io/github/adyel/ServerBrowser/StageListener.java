package io.github.adyel.ServerBrowser;

import com.dlsc.workbenchfx.Workbench;
import io.github.adyel.ServerBrowser.ui.modules.hello.HelloWorld;
import io.github.adyel.ServerBrowser.ui.modules.preference.PreferenceModule;
import io.github.adyel.ServerBrowser.ui.modules.table.TableModule;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class StageListener implements ApplicationListener<StageReady> {

  @Override
  public void onApplicationEvent(StageReady event) {
    Stage stage = event.getStage();

    stage.setTitle("ServerBrowser");
    stage.setScene(new Scene(initWorkbench()));
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
