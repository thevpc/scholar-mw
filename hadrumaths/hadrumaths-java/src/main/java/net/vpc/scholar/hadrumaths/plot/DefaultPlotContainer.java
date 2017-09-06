package net.vpc.scholar.hadrumaths.plot;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DefaultPlotContainer extends AbstractPlotContainer implements PlotContainer {
    private PlotContainer actual;
    private JPanel panel;
    private List<PlotPropertyListener> listeners=new ArrayList<>();
    private PlotPropertyListener listenerDispatcher = new PlotPropertyListener() {
        @Override
        public void onPropertyChange(PlotPropertyEvent event) {
            for (PlotPropertyListener listener : listeners) {
                listener.onPropertyChange(event);
            }
        }

        @Override
        public void onRemoved(PlotEvent event) {
            for (PlotPropertyListener listener : listeners) {
                listener.onRemoved(event);
            }
        }
    };

    public DefaultPlotContainer() {
        super();
        panel=new JPanel(new BorderLayout());
        setActual(TabbedPlotWindowContainerFactory.INSTANCE.create());
    }

    @Override
    public void setPlotWindowManager(PlotWindowManager plotWindowManager) {
        super.setPlotWindowManager(plotWindowManager);
        actual.setPlotWindowManager(plotWindowManager);
    }


    @Override
    public PlotComponent getPlotComponent(int index) {
        if(actual==null){
            return null;
        }
        return actual.getPlotComponent(index);
    }

    @Override
    public int getPlotComponentsCount() {
        if(actual==null){
            return 0;
        }
        return actual.getPlotComponentsCount();
    }

    @Override
    public PlotContainer add(int index, String containerName) {
        if(actual==null){
            throw new IllegalArgumentException("Unsupported");
        }
        return actual.add(index,containerName);
    }

    @Override
    public void add(PlotComponent component) {
        if(actual==null){
            throw new IllegalArgumentException("Unsupported");
        }
        actual.add(component);
    }

    @Override
    public void display(PlotComponent component) {
        if(actual==null){
            throw new IllegalArgumentException("Unsupported");
        }
        actual.display(component);

    }

    @Override
    public void clear() {
        if(actual==null){
            throw new IllegalArgumentException("Unsupported");
        }
        actual.clear();
    }

    @Override
    public void remove(PlotComponent component) {
        if(actual==null){
            throw new IllegalArgumentException("Unsupported");
        }
        actual.remove(component);
    }

    @Override
    public PlotContainer add(String name) {
        if(actual==null){
            throw new IllegalArgumentException("Unsupported");
        }
        return actual.add(name);
    }

    @Override
    public String getPlotTitle() {
        if(actual==null){
            return null;
        }
        return actual.getPlotTitle();
    }

    @Override
    public String getLayoutConstraints() {
        if(actual==null){
            throw new IllegalArgumentException("Unsupported");
        }
        return actual.getLayoutConstraints();
    }

    @Override
    public JComponent toComponent() {
        return panel;
    }

    @Override
    public void display() {
        if(actual==null){
            throw new IllegalArgumentException("Unsupported");
        }
        actual.display();
    }

    protected void registerActual(PlotContainer a){
        if(a!=null) {
            a.addPlotPropertyListener(listenerDispatcher);
        }
    }

    protected void unregisterActual(PlotContainer a){
        if(a!=null) {
            a.removePlotPropertyListener(listenerDispatcher);
        }
    }

    protected void setActual(PlotContainer a){
        if(a!=this.actual) {
            if( a instanceof DefaultPlotContainer){
                throw new IllegalArgumentException("Unsupported");
            }
            unregisterActual(this.actual);
            this.actual = a;
            registerActual(this.actual);
            panel.add(this.actual.toComponent(),SwingConstants.CENTER);
        }
    }

    @Override
    public void addPlotPropertyListener(PlotPropertyListener listener) {
        if(listener!=null) {
            listeners.add(listener);
        }
    }

    @Override
    public void removePlotPropertyListener(PlotPropertyListener listener) {
        if(listener!=null) {
            listeners.remove(listener);
        }
    }

    @Override
    public void removePlotComponentImpl(PlotComponent component) {
        throw new IllegalArgumentException("Never called");
    }

    @Override
    public void addComponentImpl(PlotComponent component, int index) {
        throw new IllegalArgumentException("Never called");
    }

    @Override
    public int indexOfPlotComponent(PlotComponent plotComponent) {
        if(actual!=null){
            return actual.indexOfPlotComponent(plotComponent);
        }
        return -1;
    }
}
