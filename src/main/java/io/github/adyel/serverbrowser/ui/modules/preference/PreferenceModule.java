package io.github.adyel.serverbrowser.ui.modules.preference;

import com.dlsc.workbenchfx.model.WorkbenchModule;
import com.dlsc.workbenchfx.view.controls.ToolbarItem;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.scene.Node;

public class PreferenceModule extends WorkbenchModule {

  private PreferenceModuleView preferenceModuleView;

  public PreferenceModule() {
    super("Settings", MaterialDesignIcon.SETTINGS);
    preferenceModuleView = new PreferenceModuleView();

    ToolbarItem save =
        new ToolbarItem(
            new MaterialDesignIconView(MaterialDesignIcon.CONTENT_SAVE),
            event -> preferenceModuleView.save());

    ToolbarItem discard =
        new ToolbarItem(
            new MaterialDesignIconView(MaterialDesignIcon.DELETE),
            event -> preferenceModuleView.discardChange());

    getToolbarControlsLeft().addAll(save,discard);
  }

  @Override
  public Node activate() {
    return preferenceModuleView.getPreferenceFxView();
  }

  @Override
  public boolean destroy() {
    preferenceModuleView.save();
    return true;
  }
}
