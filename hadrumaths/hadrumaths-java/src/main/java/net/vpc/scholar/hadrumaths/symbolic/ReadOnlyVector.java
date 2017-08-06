package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.AbstractTVector;
import net.vpc.scholar.hadrumaths.AbstractVector;
import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.TVectorModel;

public class ReadOnlyVector extends AbstractVector {
    private TVectorModel<Complex> model;
    public ReadOnlyVector(TVectorModel<Complex> model, boolean row) {
        super(row);
        this.model = model;
    }

    @Override
    public Class<Complex> getComponentType(){
        return Complex.class;
    }

    @Override
    public Complex get(int i) {
        return model.get(i);
    }

    @Override
    public void set(int i, Complex value) {
        throw new IllegalArgumentException("Read Only");
    }

    @Override
    public int size() {
        return model.size();
    }

}
