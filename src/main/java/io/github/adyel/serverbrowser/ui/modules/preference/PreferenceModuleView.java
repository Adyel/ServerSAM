package io.github.adyel.serverbrowser.ui.modules.preference;

import com.dlsc.preferencesfx.PreferencesFx;
import com.dlsc.preferencesfx.model.Category;
import com.dlsc.preferencesfx.model.Group;
import com.dlsc.preferencesfx.model.Setting;
import com.dlsc.preferencesfx.view.PreferencesFxView;
import com.uwetrottmann.tmdb2.entities.BaseMovie;
import io.github.adyel.serverbrowser.jpa.model.Movie;
import java.util.function.Function;
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

  Function<BaseMovie, Movie> toMovieModel =
      baseMovie ->
          Movie.builder().title(baseMovie.title).releaseDate(baseMovie.release_date).build();
}
