package net.vpc.scholar.hadruwaves.project.parameter;

import net.vpc.common.props.*;
import net.vpc.common.props.impl.PropertyListenersImpl;
import net.vpc.common.tson.Tson;
import net.vpc.common.tson.TsonElement;
import net.vpc.common.tson.TsonObjectContext;
import net.vpc.common.tson.TsonSerializable;
import net.vpc.scholar.hadrumaths.units.CapacitanceUnit;
import net.vpc.scholar.hadrumaths.units.ConductanceUnit;
import net.vpc.scholar.hadrumaths.units.CurrentUnit;
import net.vpc.scholar.hadrumaths.units.FrequencyUnit;
import net.vpc.scholar.hadrumaths.units.InductanceUnit;
import net.vpc.scholar.hadrumaths.units.LengthUnit;
import net.vpc.scholar.hadrumaths.units.ParamUnit;
import net.vpc.scholar.hadrumaths.units.PowerUnit;
import net.vpc.scholar.hadrumaths.units.ResistanceUnit;
import net.vpc.scholar.hadrumaths.units.TemperatureUnit;
import net.vpc.scholar.hadrumaths.units.TimeUnit;
import net.vpc.scholar.hadrumaths.units.UnitType;
import net.vpc.scholar.hadrumaths.units.VoltageUnit;
import net.vpc.scholar.hadruwaves.project.HWProject;

public class HWUnits implements TsonSerializable, WithListeners {

    protected WritablePValue<HWProject> project = Props.of("project").valueOf(HWProject.class, null);
    private PropertyListenersImpl listeners = new PropertyListenersImpl(this);
    private final WritablePValue<LengthUnit> lengthUnit = Props.of("lengthUnit").valueOf(LengthUnit.class, LengthUnit.mm);
    private final WritablePValue<FrequencyUnit> frequencyUnit = Props.of("frequencyUnit").valueOf(FrequencyUnit.class, FrequencyUnit.GHz);
    private final WritablePValue<CapacitanceUnit> capacitanceUnit = Props.of("capacitanceUnit").valueOf(CapacitanceUnit.class, CapacitanceUnit.SI_UNIT);
    private final WritablePValue<ConductanceUnit> conductanceUnit = Props.of("conductanceUnit").valueOf(ConductanceUnit.class, ConductanceUnit.SI_UNIT);
    private final WritablePValue<CurrentUnit> currentUnit = Props.of("currentUnit").valueOf(CurrentUnit.class, CurrentUnit.SI_UNIT);
    private final WritablePValue<InductanceUnit> inductanceUnit = Props.of("inductanceUnit").valueOf(InductanceUnit.class, InductanceUnit.SI_UNIT);
    private final WritablePValue<PowerUnit> powerUnit = Props.of("powerUnit").valueOf(PowerUnit.class, PowerUnit.SI_UNIT);
    private final WritablePValue<ResistanceUnit> resistanceUnit = Props.of("resistanceUnit").valueOf(ResistanceUnit.class, ResistanceUnit.SI_UNIT);
    private final WritablePValue<TemperatureUnit> temperatureUnit = Props.of("temperatureUnit").valueOf(TemperatureUnit.class, TemperatureUnit.SI_UNIT);
    private final WritablePValue<TimeUnit> timeUnit = Props.of("timeUnit").valueOf(TimeUnit.class, TimeUnit.SI_UNIT);
    private final WritablePValue<VoltageUnit> voltageUnit = Props.of("voltageUnit").valueOf(VoltageUnit.class, VoltageUnit.SI_UNIT);

    public HWUnits(HWProject project) {
        this.project.set(project);
        listeners.addDelegate(lengthUnit);
        listeners.addDelegate(frequencyUnit);
        listeners.addDelegate(capacitanceUnit);
        listeners.addDelegate(conductanceUnit);
        listeners.addDelegate(currentUnit);
        listeners.addDelegate(inductanceUnit);
        listeners.addDelegate(powerUnit);
        listeners.addDelegate(resistanceUnit);
        listeners.addDelegate(temperatureUnit);
        listeners.addDelegate(timeUnit);
        listeners.addDelegate(voltageUnit);
    }

    public PValue<HWProject> project() {
        return project;
    }

    @Override
    public PropertyListeners listeners() {
        return listeners;
    }

    public TsonElement toTsonElement(TsonObjectContext context) {
        return Tson.obj("Units")
                .add("lengthUnit",lengthUnit.get())
                .add("frequencyUnit",frequencyUnit.get())
                .add("capacitanceUnit",capacitanceUnit.get())
                .add("conductanceUnit",conductanceUnit.get())
                .add("currentUnit",currentUnit.get())
                .add("inductanceUnit",inductanceUnit.get())
                .add("powerUnit",powerUnit.get())
                .add("resistanceUnit",resistanceUnit.get())
                .add("temperatureUnit",temperatureUnit.get())
                .add("timeUnit",timeUnit.get())
                .add("voltageUnit",voltageUnit.get())
                .build();
    }

//    @Override
    public WritablePValue<LengthUnit> lengthUnit() {
        return lengthUnit;
    }

//    @Override
    public WritablePValue<FrequencyUnit> frequencyUnit() {
        return frequencyUnit;
    }

    public WritablePValue<CapacitanceUnit> capacitanceUnit() {
        return capacitanceUnit;
    }

    public WritablePValue<ConductanceUnit> conductanceUnit() {
        return conductanceUnit;
    }

    public WritablePValue<CurrentUnit> currentUnit() {
        return currentUnit;
    }

    public WritablePValue<InductanceUnit> inductanceUnit() {
        return inductanceUnit;
    }

    public WritablePValue<PowerUnit> powerUnit() {
        return powerUnit;
    }

    public WritablePValue<ResistanceUnit> resistanceUnit() {
        return resistanceUnit;
    }

    public WritablePValue<TemperatureUnit> temperatureUnit() {
        return temperatureUnit;
    }

    public WritablePValue<TimeUnit> timeUnit() {
        return timeUnit;
    }

    public WritablePValue<VoltageUnit> voltageUnit() {
        return voltageUnit;
    }

//    @Override
    public ParamUnit defaultUnitValue(UnitType type) {
        if (type == null) {
            return null;
        }
        ParamUnit u = null;
        if (type == UnitType.Frequency) {
            u = frequencyUnit().get();
        } else if (type == UnitType.Length) {
            u = lengthUnit().get();
        }
        if (u == null) {
            u = type.defaultValue();
        }
        return u;
    }

}
