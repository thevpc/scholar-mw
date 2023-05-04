package net.thevpc.scholar.hadruplot.libraries.laf;

import com.formdev.flatlaf.FlatLightLaf;
import net.thevpc.scholar.hadruplot.PlotUI;

import javax.swing.*;
import java.util.function.Supplier;

public class PlotUIFlat implements PlotUI {
    private String id;
    private Supplier<LookAndFeel> supplier;

    public PlotUIFlat(String id, Supplier<LookAndFeel> supplier) {
        this.id = id;
        this.supplier = supplier;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void install() {
        try {
            UIManager.setLookAndFeel(supplier.get());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }
    }

    @Override
    public void uninstall() {

    }
}
