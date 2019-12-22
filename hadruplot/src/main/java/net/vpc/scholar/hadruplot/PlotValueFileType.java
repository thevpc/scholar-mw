package net.vpc.scholar.hadruplot;


import java.io.File;

public class PlotValueFileType extends AbstractPlotValueType {
    public static final PlotValueType INSTANCE=new PlotValueFileType();
    public PlotValueFileType() {
        super("file");
    }

    public File toFile(Object o){
        return (File) o;
    }
    @Override
    public Object getValue(Object o) {
        return o;
    }
}
