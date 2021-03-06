package net.thevpc.scholar.hadruwaves.mom.str.zsfractalmodel;

import net.thevpc.scholar.hadrumaths.ComplexMatrix;

import net.thevpc.scholar.hadruwaves.mom.ModeFunctions;

import java.io.Serializable;

public class Zoperator implements Serializable {
    private ComplexMatrix matrix;
    private ModeFunctions modeFunctions;

    public Zoperator(ComplexMatrix matrix, ModeFunctions modeFunctions) {
        this.matrix = matrix;
        this.modeFunctions = modeFunctions;
    }

    public ComplexMatrix getMatrix(){
        return matrix;
    }

    public ModeFunctions getFn() {
        return modeFunctions;
    }

    public void setFn(ModeFunctions modeFunctions) {
        this.modeFunctions = modeFunctions;
    }
}
