package net.vpc.scholar.hadruwaves.mom;

import net.vpc.common.mon.ProgressMonitorFactory;
import net.vpc.scholar.hadrumaths.Axis;
import net.vpc.scholar.hadrumaths.AxisXY;
import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadruwaves.ModeType;
import net.vpc.scholar.hadruwaves.WallBorders;
import net.vpc.scholar.hadruwaves.mom.sources.Sources;
import net.vpc.scholar.hadruwaves.mom.sources.modal.CutOffModalSources;
import net.vpc.scholar.hadruwaves.mom.sources.modal.ModalSources;
import net.vpc.scholar.hadruwaves.str.MomStructureHintsManager;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Arrays;
import java.util.HashMap;

public class DefaultModeFunctionsEnv implements ModeFunctionsEnv {
    //    private AbstractStructure2D structure;
    private StrLayer[] layers = new StrLayer[0];
    //    private ProjectType projectType;
    private BoxSpace firstBoxSpace = BoxSpaceFactory.nothing();
    private BoxSpace secondBoxSpace = BoxSpaceFactory.nothing();
    private ModalSources sources = new CutOffModalSources(1);
    /**
     * defaults to 1GGz
     */
    protected double frequency = Double.NaN;
    private PropertyChangeSupport pcs=new PropertyChangeSupport(this);
    public Domain domain;
    private WallBorders borders;
    private HashMap<String, Object> hints = new HashMap<>();
    private ProgressMonitorFactory monitorFactory;

    @Override
    public ProgressMonitorFactory getMonitorFactory() {
        return monitorFactory;
    }

    public DefaultModeFunctionsEnv setMonitorFactory(ProgressMonitorFactory monitorFactory) {
        this.monitorFactory = monitorFactory;
        return this;
    }

    public DefaultModeFunctionsEnv setBorders(WallBorders borders) {
        this.borders = borders;
        return this;
    }

    public DefaultModeFunctionsEnv setSecondBoxSpace(BoxSpace secondBoxSpace) {
        BoxSpace old = this.secondBoxSpace;
        this.secondBoxSpace = secondBoxSpace;
        firePropertyChange("secondBoxSpace", old, secondBoxSpace);
        return this;
    }

    public DefaultModeFunctionsEnv setFirstBoxSpace(BoxSpace firstBoxSpace) {
        BoxSpace old = this.firstBoxSpace;
        this.firstBoxSpace = firstBoxSpace;
        firePropertyChange("firstBoxSpace", old, firstBoxSpace);
        return this;
    }

    public DefaultModeFunctionsEnv setFrequency(double frequency) {
        double old = this.frequency;
        this.frequency = frequency;
        firePropertyChange("frequency", old, frequency);
        return this;
    }

    public DefaultModeFunctionsEnv setDomain(Domain domain) {
        Domain old = this.domain;
        this.domain = domain;
        firePropertyChange("domain", old, domain);
        return this;
    }

    public DefaultModeFunctionsEnv setSources(Sources sources) {
        Sources old = this.sources;
        this.sources = (sources == null || !(sources instanceof ModalSources)) ? new CutOffModalSources(1) : (ModalSources) sources;
        firePropertyChange("sources", old, this.sources);
        return this;
    }

    @Override
    public BoxSpace getFirstBoxSpace() {
        return firstBoxSpace;
    }

    public DefaultModeFunctionsEnv setLayers(StrLayer[] couches) {
        StrLayer[] old = this.layers == null ? null : Arrays.copyOf(this.layers, this.layers.length);
        if (couches == null) {
            this.layers = StrLayer.NO_LAYERS;
        } else {
            this.layers = new StrLayer[couches.length];
            for (int i = 0; i < couches.length; i++) {
                this.layers[i] = couches[i].clone();
            }
        }
        firePropertyChange("layers", old, layers);
        return this;
    }

    @Override
    public ModalSources getSources() {
        return sources;
    }

    @Override
    public BoxSpace getSecondBoxSpace() {
        return secondBoxSpace;
    }

    @Override
    public StrLayer[] getLayers() {
        return layers;
    }

    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        pcs.firePropertyChange(propertyName, oldValue, newValue);
    }

    @Override
    public double getFrequency() {
        return frequency;
    }

    @Override
    public WallBorders getBorders() {
        return borders;
    }

    //    }

    @Override
    public Domain getDomain() {
        return domain;
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    @Override
    public void addPropertyChangeListener(String property, PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(property, listener);
    }

    @Override
    public void removePropertyChangeListener(String property, PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(property, listener);
    }

    public DefaultModeFunctionsEnv setHint(String name, Object v) {
        if (v == null) {
            hints.remove(name);
        } else {
            hints.put(name, v);
        }
        return this;
    }

    @Override
    public <T> T getHint(String name, T defaultValue) {
        T t = (T) hints.get(name);
        if (t == null) {
            return defaultValue;
        }
        return t;
    }


    public DefaultModeFunctionsEnv setHintAxisType(HintAxisType hintAxisType){
        setHint(MomStructureHintsManager.HINT_AXIS_TYPE,hintAxisType);
        return this;
    }

    public DefaultModeFunctionsEnv setHintInvariance(Axis invariantAxis){
        setHint(MomStructureHintsManager.HINT_INVARIANCE,invariantAxis);
        return this;
    }

    public DefaultModeFunctionsEnv setHintSymmetry(AxisXY symmetryAxis){
        setHint(MomStructureHintsManager.HINT_SYMMETRY,symmetryAxis);
        return this;
    }

    public DefaultModeFunctionsEnv setHintFnModes(ModeType... hintFnModeTypes){
        setHint(MomStructureHintsManager.HINT_FN_MODE,hintFnModeTypes);
        return this;
    }

}
