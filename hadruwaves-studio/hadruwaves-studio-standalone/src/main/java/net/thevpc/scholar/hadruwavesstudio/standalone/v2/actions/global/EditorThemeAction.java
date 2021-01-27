/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwavesstudio.standalone.v2.actions.global;

import net.thevpc.echo.AbstractAppAction;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.HadruwavesStudio;

import java.awt.event.ActionEvent;

/**
 *
 * @author vpc
 */
public class EditorThemeAction extends AbstractAppAction {
    
    private HadruwavesStudio studio;
    private String theme;

    public EditorThemeAction(HadruwavesStudio studio, String theme) {
        super(studio.app(), "EditorTheme_" + theme);
        this.studio=studio;
        this.theme = theme;
    }

    @Override
    public void actionPerformedImpl(ActionEvent e) {
        studio.editorThemes().id().set(theme);
    }
        @Override
    public void refresh() {

    }

}
