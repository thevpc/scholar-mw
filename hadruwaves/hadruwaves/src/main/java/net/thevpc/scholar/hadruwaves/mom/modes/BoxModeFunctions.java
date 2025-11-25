package net.thevpc.scholar.hadruwaves.mom.modes;

import net.thevpc.common.mon.*;

import net.thevpc.nuts.elem.NArrayElementBuilder;
import net.thevpc.nuts.elem.NElement;
import net.thevpc.nuts.elem.NObjectElementBuilder;
import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.Vector;
import net.thevpc.scholar.hadrumaths.cache.*;
import net.thevpc.scholar.hadrumaths.io.HFile;
import net.thevpc.scholar.hadrumaths.io.HFileFilter;
import net.thevpc.scholar.hadrumaths.scalarproducts.ScalarProductOperator;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.thevpc.scholar.hadrumaths.symbolic.ExprType;
import net.thevpc.scholar.hadrumaths.util.NElementHelper;
import net.thevpc.scholar.hadruwaves.*;
import net.thevpc.scholar.hadruwaves.mom.*;
import net.thevpc.scholar.hadruwaves.mom.sources.Sources;
import net.thevpc.scholar.hadruwaves.mom.sources.modal.ModalSources;
import net.thevpc.scholar.hadruwaves.mom.str.ModeInfoComparator;
import net.thevpc.scholar.hadruwaves.str.MomStructureHintsManager;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.Function;
import net.thevpc.common.collections.CollectionFilter;
import net.thevpc.common.collections.CollectionUtils;
import net.thevpc.common.time.Chronometer;

import static net.thevpc.scholar.hadrumaths.Expressions.*;
import static net.thevpc.scholar.hadrumaths.Maths.*;
import static net.thevpc.scholar.hadruwaves.Physics.K0;
import static net.thevpc.scholar.hadruwaves.Physics.omega;

