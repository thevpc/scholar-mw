package net.vpc.scholar.hadrumaths.plot;

import java.beans.PropertyChangeListener;
import java.io.Serializable;

/**
 * Created by vpc on 6/4/17.
 */
public interface PlotModel extends Serializable {

    String getTitle();
    String getPreferredTitle();

    PlotModel setTitle(String title);

    String getName();

    PlotModel setName(String name);

    void addPropertyChangeListener(String property, PropertyChangeListener listener);

    void addPropertyChangeListener(PropertyChangeListener listener);

    void removePropertyChangeListener(PropertyChangeListener listener);

    void removePropertyChangeListener(String property, PropertyChangeListener listener);
}
