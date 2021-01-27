/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwaves.str;

import net.thevpc.tson.Tson;
import net.thevpc.tson.TsonElement;
import net.thevpc.tson.TsonObjectContext;
import net.thevpc.scholar.hadrumaths.Axis;
import net.thevpc.scholar.hadrumaths.AxisXY;
import net.thevpc.scholar.hadrumaths.HSerializable;
import net.thevpc.scholar.hadruwaves.ModeType;
import net.thevpc.scholar.hadruwaves.mom.HintAxisType;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author vpc
 */
public class MWStructureHintsManager implements Cloneable, HSerializable {

    private HashMap<String, Object> parameters = new HashMap<String, Object>();
    private PropertyChangeSupport changeSupport;

    public MWStructureHintsManager() {
        this.changeSupport = new PropertyChangeSupport(this);
    }

    public PropertyChangeListener[] getPropertyChangeListeners(){
        return this.changeSupport.getPropertyChangeListeners();
    }

    public PropertyChangeListener[] getPropertyChangeListeners(String property){
        return this.changeSupport.getPropertyChangeListeners(property);
    }

    public void addChangeListener(PropertyChangeListener changeListener) {
        this.changeSupport.addPropertyChangeListener(changeListener);
    }

    public void removeChangeListener(PropertyChangeListener changeListener) {
        this.changeSupport.addPropertyChangeListener(changeListener);
    }


//----------------------------------------------

    public void setHint(String name) {
        setHint(name, Boolean.TRUE);
    }

    public void setHintNotNull(String name, Object value) {
        if (value == null) {
            removeHint(name);
        } else {
            setHint(name, value);
        }
    }

    public void setHint(String name, Object value) {
        Object old = parameters.put(name, value);
        changeSupport.firePropertyChange(name, old, value);
    }

    public void removeHint(String name) {
        Object old = parameters.remove(name);
        changeSupport.firePropertyChange(name, old, null);
    }

    public Object getHint(String name) {
        return parameters.get(name);
    }

    public Object getHint(String name, Object defaultValue) {
        if (parameters.containsKey(name)) {
            return parameters.get(name);
        }
        return defaultValue;
    }


    public boolean containsHint(String name) {
        return parameters.containsKey(name);
    }

    public boolean isHint(String name, boolean defaultValue) {
        if (parameters.containsKey(name)) {
            return Boolean.TRUE.equals(parameters.get(name));
        } else {
            return defaultValue;
        }
    }

    public boolean isHint(String name) {
        return Boolean.TRUE.equals(parameters.get(name));
    }

    @Override
    public MWStructureHintsManager clone() {
        try {
            final MWStructureHintsManager copy = (MWStructureHintsManager) super.clone();
            copy.parameters = (HashMap<String, Object>) this.parameters.clone();
            return copy;
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(MWStructureHintsManager.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }

    @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        return Tson.obj(getClass().getSimpleName(),null, context.elem(parameters)).build();
    }

//    public Dumper getDumpStringHelper() {
//        Dumper helper = new Dumper(getClass().getSimpleName());
//        helper.add(parameters);
//        return helper;
//    }
//
//    public String dump() {
//        return getDumpStringHelper().toString();
//    }

    public boolean isEmpty() {
        return parameters.isEmpty();
    }

    public void setAll(MWStructureHintsManager all) {
        if (all != null) {
            for (Map.Entry<String, Object> e : all.parameters.entrySet()) {
                setHint(e.getKey(), e.getValue());
            }
        }
    }
}