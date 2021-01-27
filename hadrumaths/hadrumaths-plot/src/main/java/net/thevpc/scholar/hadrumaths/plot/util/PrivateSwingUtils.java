package net.thevpc.scholar.hadrumaths.plot.util;

import sun.swing.DefaultLookup;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;

public class PrivateSwingUtils {
    public static Color getDefaultColor(JComponent c, ComponentUI ui,String name){
        Color col=DefaultLookup.getColor(c, ui, name);
        return col;
    }
}
