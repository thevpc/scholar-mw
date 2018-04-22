package net.vpc.scholar.hadruwaves.mom;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.cache.ObjectCache;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.vpc.scholar.hadrumaths.util.ProgressMonitor;
import net.vpc.scholar.hadrumaths.util.dump.Dumper;
import net.vpc.scholar.hadruwaves.*;
import net.vpc.scholar.hadruwaves.mom.sources.Sources;
import net.vpc.scholar.hadruwaves.mom.sources.modal.ModalSources;
import net.vpc.scholar.hadruwaves.mom.str.ModeInfoComparator;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

public class ModeFunctionsDelegate implements ModeFunctions {
    private ObjectCacheResolver objectCacheResolver;
    private ModeFunctions base;
    private MomStructure structure;
    private PropertyChangeSupport pcs;
    PropertyChangeListener dispatcher = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            pcs.firePropertyChange(evt);
        }
    };


    public ModeFunctionsDelegate(ModeFunctions base) {
        pcs = new PropertyChangeSupport(this);
        setBase(base);
    }

    @Override
    public void setObjectCacheResolver(ObjectCacheResolver cacheResolver) {
        this.objectCacheResolver=objectCacheResolver;
        if(base!=null){
            base.setObjectCacheResolver(objectCacheResolver);
        }
    }

    public MomStructure getStructure() {
        return structure;
    }

    public void setStructure(MomStructure structure) {
        MomStructure old = this.structure;
        this.structure = structure;
    }

    public ModeFunctions getBase() {
        return base;
    }

    public void setBase(ModeFunctions base) {
        ModeFunctions old = this.base;

        if(old!=null){
            old.removePropertyChangeListener(dispatcher);
        }
        this.base = base;
        if(this.base!=null){
            this.base.setObjectCacheResolver(objectCacheResolver);
            this.base.addPropertyChangeListener(dispatcher);
        }
    }

    public void invalidateCache() {
        base.invalidateCache();
    }

    public int length() {
        return base.length();
    }

    public int size() {
        return base.size();
    }

    public int count() {
        return base.count();
    }

    public ModeInfo mode(ModeInfo index) {
        return base.mode(index);
    }

    public ModeInfo getMode(ModeInfo index) {
        return base.getMode(index);
    }

    public ModeInfo mode(int n) {
        return base.mode(n);
    }

    public ModeInfo getMode(int n) {
        return base.getMode(n);
    }

    public boolean isHintEnableFunctionProperties() {
        return base.isHintEnableFunctionProperties();
    }

    public void setHintEnableFunctionProperties(boolean disableDefaultFunctionProperties) {
        base.setHintEnableFunctionProperties(disableDefaultFunctionProperties);
    }

    public int getMaxSize() {
        return base.getMaxSize();
    }

    public ModeFunctions setMaxSize(int maxSize) {
        return base.setMaxSize(maxSize);
    }

    @Override
    public ModeFunctions setSize(int maxSize) {
        return base.setSize(maxSize);
    }

    public TList<Expr> list() {
        return base.list();
    }

    public DoubleToVector get(int index) {
        return base.get(index);
    }

    public DoubleToVector apply(int index) {
        return base.apply(index);
    }

    public TList<Expr> toList() {
        return base.toList();
    }

    public DoubleToVector[] toArray() {
        return base.toArray();
    }

    public DoubleToVector[] arr() {
        return base.arr();
    }

    public DoubleToVector[] fn() {
        return base.fn();
    }

    public Complex[] zn() {
        return base.zn();
    }

    public Complex[] yn() {
        return base.yn();
    }

    public DoubleToVector fn(int index) {
        return base.fn(index);
    }

    public Complex zn(int index) {
        return base.zn(index);
    }

    public Complex yn(int index) {
        return base.yn(index);
    }

    public ModeInfo[] getVanishingModes() {
        return base.getVanishingModes();
    }

    public ModeInfo[] getPropagatingModes() {
        return base.getPropagatingModes();
    }

    public ModeFunctions clone() {
        return base.clone();
    }

    public ModeType[] getAllowedModes() {
        return base.getAllowedModes();
    }

    public ModeType[] getAvailableModes() {
        return base.getAvailableModes();
    }

    public double getFrequency() {
        return base.getFrequency();
    }

    public ModeFunctions setFrequency(double frequency) {
        return base.setFrequency(frequency);
    }

    public int getPropagativeModesCount() {
        return base.getPropagativeModesCount();
    }

    public ModeInfo getMode(ModeType mode, int m, int n) {
        return base.getMode(mode, m, n);
    }

    public ModeInfo[] getModes() {
        return base.getModes();
    }

    public List<ModeInfo> getModes(ModeType mode) {
        return base.getModes(mode);
    }

    public List<ModeInfo> getModes(ModeType mode, ProgressMonitor monitor) {
        return base.getModes(mode, monitor);
    }

    public List<DoubleToVector> getFunctions(ModeType mode, ProgressMonitor monitor) {
        return base.getFunctions(mode, monitor);
    }

    public ModeInfo[] getModes(ProgressMonitor monitor) {
        return base.getModes(monitor);
    }

    public double getCutoffFrequency(ModeIndex i) {
        return base.getCutoffFrequency(i);
    }

    public BoxSpace getFirstBoxSpace() {
        return base.getFirstBoxSpace();
    }

    public ModeFunctions setFirstBoxSpace(BoxSpace firstBoxSpace) {
        return base.setFirstBoxSpace(firstBoxSpace);
    }

    public BoxSpace getSecondBoxSpace() {
        return base.getSecondBoxSpace();
    }

    public ModeFunctions setSecondBoxSpace(BoxSpace secondBoxSpace) {
        return base.setSecondBoxSpace(secondBoxSpace);
    }

    public ModeFunctions setHintInvariance(Axis invariantAxis) {
        return base.setHintInvariance(invariantAxis);
    }

    public Axis getHintInvariantAxis() {
        return base.getHintInvariantAxis();
    }

    public AxisXY getHintSymmetry() {
        return base.getHintSymmetry();
    }

    public ModeFunctions setHintSymmetry(AxisXY symmetryAxis) {
        return base.setHintSymmetry(symmetryAxis);
    }

    public Domain getDomain() {
        return base.getDomain();
    }

    public ModeFunctions setDomain(Domain domain) {
        return base.setDomain(domain);
    }

    public double getK0() {
        return base.getK0();
    }

    public double getOmega() {
        return base.getOmega();
    }

    public HintAxisType getHintAxisType() {
        return base.getHintAxisType();
    }

    public ModeFunctions setHintAxisType(HintAxisType hintAxisType) {
        return base.setHintAxisType(hintAxisType);
    }

    public ModalSources getSources() {
        return base.getSources();
    }

    public ModeFunctions setSources(Sources sources) {
        return base.setSources(sources);
    }

    public ModeType[] getHintFnModes() {
        return base.getHintFnModes();
    }

    public ModeFunctions setHintFnModes(ModeType... hintFnModeTypes) {
        return base.setHintFnModes(hintFnModeTypes);
    }

    public Dumper getDumpStringHelper() {
        return base.getDumpStringHelper();
    }

    public String dump() {
        return base.dump();
    }

    public boolean isComplex() {
        return base.isComplex();
    }

    public WallBorders getBorders() {
        return base.getBorders();
    }

    public ModeInfoFilter[] getModeInfoFilters() {
        return base.getModeInfoFilters();
    }

    public ModeIndexFilter[] getModeIndexFilters() {
        return base.getModeIndexFilters();
    }

    public ModeFunctions removeModeInfoFilter(ModeInfoFilter modeInfoFilter) {
        return base.removeModeInfoFilter(modeInfoFilter);
    }

    public ModeFunctions removeModeIndexFilter(ModeIndexFilter modeIndexFilter) {
        return base.removeModeIndexFilter(modeIndexFilter);
    }

    public ModeFunctions addModeInfoFilter(ModeInfoFilter modeInfoFilter) {
        return base.addModeInfoFilter(modeInfoFilter);
    }

    public ModeFunctions addModeIndexFilter(ModeIndexFilter modeIndexFilter) {
        return base.addModeIndexFilter(modeIndexFilter);
    }

    public StrLayer[] getLayers() {
        return base.getLayers();
    }

    public ModeFunctions setLayers(StrLayer[] couches) {
        return base.setLayers(couches);
    }

    public boolean isHintInvertTETMForZmode() {
        return base.isHintInvertTETMForZmode();
    }

    public ModeFunctions setHintInvertTETMForZmode(boolean hintInvertTETMForZmode) {
        return base.setHintInvertTETMForZmode(hintInvertTETMForZmode);
    }

    public ModeIteratorFactory getModeIteratorFactory() {
        return base.getModeIteratorFactory();
    }

    public ModeFunctions setModeIteratorFactory(ModeIteratorFactory modeIteratorFactory) {
        return base.setModeIteratorFactory(modeIteratorFactory);
    }

    public ModeInfoComparator getModeInfoComparator() {
        return base.getModeInfoComparator();
    }

    public void setModeInfoComparator(ModeInfoComparator modeInfoComparator) {
        base.setModeInfoComparator(modeInfoComparator);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    public void addPropertyChangeListener(String property, PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(property, listener);
    }

    public void removePropertyChangeListener(String property, PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(property, listener);
    }

    public TVector<Complex> scalarProduct(Expr testFunction) {
        return base.scalarProduct(testFunction);
    }

    public TMatrix<Complex> scalarProduct(TList<Expr> testFunctions, ProgressMonitor monitor) {
        return base.scalarProduct(testFunctions, monitor);
    }

}
