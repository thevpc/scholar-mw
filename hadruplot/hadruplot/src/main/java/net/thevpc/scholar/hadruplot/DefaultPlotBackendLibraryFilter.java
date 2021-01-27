package net.thevpc.scholar.hadruplot;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by vpc on 1/3/17.
 */
public class DefaultPlotBackendLibraryFilter implements PlotBackendLibraryFilter{
    private Set<String> rejected=new HashSet<>();
    private Set<String> accepted=new HashSet<>();
//    boolean reject=false;
//    boolean acceptAll=false;
    public DefaultPlotBackendLibraryFilter(String value) {
        value=value==null?"":value.trim();
        if(value.isEmpty()){
            //
        }else {
            Boolean rejectCustom = null;
            for (String s : value.split("[,;|]")) {
                if (s.startsWith("!")) {
                    s = s.substring(1);
                    rejected.add(s.toLowerCase());
                } else {
                    accepted.add(s.toLowerCase());
                }
            }
        }
    }

    @Override
    public boolean accept(PlotLibrary library) {
        String s=library.getName().toLowerCase();
        if(accepted.contains(s) && rejected.contains(s)){
            return false;
        }
        if(accepted.contains(s)){
            return true;
        }
        if(rejected.contains(s)){
            return false;
        }
        if(accepted.size()==0 && rejected.size()==0){
            return true;
        }
        if(accepted.size()>0 && rejected.size()==0){
            return false;
        }
        if(accepted.size()==0 && rejected.size()>0){
            return true;
        }
        return false;
    }
}
