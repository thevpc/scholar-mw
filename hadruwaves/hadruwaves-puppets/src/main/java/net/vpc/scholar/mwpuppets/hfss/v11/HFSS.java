/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.mwpuppets.hfss.v11;

import java.util.ArrayList;
import java.util.List;
import net.vpc.common.strings.StringComparators;
import net.vpc.scholar.mwpuppets.hfss.v11.win.HFSSMainWindow;
import net.vpc.common.deskauto.DEWindow;
import net.vpc.common.deskauto.DEWindowFilter;
import net.vpc.common.deskauto.DEWindowFilterFactory;
import net.vpc.common.deskauto.DesktopEnvironment;

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
