/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools;

import java.awt.BorderLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import net.thevpc.echo.Application;
import net.thevpc.scholar.hadruwaves.project.HWSolutionProcessor;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.HadruwavesStudio;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.util.HWUtils;

/**
 *
 * @author vpc
 */
public class AbstractToolWindowPanel extends JPanel {

    private final HadruwavesStudio studio;

    public AbstractToolWindowPanel(HadruwavesStudio studio) {
        super(new BorderLayout());
        this.studio = studio;
        HWUtils.onLookChanged(studio, () -> onLookChanged());
    }

    public void setContent(JComponent c) {
        add(c, BorderLayout.CENTER);
        onLookChanged();
    }

    protected void onLookChanged() {

    }

    /**
     * @return the studio
     */
    public HadruwavesStudio studio() {
        return studio;
    }

    /**
     * @return the studio
     */
    public Application app() {
        return studio().app();
    }

    /**
     * @return the studio
     */
    public HWSolutionProcessor proc() {
        return studio().proc();
    }

}
