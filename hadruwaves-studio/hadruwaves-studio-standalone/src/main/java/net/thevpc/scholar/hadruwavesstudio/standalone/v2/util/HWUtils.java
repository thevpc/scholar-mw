package net.thevpc.scholar.hadruwavesstudio.standalone.v2.util;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.*;
import java.io.File;
import java.util.Map;

import net.thevpc.jeep.JType;
import net.thevpc.jeep.JTypes;
import net.thevpc.common.props.PropertyEvent;
import net.thevpc.common.props.PropertyListener;
import net.thevpc.echo.Application;
import net.thevpc.common.util.Tuple3;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.HadruwavesStudio;

public class HWUtils {

    private static Map<Object, Object> default0;

    public static Color getUIColor(Color defaultValue,String ...ids){
        UIDefaults d = UIManager.getDefaults();
        for (String id : ids) {
            Color c = d.getColor(id);
            if(c!=null){
                return c;
            }
        }
        return defaultValue;
    }

    public static void onLookChanged(HadruwavesStudio a, Runnable e) {
        UIManager.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("lookAndFeel".equals(evt.getPropertyName())) {
                    e.run();
                }
            }
        });
        a.app().iconSet().id().listeners().add(new PropertyListener() {
            @Override
            public void propertyUpdated(PropertyEvent event) {
                e.run();
            }
        });
        a.app().iconSet().id().listeners().add(new PropertyListener() {
            @Override
            public void propertyUpdated(PropertyEvent event) {
                e.run();
            }
        });
        a.editorThemes().id().listeners().add(new PropertyListener() {
            @Override
            public void propertyUpdated(PropertyEvent event) {
                e.run();
            }
        });
        a.editorThemes().usePlaf().listeners().add(new PropertyListener() {
            @Override
            public void propertyUpdated(PropertyEvent event) {
                e.run();
            }
        });

    }

    public static File[] getProjectFiles(File folder){
        if(folder==null){
            return new File[0];
        }
        File[] y = folder.listFiles(x -> x.getName().endsWith(".hwp.tson"));
        if(y==null){
            return new File[0];
        }
        return y;
    }

    public static File[] getSolutionFiles(File folder){
        if(folder==null){
            return new File[0];
        }
        File[] y = folder.listFiles(x -> x.getName().endsWith(".hws.tson"));
        if(y==null){
            return new File[0];
        }
        return y;
    }
}
