package io.github.adyel.ServerBrowser.ui.modules.preference;

import com.dlsc.preferencesfx.PreferencesFx;
import com.dlsc.preferencesfx.model.Category;
import com.dlsc.preferencesfx.model.Group;
import com.dlsc.preferencesfx.model.Setting;
import com.dlsc.preferencesfx.view.PreferencesFxView;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

class PreferenceModuleView {
  private PreferencesFx preferencesFx;

  private BooleanProperty darkMode = new SimpleBooleanProperty(false);

  PreferenceModuleView(){
    preferencesFx = createPreference();
  }

  private PreferencesFx createPreference() {
    return PreferencesFx.of(
        PreferenceModule.class,
        Category.of("User Interface",
            Group.of("Themes",
                Setting.of("Dark Mode", darkMode)
            )
        )
    ).persistWindowState(false).saveSettings(true).debugHistoryMode(false).buttonsVisibility(true);
  }

  void save(){
    preferencesFx.saveSettings();
  }

  void discardChange(){
    preferencesFx.discardChanges();
  }

  PreferencesFxView getPreferenceFxView(){
    return preferencesFx.getView();
  }

  public boolean isDarkMode() {
    return darkMode.get();
  }
}
