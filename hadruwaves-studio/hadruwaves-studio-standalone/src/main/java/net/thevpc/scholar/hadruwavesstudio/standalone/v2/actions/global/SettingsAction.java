/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwavesstudio.standalone.v2.actions.global;

import java.awt.event.ActionEvent;
import net.thevpc.echo.Application;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.HadruwavesStudio;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.actions.HAction;

/**
 *
 * @author vpc
 */
public class SettingsAction extends HAction {

    private final HadruwavesStudio studio;

    public SettingsAction(Application aplctn, final HadruwavesStudio studio) {
        super(aplctn, "Settings");
        this.studio = studio;
    }

    @Override
    public void actionPerformedImpl(ActionEvent e) {
        studio.openSettings();
    }
    @Override
    public void refresh() {

    }

}
