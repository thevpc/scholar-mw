/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.props.trees;

import net.thevpc.echo.impl.PValueViewProperty;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.util.HWProjectItem;
import net.thevpc.echo.impl.DefaultPropertiesNodeFolder;
import net.thevpc.scholar.hadrumaths.units.CapacitanceUnit;
import net.thevpc.scholar.hadrumaths.units.ConductanceUnit;
import net.thevpc.scholar.hadrumaths.units.CurrentUnit;
import net.thevpc.scholar.hadrumaths.units.FrequencyUnit;
import net.thevpc.scholar.hadrumaths.units.InductanceUnit;
import net.thevpc.scholar.hadruwaves.project.HWProject;
import net.thevpc.scholar.hadrumaths.units.LengthUnit;
import net.thevpc.scholar.hadrumaths.units.PowerUnit;
import net.thevpc.scholar.hadrumaths.units.ResistanceUnit;
import net.thevpc.scholar.hadrumaths.units.TemperatureUnit;
import net.thevpc.scholar.hadrumaths.units.TimeUnit;
import net.thevpc.scholar.hadrumaths.units.VoltageUnit;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.util.DefaultHWPropertiesTree;

/**
 *
 * @author vpc
 */
public class HWProjectPropertiesAware extends DefaultHWPropertiesTree {

    private final HWProject wp;
    private final HWProjectItem outer;
    private DefaultPropertiesNodeFolder root;

    public HWProjectPropertiesAware(HWProject wp, final HWProjectItem outer) {
        super(outer);
        this.outer = outer;
        this.wp = wp;

        refresh();
    }

    public void refresh() {

        root = new DefaultPropertiesNodeFolder("folder", "Project", wp);
        setRoot(root);
        DefaultPropertiesNodeFolder folder = root.addFolder("folder", "General");
        folder.add(new PValueViewProperty("property", outer.getStudio().app(), wp.name()));
        folder.add(new PValueViewProperty("property", outer.getStudio().app(), wp.description()));

        folder = root.addFolder("folder", "Paths");
        folder.add(new PValueViewProperty("property", outer.getStudio().app(), wp.filePath()));
        folder = root.addFolder("folder", "Units");
        folder.add(new PValueViewProperty("property", outer.getStudio().app(), wp.units().lengthUnit(), LengthUnit.values()));
        folder.add(new PValueViewProperty("property", outer.getStudio().app(), wp.units().frequencyUnit(), FrequencyUnit.values()));
        folder.add(new PValueViewProperty("property", outer.getStudio().app(), wp.units().timeUnit(), TimeUnit.values()));
        folder.add(new PValueViewProperty("property", outer.getStudio().app(), wp.units().currentUnit(), CurrentUnit.values()));
        folder.add(new PValueViewProperty("property", outer.getStudio().app(), wp.units().voltageUnit(), VoltageUnit.values()));
        folder.add(new PValueViewProperty("property", outer.getStudio().app(), wp.units().resistanceUnit(), ResistanceUnit.values()));
        folder.add(new PValueViewProperty("property", outer.getStudio().app(), wp.units().powerUnit(), PowerUnit.values()));
        folder.add(new PValueViewProperty("property", outer.getStudio().app(), wp.units().capacitanceUnit(), CapacitanceUnit.values()));
        folder.add(new PValueViewProperty("property", outer.getStudio().app(), wp.units().conductanceUnit(), ConductanceUnit.values()));
        folder.add(new PValueViewProperty("property", outer.getStudio().app(), wp.units().inductanceUnit(), InductanceUnit.values()));
        folder.add(new PValueViewProperty("property", outer.getStudio().app(), wp.units().temperatureUnit(), TemperatureUnit.values()));
    }

}
