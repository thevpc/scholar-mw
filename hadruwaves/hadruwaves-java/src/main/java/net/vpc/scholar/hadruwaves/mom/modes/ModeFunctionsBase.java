package net.vpc.scholar.hadruwaves.mom.modes;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.cache.ObjectCache;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.vpc.scholar.hadrumaths.symbolic.ExprList;
import net.vpc.scholar.hadrumaths.util.*;
import net.vpc.scholar.hadruwaves.*;
import net.vpc.scholar.hadruwaves.mom.*;

import static net.vpc.scholar.hadrumaths.Maths.I;
import static net.vpc.scholar.hadrumaths.Maths.cotanh;
import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.util.dump.Dumper;
//import net.vpc.scholar.tmwlib.mom.ProjectType;
import net.vpc.scholar.hadruwaves.mom.sources.Sources;
import net.vpc.scholar.hadruwaves.mom.sources.modal.CutOffModalSources;
import net.vpc.scholar.hadruwaves.mom.sources.modal.ModalSources;
import net.vpc.scholar.hadruwaves.mom.str.ModeInfoComparator;

import static net.vpc.scholar.hadrumaths.Maths.inv;
import static net.vpc.scholar.hadrumaths.Maths.EPS0;
import static net.vpc.scholar.hadruwaves.Physics.K0;
import static net.vpc.scholar.hadrumaths.Maths.U0;
import static net.vpc.scholar.hadruwaves.Physics.omega;

import static java.lang.Math.max;
import static java.lang.Math.min;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.*;

import static net.vpc.scholar.hadrumaths.Expressions.isSymmetric;
import static net.vpc.scholar.hadrumaths.Expressions.isXSymmetric;
import static net.vpc.scholar.hadrumaths.Expressions.isYSymmetric;

/**
 * FnBaseFunctions must be initialized fnBaseFunctions.setDomain(getDomain());
 * fnBaseFunctions.setFnMax(getModeFunctionsCount());
 * fnBaseFunctions.setHintFnModeTypes(getHintFnModeTypes());
 * fnBaseFunctions.setFirstBoxSpace(getFirstBoxSpace());
 * fnBaseFunctions.setSecondBoxSpace(getSecondBoxSpace());
 * fnBaseFunctions.setFrequency(getFrequency());
 * fnBaseFunctions.setProjectType(getProjectType());
 * fnBaseFunctions.setPropagativeModeSelector(getSources());
 * fnBaseFunctions.setHintGpFnAxisType(getHintAxisType());
 *
 * @author vpc
 */
public abstract class ModeFunctionsBase implements net.vpc.scholar.hadruwaves.mom.ModeFunctions {

    protected int size = Integer.MIN_VALUE;
    /**
     * defaults to 1GGz
     */
    protected double frequency = Double.NaN;
    public Domain domain;
    protected ModeIteratorFactory modeIteratorFactory = new DefaultModeIteratorFactory();
    protected double cachedk0;
    protected double cachedOmega;
    protected ModeInfo[] cachedModesEvanescents;
    protected ModeInfo[] cachedModesPropagating;
    private ModeInfo[] cachedIndexes;
    private List<ModeInfo>[] cachedIndexesByModeType=new List[ModeType.values().length];
    private volatile DoubleToVector[] cachedFn = null;
    private volatile Complex[] cachedZn = null;
    private volatile Complex[] cachedYn = null;
    protected int cachedPropagatingModesCount = -1;
    //    private AbstractStructure2D structure;
    private HintAxisType hintAxisType = HintAxisType.XY;
    private Axis hintInvariantAxis = null;
    private AxisXY hintSymmetryAxis = null;
    private StrLayer[] layers = new StrLayer[0];
//    private ProjectType projectType;
    private BoxSpace firstBoxSpace = BoxSpaceFactory.nothing();
    private BoxSpace secondBoxSpace = BoxSpaceFactory.nothing();
    private ModalSources sources = new CutOffModalSources(1);
    private boolean complex = true;
    private WallBorders borders;
    private List<ModeInfoFilter> modeInfoFilters;
    private List<ModeIndexFilter> modeIndexFilters;
    private static ModeInfoFilter[] MODE_INFO_FILTER_0 = new ModeInfoFilter[0];
    private static ModeIndexFilter[] MODE_INDEX_FILTER_0 = new ModeIndexFilter[0];
    private boolean hintInvertTETMForZmode;
    private ModeInfoComparator modeInfoComparator= CutoffModeComparator.INSTANCE;
    private boolean enableDefaultFunctionProperties;
    private PropertyChangeSupport pcs;

    public ModeFunctionsBase(boolean complex, WallBorders borders) {
        pcs=new PropertyChangeSupport(this);
        this.complex = complex;
        this.borders = borders;
    }
    public ModeFunctionsBase() {
        pcs=new PropertyChangeSupport(this);
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
        pcs.addPropertyChangeListener(property,listener);
    }

    @Override
    public void removePropertyChangeListener(String property, PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(property,listener);
    }

    @Override
    public void invalidateCache() {
        cachedk0 = Double.NaN;
        cachedOmega = Double.NaN;
        cachedIndexes = null;
        cachedFn = null;
        cachedZn = null;
        cachedYn = null;
        cachedModesEvanescents = null;
        cachedModesPropagating = null;
        cachedPropagatingModesCount = -1;
    }

    public boolean isHintEnableFunctionProperties() {
        return enableDefaultFunctionProperties;
    }



