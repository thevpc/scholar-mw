///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//
//package net.vpc.scholar.tmwlib.mom.str;
//
//import java.util.Map;
//
//import net.vpc.scholar.math.convergence.*;
//import net.vpc.scholar.math.util.ComputationMonitor;
//import net.vpc.scholar.tmwlib.mom.MomStructure;
//import net.vpc.scholar.tmwlib.mom.TestFunctions;
//
//
///**
// *
// * @author vpc
// */
//public class ConvergenceParamFnMaxGp extends DefaultConvergenceParam<MomStructure> {
//    private Integer[] fnMax;
////    private ConvergenceParam<MomStructure> fnConvergenceEvaluator;
//    private TestFunctions[] gpTestFunctions;
//    private Map<String, Object> fixedParams;
//    private ConvergenceConfig convPars;
//    private int fnIndex;
//
//    public ConvergenceParamFnMaxGp(Integer[] fnMax) {
//        super();
//        this.fnMax=fnMax;
//    }
//
//    public void dsteps(TestFunctions[] iteratedParams, Map<String, Object> fixedParams, ConvergenceConfig convPars) {
//
//        this.gpTestFunctions=iteratedParams;
//        this.fixedParams=fixedParams;
//        this.convPars=convPars;
//    }
//
//
//
//    public final MomStructure next(MomStructure object,Object varValue,int index, Map<String, Object> currentParams) {
//        ConvergenceFunctionResult<_ResultType> fr = (ConvergenceFunctionResult<_ResultType>)currentParams.get("gp.result");
//        ConvergenceResult<_ResultType> o = fr==null?null:(ConvergenceResult<_ResultType>)fr.getResultInfo();
//        if(o !=null && o.getParameters().containsKey("fnMax.index")){
//            fnIndex=(Integer)o.getParameters().get("fnMax.index");
//        }else{
//            fnIndex=0;
//        }
//        object.setTestFunctions(gpTestFunctions[index]);
//        return object;
//    }
//
//    public ConvergenceFunctionResult<_ResultType> evaluate(MomStructure object,ComputationMonitor monitor){
//        ConvergenceEvaluator<MomStructure,Integer,_ResultType> process=new ConvergenceEvaluator<MomStructure, Integer, _ResultType>(object, fnConvergenceEvaluator);
//        ConvergenceResult<_ResultType> r = process.evaluate(fnMax, fnIndex, fnMax.length, convPars, monitor);
//        return new ConvergenceFunctionResult<_ResultType>(r.getValue(),r);
//    }
//
////    public abstract _ResultType computeLevel2(MomStructure object2,ComputationMonitor monitor);
//
//    public Map<String, Object> getFixedParams() {
//        return fixedParams;
//    }
//
//    public net.vpc.scholar.tmwlib.mom.TestFunctions[] getIteratedParams() {
//        return gpTestFunctions;
//    }
//
//
//
//}
