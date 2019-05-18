package io.github.adyel.ServerBrowser.ui.modules.table;

import com.dlsc.workbenchfx.model.WorkbenchModule;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.scene.Node;

public class TableModule extends WorkbenchModule {
    public TableModule() {
        super("TableModule", MaterialDesignIcon.TABLE);
    }

    @Override
    public Node activate() {
        return new TableModuleView();
    }
}
