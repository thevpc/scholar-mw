package net.vpc.scholar.hadrumaths;

import net.vpc.common.util.TypeName;

public class ReadOnlyComplexVector extends AbstractComplexVector {
    private static final long serialVersionUID = 1L;
    private TVectorModel<Complex> model;

    public ReadOnlyComplexVector(TVectorModel<Complex> model, boolean row) {
        super(row);
        this.model = model;
    }

    @Override
    public TypeName<Complex> getComponentType() {
        return MathsBase.$COMPLEX;
    }

    @Override
    public Complex get(int i) {
        return model.get(i);
    }

    @Override
    public ComplexVector set(int i, Complex value) {
        throw new IllegalArgumentException("Read Only");
    }

    @Override
    public int size() {
        return model.size();
    }

}