//import net.thevpc.scholar.tmwlib.mom.ProjectType;
/**
 * FnBaseFunctions must be initialized fnBaseFunctions.setDomain(getDomain());
 * fnBaseFunctions.setFnMax(modeFunctionsCount());
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
public class BoxModeFunctions implements net.thevpc.scholar.hadruwaves.mom.ModeFunctions {

    private static ModeInfoFilter[] MODE_INFO_FILTER_0 = new ModeInfoFilter[0];
    private static ModeIndexFilter[] MODE_INDEX_FILTER_0 = new ModeIndexFilter[0];
    public ObjectCacheResolver cacheResolver;
    protected ModeIterator modeIterator = new DefaultModeIterator();
    protected double cachedk0;
    protected double cachedOmega;
    protected ModeInfo[] cachedModesEvanescents;
    protected ModeInfo[] cachedModesPropagating;
    protected int cachedPropagatingModesCount = -1;
    private ModeInfo[] cachedIndexes;
    private List<ModeInfo>[] cachedIndexesByModeType = new List[ModeType.values().length];
    private volatile DoubleToVector[] cachedFn = null;
    private volatile Complex[] cachedZn = null;
    private volatile Complex[] cachedYn = null;
    private boolean complex = true;
    private List<ModeInfoFilter> modeInfoFilters;
    private List<ModeIndexFilter> modeIndexFilters;
    private boolean hintInvertTETMForZmode;
    private ModeInfoComparator modeInfoComparator = CutoffModeComparator.INSTANCE;
    private boolean enableDefaultFunctionProperties;
    private PropertyChangeSupport pcs;
    private HintAxisType hintAxisType = HintAxisType.XY;
    private Axis hintInvariantAxis = null;
    private AxisXY hintSymmetryAxis = null;
    protected int size = Integer.MIN_VALUE;

    private ModeFunctionsEnv env;
    private Axis polarization = Axis.X;
    private double xphase;
    private double yphase;
    //    private double ma = 1;
//    private double mb = 0;
//    private double na = 1;
//    private double nb = 0;
//    private ModeType[] allowedModes;
    private BoxModes modesDesc;
    protected ComplexMatrix lastScalarProductProductMatrix;
    protected Vector<Expr> lastScalarProductProductMatrixInput;

    private PropertyChangeListener cacheInvalidator = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            BoxModeFunctions.this.invalidateCache();
        }
    };

    public BoxModeFunctions() {
        pcs = new PropertyChangeSupport(this);
        addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if (propertyName.equals("frequency")) {
                    if (isDefinitionFrequencyDependent()) {
                        lastScalarProductProductMatrix = null;
                        lastScalarProductProductMatrixInput = null;
                    }
                } else if (propertyName.equals("size")) {
                    lastScalarProductProductMatrix = null;
                    lastScalarProductProductMatrixInput = null;
                } else {
                    lastScalarProductProductMatrix = null;
                    lastScalarProductProductMatrixInput = null;
                }
            }
        });
    }

    public ModeFunctions setEnv(ModeFunctionsEnv env) {
        if (this.env != null) {
            this.env.removePropertyChangeListener(cacheInvalidator);
        }
        this.env = env;
        if (this.env != null) {
            this.env.addPropertyChangeListener(cacheInvalidator);
        }
        return this;
    }

    public ModeFunctionsEnv getEnv() {
        if (env == null) {
            throw new IllegalArgumentException("Missing Environment");
        }
        return env;
    }

    public ModeFunctions setPolarization(Axis polarization) {
        if (polarization == null) {
            throw new IllegalArgumentException("Unsupported null Polarization");
        }

        Axis old = this.polarization;
        this.polarization = polarization;
        firePropertyChange("polarization", old, polarization);
        return this;
    }

    public ModeFunctions setPhaseX(double alphax) {
        double old = this.xphase;
        this.xphase = alphax;
        firePropertyChange("alphax", old, alphax);
        return this;
    }

    public ModeFunctions setPhaseY(double betay) {
        double old = this.yphase;
        this.yphase = betay;
        firePropertyChange("betay", old, betay);
        return this;
    }

    public ModeFunctions setModesDesc(BoxModes modesDesc) {
        this.modesDesc = modesDesc;
        return this;
    }

    public static String toStr(int[] x) {
        StringBuilder sb = new StringBuilder("[").append(x.length).append("]{");
        for (int i = 0; i < Math.min(x.length, 3); i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append(x[i]);
        }
        if (x.length > 3) {
            sb.append("...");
        }
        for (int i = Math.max(x.length - 3, 3); i < x.length; i++) {
            if (i > Math.max(x.length - 3, 3)) {
                sb.append(",");
            }
            sb.append(x[i]);
        }
        sb.append("}");
        return sb.toString();
    }

    @Override
    public ModeFunctions addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
        return this;
    }

    @Override
    public ModeFunctions removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
        return this;
    }

    @Override
    public ModeFunctions addPropertyChangeListener(String property, PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(property, listener);
        return this;
    }

    @Override
    public ModeFunctions removePropertyChangeListener(String property, PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(property, listener);
        return this;
    }

    @Override
    public PropertyChangeListener[] getPropertyChangeListeners() {
        return pcs.getPropertyChangeListeners();
    }

    @Override
    public PropertyChangeListener[] getPropertyChangeListeners(String property) {
        return pcs.getPropertyChangeListeners(property);
    }

    @Override
    public ModeFunctions invalidateCache() {
        cachedk0 = Double.NaN;
        cachedOmega = Double.NaN;
        cachedIndexes = null;
        cachedFn = null;
        cachedZn = null;
        cachedYn = null;
        cachedModesEvanescents = null;
        cachedModesPropagating = null;
        cachedPropagatingModesCount = -1;
        lastScalarProductProductMatrix = null;
        lastScalarProductProductMatrixInput = null;
        return this;
    }

    public boolean isHintEnableFunctionProperties() {
        return enableDefaultFunctionProperties;
    }
    //    public Complex firstBoxSpaceGamma(int n) {
//        return getGammaImpl(cachedIndexes[n]);

    public ModeFunctions setHintEnableFunctionProperties(boolean disableDefaultFunctionProperties) {
        boolean old = this.enableDefaultFunctionProperties;
        this.enableDefaultFunctionProperties = disableDefaultFunctionProperties;
        firePropertyChange("enableDefaultFunctionProperties", old, this.enableDefaultFunctionProperties);
        return this;
    }

    protected ModeInfo[] getIndexesImpl(ProgressMonitor monitor0) {
        monitor0 = createProgressMonitorNotNull(monitor0, "indexes");
        final int max = getSize();
        //System.out.println("lookup for "+max+" fn modes for "+this);
        Chronometer chrono = Chronometer.start();
        final ArrayList<ModeInfo> next = new ArrayList<ModeInfo>(max);
        final Iterator<ModeIndex> iterator = getModeIterator().iterator(this);
        ProgressMonitor monitor = ProgressMonitors.incremental(monitor0, max);
        String str = toString() + ", enumerate modes";
        final String message = str + " {0,number,#}/{1,number,#}";

        Maths.invokeMonitoredAction(monitor, str, new VoidMonitoredAction() {
            @Override
            public void invoke(ProgressMonitor monitor, String messagePrefix) throws Exception {
                int index = 0;
                int bruteMax = max * 100 + 1;
                int bruteIndex = 0;
                while (index < max && bruteIndex < bruteMax) {
                    if (iterator.hasNext()) {
                        ModeIndex modeIndex = iterator.next();
                        if (doAcceptModeIndex(modeIndex)) {
                            ModeInfo modeInfo = new ModeInfo(modeIndex);
                            fillIndex(modeInfo, 0);
                            if (doAcceptModeInfo(modeInfo)) {
                                next.add(modeInfo);
                                index++;
                                monitor.inc(message, index, max);
                            }
                        }
                        bruteIndex++;
                    } else {
                        break;
                    }
                }
            }
        });
        //System.out.println("found " + next.size()+" modes in "+chrono);
        return next.toArray(new ModeInfo[0]);
    }

    private ProgressMonitor createProgressMonitorNotNull(ProgressMonitor m, String name) {
        if (m == null) {
            ProgressMonitorFactory monitor = getEnv().getMonitorFactory();
            if (monitor != null) {
                m = monitor.createMonitor(name, null);
            }
        }
        return ProgressMonitors.nonnull(m);
    }

    public BoxModes getModesDesc() {
        if (modesDesc == null) {
            modesDesc = Physics.boxModes(getEnv().getBorders(), getEnv().getDomain(), this.xphase, this.yphase, polarization);
        }
        return modesDesc;
    }

    public double getCutoffFrequency(ModeIndex i) {
        return getModesDesc().getCutoffFrequency(i);
    }

    protected Complex getGammaImpl(ModeIndex i, BoxSpace bs) {
        return getModesDesc().getGamma(i, getEnv().getFrequency(), bs);
    }

    protected DoubleToVector getFnImpl(ModeIndex i) {
        return getModesDesc().getFunction(i);
    }

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
        MutableComplex ys = new MutableComplex();
        BoxSpace[] spaces = new BoxSpace[]{env.getFirstBoxSpace(), env.getSecondBoxSpace()};
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
                    Complex y = Maths.CZERO;
                    // TODO are you sure?????????????
                    switch (space.getLimit()) {
                        case OPEN: {
//                            y = I(cachedOmega).mul(EPS0 * componentVectorSpace.getEpsr()).mul(cotanh(firstBoxSpaceGamma.mul(componentVectorSpace.getWidth()))).div(firstBoxSpaceGamma);
                            y = I(cachedOmega).mul(space.getEpsrc(env.getFrequency())).div(cotanh(gamma.mul(space.getWidth()))).div(gamma);
                            break;
                        }
                        case INFINITE: {
                            y = I(cachedOmega).mul(space.getEpsrc(env.getFrequency())).div(gamma);
                            break;
                        }
                        case ELECTRIC: {
//                            y = I.mul(cachedOmega).mul(EPS0 * componentVectorSpace.getEpsr()).div(cotanh(firstBoxSpaceGamma.mul(componentVectorSpace.getWidth()))).div(firstBoxSpaceGamma);
                            y = I.mul(cachedOmega).mul(space.getEpsrc(env.getFrequency())).mul(cotanh(gamma.mul(space.getWidth()))).div(gamma);
                            break;
                        }
                        case NOTHING: {
                            //nothing;
                            break;
                        }
                    }
                    ys.add(y);
                }
                break;
            }
            case TE: {
                for (int j = 0; j < spaces.length; j++) {
                    BoxSpace space = spaces[j];
                    gamma = j == 0 ? i.firstBoxSpaceGamma : i.secondBoxSpaceGamma;
                    Complex y = Maths.CZERO;
                    switch (space.getLimit()) {
                        case OPEN: {
                            y = gamma.div(cotanh(gamma.mul(space.getWidth())).mul(I.mul(cachedOmega).mul(U0)));
                            break;
                        }
                        case INFINITE: {
                            y = gamma.div(Complex.I.mul(cachedOmega).mul(U0));
                            break;
                        }
                        case ELECTRIC: {
                            y = gamma.mul(cotanh(gamma.mul(space.getWidth()))).div(I.mul(cachedOmega).mul(U0));
                            break;
                        }
                        case NOTHING: {
                            //nothing;
                            break;
                        }
                    }
                    ys.add(y);
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
        for (StrLayer couche : env.getLayers()) {
            ys.add(couche.getImpedance().admittanceValue());
        }
        ys.inv();
        Complex z = ys.toComplex();
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

//    @Override
    public int count(ProgressMonitor monitor) {
        return getModes(monitor).length;
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
//        setFnMax(structure.modeFunctionsCount());
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
    public synchronized DoubleToVector apply(int index) {
        return arr()[index];
    }

    @Override
    public synchronized DoubleToVector get(int index) {
        return arr()[index];
    }

    @Override
    public synchronized Vector<Expr> list() {
        return Maths.evector(arr());
    }

    @Override
    public synchronized Vector<Expr> toList() {
        return list();
    }

    @Override
    public synchronized DoubleToVector[] toArray() {
        return arr();
    }

    @Override
    public synchronized DoubleToVector[] fn() {
        return fn(null);
    }

    @Override
    public synchronized DoubleToVector[] fn(ProgressMonitor monitor) {
        return arr(monitor);
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
        return arr(null);
    }

    @Override
    public synchronized DoubleToVector[] arr(ProgressMonitor monitor) {
        if (cachedFn != null) {
            if(monitor!=null){
                monitor.terminate("mode functions loaded");
            }
            return cachedFn;
        }
        ModeInfo[] ind = getModes(monitor);
        DoubleToVector[] fn = new DoubleToVector[ind.length];
        for (int i = 0; i < fn.length; i++) {
            fn[i] = ind[i].fn;
        }
        cachedFn = fn;
        return cachedFn;
    }

    //
//    public CFunctionXY2D fn(int n) {
//        return fn()[n];
//    }
    @Override
    public synchronized Complex[] zn() {
        if (cachedZn != null) {
            return cachedZn;
        }
        ModeInfo[] ind = getModes();
        Complex[] fn = new Complex[ind.length];
        for (int i = 0; i < fn.length; i++) {
            fn[i] = ind[i].impedance.impedanceValue();
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
            fn[i] = ind[i].impedance.admittanceValue();
        }
        cachedYn = fn;
        return cachedYn;
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
            cachedModesEvanescents = i.toArray(new ModeInfo[0]);
        }
        return cachedModesEvanescents;
    }
    //    public boolean isPropagative(ModeInfo i) {
//        return i.initialIndex < sources;
//    }

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
            cachedModesPropagating = i.toArray(new ModeInfo[0]);
        }
        return cachedModesPropagating;
    }

    //
    @Override
    public BoxModeFunctions clone() {
        try {
            BoxModeFunctions functions = (BoxModeFunctions) super.clone();
            functions.env = env;
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
        return getModesDesc().getAllowedModes();
    }

    public Axis getPolarization() {
        return polarization;
    }

    public double getXphase() {
        return xphase;
    }

    public double getYphase() {
        return yphase;
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
        String p = getEnv().getBorders().toString();
        String sfx = (p.indexOf('p') >= 0) ? ("[Polarization" + polarization + "]" + (xphase != 0 ? ("[alphax=" + String.valueOf(xphase) + "]") : "") + (yphase != 0 ? ("[alphax=" + String.valueOf(yphase) + "]") : "")) : "";
        return "BoxModes[" + p + "]" + sfx;
    }

    @Override
    public int getPropagativeModesCount() {
        if (cachedPropagatingModesCount < 0) {
            Sources sources = env.getSources();
            if (sources instanceof ModalSources) {
                cachedPropagatingModesCount = ((ModalSources) sources).getSourceCountForDimensions(this);
            } else {
                cachedPropagatingModesCount = 1;
            }
        }
        return cachedPropagatingModesCount;
    }

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
        return getModesDesc().accept(o);
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
                        if (!isSymmetric(index.fn, axisSymm)) {
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
        ProgressMonitorFactory mf = getEnv().getMonitorFactory();
        return getModes(mf == null ? null : mf.createMonitor("modes", null));
    }

    protected void rebuildObj() {
//        if (getProjectType() == null) {
//            throw new IllegalArgumentException("Invalid ProjectType value");
//        }
        if (env == null) {
            throw new IllegalArgumentException("Invalid Environment value");
        }
        if (env.getFirstBoxSpace() == null) {
            throw new IllegalArgumentException("Invalid FirstBoxSpace value");
        }
        if (env.getSecondBoxSpace() == null) {
            throw new IllegalArgumentException("Invalid SecondBoxSpace value");
        }
        if (getSize() <= 0) {
            throw new IllegalArgumentException("Invalid Max Size value");
        }
        double f = env.getFrequency();
        if (Double.isNaN(f) || Double.isInfinite(f)) {
            throw new IllegalArgumentException("Invalid Frequency value");
        }
        if (env.getDomain() == null || !env.getDomain().isValid()) {
            throw new IllegalArgumentException("Invalid Domain value");
        }
        cachedOmega = omega(f);
        cachedk0 = K0(f);
    }

    @Override
    public synchronized List<ModeInfo> getModes(final ModeType mode) {
        return getModes(mode, null);
    }

    @Override
    public synchronized List<ModeInfo> getModes(final ModeType mode, ProgressMonitor monitor) {
        int modeOrdinal = mode.ordinal();
        List<ModeInfo> modeInfos = cachedIndexesByModeType[modeOrdinal];
        if (modeInfos == null) {
            cachedIndexesByModeType[modeOrdinal] = modeInfos = Collections.unmodifiableList(CollectionUtils.filter(Arrays.asList(getModes(monitor)), new CollectionFilter<ModeInfo>() {
                @Override
                public boolean accept(ModeInfo modeInfo, int baseIndex, Collection<ModeInfo> list) {
                    return modeInfo.mode.mtype == mode;
                }
            }));
        }
        return modeInfos;
    }

    @Override
    public synchronized List<DoubleToVector> getFunctions(final ModeType mode, ProgressMonitor monitor) {
        return CollectionUtils.convert(getModes(mode, monitor), new Function<ModeInfo, DoubleToVector>() {
            @Override
            public DoubleToVector apply(ModeInfo value) {
                return value.fn;
            }
        });
    }

    @Override
    public synchronized ModeInfo[] getModes(ProgressMonitor monitor) {
        monitor = createProgressMonitorNotNull(monitor, "modes");
        ObjectCache objectCache = cacheResolver == null ? null : cacheResolver.resolveObjectCache();
        try {
            if (cachedIndexes == null) {
                if (objectCache != null) {
                    try {
                        cachedIndexes = (ModeInfo[]) objectCache.load("mode-functions", null);
                    } catch (Exception ex) {
                        //ignore
                    }
                    if (cachedIndexes != null) {
                        monitor.terminate("mode functions loaded");
                        return cachedIndexes;
                    }
                }
                monitor.start("eval modes");

                if (objectCache == null) {
                    System.out.println("Are you sure you want to load modes with no cache for " + this);
                }
                rebuildObj();
                /*
             * if (getProjectType().equals(ProjectType.WAVE_GUIDE) &&
             * (!BoxLimit.INFINITE.equals(getFirstBoxSpace().limit) ||
             * !BoxLimit.INFINITE.equals(getSecondBoxSpace().limit))) {
             * throw new IllegalArgumentException("For waveguide projects limits
             * should be charge adaptee");
             }
                 */

                ProgressMonitor[] mons = ProgressMonitors.split(monitor, 0.5, 0.4, 0.1);
                final ModeInfo[] _cachedIndexes = getIndexesImpl(mons[0]);
                Comparator<ModeInfo> comparator = getModeComparator();
                if (comparator != null) {
                    Arrays.sort(_cachedIndexes, comparator);
                }
                final int _cacheSize = _cachedIndexes.length;
                for (int i = 0; i < _cacheSize; i++) {
                    _cachedIndexes[i].index = i;
                }
                ModeIndex[] propagativeModes = (env.getSources() instanceof ModalSources)
                        ? ((ModalSources) env.getSources()).getPropagatingModes(this, _cachedIndexes, getPropagativeModesCount())
                        : new ModeIndex[]{_cachedIndexes[0].mode};
                final HashSet<ModeIndex> propagativeModesSet = new HashSet<ModeIndex>(Arrays.asList(propagativeModes));
                final ProgressMonitor mon2 = mons[1];//ProgressMonitors.createIncrementalMonitor(mons[1], _cacheSize);
                final String message = toString() + ", evaluate mode properties";
                final String str = toString();
                Maths.invokeMonitoredAction(mon2, message, new VoidMonitoredAction() {
                    @Override
                    public void invoke(ProgressMonitor monitor, String messagePrefix) throws Exception {
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
                            if (_enableDefaultFunctionProperties) {
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
                                properties.put("Domain", env.getDomain().toString());
                                properties.put("symmetry", (isXSymmetric(index.fn) ? "X" : "") + (isYSymmetric(index.fn) ? "Y" : ""));
                                properties.put("propagating", index.propagating);
                                index.fn = index.fn.setProperties(properties).toDV();
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

                if (objectCache != null) {
                    try {
                        objectCache.store("mode-functions", cachedIndexes, mons[2]);
                    } catch (Exception ex) {
                        //ignore
                    }
                } else {
                    mons[2].terminate();
                }
            }
        } finally {
            monitor.terminate("mode functions loaded");
        }
        return cachedIndexes;
    }

    protected void fillIndex(ModeInfo index, int initialIndex) {
        BoxSpace space1 = env.getFirstBoxSpace();
        BoxSpace space2 = env.getSecondBoxSpace();
        index.initialIndex = initialIndex;
        index.cutOffFrequency = getCutoffFrequency(index.mode);
        index.firstBoxSpaceGamma = getGammaImpl(index.mode, space1);
        if (index.firstBoxSpaceGamma.isNaN() || index.firstBoxSpaceGamma.isInfinite() || index.firstBoxSpaceGamma.equals(Maths.CZERO)) {
            System.err.println("[Warning] firstBoxSpaceGamma=" + index.firstBoxSpaceGamma + " for " + index.mode.mtype + index.mode.m + "," + index.mode.n + (" (f=" + env.getFrequency() + ")"));
            getGammaImpl(index.mode, space1);
        }
        index.secondBoxSpaceGamma = getGammaImpl(index.mode, space2);
        if (index.secondBoxSpaceGamma.isNaN() || index.secondBoxSpaceGamma.isInfinite() || index.secondBoxSpaceGamma.equals(Maths.CZERO)) {
            System.err.println("[Warning] secondBoxSpaceGamma=" + index.secondBoxSpaceGamma + " for " + index.mode.mtype + index.mode.m + "," + index.mode.n + (" (f=" + env.getFrequency() + ")"));
            getGammaImpl(index.mode, space2);
        }
        index.impedance = Physics.impedance(getImpedanceImpl(index));
//        index.fn = Maths.vector(Complex.ONE);//getFnImplByHints(index.getMode());
        index.fn = getFnImplByHints(index.getMode());
    }

    protected DoubleToVector getFnImplByHints(ModeIndex i) {
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
                fn = Maths.vector(fn.getComponent(Axis.X), Maths.DCZERO).toDV();
                break;
            }
            case Y_ONLY: {
                fn = Maths.vector(Maths.DCZERO, fn.getComponent(Axis.Y)).toDV();
                break;
            }
        }
        return fn;
    }

    @Override
    public ModeFunctions setSize(int maxSize) {
        int old = this.size;
        this.size = maxSize;
        firePropertyChange("size", old, maxSize);
        return this;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public ModeFunctions setHintAxisType(HintAxisType hintAxisType) {
        HintAxisType old = this.hintAxisType;
        this.hintAxisType = hintAxisType;
        firePropertyChange("hintAxisType", old, this.hintAxisType);
        return this;
    }

    @Override
    public ModeFunctions setHintInvariance(Axis invariantAxis) {
        Axis old = this.hintInvariantAxis;
        this.hintInvariantAxis = invariantAxis;
        firePropertyChange("hintInvariantAxis", old, this.hintInvariantAxis);
        return this;
    }

    @Override
    public ModeFunctions setHintSymmetry(AxisXY symmetryAxis) {
        AxisXY old = this.hintSymmetryAxis;
        this.hintSymmetryAxis = symmetryAxis;
        firePropertyChange("hintSymmetryAxis", old, this.hintSymmetryAxis);
        return this;
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
        if (modeInfoFilters != null && modeInfoFilter != null) {
            List<ModeInfoFilter> old = new ArrayList<>(this.modeInfoFilters);
            if (modeInfoFilters.remove(modeInfoFilter)) {
                if (modeInfoFilters.size() == 0) {
                    modeInfoFilters = null;
                }
                firePropertyChange("modeInfoFilterRemoved", modeInfoFilter, null);
                firePropertyChange("modeInfoFilters", old, modeInfoFilters);
            }
        }
        return this;
    }

    @Override
    public ModeFunctions removeModeIndexFilter(ModeIndexFilter modeIndexFilter) {
        if (modeIndexFilters != null && modeIndexFilter != null) {
            List<ModeIndexFilter> old = new ArrayList<>(this.modeIndexFilters);
            modeIndexFilters.remove(modeIndexFilter);
            if (modeIndexFilters.size() == 0) {
                modeIndexFilters = null;
            }
            firePropertyChange("modeIndexFilterRemoved", null, modeIndexFilter);
            firePropertyChange("modeIndexFilters", old, modeInfoFilters);
        }
        return this;
    }

    @Override
    public ModeFunctions addModeInfoFilter(ModeInfoFilter modeInfoFilter) {
        if (modeInfoFilter != null) {
            List<ModeInfoFilter> old = this.modeInfoFilters == null ? null : new ArrayList<>(this.modeInfoFilters);
            if (modeInfoFilters == null) {
                modeInfoFilters = new ArrayList<ModeInfoFilter>();
                modeInfoFilters.add(modeInfoFilter);
            } else if (!modeInfoFilters.contains(modeInfoFilter)) {
                modeInfoFilters.add(modeInfoFilter);
            }
            firePropertyChange("modeInfoFilterAdded", null, modeInfoFilter);
            firePropertyChange("modeInfoFilters", old, modeInfoFilters);
        }
        return this;
    }

    @Override
    public ModeFunctions addModeIndexFilter(ModeIndexFilter modeIndexFilter) {
        if (modeIndexFilter != null) {
            List<ModeIndexFilter> old = this.modeIndexFilters == null ? null : new ArrayList<>(this.modeIndexFilters);
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
        boolean old = this.hintInvertTETMForZmode;
        this.hintInvertTETMForZmode = hintInvertTETMForZmode;
        firePropertyChange("hintInvertTETMForZmode", old, hintInvertTETMForZmode);
        return this;
    }

    @Override
    public ModeFunctions setModeIterator(ModeIterator modeIterator) {
        ModeIterator old = this.modeIterator;
        this.modeIterator = modeIterator;
        firePropertyChange("modeIterator", old, modeIterator);
        return this;
    }

    public double getK0() {
        return cachedk0;
    }

    public double getOmega() {
        return cachedOmega;
    }

    @Override
    public HintAxisType getHintAxisType() {
        HintAxisType hintAxisType = this.hintAxisType;
        if (hintAxisType == null) {
            hintAxisType = getEnv().getHint(MomStructureHintsManager.HINT_AXIS_TYPE, null);
        }
        return hintAxisType;
    }

    @Override
    public ModeType[] getHintFnModes() {
        List<ModeIndexFilter> modeIndexFilters = this.modeIndexFilters;
        if (modeIndexFilters == null) {
            modeIndexFilters = getEnv().getHint(MomStructureHintsManager.HINT_FN_MODE, null);
        }
        if (modeIndexFilters != null) {
            for (ModeIndexFilter modeIndexFilter : modeIndexFilters) {
                if (modeIndexFilter instanceof ModeFilter) {
                    return ((ModeFilter) modeIndexFilter).getAcceptedModes();
                }
            }
        }
        return null;
    }

    protected boolean isDefinitionFrequencyDependent() {
        if (modeInfoFilters != null) {
            for (ModeInfoFilter modeInfoFilter : modeInfoFilters) {
                if (modeInfoFilter.isFrequencyDependent()) {
                    return true;
                }
            }
        }

        if (modeInfoFilters != null) {
            for (ModeIndexFilter modeInfoFilter : modeIndexFilters) {
                if (modeInfoFilter.isFrequencyDependent()) {
                    return true;
                }
            }
        }
        return false;
    }

//    @Override
//    public Dumper getDumpStringHelper() {
//        return getDumpStringHelper(true, true);
//    }
    @Override
    public NElement toElement() {
        return toElement(true, true);
    }

    public NElement toElement(boolean includeFreq, boolean includeSize) {
        NObjectElementBuilder h = NElement.ofObjectBuilder(getClass().getSimpleName());

        if (!includeFreq && isDefinitionFrequencyDependent()) {
            includeFreq = true;
        }
        NObjectElementBuilder env = NElement.ofObjectBuilder();
        env.add("domain", NElementHelper.elem(getEnv().getDomain()));
        env.add("borders", NElementHelper.elem(getEnv().getBorders()));
        env.add("sources", NElementHelper.elem(getEnv().getSources()));
        env.add("layers", NElementHelper.elem(getEnv().getLayers()));
        env.add("firstBoxSpace", NElementHelper.elem(getEnv().getFirstBoxSpace()));
        env.add("secondBoxSpace", NElementHelper.elem(getEnv().getSecondBoxSpace()));

        if (includeFreq) {
            env.add("frequency", NElementHelper.elem(getEnv().getFrequency()));
        }
        if (includeSize) {
            h.add("size", NElementHelper.elem(size));
        }
        if (complex) {
            h.add("complex", NElementHelper.elem(complex));
        }
        List<Object> modeFilters = new ArrayList<>();
        if (modeIndexFilters != null) {
            modeFilters.addAll(modeIndexFilters);
        }
        if (modeInfoFilters != null) {
            modeFilters.addAll(modeInfoFilters);
        }
        h.add("filters", NElementHelper.elem(modeFilters));
        if (modeInfoComparator != null) {
            h.add("comparator", NElementHelper.elem(modeInfoComparator));
        }
        if (modeIterator != null) {
            h.add("iterator", NElementHelper.elem(modeIterator));
        }

        switch (getEnv().getBorders()) {
            case PPPP: {
                h.add("polarization", NElementHelper.elem(polarization));
                h.add("xphase", NElementHelper.elem(xphase));
                h.add("yphase", NElementHelper.elem(yphase));
                break;
            }
        }

        h.add("environment", env.build());

        NObjectElementBuilder hints = NElement.ofObjectBuilder();
        hints.add("hintInvariance", NElementHelper.elem(getHintInvariantAxis()));
        hints.add("hintFnModes", NElementHelper.elem(getHintFnModes()));
        hints.add("hintAxisType", NElementHelper.elem(getHintAxisType()));
        hints.add("hintInvariantAxis", NElementHelper.elem(getHintInvariantAxis()));
        hints.add("hintSymmetryAxis", NElementHelper.elem(getHintSymmetry()));
        hints.add("hintInvertTETMForZin", NElementHelper.elem(isHintInvertTETMForZmode()));
        h.add("hints", hints.build());
        return h.build();
    }

//    public Dumper getDumpStringHelper(boolean includeFreq, boolean includeSize) {
//        Dumper h = new Dumper(this);
//
//        return h;
//    }
//    @Override
//    public String dump() {
//        return getDumpStringHelper().toString();
//    }
    @Override
    public boolean isComplex() {
        return complex;
    }

    @Override
    public Axis getHintInvariantAxis() {
        Axis hintInvariantAxis = this.hintInvariantAxis;
        if (hintInvariantAxis == null) {
            hintInvariantAxis = getEnv().getHint(MomStructureHintsManager.HINT_INVARIANCE, null);
        }
        return hintInvariantAxis;
    }

    @Override
    public AxisXY getHintSymmetry() {
        AxisXY hintSymmetryAxis = this.hintSymmetryAxis;
        if (hintSymmetryAxis == null) {
            hintSymmetryAxis = getEnv().getHint(MomStructureHintsManager.HINT_SYMMETRY, null);
        }
        return hintSymmetryAxis;
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
    public boolean isHintInvertTETMForZmode() {
        return hintInvertTETMForZmode;
    }

    @Override
    public ModeIterator getModeIterator() {
        return modeIterator;
    }

    public ModeInfoComparator getModeComparator() {
        return modeInfoComparator;
    }

    public ModeFunctions setModeComparator(ModeInfoComparator modeInfoComparator) {
        ModeInfoComparator old = this.modeInfoComparator;
        this.modeInfoComparator = modeInfoComparator;
        firePropertyChange("modeInfoComparator", old, this.modeInfoComparator);
        return this;
    }

    protected ModeFunctions firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        invalidateCache();
        pcs.firePropertyChange(propertyName, oldValue, newValue);
        return this;
    }

    @Override
    public ModeFunctions setObjectCacheResolver(ObjectCacheResolver cacheResolver) {
        ObjectCacheResolver old = this.cacheResolver;
        this.cacheResolver = cacheResolver;
        firePropertyChange("objectCacheResolver", old, cacheResolver);
        return this;
    }

    private void propertyChange(PropertyChangeEvent evt) {
        invalidateCache();
    }

    private ObjectCache getSingleTestFunctionObjectCache(Expr testFunction) {
        NElement node = NElement.ofObjectBuilder("SingleTestModeScalarProducts")
                .add("modeFunction", toElement(false, false))
                .add("testFunction", NElementHelper.elem(testFunction.simplify()))
                .build();
        PersistenceCache persistenceCache = PersistenceCacheBuilder.of().name("SingleTestModeScalarProducts").monitorFactory(getEnv().getMonitorFactory()).build();
        return persistenceCache.getObjectCache(CacheKey.of(node), true);
    }

    private ObjectCache getMultipleTestFunctionObjectCache(Expr[] testFunctions) {
        NArrayElementBuilder testFunction2 = NElement.ofArrayBuilder();
        for (Expr testFunction : testFunctions) {
            testFunction2.addAll(new NElement[]{NElementHelper.elem(testFunction.simplify())});
        }
        NElement node = NElement.ofObjectBuilder("MultipleTestModeScalarProducts")
                .add("modeFunction", toElement(false, false))
                .add("testFunctions", testFunction2.build())
                .build();
        PersistenceCache persistenceCache = PersistenceCacheBuilder.of().name("MultipleTestModeScalarProducts").monitorFactory(getEnv().getMonitorFactory()).build();
        return persistenceCache.getObjectCache(CacheKey.of(node), true);
    }

    @Override
    public ComplexVector scalarProduct(final Expr testFunction, ProgressMonitor monitor) {
        boolean dd = getModesDesc().getBorders() != WallBorders.PPPP;
        if (dd) {
            switch (testFunction.getType()) {
                case DOUBLE_DOUBLE: {
                    break;
                }
                default: {
                    dd = false;
                }
            }
        }
        if (!Maths.Config.isCacheEnabled()) {
            return scalarProduct0(dd, testFunction, arr(), monitor);
        }
        final ObjectCache objectCache = getSingleTestFunctionObjectCache(testFunction);
        if (objectCache == null) {
            return scalarProduct0(dd, testFunction, arr(), monitor);
        }
        ProgressMonitor[] mons = monitor.split(1, 9);
        final int currentCount = count(mons[0]);
        boolean finalDd = dd;
        String id = "all-test-mode-scalar-products-" + currentCount;
        return objectCache.evaluate("test-mode-scalar-products-" + currentCount, mons[1], new CacheEvaluator() {

            @Override
            public Object evaluate(Object[] args, ProgressMonitor cacheMonitor) {
                ProgressMonitor[] mons = monitor.split(1,9);
                ComplexVector found = loadCacheScalarProduct(finalDd, testFunction, objectCache, currentCount, mons[0]);
                if (found == null) {
                    ProgressMonitor[] mons2 = mons[1].split(1,9);
                    found = scalarProduct0(finalDd, testFunction, arr(mons2[0]), mons[1]);
                }else{
                    mons[1].terminate();
                }
                return found;
            }
        });
    }

    @Override
    public ComplexMatrix scalarProduct(final Vector<Expr> testFunctions, ProgressMonitor monitor0) {
        ProgressMonitor monitor = createProgressMonitorNotNull(monitor0, "scalar-product");
        if (!Maths.Config.isCacheEnabled()) {
            return scalarProductCache0(testFunctions, monitor);
        }
        if (lastScalarProductProductMatrix != null && lastScalarProductProductMatrixInput != null && lastScalarProductProductMatrixInput.equals(testFunctions)) {
//            if(lastScalarProductProductMatrix.getColumnCount() != count()){
//                throw new IllegalStateException();
//            }
            monitor.terminate();
            return lastScalarProductProductMatrix;
        }
        lastScalarProductProductMatrixInput = testFunctions.copy();
        ObjectCache objectCache = getMultipleTestFunctionObjectCache(testFunctions.toArray());
        if (objectCache == null) {
            return scalarProductCache0(testFunctions, monitor);
        }
        ProgressMonitor[] mon = monitor.split(0.8, 0.2);
        int currentCount = count(mon[0]);
        String id = "all-test-mode-scalar-products-" + currentCount;
        ComplexMatrix evaluated = objectCache.evaluate(id, mon[1], new CacheEvaluator() {
            @Override
            public Object evaluate(Object[] args, ProgressMonitor cacheMonitor) {
                return scalarProductCache0(testFunctions, mon[1]);
            }
        });
        monitor.terminate();
        return lastScalarProductProductMatrix = evaluated;
    }

    public ComplexMatrix scalarProductCache0(Vector<Expr> testFunctions, ProgressMonitor monitor) {

        ProgressMonitor m = createProgressMonitorNotNull(monitor, "scalar-product").incremental(testFunctions.length());
        boolean dd = getModesDesc().getBorders() != WallBorders.PPPP;
        if (dd) {
            for (Expr testFunction : testFunctions) {
                if (testFunction.getType() != ExprType.DOUBLE_DOUBLE) {
                    dd = false;
                    break;
                }
            }
        }
        if (dd) {
            double[][] rows = new double[testFunctions.size()][];
            m.start();
            for (int i = 0; i < rows.length; i++) {
                ProgressMonitor sub = m.translate(i, rows.length);
                rows[i] = ((DoubleVector) scalarProduct(testFunctions.get(i), sub).to(Maths.$DOUBLE)).toDoubleArray();
                sub.terminate("test function " + (i + 1) / rows.length);
            }
            m.terminate();
            return (ComplexMatrix) new DMatrix(rows).to(Maths.$COMPLEX);
        } else {
            Complex[][] rows = new Complex[testFunctions.size()][];
            m.start();
            for (int i = 0; i < rows.length; i++) {
//                m.inc();
                rows[i] = scalarProduct(testFunctions.get(i), m.translate(i, rows.length)).toArray();
            }
            m.terminate();
            return Maths.Config.getComplexMatrixFactory().newMatrix(rows);
        }
    }

    private ComplexVector loadCacheScalarProduct(boolean dd, Expr testFunction, ObjectCache objectCache, int currentCount, ProgressMonitor monitor) {
        monitor = createProgressMonitorNotNull(monitor, "scalar-product");
        if (!getModeIterator().isAbsoluteIterator(this)) {
            //could guess indexes from existing cached files...
            monitor.terminate();
            return null;
        }
        HFile dumpFolder = objectCache.getFolder();
        HFile[] hFiles = dumpFolder.listFiles(new HFileFilter() {
            @Override
            public boolean accept(HFile pathname) {
                String name = pathname.getName();
                return name.startsWith("test-mode-scala-products-") && name.endsWith(ObjectCache.CACHE_OBJECT_SUFFIX);
            }
        });
        HFile best = null;
        int bestCount = 0;
        ProgressMonitor[] mon = monitor.split(0.1, 0.9);
        for (HFile hFile : hFiles) {
            int sn = Integer.parseInt(hFile.getSimpleName().substring("test-mode-scala-products-".length()));
            if (best == null) {
                best = hFile;
                bestCount = sn;
            } else {
                if (sn >= currentCount) {
                    if (sn < bestCount) {
                        best = hFile;
                        bestCount = sn;
                    }
                } else {
                    if (sn > bestCount) {
                        best = hFile;
                        bestCount = sn;
                    }
                }
            }
        }
        mon[0].terminate();
        if (best != null) {
            ComplexVector o = null;
            try {
                o = (ComplexVector) DefaultObjectCache.loadObject(best, null, new ProgressMonitorFactory() {
                    @Override
                    public ProgressMonitor createMonitor(String name, String description) {
                        return mon[1];
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
            mon[1].terminate();
            if (o != null) {
                if (o.size() > currentCount) {
                    monitor.terminate();
                    return (ComplexVector) Maths.list(o).sublist(0, currentCount).copy();
                } else {
                    ComplexVector copy = (ComplexVector) Maths.list(o).copy();
                    Vector<Expr> list = list();
                    list = list.sublist(copy.size(), list.length());

                    ComplexVector extra = scalarProduct0(dd, testFunction, (DoubleToVector[]) list.toArray(new DoubleToVector[0]), monitor);
                    copy.appendAll(extra);
                    monitor.terminate();
                    return copy;
                }
            }
        }
        monitor.terminate();
        return null;
    }

    private ComplexVector scalarProduct0(boolean dd, Expr testFunction, DoubleToVector[] arr, ProgressMonitor monitor) {
        monitor = createProgressMonitorNotNull(monitor, "scalar-product");
        ScalarProductOperator sp = Maths.Config.getScalarProductOperator();
        if (dd) {
            return (ComplexVector) Maths.dvector(sp.evalVDD(null, testFunction.toDV(), arr, monitor)).to(Maths.$COMPLEX);
        }
        return Maths.columnVector(sp.evalVDC(null, testFunction.toDV(), arr, monitor));
    }

    private InputStream createMonitor(InputStream is, String name, long length) {
        ProgressMonitorFactory f = getEnv().getMonitorFactory();
        if (f == null) {
            return new ProgressMonitorInputStream2(is, length,
                    new DialogProgressMonitor(null,
                            "Reading (" + Maths.formatMemory(length) + ") " + name
                    )
            );
        }
        return new ProgressMonitorInputStream2(is, length,
                f.createMonitor("Reading (" + Maths.formatMemory(length) + ") " + name, null)
        );
    }

}