    protected abstract DoubleToVector getFnImpl(ModeIndex i);
    //    public Complex firstBoxSpaceGamma(int n) {
//        return getGammaImpl(cachedIndexes[n]);

    //    }
    public abstract Complex getGammaImpl(ModeIndex i, BoxSpace bs);

    /**
     * @param i
     * @return
     */
    public Complex getImpedanceImpl(ModeInfo i) {
//        if (getProjectType().equals(ProjectType.WAVE_GUIDE)) {
//            switch (i.mode) {
//                case TM: {
//                    return i.firstBoxSpaceGamma.divide(I(cachedOmega * EPS0));
//                }
//                case TE: {
//                    return I(cachedOmega * U0).divide(i.firstBoxSpaceGamma);
//                }
//                case TEM: {
//                    //I(U0).divide(EPS0)
//                    return new Complex(U0 / EPS0).sqrt();
//                }
//            }
//        }
        //getStructure().getProjectType().equals(ProjectType.PLANAR_STRUCTURE)
        Complex ys = Complex.ZERO;
        BoxSpace[] spaces = new BoxSpace[]{getFirstBoxSpace(), getSecondBoxSpace()};
        Complex gamma;
        ModeType mode = i.mode.mtype;
        if (isHintInvertTETMForZmode()) {
            switch (mode) {
                case TE: {
                    mode = ModeType.TM;
                    break;
                }
                case TM: {
                    mode = ModeType.TE;
                    break;
                }
            }

        }
        switch (mode) {
            case TM: {
                for (int j = 0; j < spaces.length; j++) {
                    BoxSpace space = spaces[j];
                    gamma = j == 0 ? i.firstBoxSpaceGamma : i.secondBoxSpaceGamma;
                    Complex y = Complex.ZERO;
                    // TODO are you sure?????????????
                    switch (space.getLimit()) {
                        case OPEN: {
//                            y = I(cachedOmega).mul(EPS0 * componentVectorSpace.getEpsr()).mul(cotanh(firstBoxSpaceGamma.mul(componentVectorSpace.getWidth()))).div(firstBoxSpaceGamma);
                            y = I(cachedOmega).mul(EPS0 * space.getEpsr()).div(cotanh(gamma.mul(space.getWidth()))).div(gamma);
                            break;
                        }
                        case MATCHED_LOAD: {
                            y = I(cachedOmega).mul(EPS0 * space.getEpsr()).div(gamma);
                            break;
                        }
                        case SHORT: {
//                            y = I.mul(cachedOmega).mul(EPS0 * componentVectorSpace.getEpsr()).div(cotanh(firstBoxSpaceGamma.mul(componentVectorSpace.getWidth()))).div(firstBoxSpaceGamma);
                            y = I.mul(cachedOmega).mul(EPS0 * space.getEpsr()).mul(cotanh(gamma.mul(space.getWidth()))).div(gamma);
                            break;
                        }
                        case NOTHING: {
                            //nothing;
                            break;
                        }
                    }
                    ys = ys.add(y);
                }
                break;
            }
            case TE: {
                for (int j = 0; j < spaces.length; j++) {
                    BoxSpace space = spaces[j];
                    gamma = j == 0 ? i.firstBoxSpaceGamma : i.secondBoxSpaceGamma;
                    Complex y = Complex.ZERO;
                    switch (space.getLimit()) {
                        case OPEN: {
                            y = gamma.div(cotanh(gamma.mul(space.getWidth())).mul(I.mul(cachedOmega).mul(U0)));
                            break;
                        }
                        case MATCHED_LOAD: {
                            y = gamma.div(Complex.I.mul(cachedOmega).mul(U0));
                            break;
                        }
                        case SHORT: {
                            y = gamma.mul(cotanh(gamma.mul(space.getWidth()))).div(I.mul(cachedOmega).mul(U0));
                            break;
                        }
                        case NOTHING: {
                            //nothing;
                            break;
                        }
                    }
                    ys = ys.add(y);
                }
                break;
            }
            case TEM: {
                return I(U0).div(EPS0);
            }
            default: {
                throw new IllegalArgumentException("Unknown Mode " + i.mode.mtype);
            }
        }
        for (StrLayer couche : layers) {
            ys = ys.add(couche.impedance);
        }

        Complex z = ys.inv();
        if (z.isNaN()) {
            System.out.println("Zmod(" + i + ") is NaN.");
        }
        return z;
    }

    @Override
    public int length() {
        return count();
    }

    @Override
    public int size() {
        return count();
    }

    @Override
    public int count() {
        return getModes().length;
    }

    @Override
    public ModeInfo getMode(ModeInfo index) {
        if (index.index < 0) {
            return getMode(index.mode.mtype, index.mode.m, index.mode.n);
        }
        return getModes()[index.index];
    }

    @Override
    public ModeInfo getMode(int n) {
        return getModes()[n];
    }

    @Override
    public ModeInfo mode(ModeInfo index) {
        return getMode(index);
    }

    @Override
    public ModeInfo mode(int n) {
        return getModes()[n];
    }

