package net.vpc.scholar.hadrumaths.cache;

import net.vpc.common.mon.ProgressMonitorFactory;
import net.vpc.scholar.hadrumaths.io.HFile;

public class PersistenceCacheBuilder {
    private HFile rootFolder;
    private String repositoryName;
    private PersistenceCache parent;
    private ProgressMonitorFactory progressMonitorFactory;

    public static PersistenceCacheBuilder of() {
        return new PersistenceCacheBuilder();
    }

    public HFile getRootFolder() {
        return rootFolder;
    }

    public PersistenceCacheBuilder setRootFolder(HFile rootFolder) {
        this.rootFolder = rootFolder;
        return this;
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public PersistenceCacheBuilder setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
        return this;
    }

    public PersistenceCacheBuilder name(String repositoryName) {
        return setRepositoryName(repositoryName);
    }

    public PersistenceCache getParent() {
        return parent;
    }

    public PersistenceCacheBuilder setParent(PersistenceCache parent) {
        this.parent = parent;
        return this;
    }

    public ProgressMonitorFactory getMonitorFactory() {
        return progressMonitorFactory;
    }

    public PersistenceCacheBuilder setMonitorFactory(ProgressMonitorFactory progressMonitorFactory) {
        this.progressMonitorFactory = progressMonitorFactory;
        return this;
    }
    public PersistenceCacheBuilder monitorFactory(ProgressMonitorFactory progressMonitorFactory) {
        this.progressMonitorFactory = progressMonitorFactory;
        return this;
    }

    public PersistenceCache build() {
        return new PersistenceCacheImpl(rootFolder, repositoryName, parent,progressMonitorFactory);
    }
}
