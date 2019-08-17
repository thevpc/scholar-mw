package net.vpc.scholar.hadruwaves.mom;

import net.vpc.common.mon.ProgressMonitor;
import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.vpc.scholar.hadrumaths.util.dump.Dumpable;
import net.vpc.scholar.hadrumaths.util.dump.Dumper;
import net.vpc.scholar.hadruwaves.*;
import net.vpc.scholar.hadruwaves.mom.sources.Sources;
import net.vpc.scholar.hadruwaves.mom.sources.modal.ModalSources;
import net.vpc.scholar.hadruwaves.mom.str.ModeInfoComparator;

import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.List;

/**
 * Created by vpc on 3/16/15.
 */
public interface ModeFunctions extends Cloneable, Serializable, Dumpable {
    void invalidateCache();

    int length();

    int size();

    int count();

    ModeInfo mode(ModeInfo index);

    ModeInfo getMode(ModeInfo index);

    ModeInfo mode(int n);

    ModeInfo getMode(int n);

    boolean isHintEnableFunctionProperties();

    void setHintEnableFunctionProperties(boolean disableDefaultFunctionProperties);

    int getMaxSize();

    ModeFunctions setMaxSize(int maxSize);

    /**
     * @param maxSize
     * @return
     */
    @Deprecated
    ModeFunctions setSize(int maxSize);

    TList<Expr> list();

    DoubleToVector get(int index);

    DoubleToVector apply(int index);

    TList<Expr> toList();

    DoubleToVector[] toArray();

    DoubleToVector[] arr();

    DoubleToVector[] fn();

    Complex[] zn();

    Complex[] yn();

    DoubleToVector fn(int index);

    Complex zn(int index);

    Complex yn(int index);

    ModeInfo[] getVanishingModes();

    ModeInfo[] getPropagatingModes();

    ModeFunctions clone();

    ModeType[] getAllowedModes();

    ModeType[] getAvailableModes();

    double getFrequency();

    ModeFunctions setFrequency(double frequency);

    int getPropagativeModesCount();

    ModeInfo getMode(ModeType mode, int m, int n);

    ModeInfo[] getModes();

    List<ModeInfo> getModes(ModeType mode);

    List<ModeInfo> getModes(ModeType mode, ProgressMonitor monitor);

    List<DoubleToVector> getFunctions(ModeType mode, ProgressMonitor monitor);

    ModeInfo[] getModes(ProgressMonitor monitor);

    double getCutoffFrequency(ModeIndex i);

    BoxSpace getFirstBoxSpace();

    ModeFunctions setFirstBoxSpace(BoxSpace firstBoxSpace);

    BoxSpace getSecondBoxSpace();

    ModeFunctions setSecondBoxSpace(BoxSpace secondBoxSpace);

    ModeFunctions setHintInvariance(Axis invariantAxis);

    Axis getHintInvariantAxis();

    AxisXY getHintSymmetry();

    ModeFunctions setHintSymmetry(AxisXY symmetryAxis);

    Domain getDomain();

    ModeFunctions setDomain(Domain domain);

    double getK0();

    double getOmega();

    HintAxisType getHintAxisType();

    ModeFunctions setHintAxisType(HintAxisType hintAxisType);

    ModalSources getSources();

    ModeFunctions setSources(Sources sources);

    ModeType[] getHintFnModes();

    ModeFunctions setHintFnModes(ModeType... hintFnModeTypes);

    Dumper getDumpStringHelper();

    String dump();

    boolean isComplex();

    WallBorders getBorders();

    ModeInfoFilter[] getModeInfoFilters();

    ModeIndexFilter[] getModeIndexFilters();

    ModeFunctions removeModeInfoFilter(ModeInfoFilter modeInfoFilter);

    ModeFunctions removeModeIndexFilter(ModeIndexFilter modeIndexFilter);

    ModeFunctions addModeInfoFilter(ModeInfoFilter modeInfoFilter);

    ModeFunctions addModeIndexFilter(ModeIndexFilter modeIndexFilter);

    StrLayer[] getLayers();

    ModeFunctions setLayers(StrLayer[] couches);

    boolean isHintInvertTETMForZmode();

    @Deprecated
    ModeFunctions setHintInvertTETMForZmode(boolean hintInvertTETMForZmode);

    ModeIteratorFactory getModeIteratorFactory();

    ModeFunctions setModeIteratorFactory(ModeIteratorFactory modeIteratorFactory);

    ModeInfoComparator getModeInfoComparator();

    void setModeInfoComparator(ModeInfoComparator modeInfoComparator);

    void addPropertyChangeListener(PropertyChangeListener listener);

    void removePropertyChangeListener(PropertyChangeListener listener);

    void addPropertyChangeListener(String property, PropertyChangeListener listener);

    void removePropertyChangeListener(String property, PropertyChangeListener listener);

    TVector<Complex> scalarProduct(Expr testFunction);

    TMatrix<Complex> scalarProduct(TList<Expr> testFunctions, ProgressMonitor monitor);

    void setObjectCacheResolver(ObjectCacheResolver cacheResolver);
}
