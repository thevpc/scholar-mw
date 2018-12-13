package net.vpc.scholar.hadrumaths.plot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PlotModelList extends BasePlotModel implements Iterable<PlotModel> {
    private List<PlotModel> values = new ArrayList<>();

    public PlotModelList(String title) {
        setTitle(title);
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
