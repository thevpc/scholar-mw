//package net.thevpc.scholar.tmwlib.mom.project;
//
//import java.beans.PropertyChangeEvent;
//import java.io.File;
//import java.io.IOException;
//import java.util.NoSuchElementException;
//
//import net.thevpc.scholar.math.util.Configuration;
//import net.thevpc.scholar.math.Complex;
//import net.thevpc.scholar.tmwlib.mom.project.common.VariableExpression;
//import net.thevpc.scholar.tmwlib.mom.project.common.VarUnit;
//
//public class MomProjectContext implements Cloneable {
//
//    private MomProject momProject = null;
//    private File projectFile = null;
//    private VarEvaluator evaluator = new VarEvaluator();
//    
//
//    public MomProjectContext(MomProject momProject) {
//        this.momProject=momProject;
//        momProject.addProjectListener(new MomProjectListener() {
//
//            public void propertyChange(PropertyChangeEvent evt) {
//                String name = evt.getPropertyName();
//                if(name.startsWith("var.") || name.endsWith("Unit")){
//                    reset();
//                }
//            }
//        });
//        reset();
//    }
//
//    public File getProjectFile() {
//        return projectFile;
//    }
//
//    public void setProjectFile(File cacheFolder) {
//        this.projectFile = cacheFolder;
//    }
//
//    @Override
//    public MomProjectContext clone() {
//        try {
//            MomProjectContext o = (MomProjectContext) super.clone();
//            o.reset();
//            return o;
//        } catch (CloneNotSupportedException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public void unsetExpression(String name) {
//        momProject.unsetExpression(name);
//    }
//
//    public void setExpression(VariableExpression expression) {
//        momProject.setExpression(expression);
//    }
//
//    public void updateExpression(String name, String expression) {
//        momProject.updateExpression(name,expression);
//    }
//
//    public double getDimensionUnit() {
//        return momProject.getDimensionUnit();
//    }
//
//    public void setDimensionUnit(double dimensionUnit) {
//        momProject.setDimensionUnit(dimensionUnit);
//    }
//
//    public double getFrequenceUnit() {
//        return momProject.getFrequencyUnit();
//    }
//
//    public void setFrequenceUnit(double frequenceUnit) {
//        momProject.setFrequencyUnit(frequenceUnit);
//    }
//
//    public synchronized Complex getVar(String name) {
//        return evaluator.getVar(name);
//    }
//
//    public Complex evaluateComplex(String expr) {
//        return evaluator.evaluateComplex(expr);
////        return evaluate(expr, VarUnit.none);
//    }
//
//    public double evaluateDouble(String expr) {
//        return evaluator.evaluateDouble(expr);
////        Complex c = evaluate(expr, VarUnit.none);
////        if (c.getImag() == 0) {
////            return c.getReal();
////        }
////        throw new ClassCastException("Expected a real value but got " + c);
//    }
//
//    public int evaluateInt(String expr) {
//        return evaluator.evaluateInt(expr);
////        Complex c = evaluate(expr, VarUnit.INTEGER);
////        if (c.getImag() == 0) {
////            double d = c.getReal();
////            double r = Math.round(c.getReal());
////            if (Math.abs(d - r) >= 0.1) {
////                throw new ClassCastException("Expected an integer value but got " + c);
////            }
////            return (int) r;
////        }
////        throw new ClassCastException("Expected a real value but got " + c);
//    }
//
//    public double getDoubleVar(String name) {
//        return evaluator.getDoubleVar(name);
//    }
//
//    public Complex getComplexVar(String name) {
//        return evaluator.getComplexVar(name);
//    }
//
//    public double evaluateDimension(String expression) {
//        return evaluator.evaluateDimension(expression);
//        return evaluate(expression, VarUnit.DIMENSION_UNIT).getReal();
//    }
//
//    public double evaluateFrequence(String expression) {
//        return evaluate(expression, VarUnit.FREQUENCE_UNIT).getReal();
//    }
//
//    public VariableExpression getVariableExpression(String varName) {
//        return momProject.getVariableExpression(varName);
//    }
//
//    public VariableExpression[] getVariableExpressions() {
//        return momProject.getVariableExpressions();
//    }
//
//    public void reset() {
//        evaluator.update(momProject);
//    }
//
//
//  
//}
