package io.github.adyel.serverbrowser.ui.modules.hello;

import com.dlsc.workbenchfx.model.WorkbenchModule;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import io.github.adyel.serverbrowser.jpa.controller.MovieController;
import javafx.scene.Node;

public class HelloWorld extends WorkbenchModule {

  public HelloWorld() {
    super("Hello World", MaterialDesignIcon.HUMAN_GREETING);
//    MovieController movieController = new MovieController();
//    movieController.sampleTest();
  }

  @Override
  public Node activate() {
    return new HelloWorldView();
  }
}
