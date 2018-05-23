package net.vpc.scholar.hadrumaths;

import net.vpc.common.util.TypeReference;

public class ReadOnlyVector extends AbstractVector {
    private static final long serialVersionUID = 1L;
    private TVectorModel<Complex> model;

    public ReadOnlyVector(TVectorModel<Complex> model, boolean row) {
        super(row);
        this.model = model;
    }

    @Override
    public TypeReference<Complex> getComponentType() {
        return Maths.$COMPLEX;
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
