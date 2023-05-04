package net.thevpc.scholar.hadruplot.model.custom;

import net.thevpc.scholar.hadruplot.PlotPanel;
import net.thevpc.scholar.hadruplot.component.BasePlotPanel;
import net.thevpc.scholar.hadruplot.extension.PlotPanelFactory;
import net.thevpc.scholar.hadruplot.model.BasePlotModel;

import javax.swing.*;
import java.awt.*;

public class StringPlotModel extends BasePlotModel implements PlotPanelFactory {
    private String stringValue;

    public StringPlotModel(String ch) {
        this.stringValue = ch;
    }

    public String getStringValue() {
        return stringValue;
    }

    @Override
    public PlotPanel create() {
        BasePlotPanel p = new StringPlotPanel(stringValue);
        p.setModel(this);
        return p;
    }

    private static class StringPlotPanel extends TitleBasedPlotPanel {
        JTextArea text = new JTextArea("???");

        public StringPlotPanel(String value) {
            super();
            text.setEditable(false);
            text.setText(value);
            text.setFont(new Font("monospaced",Font.PLAIN,10));
            add(new JScrollPane(text), BorderLayout.CENTER);
        }

        @Override
        protected void modelChanged() {
            super.modelChanged();
            StringPlotModel m = (StringPlotModel) getModel();
            text.setText(m.stringValue);
        }
    }
}
