package net.thevpc.scholar.hadruwaves.mom;

import net.thevpc.common.mon.ProgressMonitorFactory;
import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadruwaves.WallBorders;
import net.thevpc.scholar.hadruwaves.mom.sources.Sources;

import java.beans.PropertyChangeListener;

public interface ModeFunctionsEnv {

    ProgressMonitorFactory getMonitorFactory();

    BoxSpace getFirstBoxSpace();

    WallBorders getBorders();

    BoxSpace getSecondBoxSpace();

    Domain getDomain();

    Sources getSources();

    double getFrequency();

    StrLayer[] getLayers();

    void addPropertyChangeListener(PropertyChangeListener listener);

    void removePropertyChangeListener(PropertyChangeListener listener);

    void addPropertyChangeListener(String property, PropertyChangeListener listener);

    void removePropertyChangeListener(String property, PropertyChangeListener listener);

    <T> T getHint(String name, T defaultValue) ;

}
