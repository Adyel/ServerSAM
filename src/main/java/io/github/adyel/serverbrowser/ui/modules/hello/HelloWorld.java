package io.github.adyel.serverbrowser.ui.modules.hello;

import com.dlsc.workbenchfx.model.WorkbenchModule;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import io.github.adyel.serverbrowser.jpa.controller.MovieController;
import io.github.adyel.serverbrowser.util.ApplicationContextUtils;
import javafx.scene.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class HelloWorld extends WorkbenchModule {

  public HelloWorld() {
    super("Hello World", MaterialDesignIcon.HUMAN_GREETING);

    ApplicationContext applicationContext = ApplicationContextUtils.getApplicationContext();
    MovieController movieController = applicationContext.getBean(MovieController.class);
    movieController.sampleTest();
  }

  @Override
  public Node activate() {
    return new HelloWorldView();
  }
}
