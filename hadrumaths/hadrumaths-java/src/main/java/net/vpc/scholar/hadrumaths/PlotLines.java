package net.vpc.scholar.hadrumaths;

import java.util.*;

public class PlotLines {
    private Map<String,Map<Double,Complex>> values=new LinkedHashMap<>();
    private Set<Double> xvalues=new TreeSet<>();

    public void addLine(String name){
        getLineMap(name);
    }

    private Map<Double, Complex> getLineMap(String name){
        Map<Double, Complex> doubleComplexMap = values.get(name);
        if(doubleComplexMap==null){
            doubleComplexMap=new HashMap<>();
            values.put(name,doubleComplexMap);
        }
        return doubleComplexMap;
    }

    public void addValue(String name,double x,Complex y){
        xvalues.add(x);
        getLineMap(name).put(x,y);
    }

    public TList<String> titles(){
        TList<String> names=Maths.slist(values.size());
        names.appendAll(values.keySet());
        return names;
    }

    public TList<Double> xsamples(){
        TList<Double> xvals=Maths.dlist(xvalues.size());
        xvals.appendAll(xvalues);
        return xvals;
    }

    public TList<TList<Complex>> getValues(){
        TList<TList<Complex>> all=new ArrayTList<TList<Complex>>(Maths.$CLIST,true,values.size());
        Map<String,Set<Integer>> zerosMap=new LinkedHashMap<>();
        for (Map.Entry<String, Map<Double, Complex>> e : values.entrySet()) {
            TList<Complex> t=new ArrayTList<Complex>(Maths.$COMPLEX,false,xvalues.size());
//            Complex lastYvalue=Complex.ZERO;
            int index=0;
            Set<Integer> zeros = new HashSet<>();
            for (Double xvalue : xvalues) {
                Map<Double, Complex> value = e.getValue();
                Complex yvalue = value.get(xvalue);
                if(yvalue==null){
                    yvalue=Complex.ZERO;
                    zeros.add(index);
                }
                t.append(yvalue);
//                lastYvalue=yvalue;
                index++;
            }
            if(!zeros.isEmpty()) {
                for (Integer zeroIndex : zeros) {
                    if (zeroIndex > 0 && zeroIndex + 1 < t.size() && !zeros.contains(zeroIndex - 1) && !zeros.contains(zeroIndex + 1)) {
                        t.set(zeroIndex, t.get(zeroIndex - 1).add(t.get(zeroIndex + 1)).div(2));
//                        t.set(zeroIndex, Complex.valueOf(3000));
                    }
                }
                zerosMap.put(e.getKey(),zeros);
            }
            all.append(t);
        }
        return all;
    }
}
