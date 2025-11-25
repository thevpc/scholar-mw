/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadrumaths.units;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.Callable;

import net.thevpc.nuts.elem.NElement;
import net.thevpc.nuts.elem.NToElement;

/**
 *
 * @author vpc
 */
public class UnitType implements NToElement {

    private static final Map<String, UnitType> vals = new LinkedHashMap<>();

    public static UnitType Length = new MulUnitType("Length", "General", LengthValue.SI_UNIT);
    public static UnitType Time = new MulUnitType("Time", "General", TimeValue.SI_UNIT);
    public static UnitType Frequency = new MulUnitType("Frequency", "General", FrequencyValue.SI_UNIT);
    public static UnitType Temperature = new ConvUnitType("Temperature", "General", TemperatureValue.SI_UNIT);
    public static UnitType Voltage = new MulUnitType("Voltage", "Electric", VoltageValue.SI_UNIT);
    public static UnitType Current = new MulUnitType("Current", "Electric", CurrentValue.SI_UNIT);
    public static UnitType Resistance = new MulUnitType("Resistance", "Electric", ResistanceValue.SI_UNIT);
    public static UnitType Conductance = new MulUnitType("Conductance", "Electric", ConductanceValue.SI_UNIT);
    public static UnitType Inductance = new MulUnitType("Inductance", "Electric", InductanceValue.SI_UNIT);
    public static UnitType Capacitance = new MulUnitType("Capacitance", "Electric", CapacitanceValue.SI_UNIT);
    public static UnitType Power = new MulUnitType("Power", "Electric", PowerValue.SI_UNIT);
    public static UnitType Double = new NbrUnitType("Double", "Standard", null);
    public static UnitType Integer = new NbrUnitType("Integer", "Standard", null);
    public static UnitType Complex = new NbrUnitType("Complex", "Standard", null);
    public static UnitType Boolean = new NbrUnitType("Boolean", "Standard", null);
    public static UnitType Expression = new NbrUnitType("Expression", "Standard", null);
    public static UnitType String = new UnitType("String", "Standard", null);

    private final Class<? extends Enum> unitType;
    private final ParamUnit defaultValue;
    private final String name;
    private final String group;

    public static <T extends Enum> UnitType createEnum(Class<T> enumType, String group) {
        java.lang.String name = enumType.getSimpleName();
        UnitType y = vals.get(name);
        if (y != null) {
            return y;
        }
        return new EnumUnitType(name, group, enumType);
    }

    public static <T extends Enum> UnitType forEnum(Class<T> enumType) {
        java.lang.String name = enumType.getSimpleName();
        UnitType y = vals.get(name);
        if (y != null) {
            return y;
        }
        throw new IllegalArgumentException("Must call createEnum first for type " + enumType);
    }

    public static UnitType valueOf(String n) {
        UnitType t = vals.get(n);
        if (t == null) {
            throw new NoSuchElementException("UnitType not found : " + n);
        }
        return t;
    }

    public static UnitType[] values() {
        return vals.values().toArray(new UnitType[0]);
    }

    protected UnitType(String name, String group, ParamUnit defaultValue) {
        if (vals.containsKey(name)) {
            throw new IllegalArgumentException("Name already registered " + name);
        }
        this.name = name;
        this.group = group;
        this.defaultValue = defaultValue;
        this.unitType = (Class<? extends Enum>) (defaultValue == null ? null : defaultValue.getClass());
        vals.put(name, this);
    }

    @Override
    public NElement toElement() {
        return NElement.ofName(name);
    }

    public String name() {
        return name;
    }

    public String group() {
        return group;
    }

    @Override
    public String toString() {
        return java.lang.String.valueOf(name);
    }

    public Object[] unitValues() {
        return unitType == null ? new Object[0] : unitType.getEnumConstants();
    }

    public Class<? extends Enum> unitType() {
        return unitType;
    }

    public ParamUnit defaultValue() {
        return defaultValue;
    }

    public void cast(Enum u) {
        if (u == null) {
            return;
        }
        if (!unitType.equals(u.getClass())) {
            throw new ClassCastException("Type Mismatch " + this + " / " + u);
        }
//        if (!this.equals(u.type())) {
//            throw new ClassCastException("Type Mismatch " + this + " / " + u);
//        }
    }

    public SwitchStmt branch() {
        return branch(this);
    }

    public static SwitchStmt branch(UnitType t) {
        return new SwitchStmt(t);
    }

    public <V> SwitchVal<V> branchVal() {
        return branchVal(this);
    }

    public static <V> SwitchVal<V> branchVal(UnitType t) {
        return new SwitchVal<V>(t);
    }

    public static class SwitchStmt {

        private Map<UnitType, Runnable> map = new HashMap<>();
        private Runnable def;
        private UnitType test;

        public SwitchStmt(UnitType test) {
            this.test = test;
        }

        public SwitchStmt on(UnitType t, Runnable r) {
            map.put(t, r);
            return this;
        }

        public SwitchStmt orElse(Runnable r) {
            def = r;
            return this;
        }

        void exec() {
            Runnable v = map.get(test);
            if (v == null) {
                v = def;
            }
            if (v != null) {
                v.run();
            }
        }
    }

    public static class SwitchVal<V> {

        private Map<UnitType, Callable<V>> map = new HashMap<>();
        private Callable<V> def;
        private UnitType test;

        public SwitchVal(UnitType test) {
            this.test = test;
        }

        public SwitchVal<V> on(UnitType[] t, Callable<V> r) {
            for (UnitType t0 : t) {
                map.put(t0, r);
            }
            return this;
        }

        public SwitchVal<V> on(UnitType t, Callable<V> r) {
            map.put(t, r);
            return this;
        }

        public SwitchVal<V> orElse(Callable<V> r) {
            def = r;
            return this;
        }

        public V exec() {
            Callable<V> v = map.get(test);
            if (v == null) {
                v = def;
            }
            if (v != null) {
                try {
                    return v.call();
                } catch (RuntimeException ex) {
                    throw ex;
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
            throw new IllegalArgumentException("Unsupported");
        }
    }

    public static class MulUnitType extends UnitType {

        public MulUnitType(java.lang.String name, String group, MulParamUnit defaultValue) {
            super(name, group, defaultValue);
        }

        @Override
        public MulParamUnit defaultValue() {
            return (MulParamUnit) super.defaultValue();
        }

    }

    public static class NbrUnitType extends UnitType {

        public NbrUnitType(java.lang.String name, String group, ParamUnit defaultValue) {
            super(name, group, defaultValue);
        }
    }

    public static class ConvUnitType extends UnitType {

        public ConvUnitType(java.lang.String name, String group, ConvParamUnit defaultValue) {
            super(name, group, defaultValue);
        }

        @Override
        public ConvParamUnit defaultValue() {
            return (ConvParamUnit) super.defaultValue();
        }

    }

    public static class EnumUnitType<T extends Enum> extends UnitType {

        private final Class<T> enumType;

        public EnumUnitType(java.lang.String name, String group, Class<T> enumType) {
            super(name, group, null);
            this.enumType = enumType;
        }

        public Class<T> getEnumType() {
            return enumType;
        }

    }

}
