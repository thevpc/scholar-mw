package net.thevpc.scholar.hadruplot.model;

import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import java.util.function.ToDoubleFunction;
import net.thevpc.scholar.hadruplot.LibraryPlotType;
import net.thevpc.scholar.hadruplot.PlotModelType;

/**
 * Created by vpc on 6/4/17.
 */
public interface PlotModel extends Serializable {

//    PlotValue getPlotValue();
    
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

    public PlotModel setPlotType(LibraryPlotType plotType);

    PlotModel setProperty(String key, Object value);

    PlotModel setProperties(Map<String, Object> other);

    PlotModel removeProperty(String key);

    Object getProperty(String key, Object defaultValue);

    Object getProperty(String key);

    PlotModel setProperty(int index, String key, Object value);

    Object getProperty(int index, String key);

    Object getProperty(int index, String key, Object defaultValue);

    ToDoubleFunction<Object> getConverter();

    PlotModel setConverter(ToDoubleFunction<Object> converter);

    Map<String, Object> getProperties();
}
