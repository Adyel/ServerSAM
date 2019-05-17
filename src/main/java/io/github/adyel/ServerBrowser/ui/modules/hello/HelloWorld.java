package io.github.adyel.ServerBrowser.ui.modules.hello;

import com.dlsc.workbenchfx.model.WorkbenchModule;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.scene.Node;

public class HelloWorld extends WorkbenchModule {

    public HelloWorld() {
        super("Hello World", MaterialDesignIcon.HUMAN_GREETING);
    }

    @Override
  public Node activate() {
    return new HelloWorldView();
  }
}
