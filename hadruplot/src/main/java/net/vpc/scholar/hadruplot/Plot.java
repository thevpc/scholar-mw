package net.vpc.scholar.hadruplot;

import net.vpc.common.io.FileUtils;
import net.vpc.common.strings.StringUtils;
import net.vpc.common.swings.*;
import net.vpc.common.util.*;
import net.vpc.scholar.hadruplot.actions.AbstractPlotAction;
import net.vpc.scholar.hadruplot.actions.CopyImageToClipboardAction;
import net.vpc.scholar.hadruplot.actions.LoadPlotAction;
import net.vpc.scholar.hadruplot.actions.SaveJFigAction;
import net.vpc.scholar.hadruplot.console.PlotComponentDisplayer;
import net.vpc.scholar.hadruplot.console.PlotConfigManager;
import net.vpc.scholar.hadruplot.console.PlotConsole;
import net.vpc.scholar.hadruplot.console.PlotManager;
import net.vpc.scholar.hadruplot.util.PlotSwingUtils;
import net.vpc.scholar.hadruplot.util.SimpleProducer;
import org.jfree.chart.ChartPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.Vector;

public final class Plot {

    public static final ColorPalette DEFAULT_COLOR_PALETTE = HSBColorPalette.DEFAULT_PALETTE;
    public static final ColorPalette GREY_COLOR_PALETTE = HSBColorPalette.GRAY_PALETTE;
    public static final ColorPalette SPECTRUM_COLOR_PALETTE = ColorPalette2.makeDefaultPalette("Spectrum");
    public static final ColorPalette PALE_SPECTRUM_COLOR_PALETTE = ColorPalette2.makeDefaultPalette("PaleSpectrum");
    public static final ColorPalette DARK_SPECTRUM_COLOR_PALETTE = ColorPalette2.makeDefaultPalette("DarkSpectrum");
    public static final ColorPalette GRAYSCALE_COLOR_PALETTE = ColorPalette2.makeDefaultPalette("Grayscale");
    public static final ColorPalette CYCLIC_GRAYSCALE_COLOR_PALETTE = ColorPalette2.makeDefaultPalette("CyclicGrayscale");
    public static final ColorPalette CYCLIC_RED_CYAN_COLOR_PALETTE = ColorPalette2.makeDefaultPalette("CyclicRedCyan");
    public static final ColorPalette EARTH_SKY_COLOR_PALETTE = ColorPalette2.makeDefaultPalette("EarthSky");
    public static final ColorPalette HOT_COLD_COLOR_PALETTE = ColorPalette2.makeDefaultPalette("HotCold");
    public static final ColorPalette FIRE_COLOR_PALETTE = ColorPalette2.makeDefaultPalette("Fire");
    /**
     * Java Figure
     */
    public static final String JFIG_FILE_EXTENSION = "jfig";
    /**
     * Java Figure
     */
    public static final String JFIGDATA_FILE_EXTENSION = "jfigdata";
    /**
     * Java Serializable Figure
     */
    public static final String JFIGOBJ_FILE_EXTENSION = "jfigobj";
    /**
     * Java Figure Bundle
     */
    public static final String JFB_FILE_EXTENSION = "jfb";
    private static PlotWindowManager defaultWindowManager = PlotWindowManagerFactory.createScatteredFrames();
    private static Map<String, PlotComponent> cachedPlotComponent = new LinkedHashMap<>();
    public static final PlotConfig Config = new PlotConfig();

    static {
        ServiceLoader<HadruplotService> loader = ServiceLoader.load(HadruplotService.class);
        TreeMap<Integer, java.util.List<HadruplotService>> all = new TreeMap<>();
        for (HadruplotService hadruplotService : loader) {
            HadruplotServiceDesc d = hadruplotService.getClass().getAnnotation(HadruplotServiceDesc.class);
            if (d == null) {
                throw new IllegalArgumentException("Missing @HadruplotServiceDesc for " + hadruplotService.getClass());
            }
            java.util.List<HadruplotService> values = all.get(d.order());
            if (values == null) {
                values = new ArrayList<HadruplotService>();
                all.put(d.order(), values);
            }
            values.add(hadruplotService);
        }

        for (Map.Entry<Integer, java.util.List<HadruplotService>> listEntry : all.entrySet()) {
            for (HadruplotService hadrumathsService : listEntry.getValue()) {
                hadrumathsService.installService();
            }
        }
    }

