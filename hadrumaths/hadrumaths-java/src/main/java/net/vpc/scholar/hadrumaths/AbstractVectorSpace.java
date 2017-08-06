package net.vpc.scholar.hadrumaths;

import java.util.List;

public abstract class AbstractVectorSpace<T> implements VectorSpace<T>{
    @Override
    public T addAll(List<T> b) {
        RepeatableOp<T> o = addRepeatableOp();
        for (T t : b) {
            o.append(t);
        }
        return o.eval();
    }

    @Override
    public T mulAll(List<T> b) {
        RepeatableOp<T> o = mulRepeatableOp();
        for (T t : b) {
            o.append(t);
        }
        return o.eval();
    }
}
