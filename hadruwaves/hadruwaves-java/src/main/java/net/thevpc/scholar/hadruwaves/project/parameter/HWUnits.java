package net.thevpc.scholar.hadruwaves.project.parameter;

import net.thevpc.common.props.*;
import net.thevpc.common.props.impl.PropertyListenersImpl;
import net.thevpc.tson.Tson;
import net.thevpc.tson.TsonElement;
import net.thevpc.tson.TsonObjectContext;
import net.thevpc.tson.TsonSerializable;
import net.thevpc.scholar.hadrumaths.units.CapacitanceUnit;
import net.thevpc.scholar.hadrumaths.units.ConductanceUnit;
import net.thevpc.scholar.hadrumaths.units.CurrentUnit;
import net.thevpc.scholar.hadrumaths.units.FrequencyUnit;
import net.thevpc.scholar.hadrumaths.units.InductanceUnit;
import net.thevpc.scholar.hadrumaths.units.LengthUnit;
import net.thevpc.scholar.hadrumaths.units.ParamUnit;
import net.thevpc.scholar.hadrumaths.units.PowerUnit;
import net.thevpc.scholar.hadrumaths.units.ResistanceUnit;
import net.thevpc.scholar.hadrumaths.units.TemperatureUnit;
import net.thevpc.scholar.hadrumaths.units.TimeUnit;
import net.thevpc.scholar.hadrumaths.units.UnitType;
import net.thevpc.scholar.hadrumaths.units.VoltageUnit;
import net.thevpc.scholar.hadruwaves.project.HWProject;

public class HWUnits implements TsonSerializable, WithListeners {

    protected WritableValue<HWProject> project = Props.of("project").valueOf(HWProject.class, null);
    private PropertyListenersImpl listeners = new PropertyListenersImpl(this);
    private final WritableValue<LengthUnit> lengthUnit = Props.of("lengthUnit").valueOf(LengthUnit.class, LengthUnit.mm);
    private final WritableValue<FrequencyUnit> frequencyUnit = Props.of("frequencyUnit").valueOf(FrequencyUnit.class, FrequencyUnit.GHz);
    private final WritableValue<CapacitanceUnit> capacitanceUnit = Props.of("capacitanceUnit").valueOf(CapacitanceUnit.class, CapacitanceUnit.SI_UNIT);
    private final WritableValue<ConductanceUnit> conductanceUnit = Props.of("conductanceUnit").valueOf(ConductanceUnit.class, ConductanceUnit.SI_UNIT);
    private final WritableValue<CurrentUnit> currentUnit = Props.of("currentUnit").valueOf(CurrentUnit.class, CurrentUnit.SI_UNIT);
    private final WritableValue<InductanceUnit> inductanceUnit = Props.of("inductanceUnit").valueOf(InductanceUnit.class, InductanceUnit.SI_UNIT);
    private final WritableValue<PowerUnit> powerUnit = Props.of("powerUnit").valueOf(PowerUnit.class, PowerUnit.SI_UNIT);
    private final WritableValue<ResistanceUnit> resistanceUnit = Props.of("resistanceUnit").valueOf(ResistanceUnit.class, ResistanceUnit.SI_UNIT);
    private final WritableValue<TemperatureUnit> temperatureUnit = Props.of("temperatureUnit").valueOf(TemperatureUnit.class, TemperatureUnit.SI_UNIT);
    private final WritableValue<TimeUnit> timeUnit = Props.of("timeUnit").valueOf(TimeUnit.class, TimeUnit.SI_UNIT);
    private final WritableValue<VoltageUnit> voltageUnit = Props.of("voltageUnit").valueOf(VoltageUnit.class, VoltageUnit.SI_UNIT);

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

    public ObservableValue<HWProject> project() {
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
    public WritableValue<LengthUnit> lengthUnit() {
        return lengthUnit;
    }

//    @Override
    public WritableValue<FrequencyUnit> frequencyUnit() {
        return frequencyUnit;
    }

    public WritableValue<CapacitanceUnit> capacitanceUnit() {
        return capacitanceUnit;
    }

    public WritableValue<ConductanceUnit> conductanceUnit() {
        return conductanceUnit;
    }

    public WritableValue<CurrentUnit> currentUnit() {
        return currentUnit;
    }

    public WritableValue<InductanceUnit> inductanceUnit() {
        return inductanceUnit;
    }

    public WritableValue<PowerUnit> powerUnit() {
        return powerUnit;
    }

    public WritableValue<ResistanceUnit> resistanceUnit() {
        return resistanceUnit;
    }

    public WritableValue<TemperatureUnit> temperatureUnit() {
        return temperatureUnit;
    }

    public WritableValue<TimeUnit> timeUnit() {
        return timeUnit;
    }

    public WritableValue<VoltageUnit> voltageUnit() {
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
