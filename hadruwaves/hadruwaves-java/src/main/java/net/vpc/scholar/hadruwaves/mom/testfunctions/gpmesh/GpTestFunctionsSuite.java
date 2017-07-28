/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.hadruwaves.mom.testfunctions.gpmesh;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author vpc
 */
public class GpTestFunctionsSuite {
    private String title;
    private ArrayList<net.vpc.scholar.hadruwaves.mom.TestFunctions> functions=new ArrayList<net.vpc.scholar.hadruwaves.mom.TestFunctions>();

    public GpTestFunctionsSuite(String title, net.vpc.scholar.hadruwaves.mom.TestFunctions... functions) {
        this.title = title;
        this.functions.addAll(Arrays.asList(functions));
    }

    public net.vpc.scholar.hadruwaves.mom.TestFunctions[] getFunctions() {
        return functions.toArray(new net.vpc.scholar.hadruwaves.mom.TestFunctions[functions.size()]);
    }
    
    public void addFunction(net.vpc.scholar.hadruwaves.mom.TestFunctions gp){
        functions.add(gp);
    }

    public String getTitle() {
        return title;
    }
    
}
