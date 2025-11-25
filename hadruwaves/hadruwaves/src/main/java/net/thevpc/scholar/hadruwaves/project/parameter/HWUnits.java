package net.thevpc.scholar.hadruwaves.project.parameter;

import net.thevpc.common.props.*;
import net.thevpc.common.props.impl.DefaultPropertyListeners;
import net.thevpc.common.props.impl.PropertyBase;

import net.thevpc.nuts.elem.NElement;

import net.thevpc.nuts.elem.NToElement;
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
import net.thevpc.scholar.hadrumaths.util.NElementHelper;
import net.thevpc.scholar.hadruwaves.project.HWProject;

public class HWUnits extends PropertyBase implements NToElement {

    protected WritableValue<HWProject> project = Props.of("project").valueOf(HWProject.class, null);
    private DefaultPropertyListeners listeners = new DefaultPropertyListeners(this);
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

    public HWUnits(String name,HWProject project) {
        super(name);
        this.project.set(project);
        propagateEvents(lengthUnit);
        propagateEvents(frequencyUnit);
        propagateEvents(capacitanceUnit);
        propagateEvents(conductanceUnit);
        propagateEvents(currentUnit);
        propagateEvents(inductanceUnit);
        propagateEvents(powerUnit);
        propagateEvents(resistanceUnit);
        propagateEvents(temperatureUnit);
        propagateEvents(timeUnit);
        propagateEvents(voltageUnit);
    }

    public ObservableValue<HWProject> project() {
        return project;
    }

    @Override
    public PropertyListeners events() {
        return listeners;
    }

    public NElement toElement() {
        return NElement.ofObjectBuilder("Units")
                .add("lengthUnit", NElementHelper.elem(lengthUnit.get()))
                .add("frequencyUnit",NElementHelper.elem(frequencyUnit.get()))
                .add("capacitanceUnit",NElementHelper.elem(capacitanceUnit.get()))
                .add("conductanceUnit",NElementHelper.elem(conductanceUnit.get()))
                .add("currentUnit",NElementHelper.elem(currentUnit.get()))
                .add("inductanceUnit",NElementHelper.elem(inductanceUnit.get()))
                .add("powerUnit",NElementHelper.elem(powerUnit.get()))
                .add("resistanceUnit",NElementHelper.elem(resistanceUnit.get()))
                .add("temperatureUnit",NElementHelper.elem(temperatureUnit.get()))
                .add("timeUnit",NElementHelper.elem(timeUnit.get()))
                .add("voltageUnit",NElementHelper.elem(voltageUnit.get()))
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
