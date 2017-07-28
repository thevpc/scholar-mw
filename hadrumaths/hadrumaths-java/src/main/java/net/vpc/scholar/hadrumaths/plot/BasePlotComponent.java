package net.vpc.scholar.hadrumaths.plot;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class BasePlotComponent extends JPanel implements PlotComponent{
    private List<PlotPropertyListener> listeners;

    public BasePlotComponent(LayoutManager layout, boolean isDoubleBuffered) {
        super(layout, isDoubleBuffered);
    }

    public BasePlotComponent(LayoutManager layout) {
        super(layout);
    }

    public BasePlotComponent(boolean isDoubleBuffered) {
        super(isDoubleBuffered);
    }

    public BasePlotComponent() {
    }

    @Override
    public void addPlotPropertyListener(PlotPropertyListener listener) {
        if(listeners==null){
            listeners=new ArrayList<>();
        }
        listeners.add(listener);
    }
    @Override
    public void removePlotPropertyListener(PlotPropertyListener listener) {
        if(listeners!=null){
            listeners.remove(listener);
        }
    }

    protected void firePlotPropertyEvent(String propertyName,Object oldValue,Object newValue){
        if(!Objects.equals(oldValue,newValue)){
            if(listeners!=null){
                PlotPropertyEvent event=null;
                for (PlotPropertyListener listener : listeners) {
                    if (event == null){
                        event = new PlotPropertyEvent(this, propertyName, oldValue, newValue);
                    }
                    listener.onPropertyChange(event);
                }
            }
        }
    }
}
