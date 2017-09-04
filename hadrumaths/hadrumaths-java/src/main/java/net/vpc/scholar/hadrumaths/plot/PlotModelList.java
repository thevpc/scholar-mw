package net.vpc.scholar.hadrumaths.plot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PlotModelList implements PlotModel , Iterable<PlotModel>{
    private List<PlotModel> values=new ArrayList<>();
    private String title;
    public PlotModelList(String title) {
        this.title=title;
    }

    public void add(PlotModel other){
        if(other!=null) {
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

    public String getTitle() {
        return title;
    }
}
