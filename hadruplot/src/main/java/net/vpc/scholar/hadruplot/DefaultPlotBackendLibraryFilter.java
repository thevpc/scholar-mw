package net.vpc.scholar.hadruplot;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by vpc on 1/3/17.
 */
public class DefaultPlotBackendLibraryFilter implements PlotBackendLibraryFilter{
    private Set<String> all=new HashSet<>();
    boolean reject=false;
    boolean acceptAll=false;
    public DefaultPlotBackendLibraryFilter(String value) {
        value=value==null?"":value.trim();
        if(value.isEmpty()){
            reject=true;
        }else {
            Boolean rejectCustom = null;
            for (String s : value.split("[,;|]")) {
                if (s.startsWith("!")) {
                    if (rejectCustom == null || rejectCustom == true) {
                        reject = true;
                        rejectCustom = true;
                        s = s.substring(1);
                        all.add(s.toLowerCase());
                    } else {
                        throw new IllegalArgumentException("Cannot both reject and accept libraries");
                    }
                } else {
                    if (rejectCustom == null || rejectCustom == false) {
                        reject = false;
                        rejectCustom = false;
                        all.add(s.toLowerCase());
                    } else {
                        throw new IllegalArgumentException("Cannot both reject and accept libraries");
                    }
                }
            }
        }
    }

    @Override
    public boolean accept(PlotLibrary library) {
        return reject==!all.contains(library.getName().toLowerCase());
    }
}
