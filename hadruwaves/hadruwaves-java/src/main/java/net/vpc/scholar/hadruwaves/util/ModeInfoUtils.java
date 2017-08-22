/*
 * ModeInfoUtils.java
 *
 * Created on 3 oct. 2007, 18:47:10
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.hadruwaves.util;

import net.vpc.scholar.hadrumaths.Axis;
import net.vpc.scholar.hadruwaves.ModeInfo;
import net.vpc.scholar.hadruwaves.ModeInfoFilter;

/**
 *
 * @author vpc
 */
public final class ModeInfoUtils {
    private ModeInfoUtils(){
        
    }
    
    public static ModeInfoFilter and(ModeInfoFilter... all){
        return new AndCollection(all);
    }
    
    public static ModeInfoFilter or(ModeInfoFilter... all){
        return new OrCollection(all);
    }
    
    public static ModeInfoFilter invariant(Axis axis){
        return new Invariance(axis);
    }

    private static class AndCollection implements ModeInfoFilter {

        private ModeInfoFilter[] all;

        public AndCollection(ModeInfoFilter... all) {
            this.all = all;
        }

        public boolean acceptModeInfo(ModeInfo info) {
            for (ModeInfoFilter modeInfoFilter : all) {
                if (!modeInfoFilter.acceptModeInfo(info)) {
                    return false;
                }
            }
            return true;
        }

        public String dump() {
            if (all.length == 1) {
                return all[0].dump();
            }
            StringBuilder s = new StringBuilder("(");
            for (int i = 0; i < all.length; i++) {
                ModeInfoFilter modeInfoFilter = all[i];
                if (i > 0) {
                    s.append("&&");
                }
                s.append(modeInfoFilter.dump());
            }
            s.append(")");
            return s.toString();
        }

        @Override
        public boolean isFrequencyDependent() {
            for (ModeInfoFilter modeInfoFilter : all) {
                if(modeInfoFilter.isFrequencyDependent()){
                    return true;
                }
            }
            return false;
        }
    }

    private static class Invariance implements ModeInfoFilter {
        public Axis axis;
        public Invariance(Axis axis) {
            this.axis=axis;
        }

        public boolean acceptModeInfo(ModeInfo info) {
            return info.fn.getComponent(Axis.X).isInvariant(axis) && info.fn.getComponent(Axis.Y).isInvariant(axis);
        }

        public String dump() {
            return "Invariance("+axis+")";
        }

        @Override
        public boolean isFrequencyDependent() {
            return false;
        }
    }

    private static class OrCollection implements ModeInfoFilter {

        private ModeInfoFilter[] all;

        public OrCollection(ModeInfoFilter... all) {
            this.all = all;
        }

        public boolean acceptModeInfo(ModeInfo info) {
            for (ModeInfoFilter modeInfoFilter : all) {
                if (modeInfoFilter.acceptModeInfo(info)) {
                    return true;
                }
            }
            return false;
        }

        public String dump() {
            if (all.length == 1) {
                return all[0].dump();
            }
            StringBuilder s = new StringBuilder("(");
            for (int i = 0; i < all.length; i++) {
                ModeInfoFilter modeInfoFilter = all[i];
                if (i > 0) {
                    s.append("||");
                }
                s.append(modeInfoFilter.dump());
            }
            s.append(")");
            return s.toString();
        }

        @Override
        public boolean isFrequencyDependent() {
            for (ModeInfoFilter modeInfoFilter : all) {
                if(modeInfoFilter.isFrequencyDependent()){
                    return true;
                }
            }
            return false;
        }
    }
}