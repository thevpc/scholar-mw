package net.vpc.scholar.hadruwaves.mom;

import net.vpc.scholar.hadrumaths.Axis;
import net.vpc.scholar.hadrumaths.AxisXY;
import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.cache.ObjectCache;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.vpc.scholar.hadrumaths.symbolic.ExprList;
import net.vpc.scholar.hadrumaths.util.ComputationMonitor;
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

    int getSize();

    ModeFunctions setSize(int fnMax);

    ExprList list();

    DoubleToVector get(int index);
    DoubleToVector apply(int index);

    ExprList toList();

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

    List<ModeInfo> getModes(ModeType mode, ComputationMonitor monitor);

    List<DoubleToVector> getFunctions(ModeType mode, ComputationMonitor monitor);

    ModeInfo[] getModes(ComputationMonitor monitor, ObjectCache cache);

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

    void addPropertyChangeListener(String property,PropertyChangeListener listener);

    void removePropertyChangeListener(String property,PropertyChangeListener listener);
}
