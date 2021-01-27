package net.thevpc.scholar.hadruwaves.str;

import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.common.mon.ProgressMonitorFactory;
import net.thevpc.common.mon.ProgressMonitors;
import net.thevpc.scholar.hadrumaths.ComplexMatrix;
import net.thevpc.scholar.hadrumaths.cache.CacheAware;
import net.thevpc.scholar.hadrumaths.cache.ObjectCache;
import net.thevpc.scholar.hadrumaths.cache.PersistenceCache;
import net.thevpc.scholar.hadrumaths.cache.PersistenceCacheBuilder;
import net.thevpc.scholar.hadruplot.console.ConsoleAwareObject;
import net.thevpc.scholar.hadruplot.console.ConsoleLogger;
import net.thevpc.scholar.hadruplot.console.NullConsoleLogger;
import net.thevpc.scholar.hadruplot.console.params.ParamTarget;
import net.thevpc.scholar.hadruwaves.mom.MWStructureMemoryCache;
import net.thevpc.scholar.hadruwaves.mom.str.DefaultMomStructureErrorHandler;
import net.thevpc.scholar.hadruwaves.mom.str.MWStructureErrorHandler;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractMWStructure<T extends AbstractMWStructure> implements MWStructure, ConsoleAwareObject, CacheAware {
    private PersistenceCache persistentCache;
    private ProgressMonitorFactory monitorFactory;
    private MWStructureErrorHandler errorHandler = new DefaultMomStructureErrorHandler();
    private PropertyChangeSupport pcs;
    private MWStructureMemoryCache memoryCache;
    private Map<String, Object> parameters = new HashMap<String, Object>();
    /**
     * user objects are not included in the dump and could be used as extra info
     * on structure extra info should not influence structure characteristics
     * calculation
     */
    private HashMap<String, Object> userObjects = new HashMap<String, Object>();
    /**
     * domaine de la structure
     */
    //    private transient HashMap<String, Object> hints = new HashMap<String, Object>();
    private ConsoleLogger log = NullConsoleLogger.INSTANCE;
    private String name;
    private boolean rebuild = true;
    private MWStructureHintsManager hintsManager;

    private ProgressMonitorFactory monitorFactoryDelegate = new DelegateProgressMonitorFactory();
    private DelegateHintsPropertyChangeListener delegateHintChangeListener;

    public AbstractMWStructure() {
        persistentCache = PersistenceCacheBuilder.of().name("MomStructure").setMonitorFactory(monitorFactoryDelegate).build();
        memoryCache = new MWStructureMemoryCache(this);
        pcs = new PropertyChangeSupport(this);
    }

    public MWStructureMemoryCache getMemoryCache() {
        return memoryCache;
    }

    @Override
    public ConsoleLogger getLog() {
        return log;
    }

    public T setLog(ConsoleLogger log) {
        ConsoleLogger old = this.log;
        this.log = log;
        firePropertyChange("log", old, log);
        return (T)this;
    }

    public ParamTarget getTarget() {
        ParamTarget t = (ParamTarget) getUserObject("Target");
        return t == null ? ParamTarget.REFERENCE : t;
    }

    public T setTarget(ParamTarget target) {
        setUserObject("Target", target);
        return (T)this;
    }

    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        invalidateCache();
        pcs.firePropertyChange(propertyName, oldValue, newValue);
    }

    public T invalidateCache() {
        rebuild = true;
        memoryCache.reset();
        return (T)this;
    }

    public T setParameter(String name) {
        setParameter(name, Boolean.TRUE);
        return (T)this;
    }

    public T setParameterNotNull(String name, Object value) {
        if (value == null) {
            removeParameter(name);
        } else {
            setParameter(name, value);
        }
        return (T)this;
    }

    public T setParameter(String name, Object value) {
        Object old = parameters.put(name, value);
        firePropertyChange("parameter." + name, old, value);
        invalidateCache();
        return (T)this;
    }

    public T removeParameter(String name) {
        Object old = parameters.remove(name);
        firePropertyChange("parameter." + name, old, null);
        invalidateCache();
        return (T)this;
    }

    public Object getParameter(String name) {
        return parameters.get(name);
    }

    public Object getParameter(String name, Object defaultValue) {
        if (parameters.containsKey(name)) {
            return parameters.get(name);
        }
        return defaultValue;
    }

    public Number getParameterNumber(String name, Number defaultValue) {
        return (Number) getParameter(name, defaultValue);
    }

    public boolean containsParameter(String name) {
        return parameters.containsKey(name);
    }

    public boolean isParameter(String name, boolean defaultValue) {
        if (parameters.containsKey(name)) {
            return Boolean.TRUE.equals(parameters.get(name));
        } else {
            return defaultValue;
        }
    }

    public boolean isParameter(String name) {
        return Boolean.TRUE.equals(parameters.get(name));
    }

    @Override
    public Map<String, Object> getParameters() {
        return parameters;
    }

    public PersistenceCache getPersistentCache() {
        return persistentCache;
    }

    public Object getUserObject(String name, Object defaultValue) {
        if (userObjects.containsKey(name)) {
            return userObjects.get(name);
        }
        return defaultValue;
    }

    public Object getUserObject(String name) {
        return userObjects.get(name);
    }

    public T removeUserObject(String name) {
        Object old = userObjects.remove(name);
        firePropertyChange("userObject." + name, old, null);
        return (T)this;
    }

    public T setUserObject(String name, Object value) {
        Object old = userObjects.put(name, value);
        firePropertyChange("userObject." + name, old, value);
        return (T)this;
    }

    @Override
    public ProgressMonitorFactory getMonitorFactory() {
        return monitorFactory;
    }

    @Override
    public T setMonitorFactory(ProgressMonitorFactory monitor) {
        ProgressMonitorFactory old = this.monitorFactory;
        this.monitorFactory = monitor;
        firePropertyChange("monitorFactory", old, monitorFactory);
        return (T)this;
    }

    @Override
    public ProgressMonitorFactory monitorFactory() {
        return getMonitorFactory();
    }

    @Override
    public T monitorFactory(ProgressMonitorFactory monitor) {
        setMonitorFactory(monitor);
        return (T)this;
    }

    /**
     * Name is a simple value for qualifying a structure. It DOES NOT has any
     * action on dump. It is not dumpable so that changing names WILL NOT
     * invalidate cache
     *
     * @return name
     */
    public String getName() {
        return name == null ? "NONAME" : name;
    }

    public T setName(String name) {
        String old = this.name;
        this.name = name;
        firePropertyChange("name", old, name);
        return (T)this;
    }

    @Override
    public Map<String, Object> getUserObjects() {
        return userObjects;
    }

    public MWStructureErrorHandler getErrorHandler() {
        return errorHandler;
    }

    public MWStructureHintsManager getHintsManager() {
        return hintsManager;
    }

    public T setHintsManager(MWStructureHintsManager hintsManager) {
        MWStructureHintsManager old = this.hintsManager;
        if (old != hintsManager) {
            if (old != null) {
                old.removeChangeListener(delegateHintChangeListener);
                for (PropertyChangeListener p : old.getPropertyChangeListeners()) {
                    if (p instanceof DelegateHintsPropertyChangeListener) {
                        old.removeChangeListener(p);
                    }
                }
            }
            delegateHintChangeListener = new DelegateHintsPropertyChangeListener(this);
            this.hintsManager = hintsManager;
            if (hintsManager != null) {
                hintsManager.addChangeListener(delegateHintChangeListener);
            }
            firePropertyChange("hintsManager", old, hintsManager);
        }
        return (T)this;
    }

    public String getParametersString() {
        return parameters.values().toString();
    }

    /**
     * @return true if revalidate was really performed
     */
    public boolean build() {
        if (rebuild) {
            forceRebuild();
            rebuild = false;
            return true;
        }
        return false;
    }

    protected void forceRebuild() {

    }

    public PersistenceCache getCacheConfig() {
        return getPersistentCache();
    }

    //    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    //    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    //    @Override
    public void addPropertyChangeListener(String property, PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(property, listener);
    }

    //    @Override
    public void removePropertyChangeListener(String property, PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(property, listener);
    }

    public void wdebug(String title, Throwable e) {
        wdebug(title, e, null);
    }

    public void wdebug(String title, Throwable e, ComplexMatrix m) {
        if (errorHandler != null) {
            errorHandler.showError(title, e, m, this);
        }
    }

    public MWStructure setErrorHandler(MWStructureErrorHandler errorHandler) {
        MWStructureErrorHandler old = this.errorHandler;
        this.errorHandler = errorHandler;
        firePropertyChange("errorHandler", old, errorHandler);
        return this;
    }

    public MWStructure load(MWStructure other) {
        if (other != null) {
            for (Map.Entry<String, Object> e : other.getParameters().entrySet()) {
                setParameter(e.getKey(), e.getValue());
            }
            this.persistentCache.setAll(other.getPersistentCache());
            for (Map.Entry<String, Object> e : other.getUserObjects().entrySet()) {
                setUserObject(e.getKey(), e.getValue());
            }
            setLog(other.getLog());
            setName(other.getName());
            setErrorHandler(other.getErrorHandler());
            setMonitorFactory(other.getMonitorFactory());
            hintsManager.setAll(other.getHintsManager());
            this.invalidateCache();
        }
        return this;
    }

    @Override
    public abstract String dump();

    public ObjectCache getObjectCache() {
        if (getPersistentCache().isEnabled()) {
            ObjectCache objectCache = getPersistentCache().getObjectCache(getKey(), true);
            if (objectCache != null) {
                return objectCache;
            }
        }
        return null;
    }

    protected static class DelegateHintsPropertyChangeListener implements PropertyChangeListener {
        private AbstractMWStructure str;

        public DelegateHintsPropertyChangeListener(AbstractMWStructure str) {
            this.str = str;
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            str.pcs.firePropertyChange("hint." + evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
        }
    }

    private class DelegateProgressMonitorFactory implements ProgressMonitorFactory {
        @Override
        public ProgressMonitor createMonitor(String name, String description) {
            ProgressMonitorFactory f = getMonitorFactory();
            return f == null ? ProgressMonitors.none() : f.createMonitor(name, description);
        }
    }

    @Override
    public T clone() {
        try {
            return (T)super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalArgumentException("Unexpected");
        }
    }
}