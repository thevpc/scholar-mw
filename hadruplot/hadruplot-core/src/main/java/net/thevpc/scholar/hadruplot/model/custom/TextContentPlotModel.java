package net.thevpc.scholar.hadruplot.model.custom;

import net.thevpc.scholar.hadruplot.PlotPanel;
import net.thevpc.scholar.hadruplot.TextContent;
import net.thevpc.scholar.hadruplot.component.BasePlotPanel;
import net.thevpc.scholar.hadruplot.extension.PlotPanelFactory;
import net.thevpc.scholar.hadruplot.model.BasePlotModel;

import javax.swing.*;
import java.awt.*;

public class TextContentPlotModel extends BasePlotModel implements PlotPanelFactory {
    private TextContent stringValue;

    public TextContentPlotModel(TextContent ch) {
        this.stringValue = ch;
    }

    public TextContent getStringValue() {
        return stringValue;
    }

    @Override
    public PlotPanel create() {
        BasePlotPanel p = new TextContentPlotPanel(stringValue);
        p.setModel(this);
        return p;
    }

    private static class TextContentPlotPanel extends TitleBasedPlotPanel {
        JTextArea text = new JTextArea("???");

        public TextContentPlotPanel(TextContent value) {
            super();
            text.setEditable(false);
            text.setText(value.getText());
            text.setFont(new Font("monospaced",Font.PLAIN,10));
            add(new JScrollPane(text), BorderLayout.CENTER);
        }

        @Override
        protected void modelChanged() {
            super.modelChanged();
            TextContentPlotModel m = (TextContentPlotModel) getModel();
            text.setText(m.stringValue.getText());
        }
    }
}
