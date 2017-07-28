package net.vpc.scholar.hadruwaves.mom.str.zsfractalmodel;

import net.vpc.scholar.hadrumaths.Matrix;
import net.vpc.scholar.hadruwaves.mom.ModeFunctions;

import java.io.Serializable;

public class Yoperator implements Serializable {
    private Matrix matrix;
    private ModeFunctions modeFunctions;

    public Yoperator(Matrix matrix, ModeFunctions modeFunctions) {
        this.matrix = matrix;
        this.modeFunctions = modeFunctions;
    }

    public Matrix getMatrix(){
        return matrix;
    }

    public ModeFunctions getFn() {
        return modeFunctions;
    }

    public void setFn(ModeFunctions modeFunctions) {
        this.modeFunctions = modeFunctions;
    }
}
