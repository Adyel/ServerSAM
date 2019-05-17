package io.github.adyel.ServerBrowser;

import com.dlsc.workbenchfx.Workbench;
import io.github.adyel.ServerBrowser.ui.modules.hello.HelloWorld;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class StageListener implements ApplicationListener<StageReady> {

    private HelloWorld helloWorld = new HelloWorld();

    @Override
    public void onApplicationEvent(StageReady event) {
        Stage stage = event.getStage();

        stage.setTitle("ServerBrowser");
        stage.setScene(new Scene(initWorkbench()));
        stage.centerOnScreen();
        stage.show();

    }

    private Workbench initWorkbench(){
        return Workbench.builder(
                helloWorld
        ).build();
    }
}