    //    public void setStructure(AbstractStructure2D structure) {
//        this.structure = structure;
//        this.domain = structure.getDomain();
//        this.sources = structure.getNbSources();
//        this.freq = structure.getF();
//        this.sources = structure.getSources();
//        setFnMax(structure.getModeFunctionsCount());
//        invalidateCache();
//    }
    //    private void setDomain(Domain domain) {
//        this.domain = domain;
//        invalidateCache();
//    }
//
//    private void setSources(int sources) {
//        this.sources = sources;
//        invalidateCache();
//    }
//
//    private void setFrequency(double freq) {
//        this.freq = freq;
//        omega = omega(this.freq);
//        K0 = K0(this.freq);// omega * Math.sqrt(U0 * EPS0);//nombre d'onde
//        invalidateCache();
    //    }
    @Override
    public int getSize() {
        return size;
    }


    @Override
    public synchronized DoubleToVector apply(int index) {
        return arr()[index];
    }

    @Override
    public synchronized DoubleToVector get(int index) {
        return arr()[index];
    }

    @Override
    public synchronized ExprList list() {
        return new ArrayExprList(arr());
    }

    @Override
    public synchronized ExprList toList() {
        return list();
    }

    @Override
    public synchronized DoubleToVector[] toArray() {
        return arr();
    }


    @Override
    public synchronized DoubleToVector[] fn() {
        return arr();
    }

    @Override
    public DoubleToVector fn(int index) {
        return fn()[index];
    }

    @Override
    public Complex zn(int index) {
        return zn()[index];
    }

    @Override
    public Complex yn(int index) {
        return yn()[index];
    }

    @Override
    public synchronized DoubleToVector[] arr() {
        if (cachedFn != null) {
            return cachedFn;
        }
        ModeInfo[] ind = getModes();
        DoubleToVector[] fn = new DoubleToVector[ind.length];
        for (int i = 0; i < fn.length; i++) {
            fn[i] = ind[i].fn;
        }
        cachedFn = fn;
        return cachedFn;
    }

    @Override
    public double getCutoffFrequency(ModeIndex i) {
        return 0;
    }

    @Override
    public synchronized Complex[] zn() {
        if (cachedZn != null) {
            return cachedZn;
        }
        ModeInfo[] ind = getModes();
        Complex[] fn = new Complex[ind.length];
        for (int i = 0; i < fn.length; i++) {
            fn[i] = ind[i].impedance;
        }
        cachedZn = fn;
        return cachedZn;
    }

    @Override
    public synchronized Complex[] yn() {
        if (cachedYn != null) {
            return cachedYn;
        }
        ModeInfo[] ind = getModes();
        Complex[] fn = new Complex[ind.length];
        for (int i = 0; i < fn.length; i++) {
            fn[i] = ind[i].impedance.inv();
        }
        cachedYn = fn;
        return cachedYn;
    }
    //
//    public CFunctionXY2D fn(int n) {
//        return fn()[n];
//    }

    //    public Complex zn(int n) {
//        if (cachedZn == null) {
//            FnIndexes[] ind=getModes();
//            Complex[] zn = new Complex[ind.length];
//            for (int i = 0; i < ind.length; i++) {
//                zn[i] = getImpedanceImpl(ind[i]);
//            }
//            cachedZn = zn;
//        }
//        return cachedZn[n];
    //    }
    public static String toStr(int[] x) {
        StringBuilder sb = new StringBuilder("[").append(x.length).append("]{");
        for (int i = 0; i < min(x.length, 3); i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append(x[i]);
        }
        if (x.length > 3) {
            sb.append("...");
        }
        for (int i = max(x.length - 3, 3); i < x.length; i++) {
            if (i > max(x.length - 3, 3)) {
                sb.append(",");
            }
            sb.append(x[i]);
        }
        sb.append("}");
        return sb.toString();
    }

    @Override
    public ModeInfo[] getVanishingModes() {
        if (cachedModesEvanescents == null) {
            ArrayList<ModeInfo> i = new ArrayList<ModeInfo>(count());
            ModeInfo[] indexes = getModes();
            int max = indexes.length;
            for (int n = 0; n < max; n++) {
                if (!indexes[n].propagating) {
                    i.add(indexes[n]);
                }
            }
            cachedModesEvanescents = i.toArray(new ModeInfo[i.size()]);
        }
        return cachedModesEvanescents;
    }

    @Override
    public ModeInfo[] getPropagatingModes() {
        if (cachedModesPropagating == null) {
            ModeInfo[] indexes = getModes();
            int max = indexes.length;
            ArrayList<ModeInfo> i = new ArrayList<ModeInfo>(max + 1);
            for (int n = 0; n < max; n++) {
                if (indexes[n].propagating) {
                    i.add(indexes[n]);
                }
            }
            cachedModesPropagating = i.toArray(new ModeInfo[i.size()]);
        }
        return cachedModesPropagating;
    }
    //    public boolean isPropagative(ModeInfo i) {
//        return i.initialIndex < sources;
//    }