    private Plot() {
    }

    public static ColorPalette getColorPalette(String name) {
        for (ColorPalette p : getAvailableColorPalettes()) {
            if (p.getName().equals(name)) {
                return p;
            }
        }
        return HSBColorPalette.DEFAULT_PALETTE;
    }

    public static ColorPalette[] getAvailableColorPalettes() {
        return new ColorPalette[]{
                DEFAULT_COLOR_PALETTE,
                GREY_COLOR_PALETTE,
                SPECTRUM_COLOR_PALETTE,
                PALE_SPECTRUM_COLOR_PALETTE,
                DARK_SPECTRUM_COLOR_PALETTE,
                GRAYSCALE_COLOR_PALETTE,
                CYCLIC_GRAYSCALE_COLOR_PALETTE,
                CYCLIC_RED_CYAN_COLOR_PALETTE,
                EARTH_SKY_COLOR_PALETTE,
                HOT_COLD_COLOR_PALETTE,
                FIRE_COLOR_PALETTE,};
    }

    public static PlotContainer create(String container) {
        return getDefaultWindowManager().add(container);
    }

    public static PlotWindowManager getDefaultWindowManager() {
        return defaultWindowManager;
    }

    public static void setDefaultWindowManager(PlotWindowManagerProvider windowManager) {
        Plot.defaultWindowManager = windowManager == null ? null : windowManager.getPlotWindowManager();
    }

    public static void setDefaultWindowManager(PlotWindowManager windowManager) {
        Plot.defaultWindowManager = windowManager;
    }

    public static PlotContainer addPlotContainer(String name) {
        return getDefaultWindowManager().add(name);
    }

    public static PlotBuilder builder() {
        PlotManager m = Config.getManager();
        PlotBuilder b = null;
        if (m != null) {
            b = m.createPlotBuilder();
            if (b != null) {
                return b;
            }
        }
        return new PlotBuilder().cd("/" + Config.getDefaultWindowTitle());
    }

    public static PlotBuilder domain(PlotDomain domain) {
        return builder().domain(domain);
    }

    public static PlotBuilder windowManager(PlotWindowManager windowManager) {
        return builder().windowManager(windowManager);
    }

    public static PlotBuilder xname(String xname) {
        return builder().xname(xname);
    }

    public static PlotBuilder yname(String yname) {
        return builder().yname(yname);
    }

    public static PlotBuilder samples(int xsamples, int ysamples) {
        return builder().samples(xsamples, ysamples);
    }

    public static PlotBuilder samples(int xsamples, int ysamples, int zsample) {
        return builder().samples(xsamples, ysamples, zsample);
    }

    public static PlotBuilder xsamples(double[] values) {
        return builder().xsamples(values);
    }

    public static PlotBuilder ysamples(double[] values) {
        return builder().ysamples(values);
    }

    public static PlotBuilder zsamples(double[] values) {
        return builder().zsamples(values);
    }

    public static PlotBuilder xsamples(int value) {
        return builder().xsamples(value);
    }

    public static PlotBuilder title(String title) {
        return builder().title(title);
    }

    public static PlotBuilder name(String name) {
        return builder().name(name);
    }

    public static PlotBuilder titles(String... titles) {
        return builder().titles(titles);
    }

    public static PlotBuilder nodisplay() {
        return builder().nodisplay();
    }

    public static PlotBuilder display(PlotContainer parent) {
        return builder().display(parent);
    }

    public static PlotBuilder update(PlotComponent component) {
        return builder().update(component);
    }

    public static PlotBuilder update(String component) {
        return builder().update(component);
    }

    public static PlotBuilder display() {
        return builder().display();
    }

    public static PlotBuilder display(boolean display) {
        return builder().display(display);
    }

    public static PlotBuilder converter(PlotDoubleConverter converter) {
        return builder().converter(converter);
    }

    public static PlotBuilder asHeatMap() {
        return builder().asHeatMap();
    }

    public static PlotBuilder asMatrix() {
        return builder().asMatrix();
    }

    public static PlotBuilder asMesh() {
        return builder().asMesh();
    }

    public static PlotBuilder asTable() {
        return builder().asTable();
    }

    public static PlotBuilder asCurve() {
        return builder().asCurve();
    }

