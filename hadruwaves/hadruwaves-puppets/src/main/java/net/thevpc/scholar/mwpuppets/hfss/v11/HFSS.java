/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.mwpuppets.hfss.v11;

import java.util.ArrayList;
import java.util.List;
import net.thevpc.common.strings.StringComparators;
import net.thevpc.scholar.mwpuppets.hfss.v11.win.HFSSMainWindow;
import net.thevpc.common.deskauto.DEWindow;
import net.thevpc.common.deskauto.DEWindowFilter;
import net.thevpc.common.deskauto.DEWindowFilterFactory;
import net.thevpc.common.deskauto.DesktopEnvironment;

/**
 *
 * @author vpc
 */
public class HFSS {

    private static HFSS instance;
    private DEWindowFilter mainWindowFilter = DEWindowFilterFactory.and(
            DEWindowFilterFactory.module(StringComparators.ilike("*hfss.exe*")), 
            DEWindowFilterFactory.not(DEWindowFilterFactory.child()), 
            DEWindowFilterFactory.title(StringComparators.like("Ansoft HFSS *3D Modeler*"))
    );

    public static HFSS getInstance() {
        if (instance == null) {
            instance = new HFSS();
        }
        return instance;
    }

    public HFSSMainWindow findMainWindow() {
        DEWindow win = DesktopEnvironment.getInstance().findSingleWindow(mainWindowFilter);
        return new HFSSMainWindow(win);
    }

    public List<HFSSWindow> findWindows() {
        List<HFSSWindow> all = new ArrayList<>();
        for (DEWindow d : DesktopEnvironment.getInstance().findWindows(DEWindowFilterFactory.module(StringComparators.ilike("*hfss.exe*")))) {
            all.add(createHFSSWindow(d));
        }
        return all;
    }

    public HFSSWindow createHFSSWindow(DEWindow w) {
        if (mainWindowFilter.accept(w)) {
            return new HFSSMainWindow(w);
        }
        return new HFSSWindow(w);
    }
}
