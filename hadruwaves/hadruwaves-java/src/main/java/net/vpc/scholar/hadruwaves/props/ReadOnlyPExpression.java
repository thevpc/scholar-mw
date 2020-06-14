package net.vpc.scholar.hadruwaves.props;

import net.vpc.common.props.Property;
import net.vpc.common.props.PropertyType;
import net.vpc.common.props.impl.DelegateProperty;
import net.vpc.scholar.hadrumaths.units.UnitType;
import net.vpc.scholar.hadruwaves.project.configuration.HWConfigurationRun;

public class ReadOnlyPExpression<T> extends DelegateProperty<T> implements PExpression<T> {

    public ReadOnlyPExpression(PExpression<T> v) {
        super(v);
    }

    @Override
    public UnitType unitType() {
        return getBase().unitType();
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
    public PropertyType valueType() {
        return getBase().valueType();
    }

    @Override
    public String get() {
        return getBase().get();
    }

    @Override
    public T eval(HWConfigurationRun configuration) {
        return getBase().eval(configuration);
    }

    @Override
    public String toString() {
        return getBase().toString();
    }

    @Override
    public Property readOnly() {
        return this;
    }

}