    public static PlotBuilder asBar() {
        return builder().asBar();
    }

    public static PlotBuilder asRing() {
        return builder().asRing();
    }

    public static PlotBuilder asBubble() {
        return builder().asBubble();
    }

    public static PlotBuilder asArea() {
        return builder().asArea();
    }

    public static PlotBuilder asPie() {
        return builder().asPie();
    }

    public static PlotBuilder asPolar() {
        return builder().asPolar();
    }

    public static PlotBuilder asField() {
        return builder().asField();
    }

    public static PlotBuilder asAbs() {
        return builder().asAbs();
    }

    public static PlotBuilder asImag() {
        return builder().asImag();
    }

    public static PlotBuilder asReal() {
        return builder().asReal();
    }

    public static PlotBuilder asDB() {
        return builder().asDB();
    }

    public static PlotBuilder asDB2() {
        return builder().asDB2();
    }

    public static PlotBuilder asArg() {
        return builder().asArg();
    }

    public static PlotBuilder plotType(PlotType plotType) {
        return builder().plotType(plotType);
    }

    public static PlotComponent plot(Object... obj) {
        return builder().plot(obj);
    }

    public static PlotBuilder ysamples(int value) {
        return builder().ysamples(value);
    }

    public static PlotBuilder zsamples(int value) {
        return builder().zsamples(value);
    }

