package net.vpc.scholar.hadruwaves.props;

import net.vpc.common.prpbind.PropertyType;
import net.vpc.common.prpbind.impl.DelegateProperty;
import net.vpc.scholar.hadruwaves.project.HWProjectEnv;

public class ReadOnlyPExpression<T> extends DelegateProperty<T> implements PExpression<T> {
    public ReadOnlyPExpression(PExpression<T> v) {
        super(v);
    }

    @Override
    public PExpression<T> getBase() {
        return (PExpression<T>) super.getBase();
    }

//    public static <T> ReadOnlyPValue<T> of(PValue<T> v) {
//        if (v instanceof ReadOnlyPValue<?>) {
//            return (ReadOnlyPValue<T>) v;
//        }else{
//            if(ro==null){
//        }
//        return new ReadOnlyPValue<T>(v);
//    }


    @Override
    public PropertyType getValueType() {
        return getBase().getValueType();
    }

    @Override
    public String get() {
        return getBase().get();
    }

    @Override
    public T eval(HWProjectEnv env) {
        return getBase().eval(env);
    }
}
