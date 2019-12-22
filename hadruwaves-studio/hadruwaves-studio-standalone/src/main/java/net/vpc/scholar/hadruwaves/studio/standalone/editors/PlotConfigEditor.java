package net.vpc.scholar.hadruwaves.studio.standalone.editors;

import net.vpc.scholar.hadruwaves.studio.standalone.ECExpressionField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import net.vpc.lib.pheromone.application.swing.DataTypeEditor;
import net.vpc.lib.pheromone.application.swing.ECGroupPanel;
import net.vpc.lib.pheromone.application.swing.ECRadioButton;
import net.vpc.lib.pheromone.application.swing.ECTextField;
import net.vpc.lib.pheromone.application.swing.Swings;
import net.vpc.scholar.hadruwaves.studio.standalone.TMWLabApplication;
import net.vpc.scholar.hadruwaves.mom.project.MomProject;

/**
 * Created by IntelliJ IDEA. User: Taha Date: 3 aout 2003 Time: 01:19:06 To
 * change this template use Options | File Templates.
 */
public class PlotConfigEditor extends JPanel {

    private JRadioButton plotFxCheckBox = new ECRadioButton("plotFxCheckBox");
    private JRadioButton plotFyCheckBox = new ECRadioButton("plotFyCheckBox");

    private JRadioButton xLeadingRadioButton = new ECRadioButton("plotFyCheckBox");
    private JRadioButton yLeadingRadioButton = new ECRadioButton("plotFyCheckBox");
    private JRadioButton surfaceRadioButton = new ECRadioButton("plotFyCheckBox");

    private ECExpressionField plotYmin = new ECExpressionField("plotXmin", false);
    private ECExpressionField plotYPoint = new ECExpressionField("plotYPoint", false);
    private ECExpressionField plotYmax = new ECExpressionField("plotYmax", false);
    private ECExpressionField plotYcount = new ECExpressionField("plotYcount", false);

    private ECExpressionField plotXmin = new ECExpressionField("plotYmin", false);
    private ECExpressionField plotXmax = new ECExpressionField("plotYmax", false);
    private ECExpressionField plotXPoint = new ECExpressionField("plotYPoint", false);
    private ECExpressionField plotXcount = new ECExpressionField("plotYcount", false);

    private ECExpressionField plotZmin = new ECExpressionField("plotYmin", false);
    private ECExpressionField plotZmax = new ECExpressionField("plotYmax", false);
    private ECExpressionField plotZPoint = new ECExpressionField("plotYPoint", false);
    private ECExpressionField plotZcount = new ECExpressionField("plotYcount", false);

//    private ECExpressionField plotFreqMin = new ECExpressionField("plotFreqMin", false);
//    private ECExpressionField plotFreqMax = new ECExpressionField("plotFreqMax", false);
//    private ECExpressionField plotFreqCount = new ECExpressionField("plotFreqCount", false);
    private ECTextField iterName = new ECTextField("iterName", false);
    private ECExpressionField iterMin = new ECExpressionField("iterMin", false);
    private ECExpressionField iterMax = new ECExpressionField("iterMax", false);
    private ECExpressionField iterCount = new ECExpressionField("iterCount", false);

    private ECTextField plotIndex1 = new ECTextField("index1", 16, 1, 40, false);
    private ECTextField plotIndex2 = new ECTextField("index2", 16, 1, 40, false);
    private ECExpressionField relativeErr = new ECExpressionField("relativeErr", false);

    private JComponent iterPanel;
//    private JComponent plotFreqPanel;
    private JComponent plotXPanel;
    private JComponent plotZPanel;
    private JComponent plotYPanel;
    private JComponent plotLeadingPanel;
    private JComponent plotAxisPanel;

    private JComponent plotZ0Panel;
    private JComponent plotRelativeErrPanel;

    public void setVisibleIterPanel(boolean b) {
        iterPanel.setVisible(b);
    }

//    public void setVisibleFreqPanel(boolean b) {
//        plotFreqPanel.setVisible(b);
//    }
    public void setVisibleXPanel(boolean b) {
        plotXPanel.setVisible(b);
    }

    public void setVisibleZPanel(boolean b) {
        plotZPanel.setVisible(b);
    }

    public void setVisibleZ0Panel(boolean b) {
        plotZ0Panel.setVisible(b);
    }

    public void setVisibleYPanel(boolean b) {
        plotYPanel.setVisible(b);
    }

    public void setVisibleLeadingPanel(boolean b) {
        plotLeadingPanel.setVisible(b);
    }