    public static PlotConsole console() {
        final PlotConsole plotConsole = new PlotConsole();
        try {
            SwingUtilities3.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    plotConsole.show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return plotConsole;
    }

    public static PlotConsole console(boolean autoSave) {
        return new PlotConsole(autoSave);
    }

    public static PlotConsole console(File folder, boolean autoSave) {
        return new PlotConsole(folder, autoSave);
    }

    public static PlotConsole console(File folder) {
        return new PlotConsole(folder);
    }

    public static PlotConsole console(String title, File folder) {
        return new PlotConsole(title, folder);
    }

    public static void plot(File selectedFile, PlotComponentDisplayer context) throws IOException {
        PlotComponent plot = loadPlot(selectedFile);
//                            frame.getContentPane().setLayout(new BorderLayout());
//                            openFrames++;
//                            frame.addWindowListener(wa);
//                            frame.setTitle(selectedFile.getPath());

        if (context == null) {
            plot.display();
        } else {
            context.display(plot);
        }
    }

    public static boolean acceptExtension(String e) {
        for (PlotFileType plotFileType : PlotConfigManager.getPlotFileTypes()) {
            for (String extension : plotFileType.getExtensions()) {
                if (extension.equalsIgnoreCase(e)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean acceptFileByExtension(File file) {
        String e = FileUtils.getFileExtension(file);
        return acceptExtension(e);
    }

    public static PlotComponent loadPlot(File file) throws IOException {
        return create(loadPlotModel(file), Plot.getDefaultWindowManager());
    }

    public static PlotPanel create(PlotModel model, PlotWindowManager windowManager) {
        if (model == null) {
            throw new NullPointerException("Null Model");
        }
        for (PlotModelPanelFactory plotModelPanelFactory : PlotConfigManager.getPlotModelPanelFactories()) {
            if (plotModelPanelFactory.acceptModel(model)) {
                return plotModelPanelFactory.createPanel(model, windowManager);
            }
        }
        if (model instanceof ValuesPlotModel) {
            return new ValuesPlotPanel((ValuesPlotModel) model, windowManager);
        }
        if (model instanceof PlotModelList) {
            return new PlotModelListPanel((PlotModelList) model, windowManager);
        }
        if (model instanceof PlotPanel) {
            return (PlotPanel) model;
        }
        if (model instanceof PlotPanelFactory) {
            return ((PlotPanelFactory) model).create(model);
        }
        throw new IllegalArgumentException("Unsupported Model " + model);
    }

    public static void savePlot(File file, String preferredExtension, PlotModelProvider plotProvider) throws IOException {
        if (StringUtils.isBlank(preferredExtension)) {
            preferredExtension = FileUtils.getFileExtension(file).toLowerCase();
        } else {
            preferredExtension = preferredExtension.trim().toLowerCase();
            if (StringUtils.isBlank(preferredExtension)) {
                preferredExtension = JFIGOBJ_FILE_EXTENSION;
            }
        }
        if (!file.getName().toLowerCase().endsWith("." + preferredExtension)) {
            file = new File(file.getPath() + "." + preferredExtension);
        }

        for (PlotFileType plotFileType : PlotConfigManager.getPlotFileTypes()) {
            for (String extension : plotFileType.getExtensions()) {
                if (extension.equalsIgnoreCase(preferredExtension)) {
                    plotFileType.save(file, plotProvider);
                    return;
                }
            }
        }
        throw new IOException("Unsupported");
    }

    public static BufferedImage getImage(PlotComponent plotProvider) {
        Component c = plotProvider.toComponent();
        int graphWidth = c.getSize().width;
        int graphHeight = c.getSize().height;
        if (graphWidth > 0 && graphHeight > 0) {
            BufferedImage bi = new BufferedImage(graphWidth, graphHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = bi.createGraphics();
            c.paint(g2d);
            return bi;
        } else {
            BufferedImage bi = new BufferedImage(600, 500, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = bi.createGraphics();
            c.paint(g2d);
            return bi;
//            return null;
        }
    }

    public static BufferedImage getImage(PlotModelProvider plotProvider) {
        Component c = plotProvider.getComponent();
        int graphWidth = c.getSize().width;
        int graphHeight = c.getSize().height;
        if (graphWidth > 0 && graphHeight > 0) {
            BufferedImage bi = new BufferedImage(graphWidth, graphHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = bi.createGraphics();
            c.paint(g2d);
            return bi;
        } else {
            BufferedImage bi = new BufferedImage(600, 500, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = bi.createGraphics();
            c.paint(g2d);
            return bi;
//            return null;
        }
    }

    public static JPopupMenu buildJPopupMenu(PlotComponentPanel mainComponent, final PlotModelProvider modelProvider) {
        JPopupMenu popupMenu = mainComponent.getPopupMenu();
        if (popupMenu != null) {
            return buildJPopupMenu(popupMenu, new SimplePlotModelProvider(modelProvider.getModel(), mainComponent.toComponent()));
        }
        return null;
    }

    public static JPopupMenu buildJPopupMenu(JComponent mainComponent, final PlotModelProvider modelProvider) {
        JPopupMenu componentPopupMenu = null;
        if (mainComponent instanceof PlotComponentPanel) {
            componentPopupMenu = ((PlotComponentPanel) mainComponent).getPopupMenu();
        }
        if (componentPopupMenu == null) {
            if (mainComponent instanceof ChartPanel) {
                componentPopupMenu = ((ChartPanel) mainComponent).getPopupMenu();
            }
        }
        if (componentPopupMenu == null) {
            componentPopupMenu = mainComponent.getComponentPopupMenu();
        }
        if (componentPopupMenu == null) {
            componentPopupMenu = new JPopupMenu();
            mainComponent.setComponentPopupMenu(componentPopupMenu);
        } else {
            componentPopupMenu.addSeparator();
        }
        return buildJPopupMenu(mainComponent, modelProvider);
    }

    public static JPopupMenu buildJPopupMenu(final JPopupMenu componentPopupMenu, final PlotModelProvider modelProvider) {
        componentPopupMenu.addSeparator();
        JPopupMenu.setDefaultLightWeightPopupEnabled(false);
        boolean enableViewMenu = true;
        boolean enableLibMenu = true;

        JMenuItem selectY = new JMenuItem("Configure Series");
        selectY.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                configureSeries(modelProvider);
            }
        });
        JMenuItem extProperties = new JMenuItem("Extra Properties");

        JMenu functionsMenu = new JMenu("Function");
        componentPopupMenu.add(functionsMenu);

        JMenu viewMenu = null;
        if (enableViewMenu) {
            viewMenu = new JMenu("Chart Type");
        }
        if (viewMenu != null) {
            componentPopupMenu.add(viewMenu);
        }

        JMenu libMenu = null;
        if (enableLibMenu) {
            libMenu = new JMenu("Library");
        }
        if (libMenu != null) {
            componentPopupMenu.add(libMenu);
        }
        componentPopupMenu.add(extProperties);
        componentPopupMenu.add(selectY);

        ButtonGroup g;
        JCheckBoxMenuItem f;
        final PlotModel amodel = modelProvider.getModel();
        PlotModelPopupFactoryContext ctx = new PlotModelPopupFactoryContext(amodel, modelProvider, functionsMenu, viewMenu, libMenu, extProperties);
        for (PlotModelPopupFactory plotModelPopupFactory : PlotConfigManager.getPlotModelPopupFactories()) {
            plotModelPopupFactory.preparePopup(ctx);
        }
        componentPopupMenu.addSeparator();
        componentPopupMenu.add(new CopyImageToClipboardAction(modelProvider));
        componentPopupMenu.add(new SaveJFigAction(modelProvider));
        componentPopupMenu.add(new LoadPlotAction());
        PlotSwingUtils.onRefreshComponentTree(componentPopupMenu);
        return componentPopupMenu;
    }


    public static void configureSeries(final PlotModelProvider modelProvider) {
        PlotModel amodel = modelProvider.getModel();
        if (!(amodel instanceof ValuesPlotModel)) {
            return;
        }
        ValuesPlotModel model = (ValuesPlotModel) amodel;
        Number polarAngleOffset = (Number) model.getProperties().get("polarAngleOffset");
        Boolean polarClockwise = (Boolean) model.getProperties().get("polarClockwise");
        String[] ytitles = model.getYtitles();
        if ((ytitles == null || ytitles.length == 0) && model.getZ().length > 0) {
            ytitles = new String[model.getZ().length];
            for (int i = 0; i < ytitles.length; i++) {
                ytitles[i] = String.valueOf(i + 1);
            }
        }
        if (ytitles != null && ytitles.length > 0) {
            JTabbedPane jTabbedPane = new JTabbedPane();
            JPanel general = new JPanel(
                    new GridBagLayout2()
                            .addLine("[<showLegend     ][<defaultMaxLegendLabel][defaultMaxLegendText -]")
                            .addLine("[<alternateColor ][<nodeLabelCheckBox  ][<threeDCheckBox  ]")
                            .addLine("[<alternateNode  ][<defaultNodeTypeLabel][defaultNodeType - ][< zeroForDefaultsLabel2]")
                            .addLine("[<alternateLine  ][<defaultLineTypeLabel][defaultLineType - ][< zeroForDefaultsLabel1]")
                            .addLine("[<lineStepTypeLabel][lineStepTypeCombo -]")
                            .addLine("")
                            .addLine("[<polarOffsetLabel][polarOffsetText -][<clockwiseCheckBox  ]")
                            .setInsets(".*", new Insets(3, 3, 3, 3))
            );
            PlotViewConfig config = (PlotViewConfig) model.getProperty("config", null);
            if (config == null) {
                config = new PlotViewConfig();
                model.setProperty("config", config);
            }
            final JCheckBox showLegendCheckBox = new JCheckBox("Show Legend", config.showLegend.get(true));
            final JCheckBox clockwiseCheckBox = new JCheckBox("Clock Wise (polar)", config.clockwise.get(((polarClockwise == null ? true : polarClockwise.booleanValue()))));
            final JCheckBox threeDCheckBox = new JCheckBox("3D", config.threeD.get(false));
            final JCheckBox alternateColorCheckBox = new JCheckBox("Alternate Color", config.alternateColor.get(true));
            final JCheckBox alternateLineCheckBox = new JCheckBox("Alternate Line Type", config.alternateLine.get(false));
            final JCheckBox alternateNodeCheckBox = new JCheckBox("Alternate Node Type", config.alternateNode.get(false));
            final JCheckBox nodeLabelCheckBox = new JCheckBox("Show Labels", config.nodeLabel.get(false));
            final JSpinner defaultLineType = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1));
            final JSpinner defaultNodeType = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1));
            final JLabel defaultLineTypeLabel = new JLabel("Default Line Type");
            final JLabel defaultNodeTypeLabel = new JLabel("Default Node Type");
            final JLabel polarOffsetLabel = new JLabel("Polar Offset");
            final JLabel defaultMaxLegendLabel = new JLabel("Max Legend");
            final JLabel lineStepTypeLabel = new JLabel("Interpolation");
            final JTextField defaultMaxLegendText = new JTextField(String.valueOf(config.maxLegendCount.get(Plot.Config.getMaxLegendCount())));
            final JTextField polarOffsetText = new JTextField(String.valueOf(config.polarAngleOffset.get((polarAngleOffset == null ? 0 : polarAngleOffset.doubleValue()))));
            final JComboBox lineStepTypeCombo = new JComboBox(new Vector(Arrays.asList(PlotConfigLineStepType.values())));
            lineStepTypeCombo.setSelectedItem(config.lineStepType == null ? PlotConfigLineStepType.DEFAULT : config.lineStepType);
            defaultLineType.setEnabled(!alternateLineCheckBox.isSelected());
            defaultNodeType.setEnabled(!alternateNodeCheckBox.isSelected());
            ItemListener itemListener = new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    defaultLineType.setEnabled(!alternateLineCheckBox.isSelected());
                    defaultLineTypeLabel.setEnabled(!alternateLineCheckBox.isSelected());
                    defaultNodeType.setEnabled(!alternateNodeCheckBox.isSelected());
                    defaultNodeTypeLabel.setEnabled(!alternateNodeCheckBox.isSelected());
                }
            };
            alternateLineCheckBox.addItemListener(itemListener);
            alternateNodeCheckBox.addItemListener(itemListener);
            alternateColorCheckBox.addItemListener(itemListener);

            defaultLineType.setValue(config.lineType.get(0));
            defaultNodeType.setValue(config.nodeType.get(0));
            general.add(showLegendCheckBox, "showLegend");
            general.add(defaultLineTypeLabel, "defaultLineTypeLabel");
            general.add(defaultNodeTypeLabel, "defaultNodeTypeLabel");
            general.add(defaultLineType, "defaultLineType");
            general.add(defaultNodeType, "defaultNodeType");
            general.add(alternateColorCheckBox, "alternateColor");
            general.add(alternateNodeCheckBox, "alternateNode");
            general.add(nodeLabelCheckBox, "nodeLabelCheckBox");
            general.add(alternateLineCheckBox, "alternateLine");
            general.add(defaultMaxLegendLabel, "defaultMaxLegendLabel");
            general.add(defaultMaxLegendText, "defaultMaxLegendText");
            general.add(threeDCheckBox, "threeDCheckBox");
            general.add(clockwiseCheckBox, "clockwiseCheckBox");
            general.add(polarOffsetLabel, "polarOffsetLabel");
            general.add(polarOffsetText, "polarOffsetText");
            general.add(lineStepTypeLabel, "lineStepTypeLabel");
            general.add(lineStepTypeCombo, "lineStepTypeCombo");
            JLabel zeroForDefaultsLabel1 = new JLabel("<html><font size=-3 color=gray>(let zero for defaults)</font>");
            JLabel zeroForDefaultsLabel2 = new JLabel("<html><font size=-3 color=gray>(let zero for defaults)</font>");
            general.add(zeroForDefaultsLabel1, "zeroForDefaultsLabel1");
            general.add(zeroForDefaultsLabel2, "zeroForDefaultsLabel2");
            ModelSeriesItem[] lines = new ModelSeriesItem[ytitles.length];
            ColorPalette defaultPaintArray = PlotConfigManager.DEFAULT_PALETTE;
            config.ensureChildrenSize(ytitles.length);
            for (int i = 0; i < ytitles.length; i++) {
                lines[i] = new ModelSeriesItem();
                lines[i].setIndex(i);
                lines[i].setTitle(ytitles[i]);
                PlotViewConfig lineConfig = config.children.get(i);
                Color col = (Color) lineConfig.color;
                if (col == null) {
                    col = defaultPaintArray.getColor(((float) i) / ytitles.length);
                }
                lines[i].setColor(col);
                lines[i].setVisible(model.getYVisible(i));
                lines[i].setLineType(lineConfig.lineType.get(0));
                lines[i].setNodeType(lineConfig.nodeType.get(0));
                lines[i].setXmultiplier(lineConfig.xmultiplier.get(1));
                lines[i].setYmultiplier(lineConfig.ymultiplier.get(1));
            }
            jTabbedPane.addTab("General", general);
            ;

            final ModelSeriesModel seriesModel = new ModelSeriesModel(lines);
            JTableHelper series = SwingUtilities3.createIndexedTable(seriesModel);
            JButton cornerButton = new JButton("#");
            JMenuBar b = new JMenuBar();
            JMenu m = new JMenu("#");
            b.add(m);
            ValuesPlotTableModel tmodel = new ValuesPlotTableModel(modelProvider);
            JMenuItem select_all = new JMenuItem("select all");
            select_all.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    seriesModel.setSelectAll();
                }
            });
            m.add(select_all);
            JMenuItem select_none = new JMenuItem("select none");
            select_none.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    seriesModel.setSelectNone();
                }
            });
            m.add(select_none);
            series.getPane().setCorner(JScrollPane.UPPER_LEFT_CORNER, b);

            TableColumn column = series.getTable().getColumnModel().getColumn(4);

