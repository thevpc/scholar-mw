package net.vpc.scholar.hadruwaves.studio.standalone.v2.win;

import net.vpc.common.swings.app.impl.swing.PListComponentModel;
import net.vpc.scholar.hadruwaves.studio.standalone.v2.HadruwavesStudioV2;
import org.jdesktop.swingx.JXList;

import javax.swing.*;
import java.awt.*;

public class HWSProjectHistoryTool extends JPanel {
    private HadruwavesStudioV2 application;

    public HWSProjectHistoryTool(HadruwavesStudioV2 application) {
        super(new BorderLayout());
        this.application = application;
        JXList view = new JXList(new PListComponentModel<String>(application.getApplication().history().description()));
        add(new JScrollPane(view));
    }

}
