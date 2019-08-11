package io.github.adyel.serverbrowser.ui.modules.hello;

import com.dlsc.workbenchfx.model.WorkbenchModule;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import io.github.adyel.serverbrowser.jpa.controller.MovieController;
import io.github.adyel.serverbrowser.util.ApplicationContextUtils;
import io.github.adyel.serverbrowser.util.Parser;
import java.util.Arrays;
import java.util.List;
import javafx.scene.Node;
import org.springframework.context.ApplicationContext;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class HelloWorld extends WorkbenchModule {

  public HelloWorld() {
    super("Info", MaterialDesignIcon.INFORMATION);
  }

  @Override
  public Node activate() {
    return new HelloWorldView();
  }
}