    public void setVisibleAxisPanel(boolean b) {
        plotAxisPanel.setVisible(b);
    }

    public void setVisibleRelativeErrPanel(boolean b) {
        plotRelativeErrPanel.setVisible(b);
    }

    public PlotConfigEditor(MomProjectEditor editor) {
        super(new BorderLayout());
        ECGroupPanel panel = new ECGroupPanel();

        ButtonGroup leading = new ButtonGroup();
        leading.add(xLeadingRadioButton);
        leading.add(yLeadingRadioButton);
        leading.add(surfaceRadioButton);

        ButtonGroup comp = new ButtonGroup();
        comp.add(plotFxCheckBox);
        comp.add(plotFyCheckBox);

        xLeadingRadioButton.setText(editor.getResources().get("plot.xleading"));
        yLeadingRadioButton.setText(editor.getResources().get("plot.yleading"));
        surfaceRadioButton.setText(editor.getResources().get("plot.surface"));

        plotFxCheckBox.setText(editor.getResources().get("plot.plotFx"));
        plotFyCheckBox.setText(editor.getResources().get("plot.plotFy"));

        plotXmin.getHelper().setDescription(editor.getResources().get("plot.Xmin"));
        plotXPoint.getHelper().setDescription(editor.getResources().get("plot.XPoint"));
        plotXmax.getHelper().setDescription(editor.getResources().get("plot.Xmax"));
        plotXcount.getHelper().setDescription(editor.getResources().get("plot.Xcount"));

        plotYmin.getHelper().setDescription(editor.getResources().get("plot.Ymin"));
        plotYPoint.getHelper().setDescription(editor.getResources().get("plot.YPoint"));
        plotYmax.getHelper().setDescription(editor.getResources().get("plot.Ymax"));
        plotYcount.getHelper().setDescription(editor.getResources().get("plot.Ycount"));

//        plotFreqMin.setDescription(application.getResources().get("plot.FreqMin"));
//        plotFreqMax.setDescription(application.getResources().get("plot.FreqMax"));
//        plotFreqCount.setDescription(application.getResources().get("plot.FreqCount"));
        iterName.getHelper().setDescription(editor.getResources().get("plot.IterName"));
        iterMin.getHelper().setDescription(editor.getResources().get("plot.IterMin"));
        iterMax.getHelper().setDescription(editor.getResources().get("plot.IterMax"));
        iterCount.getHelper().setDescription(editor.getResources().get("plot.IterCount"));

        plotIndex1.getHelper().setDescription("Indice 1");
        plotIndex2.getHelper().setDescription("Indice 2");
        relativeErr.getHelper().setDescription(editor.getResources().get("plot.relativeErr"));

        plotAxisPanel = new JPanel(new GridLayout(1, 2));
        plotAxisPanel.add(plotFxCheckBox);
        plotAxisPanel.add(plotFyCheckBox);
        panel.add(plotAxisPanel);

        plotAxisPanel.setBorder(BorderFactory.createTitledBorder(editor.getResources().get("PlotConfigData.plotAxisPanel")));
        plotAxisPanel.setVisible(false);

        plotLeadingPanel = new JPanel(new GridLayout(1, 3));
        plotLeadingPanel.add(xLeadingRadioButton);
        plotLeadingPanel.add(yLeadingRadioButton);
        plotLeadingPanel.add(surfaceRadioButton);
        panel.add(plotLeadingPanel);
        plotLeadingPanel.setBorder(BorderFactory.createTitledBorder(editor.getResources().get("PlotConfigData.plotLeadingPanel")));
        plotLeadingPanel.setVisible(false);

        plotXPanel = panel.add(new DataTypeEditor[]{plotXPoint, plotXmin, plotXmax, plotXcount}, 1);
        plotXPanel.setBorder(BorderFactory.createTitledBorder(editor.getResources().get("PlotConfigData.plotXPanel")));
        plotXPanel.setVisible(false);

        plotYPanel = panel.add(new DataTypeEditor[]{plotYPoint, plotYmin, plotYmax, plotYcount}, 1);
        plotYPanel.setBorder(BorderFactory.createTitledBorder(editor.getResources().get("PlotConfigData.plotYPanel")));
        plotYPanel.setVisible(false);

        plotZPanel = panel.add(new DataTypeEditor[]{plotZPoint, plotZmin, plotZmax, plotZcount}, 1);
        plotZPanel.setBorder(BorderFactory.createTitledBorder(editor.getResources().get("PlotConfigData.plotZPanel")));
        plotZPanel.setVisible(false);

//        plotFreqPanel = panel.add(new DataTypeEditor[]{plotFreqMin, plotFreqMax, plotFreqCount}, 1);
//        plotFreqPanel.setBorder(BorderFactory.createTitledBorder(application.getResources().get("PlotConfigData.plotFreqPanel")));
//        plotFreqPanel.setVisible(false);
        iterPanel = panel.add(new DataTypeEditor[]{iterName, iterMin, iterMax, iterCount}, 1);
        iterPanel.setBorder(BorderFactory.createTitledBorder(editor.getResources().get("PlotConfigData.iterPanel")));
        iterPanel.setVisible(false);

        plotZ0Panel = panel.add(new DataTypeEditor[]{plotIndex1, plotIndex2}, 1);
        plotZ0Panel.setBorder(BorderFactory.createTitledBorder(editor.getResources().get("PlotConfigData.plotZ0Panel")));
        plotZ0Panel.setVisible(false);

        plotRelativeErrPanel = panel.add(new DataTypeEditor[]{relativeErr}, 1);
        plotRelativeErrPanel.setBorder(BorderFactory.createTitledBorder(editor.getResources().get("PlotConfigData.plotRelativeErrPanel")));
        plotRelativeErrPanel.setVisible(false);

        add(panel);
        plotFxCheckBox.setSelected(true);
        ActionListener leadingListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                revalidateDependencies();
            }
        };
        surfaceRadioButton.setSelected(true);
        xLeadingRadioButton.addActionListener(leadingListener);
        yLeadingRadioButton.addActionListener(leadingListener);
        surfaceRadioButton.addActionListener(leadingListener);
        resetValues();
    }

    public void revalidateDependencies() {
        boolean xl = xLeadingRadioButton.isSelected();
        boolean yl = yLeadingRadioButton.isSelected();
        boolean surf = surfaceRadioButton.isSelected();

        plotXPoint.setVisible(true);
        plotYPoint.setVisible(true);
        plotZPoint.setVisible(true);
//        plotYmin.setEnabled(surf||yl);
//        plotYmax.setEnabled(surf||yl);
//        plotYcount.setEnabled(surf||yl);
//        plotYPoint.setEnabled(xl);
//
//        plotXmin.setEnabled(surf||xl);
//        plotXmax.setEnabled(surf||xl);
//        plotXcount.setEnabled(surf||xl);
//        plotXPoint.setEnabled(yl);

        plotXmin.setVisible(surf || xl);
        plotXmin.getHelper().getRelativeLabel().setVisible(surf || xl);
        plotXmax.setVisible(surf || xl);
        plotXmax.getHelper().getRelativeLabel().setVisible(surf || xl);
        plotXcount.setVisible(surf || xl);
        plotXcount.getHelper().getRelativeLabel().setVisible(surf || xl);
        plotXPoint.setVisible(yl);
        plotXPoint.getHelper().getRelativeLabel().setVisible(yl);

        plotYmin.setVisible(surf || yl);
        plotYmin.getHelper().getRelativeLabel().setVisible(surf || yl);
        plotYmax.setVisible(surf || yl);
        plotYmax.getHelper().getRelativeLabel().setVisible(surf || yl);
        plotYcount.setVisible(surf || yl);
        plotYcount.getHelper().getRelativeLabel().setVisible(surf || yl);
        plotYPoint.setVisible(xl);
        plotYPoint.getHelper().getRelativeLabel().setVisible(xl);
        JDialog d = (JDialog) SwingUtilities.getAncestorOfClass(JDialog.class, this);
        if (d != null) {
            d.pack();
        }
    }

    public void setPlotConfig(PlotConfigData config) {
        plotFxCheckBox.setSelected(config.plotFx);
        plotFyCheckBox.setSelected(config.plotFy);
        xLeadingRadioButton.setSelected(config.isXLeading());
        yLeadingRadioButton.setSelected(config.isYLeading());
        surfaceRadioButton.setSelected(config.isSurface());

        plotXmin.getHelper().setObject(config.xMinExpression);
        plotXPoint.getHelper().setObject(config.xPointExpression);
        plotXmax.getHelper().setObject(config.xMaxExpression);
        plotXcount.getHelper().setObject(config.xCountExpression);

        plotZmin.getHelper().setObject(config.zMinExpression);
        plotZPoint.getHelper().setObject(config.zPointExpression);
        plotZmax.getHelper().setObject(config.zMaxExpression);
        plotZcount.getHelper().setObject(config.zCountExpression);

        plotYmin.getHelper().setObject(config.yMinExpression);
        plotYPoint.getHelper().setObject(config.yPointExpression);
        plotYmax.getHelper().setObject(config.yMaxExpression);
        plotYcount.getHelper().setObject(config.yCountExpression);

//        plotFreqMin.setObject(config.freqMinExpression);
//        plotFreqMax.setObject(config.freqMaxExpression);
//        plotFreqCount.setObject(config.freqCountExpression);
        iterName.getHelper().setObject(config.iterName);
        iterMin.getHelper().setObject(config.iterMinExpression);
        iterMax.getHelper().setObject(config.iterMaxExpression);
        iterCount.getHelper().setObject(config.iterCountExpression);

        plotIndex1.getHelper().setObject(config.index1 == null || config.index1.length() == 0 ? "1" : config.index1);
        plotIndex2.getHelper().setObject(config.index2 == null || config.index2.length() == 0 ? "1" : config.index2);
        relativeErr.getHelper().setObject(config.relativeErrExpression == null ? "0.1" : config.relativeErrExpression);

        revalidateDependencies();
    }

    public PlotConfigData getPlotConfig() {
        PlotConfigData plotConfig = new PlotConfigData();
        plotConfig.plotFx = plotFxCheckBox.isSelected();
        plotConfig.plotFy = plotFyCheckBox.isSelected();
        plotConfig.xMinExpression = plotXmin.getString();
        plotConfig.xPointExpression = plotXPoint.getString();
        plotConfig.xMaxExpression = plotXmax.getString();
        plotConfig.xCountExpression = plotXcount.getString();
        plotConfig.yMinExpression = plotYmin.getString();
        plotConfig.yPointExpression = plotYPoint.getString();
        plotConfig.yMaxExpression = plotYmax.getString();
        plotConfig.yCountExpression = plotYcount.getString();
        plotConfig.zMinExpression = plotZmin.getString();
        plotConfig.zPointExpression = plotZPoint.getString();
        plotConfig.zMaxExpression = plotZmax.getString();
        plotConfig.zCountExpression = plotZcount.getString();

//        plotConfig.freqMinExpression = plotFreqMin.getString();
//        plotConfig.freqMaxExpression = plotFreqMax.getString();
//        plotConfig.freqCountExpression = plotFreqCount.getString();
        plotConfig.iterName = iterName.getString();
        plotConfig.iterMinExpression = iterMin.getString();
        plotConfig.iterMaxExpression = iterMax.getString();
        plotConfig.iterCountExpression = iterCount.getString();

        plotConfig.index1 = plotIndex1.getString();
        plotConfig.index2 = plotIndex2.getString();
        plotConfig.relativeErrExpression = relativeErr.getString();
        plotConfig.leadingX = xLeadingRadioButton.isSelected();
        plotConfig.leadingY = yLeadingRadioButton.isSelected();
        return plotConfig;
    }

    public void resetValues() {
        plotXmin.getHelper().setObject("0");
        plotXPoint.getHelper().setObject("0");
        plotXmax.getHelper().setObject("0");
        plotXcount.getHelper().setObject("1");

        plotYmin.getHelper().setObject("0");
        plotYPoint.getHelper().setObject("0");
        plotYmax.getHelper().setObject("0");
        plotYcount.getHelper().setObject("1");

//        plotFreqMin.setObject("0");
//        plotFreqMax.setObject("0");
//        plotFreqCount.setObject("1");
        iterName.getHelper().setObject("ITER");
        iterMin.getHelper().setObject("0");
        iterMax.getHelper().setObject("0");
        iterCount.getHelper().setObject("1");

        plotIndex1.getHelper().setObject("1");
        plotIndex2.getHelper().setObject("1");
        relativeErr.getHelper().setObject("0.1");
        revalidateDependencies();
    }

    public PlotConfigData showDialog(Component parent, String tile, MomProject str) {
        PlotConfigData config = new PlotConfigData();
        config.setContext(str);
        config.loadConfig(str.getConfiguration(), "plot");
        setPlotConfig(config);
        while (true) {
            int ret = JOptionPane.showOptionDialog(parent, this, tile, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, new String[]{
                Swings.getResources().get("ok"),
                Swings.getResources().get("apply"),
                Swings.getResources().get("cancel")
            }, Swings.getResources().get("ok"));
            if (ret == 2 || ret == -1) {
                return null;
            } else if (ret == 1) {
                config = getPlotConfig();
                config.setContext(str);
                config.recompile();
                config.saveConfig(str.getConfiguration(), "plot");
            } else {
                config = getPlotConfig();
                config.setContext(str);
                config.recompile();
                //config.saveConfig(configuration,"plot");
                return config;
            }
        }
    }
}
