package net.vpc.scholar.hadrumaths.plot.component;

import net.vpc.scholar.hadrumaths.Axis;
import net.vpc.scholar.hadrumaths.CharactersTable;
import net.vpc.scholar.hadrumaths.plot.model.AxisVectorPlotModel;
import net.vpc.scholar.hadruplot.*;
import net.vpc.scholar.hadruplot.model.PlotModel;

import javax.swing.*;
import java.awt.*;
import java.util.function.ToDoubleFunction;

public class AxisVectorPlotPanel extends BasePlotComponent implements PlotPanel {
    private AxisVectorPlotModel model;
    @Override
    public void setModel(PlotModel model) {
        this.model=(AxisVectorPlotModel) model;
    }

    @Override
    public boolean accept(PlotModel model) {
        return model instanceof AxisVectorPlotModel;
    }

    public AxisVectorPlotPanel(AxisVectorPlotModel model, PlotWindowManager windowManager) {
        super(new GridLayout(1,-1));
        setModel(model);
        add(create(model,0));
        add(create(model,1));
        add(create(model,2));
    }

    private JComponent create(AxisVectorPlotModel model, int index){
        PlotBuilder builder = Plot.display(false);
        LibraryPlotType plotType = model.getPlotType();
        if(plotType!=null) {
            builder.plotType(plotType.getType());
            builder.setLibrary(plotType.getLibrary());
        }
        ToDoubleFunction<Object> converter = model.getConverter();
        if(converter!=null){
            builder.converter(converter);
        }
        String title=model.getTitle();
        String title2="";
        switch (model.getVector().getCoordinates()){
            case CARTESIAN:{
                title2=Axis.cartesian(index).name()+" Axis";
                break;
            }
            case SPHERICAL:{
                switch (index){
                    case 0:{
                        title2=("R Axis");
                        break;
                    }
                    case 1:{
                        title2=(CharactersTable.THETA+" Axis");
                        break;
                    }
                    case 2:{
                        title2=(CharactersTable.PHI+" Axis");
                        break;
                    }
                }
                break;
            }
            case CYLINDRICAL:{
                switch (index){
                    case 0:{
                        title2=("R Axis");
                        break;
                    }
                    case 1:{
                        title2=(CharactersTable.THETA+" Axis");
                        break;
                    }
                    case 2:{
                        title2=("Z Axis");
                        break;
                    }
                }
                break;
            }
        }
        builder.title(title2);
//        builder.title(model.getTitle()+" ");
        PlotComponent c1 = builder.plot(model.getVector().get(index));
        JComponent jComponent = c1.toComponent();
//        jComponent.setBorder(BorderFactory.createTitledBorder(title2));

        return jComponent;
    }

    @Override
    public PlotModel getModel() {
        return model;
    }
}