    //
    @Override
    public ModeFunctionsBase clone() {
        try {
            ModeFunctionsBase functions = (ModeFunctionsBase) super.clone();
            functions.setLayers(functions.layers);
            functions.modeInfoFilters = null;
            if (this.modeInfoFilters != null) {
                for (ModeInfoFilter modeInfoFilter : this.modeInfoFilters) {
                    functions.addModeInfoFilter(modeInfoFilter);
                }
            }
            functions.modeIndexFilters = null;
            if (this.modeIndexFilters != null) {
                for (ModeIndexFilter modeIndexFilter : this.modeIndexFilters) {
                    functions.addModeIndexFilter(modeIndexFilter);
                }
            }
            functions.invalidateCache();
            return functions;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Modes that are allowed by the geometry
     *
     * @return allowed modes
     */
    @Override
    public ModeType[] getAllowedModes() {
        return ModeType.values();
    }

    /**
     * intersection between AllowedModes and hintFnModes
     *
     * @return AvailableModes
     */
    @Override
    public ModeType[] getAvailableModes() {
        List<ModeType> available = new ArrayList<ModeType>(Arrays.asList(getAllowedModes()));
        ModeType[] hintFnModes = getHintFnModes();
        if ((hintFnModes != null && hintFnModes.length > 0)) {
            available.retainAll(new TreeSet<ModeType>(Arrays.asList(hintFnModes)));
        }
        return available.toArray(new ModeType[available.size()]);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    @Override
    public double getFrequency() {
        return frequency;
    }

    @Override
    public int getPropagativeModesCount() {
        if (cachedPropagatingModesCount < 0) {
            cachedPropagatingModesCount = getSources().getSourceCountForDimensions(this);
        }
        return cachedPropagatingModesCount;
    }

    protected abstract ModeInfo[] getIndexesImpl(ComputationMonitor par0);

    protected boolean doAcceptModeIndex(ModeIndex o) {
        if (modeIndexFilters != null) {
            for (ModeIndexFilter modeIndexFilter : modeIndexFilters) {
                if (!modeIndexFilter.acceptModeIndex(o)) {
                    System.out.println("modeIndexFilter rejected " + o);
                    return false;
                }
            }
        }
//        Mode[] fnModeTypes = getHintFnModeTypes();
//        if (fnModeTypes != null && fnModeTypes.length > 0) {
//            boolean modeFound = false;
//            for (Mode fnModeType : fnModeTypes) {
//                if (o.mode.equals(fnModeType)) {
//                    modeFound = true;
//                    break;
//                }
//            }
//            if (!modeFound) {
//                return false;
//            }
//        }
        return true;
    }

    protected boolean doAcceptModeInfo(ModeInfo index) {
        if (index.fn.isZero()) {
            //System.out.println(index+" rejected : zero");
            return false;
        }
        Axis axisIndep = getHintInvariantAxis();
        AxisXY axisSymm = getHintSymmetry();
        if (modeInfoFilters != null || axisIndep != null || axisSymm != null) {
            boolean doAddIt = true;
            if (axisIndep != null) {
                if (!index.fn.isInvariant(axisIndep)) {
                    System.out.println(index + " rejected : not invariant " + axisIndep);
                    return false;
                }
            }
            if (axisSymm != null) {
                switch (axisSymm) {
                    case X: {
                        if (!isSymmetric(index.fn,axisSymm)) {
                            System.out.println(index + " rejected : not symm " + axisSymm);
                            return false;
                        }
                    }
                }
            }
            if (doAddIt && modeInfoFilters != null) {
                for (ModeInfoFilter fil : modeInfoFilters) {
                    if (!fil.acceptModeInfo(index)) {
                        System.out.println(index + " rejected : invalid for " + fil);
                        return false;
                    }

                }
            }
        }
        return true;
    }

    @Override
    public ModeInfo getMode(ModeType mode, int m, int n) {
        ModeInfo[] indexes = getModes();
        for (ModeInfo index : indexes) {
            if (index.mode.mtype.equals(mode) && index.mode.m == m && index.mode.n == n) {
                return index;
            }
        }
        return null;
    }

    @Override
    public synchronized ModeInfo[] getModes() {
        return getModes(ComputationMonitorFactory.none(),null);
    }

    protected void a() {
//        if (getProjectType() == null) {
//            throw new IllegalArgumentException("Invalid ProjectType value");
//        }
        if (getFirstBoxSpace() == null) {
            throw new IllegalArgumentException("Invalid FirstBoxSpace value");
        }
        if (getSecondBoxSpace() == null) {
            throw new IllegalArgumentException("Invalid SecondBoxSpace value");
        }
        if (getSize() <= 0) {
            throw new IllegalArgumentException("Invalid Size value");
        }
        double f = getFrequency();
        if (Double.isNaN(f) || Double.isInfinite(f)) {
            throw new IllegalArgumentException("Invalid Frequency value");
        }
        if (domain == null || !domain.isValid()) {
            throw new IllegalArgumentException("Invalid Domain value");
        }
        cachedOmega = omega(f);
        cachedk0 = K0(f);
    }

    @Override
    public synchronized List<ModeInfo> getModes(final ModeType mode) {
        return getModes(mode,null);
    }

    @Override
    public synchronized List<ModeInfo> getModes(final ModeType mode, ComputationMonitor monitor) {
        int modeOrdinal = mode.ordinal();
        List<ModeInfo> modeInfos = cachedIndexesByModeType[modeOrdinal];
        if(modeInfos ==null){
            cachedIndexesByModeType[modeOrdinal]=modeInfos=Collections.unmodifiableList(CollectionsUtils.filter(Arrays.asList(getModes(monitor,null)), new CollectionFilter<ModeInfo>() {
                @Override
                public boolean accept(ModeInfo modeInfo, int baseIndex, Collection<ModeInfo> list) {
                    return modeInfo.mode.mtype == mode;
                }
            }));
        }
        return modeInfos;
    }

    @Override
    public synchronized List<DoubleToVector> getFunctions(final ModeType mode, ComputationMonitor monitor) {
        return CollectionsUtils.convert(getModes(mode, monitor), new Converter<ModeInfo, DoubleToVector>() {
            @Override
            public DoubleToVector convert(ModeInfo modeInfo) {
                return modeInfo.fn;
            }
        });
    }

    @Override
    public synchronized ModeInfo[] getModes(ComputationMonitor monitor,ObjectCache objectCache) {
        monitor=ComputationMonitorFactory.enhance(monitor);
        if (cachedIndexes == null) {
            if(objectCache!=null){
                try {
                    cachedIndexes = (ModeInfo[]) objectCache.load("mode-functions", null);
                }catch (Exception ex){
                    //ignore
                }
                if(cachedIndexes!=null){
                    return cachedIndexes;
                }
            }
            if(objectCache==null){
                System.out.println("Are you sure you want to load modes with no cache for "+this);
            }
            a();
            /*
             * if (getProjectType().equals(ProjectType.WAVE_GUIDE) &&
             * (!BoxLimit.CHARGE_ADAPTEE.equals(getFirstBoxSpace().limit) ||
             * !BoxLimit.CHARGE_ADAPTEE.equals(getSecondBoxSpace().limit))) {
             * throw new IllegalArgumentException("For waveguide projects limits
             * should be charge adaptee");
             }
             */

            EnhancedComputationMonitor[] mons = ComputationMonitorFactory.split(monitor, 2);
            ModeInfo[] _cachedIndexes = getIndexesImpl(mons[0]);
            Comparator<ModeInfo> comparator = getModeInfoComparator();
            if(comparator!=null) {
                Arrays.sort(_cachedIndexes, comparator);
            }
            int _cacheSize = _cachedIndexes.length;
            for (int i = 0; i < _cacheSize; i++) {
                _cachedIndexes[i].index = i;
            }
            ModeIndex[] propagativeModes = sources.getPropagatingModes(this, _cachedIndexes, getPropagativeModesCount());
            HashSet<ModeIndex> propagativeModesSet = new HashSet<ModeIndex>(Arrays.asList(propagativeModes));
            EnhancedComputationMonitor mon2=mons[1];//ComputationMonitorFactory.createIncrementalMonitor(mons[1], _cacheSize);
            String message = toString() + ", evaluate mode properties";
            String str = toString();
            Maths.invokeMonitoredAction(mon2, message, new VoidMonitoredAction() {
                @Override
                public void invoke(EnhancedComputationMonitor monitor, String messagePrefix) throws Exception {
                    int propagativeCount = 0;
                    int nonPropagativeCount = 0;
                    boolean _enableDefaultFunctionProperties = isHintEnableFunctionProperties();
                    for (int i = 0; i < _cacheSize; i++) {
                        ModeInfo index = _cachedIndexes[i];
                        index.propagating = propagativeModesSet.contains(index.getMode());
                        if (index.propagating) {
                            propagativeCount++;
                        } else {
                            nonPropagativeCount++;
                        }
                        if(_enableDefaultFunctionProperties) {
                            HashMap<String, Object> properties = new HashMap<String, Object>(15);
                            properties.put("Type", str);
                            properties.put("Mode", index.mode.mtype.toString());
                            properties.put("m", index.mode.m);
                            properties.put("n", index.mode.n);
                            properties.put("CutOffFrequency", Maths.formatFrequency(index.cutOffFrequency));
                            properties.put("Zmn", index.impedance);
                            properties.put("Gamma First Space", index.firstBoxSpaceGamma);
                            properties.put("Gamma Second Space", index.secondBoxSpaceGamma);
                            properties.put("InitialIndex", index.initialIndex);
                            properties.put("index", index.index);
                            properties.put("invariance", (index.fn.isInvariant(Axis.X) ? "X" : "") + (index.fn.isInvariant(Axis.Y) ? "Y" : ""));
                            properties.put("Domain", domain.toString());
                            properties.put("index", index.index);
                            properties.put("symmetry", (isXSymmetric(index.fn)?"X":"")+(isYSymmetric(index.fn)?"Y":""));
                            properties.put("propagating", index.propagating);
                            index.fn=(DoubleToVector) index.fn.setProperties(properties);
                        }
                        mon2.setProgress(i, _cacheSize, message);
                    }
                    if (propagativeCount == 0) {
                        System.err.println("propagatingCount = " + propagativeCount);
                    }
                    if (nonPropagativeCount == 0) {
                        System.err.println("nonPropagatingCount = " + propagativeCount);
                    }
                }
            });

            cachedIndexes = _cachedIndexes;

            if(objectCache!=null){
                try {
                    objectCache.store("mode-functions", cachedIndexes,monitor);
                }catch (Exception ex){
                    //ignore
                }
            }
        }
        return cachedIndexes;
    }

    protected void fillIndex(ModeInfo index, int initialIndex) {
        BoxSpace space1 = getFirstBoxSpace();
        BoxSpace space2 = getSecondBoxSpace();
        index.initialIndex = initialIndex;
        index.cutOffFrequency = getCutoffFrequency(index.mode);
        index.firstBoxSpaceGamma = getGammaImpl(index.mode, space1);
        if (index.firstBoxSpaceGamma.isNaN() || index.firstBoxSpaceGamma.isInfinite() || index.firstBoxSpaceGamma.equals(Complex.ZERO)) {
            System.err.println("[Warning] firstBoxSpaceGamma=" + index.firstBoxSpaceGamma + " for " + index.mode.mtype + index.mode.m + "," + index.mode.n + (" (f=" + frequency + ")"));
            getGammaImpl(index.mode, space1);
        }
        index.secondBoxSpaceGamma = getGammaImpl(index.mode, space2);
        if (index.secondBoxSpaceGamma.isNaN() || index.secondBoxSpaceGamma.isInfinite() || index.secondBoxSpaceGamma.equals(Complex.ZERO)) {
            System.err.println("[Warning] secondBoxSpaceGamma=" + index.secondBoxSpaceGamma + " for " + index.mode.mtype + index.mode.m + "," + index.mode.n + (" (f=" + frequency + ")"));
            getGammaImpl(index.mode, space2);
        }
        index.impedance = getImpedanceImpl(index);
//        index.fn = Maths.vector(Complex.ONE);//getFnImplByHints(index.getMode());
        index.fn = getFnImplByHints(index.getMode());
    }

    protected DoubleToVector getFnImplByHints(ModeIndex i){
        HintAxisType fnAxis = hintAxisType;
        DoubleToVector fn = getFnImpl(i);
        switch (fnAxis) {
            case XY:
//            case XY_UNCOUPLED_GROUPED:
            case XY_SEPARATED: {
                // do nothing
                break;
            }
            case X_ONLY: {
                fn= Maths.vector(fn.getComponent(Axis.X), Maths.DCZERO);
                break;
            }
            case Y_ONLY: {
                fn=Maths.vector(Maths.DCZERO, fn.getComponent(Axis.Y));
                break;
            }
        }
        return fn;
    }

    @Override
    public ModeFunctions setSize(int fnMax) {
        int old=this.size;
        this.size = fnMax;
        firePropertyChange("size",old,size);
        return this;
    }
    public void setHintEnableFunctionProperties(boolean disableDefaultFunctionProperties) {
        boolean old=this.enableDefaultFunctionProperties;
        this.enableDefaultFunctionProperties = disableDefaultFunctionProperties;
        firePropertyChange("enableDefaultFunctionProperties",old,this.enableDefaultFunctionProperties);
    }
    public void setModeInfoComparator(ModeInfoComparator modeInfoComparator) {
        ModeInfoComparator old=this.modeInfoComparator;
        this.modeInfoComparator = modeInfoComparator;
        firePropertyChange("modeInfoComparator",old,this.modeInfoComparator);
    }

    //    public int getSources() {
//        return sources;

    //    }
//    public ProjectType getProjectType() {
//        return projectType;
//    }
//
//    public void setProjectType(ProjectType projectType) {
//        this.projectType = projectType;
//        invalidateCache();
//    }

    @Override
    public ModeFunctions setFirstBoxSpace(BoxSpace firstBoxSpace) {
        BoxSpace old=this.firstBoxSpace;
        this.firstBoxSpace = firstBoxSpace;
        firePropertyChange("firstBoxSpace",old,firstBoxSpace);
        return this;
    }

    @Override
    public ModeFunctions setSecondBoxSpace(BoxSpace secondBoxSpace) {
        BoxSpace old=this.secondBoxSpace;
        this.secondBoxSpace = secondBoxSpace;
        firePropertyChange("secondBoxSpace",old,secondBoxSpace);
        return this;
    }

    @Override
    public ModeFunctions setFrequency(double frequency) {
        double old=this.frequency;
        this.frequency = frequency;
        firePropertyChange("frequency",old,frequency);
        return this;
    }

    @Override
    public ModeFunctions setDomain(Domain domain) {
        Domain old=this.domain;
        this.domain = domain;
        firePropertyChange("domain",old,domain);
        return this;
    }

    @Override
    public ModeFunctions setSources(Sources sources) {
        Sources old=this.sources;
        this.sources = (sources == null || !(sources instanceof ModalSources)) ? new CutOffModalSources(1) : (ModalSources) sources;
        firePropertyChange("sources",old,this.sources);
        return this;
    }

    @Override
    public ModeFunctions setHintAxisType(HintAxisType hintAxisType) {
        HintAxisType old=this.hintAxisType;
        this.hintAxisType = hintAxisType;
        firePropertyChange("hintAxisType",old,this.hintAxisType);
        return this;
    }

    @Override
    public ModeFunctions setHintInvariance(Axis invariantAxis) {
        Axis old=this.hintInvariantAxis;
        this.hintInvariantAxis = invariantAxis;
        firePropertyChange("hintInvariantAxis",old,this.hintInvariantAxis);
        return this;
    }


    @Override
    public ModeFunctions setHintSymmetry(AxisXY symmetryAxis) {
        AxisXY old=this.hintSymmetryAxis;
        this.hintSymmetryAxis = symmetryAxis;
        firePropertyChange("hintSymmetryAxis",old,this.hintSymmetryAxis);
        return this;
    }

    @Override
    public ModeFunctions setHintFnModes(ModeType... hintFnModeTypes) {
        ModeIndexFilter found = null;
        if (modeIndexFilters != null) {
            for (ModeIndexFilter modeIndexFilter : modeIndexFilters) {
                if (modeIndexFilter instanceof ModeFilter) {
                    found = modeIndexFilter;
                    break;
                }
            }
        }
        if (found != null) {
            removeModeIndexFilter(found);
        }
        if (hintFnModeTypes != null && hintFnModeTypes.length > 0) {
            addModeIndexFilter(new ModeFilter(hintFnModeTypes));
        }
        return this;
    }
    @Override
    public ModeFunctions removeModeInfoFilter(ModeInfoFilter modeInfoFilter) {
        if (modeInfoFilters != null && modeInfoFilter!=null) {
            List<ModeInfoFilter> old=new ArrayList<>(this.modeInfoFilters);
            if(modeInfoFilters.remove(modeInfoFilter)) {
                if (modeInfoFilters.size() == 0) {
                    modeInfoFilters = null;
                }
                firePropertyChange("modeInfoFilterRemoved",modeInfoFilter,null);
                firePropertyChange("modeInfoFilters",old,modeInfoFilters);
            }
        }
        return this;
    }

    @Override
    public ModeFunctions removeModeIndexFilter(ModeIndexFilter modeIndexFilter) {
        if (modeIndexFilters != null && modeIndexFilter!=null) {
            List<ModeIndexFilter> old=new ArrayList<>(this.modeIndexFilters);
            modeIndexFilters.remove(modeIndexFilter);
            if (modeIndexFilters.size() == 0) {
                modeIndexFilters = null;
            }
            firePropertyChange("modeIndexFilterRemoved",null,modeIndexFilter);
            firePropertyChange("modeIndexFilters",old,modeInfoFilters);
        }
        return this;
    }

    @Override
    public ModeFunctions addModeInfoFilter(ModeInfoFilter modeInfoFilter) {
        if(modeInfoFilter!=null) {
            List<ModeInfoFilter> old=this.modeInfoFilters==null?null:new ArrayList<>(this.modeInfoFilters);
            if (modeInfoFilters == null) {
                modeInfoFilters = new ArrayList<ModeInfoFilter>();
                modeInfoFilters.add(modeInfoFilter);
            } else if (!modeInfoFilters.contains(modeInfoFilter)) {
                modeInfoFilters.add(modeInfoFilter);
            }
            firePropertyChange("modeInfoFilterAdded",null,modeInfoFilter);
            firePropertyChange("modeInfoFilters",old,modeInfoFilters);
        }
        return this;
    }

    @Override
    public ModeFunctions addModeIndexFilter(ModeIndexFilter modeIndexFilter) {
        if(modeIndexFilter!=null) {
            List<ModeIndexFilter> old=this.modeIndexFilters==null?null:new ArrayList<>(this.modeIndexFilters);
            if (modeIndexFilters == null) {
                modeIndexFilters = new ArrayList<ModeIndexFilter>();
                modeIndexFilters.add(modeIndexFilter);
            } else if (!modeIndexFilters.contains(modeIndexFilter)) {
                modeIndexFilters.add(modeIndexFilter);
            }
            firePropertyChange("modeIndexFilterAdded", null, modeIndexFilter);
            firePropertyChange("modeIndexFilters", old, modeIndexFilters);
        }
        return this;
    }
    @Override
    public ModeFunctions setLayers(StrLayer[] couches) {
        StrLayer[] old=this.layers==null?null:Arrays.copyOf(this.layers,this.layers.length);
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
    /**
     * should not be called with true
     *
     * @param hintInvertTETMForZmode true if should TE/TM are inverted!
     * @return this
     * @Deprecated
     */
    @Override
    @Deprecated
    public ModeFunctions setHintInvertTETMForZmode(boolean hintInvertTETMForZmode) {
        boolean old=this.hintInvertTETMForZmode;
        this.hintInvertTETMForZmode = hintInvertTETMForZmode;
        firePropertyChange("hintInvertTETMForZmode", old, hintInvertTETMForZmode);
        return this;
    }
    @Override
    public ModeFunctions setModeIteratorFactory(ModeIteratorFactory modeIteratorFactory) {
        ModeIteratorFactory old=this.modeIteratorFactory;
        this.modeIteratorFactory = modeIteratorFactory;
        firePropertyChange("modeIteratorFactory", old, modeIteratorFactory);
        return this;
    }


    @Override
    public BoxSpace getFirstBoxSpace() {
        return firstBoxSpace;
    }

    @Override
    public BoxSpace getSecondBoxSpace() {
        return secondBoxSpace;
    }

    @Override
    public Axis getHintInvariantAxis() {
        return hintInvariantAxis;
    }
    @Override
    public AxisXY getHintSymmetry() {
        return hintSymmetryAxis;
    }
    //    public void setSources(int sources) {
//        this.sources = sources;
//        invalidateCache();

    //    }
    @Override
    public Domain getDomain() {
        return domain;
    }

    @Override
    public double getK0() {
        return cachedk0;
    }

    @Override
    public double getOmega() {
        return cachedOmega;
    }

    @Override
    public HintAxisType getHintAxisType() {
        return hintAxisType;
    }

    @Override
    public ModalSources getSources() {
        return sources;
    }

    @Override
    public ModeType[] getHintFnModes() {
        if (modeIndexFilters != null) {
            for (ModeIndexFilter modeIndexFilter : modeIndexFilters) {
                if (modeIndexFilter instanceof ModeFilter) {
                    return ((ModeFilter) modeIndexFilter).getAcceptedModes();
                }
            }
        }
        return null;
    }

    @Override
    public Dumper getDumpStringHelper() {
        Dumper h = new Dumper(this);
        h.add("fnMax", size);
        h.add("freq", frequency);
        h.add("domain", domain);
        h.addNonNull("hintGpFnAxisType", hintAxisType);
        h.addNonNull("hintInvariantAxis", hintInvariantAxis);
        h.addNonNull("hintSymmetryAxis", hintSymmetryAxis);
//        h.add("projectType", projectType);
        h.add("firstBoxSpace", firstBoxSpace);
        h.add("secondBoxSpace", secondBoxSpace);
        h.addNonNull("sources", sources);
        h.addNonNull("modeInfoFilters", modeInfoFilters);
        h.addNonNull("modeIndexFilters", modeIndexFilters);
        h.addNonNull("modeInfoComparator", modeInfoComparator);
        h.addNonNull("modeIteratorFactory", modeIteratorFactory);
        if (isHintInvertTETMForZmode()) {
            h.add("hintInvertTETMForZin", true);
        }
        h.add("borders", borders);
        return h;
    }

    @Override
    public String dump() {
        return getDumpStringHelper().toString();
    }

    @Override
    public boolean isComplex() {
        return complex;
    }

    @Override
    public WallBorders getBorders() {
        return borders;
    }

    /**
     * <pre>
     *         ArrayList<ModeInfo> next = new ArrayList<ModeInfo>();
     *  int max = getFnMax();
     *  int count = 0;
     *  Mode[] modes = new ModeType[]{ModeType.TM, ModeType.TE}; // TM prioritaire
     *   out:
     *  for (int i = 0;; i++) {
     *      Collection<ModeInfo> modeInfos = nextModeInfos(i, modes, true);
     *      for (ModeInfo modeInfo : modeInfos) {
     *          next.add(modeInfo);
     *          count++;
     *          if (count > max) {
     *              break out;
     *          }
     *      }
     *  }
     * <p/>
     * </pre>
     *
     * @param iter
     * @param modes
     * @param filter
     * @return
     */
    protected Collection<ModeInfo> nextModeInfos(int iter, ModeType[] modes, boolean filter) {
        int min = 0;
        Collection<ModeInfo> all = new ArrayList<ModeInfo>();
        if (filter) {
            ModeInfo mi;
            if (iter == min) {
                for (ModeType mode : modes) {
                    mi = new ModeInfo(mode, 0, 0, -1);
                    if (doAcceptModeIndex(mi.getMode())) {
                        fillIndex(mi, all.size());
                        if (doAcceptModeInfo(mi)) {
                            all.add(mi);
                        }
                    }
                }
            } else {
                for (int i = min; i < iter; i++) {
                    for (ModeType mode : modes) {
                        mi = new ModeInfo(mode, i, iter, -1);
                        if (doAcceptModeIndex(mi.getMode())) {
                            fillIndex(mi, all.size());
                            if (doAcceptModeInfo(mi)) {
                                all.add(mi);
                            }
                        }
                    }
                    for (ModeType mode : modes) {
                        mi = new ModeInfo(mode, iter, i, -1);
                        if (doAcceptModeIndex(mi.getMode())) {
                            fillIndex(mi, all.size());
                            if (doAcceptModeInfo(mi)) {
                                all.add(mi);
                            }
                        }
                    }
                }
                for (ModeType mode : modes) {
                    mi = new ModeInfo(mode, iter, iter, -1);
                    if (doAcceptModeIndex(mi.getMode())) {
                        fillIndex(mi, all.size());
                        if (doAcceptModeInfo(mi)) {
                            all.add(mi);
                        }
                    }
                }
            }
        } else {
            if (iter == min) {
                for (ModeType mode : modes) {
                    ModeInfo mi = new ModeInfo(mode, 0, 0, -1);
                    if (doAcceptModeIndex(mi.getMode())) {
                        fillIndex(mi, all.size());
                        if (doAcceptModeInfo(mi)) {
                            all.add(mi);
                        }
                    }
                }
            } else {
                for (int i = min; i < iter; i++) {
                    for (ModeType mode : modes) {
                        ModeInfo mi = new ModeInfo(mode, i, iter, -1);
                        if (doAcceptModeIndex(mi.getMode())) {
                            fillIndex(mi, all.size());
                            if (doAcceptModeInfo(mi)) {
                                all.add(mi);
                            }
                        }
                    }
                    for (ModeType mode : modes) {
                        ModeInfo mi = new ModeInfo(mode, iter, i, -1);
                        if (doAcceptModeIndex(mi.getMode())) {
                            fillIndex(mi, all.size());
                            if (doAcceptModeInfo(mi)) {
                                all.add(mi);
                            }
                        }
                    }
                }
                for (ModeType mode : modes) {
                    ModeInfo mi = new ModeInfo(mode, iter, iter, -1);
                    if (doAcceptModeIndex(mi.getMode())) {
                        fillIndex(mi, all.size());
                        if (doAcceptModeInfo(mi)) {
                            all.add(mi);
                        }
                    }
                }
            }
        }
        return all;
    }

    @Override
    public ModeInfoFilter[] getModeInfoFilters() {
        return modeInfoFilters == null ? MODE_INFO_FILTER_0 : modeInfoFilters.toArray(new ModeInfoFilter[modeInfoFilters.size()]);
    }

    @Override
    public ModeIndexFilter[] getModeIndexFilters() {
        return modeIndexFilters == null ? MODE_INDEX_FILTER_0 : modeIndexFilters.toArray(new ModeIndexFilter[modeIndexFilters.size()]);
    }


    @Override
    public StrLayer[] getLayers() {
        return layers;
    }


    @Override
    public boolean isHintInvertTETMForZmode() {
        return hintInvertTETMForZmode;
    }


    @Override
    public ModeIteratorFactory getModeIteratorFactory() {
        return modeIteratorFactory;
    }


    public ModeInfoComparator getModeInfoComparator() {
        return modeInfoComparator;
    }


    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        invalidateCache();
        pcs.firePropertyChange(propertyName,oldValue,newValue);
    }
}
