package net.thevpc.scholar.hadruwaves.mom.project.common;

import java.util.ArrayList;
import net.thevpc.scholar.hadruwaves.mom.project.MomProject;

/**
 * User: vpc
 * Date: 24 juin 2005
 * Time: 12:36:49
 */
public class AreaFractalGroup extends AreaGroup{
    private AreaZone generatorArea=null;
    private AreaZone generatedArea=null;
    private int fractalIteration=1;
    private boolean designMode=true;
    private boolean shouldRebuild=true;

    public AreaFractalGroup(String name,MomProject project) {
        super(name,project);
    }

    public AreaZone getGeneratorArea() {
        return generatorArea;
    }

    public void setGeneratorArea(AreaZone generatorArea) {
        if(generatorArea==null || containsZone(generatorArea)){
            this.generatorArea = generatorArea;
            shouldRebuild=true;
        }
    }

    public AreaZone getGeneratedArea() {
        return generatedArea;
    }

    public void setGeneratedArea(AreaZone generatedArea) {
        if(generatedArea==null || containsZone(generatedArea)){
            this.generatedArea = generatedArea;
            shouldRebuild=true;
        }
    }

    public int getFractalIteration() {
        return fractalIteration;
    }

    public void setFractalIteration(int fractalIteration) {
        if(fractalIteration<0){
            fractalIteration=1;
        }
        if(this.fractalIteration!=fractalIteration){
            this.fractalIteration = fractalIteration;
            shouldRebuild=true;
        }
    }

    @Override
    public void recompile() {
        if(shouldRebuild){

            for (AreaGroup areaGroup : new ArrayList<AreaGroup>(super.areaGroupsList)) {
                if(areaGroup==generatorArea){
                    areaGroup.setEnabled(designMode|| fractalIteration==0);
                }else if(areaGroup==generatedArea){
                    areaGroup.setEnabled(designMode|| fractalIteration==1);
                }else{
                    removeAreaGroup(areaGroup);
                }
            }
            for (Area area : new ArrayList<Area>(super.areas)) {
                if(area==generatorArea){
                    area.setEnabled(designMode|| fractalIteration==0);
                }else if(area==generatedArea){
                    area.setEnabled(designMode|| fractalIteration==1);
                }else{
                    removeArea(area);
                }
            }

            if(fractalIteration>=2){
                // rajouter les area calculée pour le fractalIteration
                for(int k=2;k<=fractalIteration;k++){
                    // ?????
                }

            }
        }
        super.recompile();
    }
    public boolean isDesignMode() {
        return designMode;
    }

    public void setDesignMode(boolean designMode) {
        this.designMode = designMode;
        shouldRebuild=true;
    }
}
