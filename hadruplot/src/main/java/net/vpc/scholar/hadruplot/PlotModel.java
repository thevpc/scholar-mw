package net.vpc.scholar.hadruplot;

import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.Set;

/**
 * Created by vpc on 6/4/17.
 */
public interface PlotModel extends Serializable {

    String getColorPalette();

    PlotModel setColorPalette(String colorPalette);

    boolean accept(PlotModelType t);

    Set<PlotModelType> getModelTypes();

    PlotModel1D toPlotModel1D();

    PlotModel2D toPlotModel2D();

    PlotModel3D toPlotModel3D();

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
