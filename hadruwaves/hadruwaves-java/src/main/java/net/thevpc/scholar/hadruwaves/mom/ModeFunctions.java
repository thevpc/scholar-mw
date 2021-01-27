package net.thevpc.scholar.hadruwaves.mom;

import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.thevpc.scholar.hadruwaves.*;
import net.thevpc.scholar.hadruwaves.mom.str.ModeInfoComparator;

import java.beans.PropertyChangeListener;
import java.util.List;

/**
 * Created by vpc on 3/16/15.
 */
public interface ModeFunctions extends Cloneable, HSerializable {
    ModeFunctions invalidateCache();

    int length();

    int size();

    int count();

    ModeInfo mode(ModeInfo index);

    ModeInfo getMode(ModeInfo index);

    ModeInfo mode(int n);

    ModeInfo getMode(int n);

    boolean isHintEnableFunctionProperties();

    ModeFunctions setHintEnableFunctionProperties(boolean disableDefaultFunctionProperties);

    int getSize();

    ModeFunctions setSize(int maxSize);

    HintAxisType getHintAxisType();

    ModeFunctions setHintAxisType(HintAxisType hintAxisType);

    ModeFunctions setHintInvariance(Axis invariantAxis);

    Axis getHintInvariantAxis();

    AxisXY getHintSymmetry();

    ModeFunctions setHintSymmetry(AxisXY symmetryAxis);

    ModeType[] getHintFnModes();

    ModeFunctions setHintFnModes(ModeType... hintFnModeTypes);

    Vector<Expr> list();

    DoubleToVector get(int index);

    DoubleToVector apply(int index);

    Vector<Expr> toList();

    DoubleToVector[] toArray();

    DoubleToVector[] arr();

    DoubleToVector[] arr(ProgressMonitor monitor);

    DoubleToVector[] fn();

    DoubleToVector[] fn(ProgressMonitor monitor);

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

    int getPropagativeModesCount();

    ModeInfo getMode(ModeType mode, int m, int n);

    ModeInfo[] getModes();

    List<ModeInfo> getModes(ModeType mode);

    List<ModeInfo> getModes(ModeType mode, ProgressMonitor monitor);

    List<DoubleToVector> getFunctions(ModeType mode, ProgressMonitor monitor);

    ModeInfo[] getModes(ProgressMonitor monitor);

    double getCutoffFrequency(ModeIndex i);

    boolean isComplex();

//    Dumper getDumpStringHelper();

    ModeInfoFilter[] getModeInfoFilters();

    ModeIndexFilter[] getModeIndexFilters();

    ModeFunctions removeModeInfoFilter(ModeInfoFilter modeInfoFilter);

    ModeFunctions removeModeIndexFilter(ModeIndexFilter modeIndexFilter);

    ModeFunctions addModeInfoFilter(ModeInfoFilter modeInfoFilter);

    ModeFunctions addModeIndexFilter(ModeIndexFilter modeIndexFilter);

    boolean isHintInvertTETMForZmode();

    @Deprecated
    ModeFunctions setHintInvertTETMForZmode(boolean hintInvertTETMForZmode);

    ModeIterator getModeIterator();

    ModeFunctions setModeIterator(ModeIterator modeIterator);

    ModeInfoComparator getModeComparator();

    ModeFunctions setModeComparator(ModeInfoComparator modeInfoComparator);

    ModeFunctions addPropertyChangeListener(PropertyChangeListener listener);

    PropertyChangeListener[] getPropertyChangeListeners();

    PropertyChangeListener[] getPropertyChangeListeners(String property);

    ModeFunctions removePropertyChangeListener(PropertyChangeListener listener);

    ModeFunctions addPropertyChangeListener(String property, PropertyChangeListener listener);

    ModeFunctions removePropertyChangeListener(String property, PropertyChangeListener listener);

    ComplexVector scalarProduct(Expr testFunction, ProgressMonitor monitor);

    ComplexMatrix scalarProduct(Vector<Expr> testFunctions, ProgressMonitor monitor);

    ModeFunctions setObjectCacheResolver(ObjectCacheResolver cacheResolver);

    ModeFunctionsEnv getEnv();

    ModeFunctions setEnv(ModeFunctionsEnv env);

    Axis getPolarization();
}