//            ColorChooserEditor editor = new ColorChooserEditor();
            column.setCellEditor(new ColorChooserEditor());
            column.setCellRenderer(new ColorChooserEditor());
//            series.getPane().setCorner(JScrollPane.UPPER_TRAILING_CORNER, cornerButton);

            jTabbedPane.addTab("Series", series.getPane());

            jTabbedPane.addTab("Data", SwingUtilities3.createIndexedTable(tmodel).getPane());
            if (JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(null, jTabbedPane, "Configure Series...", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE)) {
                config.showLegend.set(showLegendCheckBox.isSelected());
                config.alternateColor.set(alternateColorCheckBox.isSelected());
                config.alternateNode.set(alternateNodeCheckBox.isSelected());
                config.alternateLine.set(alternateLineCheckBox.isSelected());
                config.threeD.set(threeDCheckBox.isSelected());
                config.nodeLabel.set(nodeLabelCheckBox.isSelected());
                config.clockwise.set(clockwiseCheckBox.isSelected());
                config.polarAngleOffset.set(Convert.toDouble(polarOffsetText.getText(), DoubleParserConfig.LENIENT));
                config.nodeType.set(((Number) defaultNodeType.getValue()).intValue());
                config.lineType.set(((Number) defaultLineType.getValue()).intValue());
                config.maxLegendCount.set(Convert.toInt(defaultMaxLegendText.getText(), IntegerParserConfig.LENIENT));
                config.lineStepType = (PlotConfigLineStepType) lineStepTypeCombo.getSelectedItem();

                for (int i = 0; i < ytitles.length; i++) {
                    model.setYVisible(i, lines[i].isVisible());
                    PlotViewConfig lineConfig = config.children.get(i);
                    lineConfig.lineType.set(lines[i].getLineType());
                    lineConfig.nodeType.set(lines[i].getNodeType());
                    lineConfig.xmultiplier.set(lines[i].getXmultiplier().doubleValue());
                    lineConfig.ymultiplier.set(lines[i].getYmultiplier().doubleValue());
                    lineConfig.color = lines[i].getColor();
                }
                model.modelUpdated();
            }
        }
    }

    public static PlotModel loadPlotModel(File file) {
        String e = FileUtils.getFileExtension(file).toLowerCase();
        for (PlotFileType plotFileType : PlotConfigManager.getPlotFileTypes()) {
            for (String extension : plotFileType.getExtensions()) {
                if (extension.equalsIgnoreCase(e)) {
                    return plotFileType.loadModel(file);
                }
            }
        }
        throw new UnsupportedOperationException("Unsupported loading for file " + file);
    }

    public static PlotBuilder samples(Samples samples) {
        return builder().samples(samples);
    }

    public static PlotBuilder xformat(DoubleFormat format) {
        return builder().xformat(format);
    }

    public static PlotBuilder xformat(String format) {
        return builder().xformat(format);
    }

    public static PlotBuilder yformat(String format) {
        return builder().yformat(format);
    }

    public static PlotComponent getCachedPlotComponent(String name) {
        return cachedPlotComponent.get(name);
    }

    public static PlotComponent setCachedPlotComponent(String name, PlotComponent component) {
        if (component == null) {
            cachedPlotComponent.remove(name);
        } else {
            cachedPlotComponent.put(name, component);
        }
        return component;
    }

    public static PlotBuilder plotBuilderListener(PlotBuilderListener listener) {
        return builder().addPlotBuilderListener(listener);
    }

    public static PlotBuilder cd(String path) {
        return builder().cd(path);
    }

    public static class DoubleTypeAction extends ValuesModelAction implements Serializable {

        private PlotDoubleConverter type;

        public DoubleTypeAction(PlotModelProvider modelProvider, String name, PlotDoubleConverter type) {
            super(name, modelProvider);
            this.type = type;
        }

        public void actionPerformed(ActionEvent e) {
            getModel().setConverter(type);
        }
    }

    //    private static class PlotMeshAction extends ValuesModelAction {
