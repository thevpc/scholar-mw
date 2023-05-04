package net.thevpc.scholar.hadruplot.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.thevpc.scholar.hadruplot.DefaultPlotValue;
import net.thevpc.scholar.hadruplot.PlotValue;
import net.thevpc.scholar.hadruplot.model.value.PlotValueModelType;

public class PlotModelList extends BasePlotModel implements Iterable<PlotModel> {

    private List<PlotModel> values = new ArrayList<>();
    private PlotValue value = new DefaultPlotValue(PlotValueModelType.INSTANCE.getName() + "[]", values);

    public PlotModelList(String title) {
        setTitle(title);
    }

//    @Override
    public PlotValue getPlotValue() {
        return value;
    }

    public void add(PlotModel other) {
        if (other != null) {
            values.add(other);
        }
    }

    @Override
    public Iterator<PlotModel> iterator() {
        return values.iterator();
    }

    public int size() {
        return values.size();
    }
}
