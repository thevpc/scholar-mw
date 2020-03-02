package net.vpc.scholar.hadruwaves.studio.standalone.v1.actions;

import java.util.HashMap;

import javax.swing.JOptionPane;
import net.vpc.common.log.Log;
import net.vpc.lib.pheromone.ariana.util.Exceptions;
import net.vpc.lib.pheromone.ariana.util.Resources;

import net.vpc.scholar.hadruwaves.studio.standalone.v1.TMWLabApplication;
import net.vpc.scholar.hadruwaves.mom.util.MomStrHelper;
import net.vpc.scholar.hadruwaves.mom.util.MomStrHelperImpl;
import net.vpc.scholar.hadruwaves.studio.standalone.v1.editors.MomProjectEditor;

/**
 * User: vpc
 * Date: 11 févr. 2005
 * Time: 22:54:12
 */
public class RunningProjectThread extends Thread {

    private net.vpc.scholar.hadruwaves.mom.project.common.RunAction runAction;
    protected HashMap<String, Object> properties;
    protected StructureAction structureAction;
    protected boolean stopped;
    protected MomStrHelper helper;

    public RunningProjectThread(StructureAction a) {
        super();
        this.properties = new HashMap<String, Object>();
        this.structureAction = a;
        this.runAction = null;
    }
    
    public TMWLabApplication getApplication(){
        return (TMWLabApplication)structureAction.getApplication();
    }
    
    
    public MomProjectEditor getMomProjectEditor(){
        return (MomProjectEditor)structureAction.getEditor();
    }
    
    public Resources getResources() {
        return structureAction.getResources();
    }

    public void run() {
        Object key = null;
        try {
            structureAction.configure(this);
            if (structureAction.isJxyBuildRequired()) {
                if (!structureAction.checkBuildConfigure(this)) {
                    return;
                }
            }
            if (stopped) {
                return;
            }
            key = structureAction.getEditor().startLongOperation(this, structureAction.getTitle());
            if (structureAction.isJxyBuildRequired()) {
                structureAction.checkBuildExecute(this);
            }
            structureAction.execute(this);
        } catch (Throwable throwable) {
            Log.error(throwable);
            structureAction.getEditor().endLongOperation(key);
            JOptionPane.showMessageDialog(structureAction.getEditor(), Exceptions.getMessage(throwable));
            return;
        } finally {
            structureAction.getEditor().endLongOperation(key);
        }
        structureAction.terminateProcess(this);
    }

    public net.vpc.scholar.hadruwaves.mom.project.common.RunAction getRunAction() {
        return runAction;
    }

    public HashMap<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(HashMap<String, Object> properties) {
        this.properties = properties;
    }

    public StructureAction getStructureAction() {
        return structureAction;
    }

    public void setStructureAction(StructureAction structureAction) {
        this.structureAction = structureAction;
    }

    public boolean isStopped() {
        return stopped;
    }

    public void setStopped(boolean stopped) {
        this.stopped = stopped;
    }

    public void setRunAction(net.vpc.scholar.hadruwaves.mom.project.common.RunAction runAction) {
        this.runAction = runAction;
    }

    public MomStrHelper getHelper(boolean create) {
        if (helper == null && create) {
            MomStrHelper jxy = new MomStrHelperImpl();
            if (structureAction.getEditor().getConfigFile() == null) {
                throw new IllegalArgumentException("You should save Structure before");
            }
            jxy.init(structureAction.getEditor().getProject());
            helper = jxy;
        }
        return helper;
    }
//    public MomStrHelper getHelper() {
//        return helper;
//    }

    public void setHelper(MomStrHelper helper) {
        this.helper = helper;
    }
}