//        public PlotMeshAction(PlotModelProvider modelProvider) {
//            super("Mesh 3D", modelProvider);
//        }
//
//
//        public void actionPerformed(ActionEvent e) {
//            getModel().setPlotType(PlotType.MESH);
//        }
//    }
//
//    private static class PlotMatrixAction extends ValuesModelAction {
//        public PlotMatrixAction(PlotModelProvider modelProvider) {
//            super("Matrix", modelProvider);
//        }
//
//
//        public void actionPerformed(ActionEvent e) {
//            getModel().setPlotType(PlotType.MATRIX);
//        }
//    }
//
//    private static class PlotPolarAction extends ValuesModelAction {
//        public PlotPolarAction(PlotModelProvider modelProvider) {
//            super("Polar", modelProvider);
//        }
//
//
//        public void actionPerformed(ActionEvent e) {
//            getModel().setPlotType(PlotType.POLAR);
//        }
//    }
    private static abstract class ModelAction extends AbstractPlotAction implements Serializable {

        PlotModelProvider modelProvider;

        private ModelAction(String name, PlotModelProvider modelProvider) {
            super(name);
            this.modelProvider = modelProvider;
        }

        public PlotModelProvider getModelProvider() {
            return modelProvider;
        }

        public PlotModel getModel() {
            return getModelProvider().getModel();
        }
    }

    private static abstract class ValuesModelAction extends ModelAction {

        public ValuesModelAction(String name, PlotModelProvider modelProvider) {
            super(name, modelProvider);
        }

        public ValuesPlotModel getModel() {
            return (ValuesPlotModel) getModelProvider().getModel();
        }
    }

    public static class PlotTypeAction extends ValuesModelAction {

        private PlotType plotType;

        public PlotTypeAction(PlotModelProvider modelProvider, String name, PlotType plotType) {
            super(name, modelProvider);
            this.plotType = plotType;
        }

        public PlotType getPlotType() {
            return plotType;
        }

        public void actionPerformed(ActionEvent e) {
            getModel().setPlotType(plotType);
        }
    }

    public static class PlotLibraryAction extends ValuesModelAction {

        private SimpleProducer<Set<String>> libs;

        public PlotLibraryAction(PlotModelProvider modelProvider, String name, SimpleProducer<Set<String>> libs) {
            super(name, modelProvider);
            this.libs = libs;
        }

        public void actionPerformed(ActionEvent e) {
            Set<String> libs = this.libs.produce();
            if (libs.isEmpty()) {
                getModel().setLibraries("");
            } else {
                StringBuilder sb = new StringBuilder();
                for (PlotLibrary library : PlotBackendLibraries.getLibraries()) {
                    if (!libs.contains(library.getName())) {
                        if (sb.length() > 0) {
                            sb.append(",");
                        }
                        sb.append("!").append(library.getName());
                    }
                }
                getModel().setLibraries(sb.toString());
            }
        }
    }

    public static BufferedImage createImage(Component c) {
        int graphWidth = c.getSize().width;
        int graphHeight = c.getSize().height;
        if (graphWidth > 0 && graphHeight > 0) {
            BufferedImage bi = new BufferedImage(graphWidth, graphHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = bi.createGraphics();
            c.paint(g2d);
            return bi;
        } else {
            BufferedImage bi = new BufferedImage(600, 500, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = bi.createGraphics();
            c.paint(g2d);
            return bi;
        }
    }

    public static void saveImageFile(PlotComponent component, String file) {
        final File f = FileUtils.expandFile(file);
        final String n = f.getName().toLowerCase();
        if (n.endsWith(".png")) {
            BufferedImage img = component.getImage();
            if (img != null) {
                try {
                    ImageIO.write(img, "PNG", f);
                    return;
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
            throw new RuntimeException("Unable to save image " + f);
        }
        if (n.endsWith(".jpg") || n.endsWith(".jpeg")) {
            BufferedImage img = component.getImage();
            if (img != null) {
                try {
                    ImageIO.write(img, "JPG", f);
                    return;
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
            throw new RuntimeException("Unable to save image " + f);
        }
        throw new RuntimeException("Unable to save image " + f);
    }
}
