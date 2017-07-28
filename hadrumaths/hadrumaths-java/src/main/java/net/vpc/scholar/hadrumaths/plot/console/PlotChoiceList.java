package net.vpc.scholar.hadrumaths.plot.console;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.lang.reflect.Method;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime 17 janv. 2007 12:27:15
 */
public class PlotChoiceList implements Iterable<PlotChoice> {
    private LinkedHashMap<String, PlotChoice> map = new LinkedHashMap<String, PlotChoice>();
    private PlotConsoleProjectTemplate template;
    public PlotChoiceList(PlotConsoleProjectTemplate template) {
        this.template=template;
        java.lang.reflect.Method[] methods = template.getClass().getMethods();
        for (Method method : methods) {
            StudyMethod annotation = method.getAnnotation(StudyMethod.class);
            if (annotation != null) {
                if (method.getParameterTypes().length != 0) {
                    throw new IllegalArgumentException("StudyMethod should not have parameters");
                }
                add(method.getName());
            }
        }
    }

    public void add(String method, Object ... params) {
        add(new PlotChoiceMethod(template,method,params));
    }

    public PlotConsoleProjectTemplate getTemplate() {
        return template;
    }

    public void add(PlotChoice choice) {
        map.put(choice.getId(), choice);
    }

    public boolean isChosen(String id) {
        return map.get(id).isEnabled();
    }

    public Iterator<PlotChoice> iterator() {
        return map.values().iterator();
    }
    
    public int size(){
        return map.size();
    }
}
