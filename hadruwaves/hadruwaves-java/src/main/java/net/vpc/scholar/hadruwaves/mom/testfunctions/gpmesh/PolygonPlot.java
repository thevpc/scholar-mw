package net.vpc.scholar.hadruwaves.mom.testfunctions.gpmesh;

import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.geom.*;
import net.vpc.scholar.hadrumaths.geom.Polygon;
import net.vpc.scholar.hadrumaths.meshalgo.MeshAlgo;
import net.vpc.scholar.hadrumaths.meshalgo.MeshZone;
import net.vpc.scholar.hadrumaths.meshalgo.rect.MeshAlgoRect;
import net.vpc.scholar.hadrumaths.meshalgo.triconsdes.MeshConsDesAlgo;
import net.vpc.scholar.hadrumaths.plot.swings.GridBagLayout2;
import net.vpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern.GpPattern;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Arrays;
import java.util.BitSet;


/**
 * @author : vpc
 * @creationtime 18 janv. 2006 14:51:22
 */
public class PolygonPlot extends JPanel {

    private BitSet selected;
    private GeometryList currentGeometryList;
    private PolygonComponent polygonComponent;
    private JTable selectionTable = new JTable(new AbstractTableModel() {
        @Override
        public int getRowCount() {
            return polygonComponent.getMeshZones().size();
        }

        @Override
        public int getColumnCount() {
            return 2;
        }

        @Override
        public String getColumnName(int columnIndex) {
            switch (columnIndex) {
                case 0: {
                    return "selected";
                }
                case 1: {
                    return "Mesh";
                }
            }
            return null;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            switch (columnIndex) {
                case 0: {
                    return Boolean.class;
                }
                case 1: {
                    return String.class;
                }
            }
            return Object.class;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            switch (columnIndex) {
                case 0: {
                    return true;
                }
                case 1: {
                    return false;
                }
            }
            return false;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            switch (columnIndex) {
                case 0: {
                    return selected.get(rowIndex);
                }
                case 1: {
                    return "Polygon " + String.valueOf(rowIndex + 1);
                }
            }
            return null;
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            switch (columnIndex) {
                case 0: {
                    selected.set(rowIndex, (Boolean) aValue);
                    polygonComponent.getMeshZones().get(rowIndex).setEnabled((Boolean) aValue);
                    updatePolygonPainting();
                    break;
                }
                case 1: {
//
                }
            }
        }
    });

    private Color polygonForegroundColor = Color.YELLOW;
    private Color polygonEdgeColor = Color.BLACK;
    private Color gridForegroundColor = Color.BLUE;
    private Color gridMeshMaxColor = Color.DARK_GRAY;
    private Color gridMeshMinColor = Color.GRAY;
    private Color gridEdgeColor = Color.RED;

    JCheckBox fillArea = new JCheckBox("Fill Area", true);
    JCheckBox drawArea = new JCheckBox("Draw Area", true);
    JCheckBox fillGrid = new JCheckBox("Fill Grid", true);
    JCheckBox drawGrid = new JCheckBox("Draw Grid", true);
    JCheckBox meshMin = new JCheckBox("Mesh Min", true);
    JCheckBox meshMax = new JCheckBox("Mesh Max", true);
    JCheckBox drawMain = new JCheckBox("Draw Main", true);
    JCheckBox drawAttachX = new JCheckBox("draw AttachX", true);
    JCheckBox drawAttachY = new JCheckBox("draw AttachY", true);
    JCheckBox drawAttachNorth = new JCheckBox("draw Attach North", true);
    JCheckBox drawAttachSouth = new JCheckBox("draw Attach South", true);
    JCheckBox drawAttachEast = new JCheckBox("draw Attach East", true);
    JCheckBox drawAttachWest = new JCheckBox("draw Attach West", true);
    JComboBox polygons = new JComboBox();
    JCheckBox gridSliderGlue = new JCheckBox("");
    JSpinner minRelativeSizeX = new JSpinner(new SpinnerNumberModel(1, 0, 100, 1));
    JSpinner minRelativeSizeY = new JSpinner(new SpinnerNumberModel(1, 0, 100, 1));
    JSpinner maxRelativeSizeX = new JSpinner(new SpinnerNumberModel(1, 0, 100, 1));
    JSpinner maxRelativeSizeY = new JSpinner(new SpinnerNumberModel(1, 0, 100, 1));

    JCheckBox zoomSpliderGlue = new JCheckBox("");
    JSlider zoomSpliderX = new JSlider(0, 10000);
    JSlider zoomSpliderY = new JSlider(0, 10000);

    JSlider translateSliderX = new JSlider(-10000, 10000, 0);
    JSlider translateSpliderY = new JSlider(-10000, 10000, 0);
    MeshAlgo meshAlgo;
    GpPattern pattern;
    Domain globalDomain;

    public PolygonPlot(Domain rec, Polygon polygon, MeshAlgo meshAlgo, GpPattern pattern, Domain globalDomain) {
        this(new DefaultGeometryList(rec, polygon), meshAlgo, pattern, globalDomain);
    }

    public PolygonPlot(GeometryList geometryList, MeshAlgo meshAlgo, GpPattern pattern, Domain globalDomain) {
        super(new BorderLayout());
        this.meshAlgo = meshAlgo;
        this.pattern = pattern;
        this.globalDomain = globalDomain;
        this.polygonComponent = new PolygonComponent(geometryList, meshAlgo, pattern, globalDomain);
        this.polygonComponent.setGridEdgeColor(gridEdgeColor);
        this.polygonComponent.setGridForegroundColor(gridForegroundColor);
        this.polygonComponent.setGridMeshMaxColor(gridMeshMaxColor);
        this.polygonComponent.setGridMeshMinColor(gridMeshMinColor);
        this.polygonComponent.setPolygonEdgeColor(polygonEdgeColor);
        this.polygonComponent.setPolygonForegroundColor(polygonForegroundColor);

        polygonComponent.setPreferredSize(new Dimension(500, 500));
        JPanel p0;
        zoomSpliderX.setMinorTickSpacing(10);
        zoomSpliderX.setPaintTicks(true);
        zoomSpliderX.setSnapToTicks(true);

        translateSliderX.setMinorTickSpacing(10);
        translateSliderX.setPaintTicks(true);
        translateSliderX.setSnapToTicks(true);

        translateSpliderY.setMinorTickSpacing(10);
        translateSpliderY.setPaintTicks(true);
        translateSpliderY.setSnapToTicks(true);

        zoomSpliderY.setMinorTickSpacing(10);
        zoomSpliderY.setPaintTicks(true);
        zoomSpliderY.setSnapToTicks(true);

//        gridSpliderX.setMinorTickSpacing(1);
//        gridSpliderY.setMinorTickSpacing(1);
//        precisionSpliderX.setMinorTickSpacing(1);
//        precisionSpliderY.setMinorTickSpacing(1);
//        gridSpliderX.setPaintTicks(true);
//        gridSpliderY.setPaintTicks(true);
//        precisionSpliderX.setPaintTicks(true);
//        precisionSpliderY.setPaintTicks(true);
//        gridSpliderX.setSnapToTicks(true);
//        gridSpliderY.setSnapToTicks(true);
//        precisionSpliderX.setInverted(true);
//        precisionSpliderY.setInverted(true);

//        Box b = Box.createVerticalBox();
        p0 = new JPanel(new GridBagLayout2(
                "[<X+  :     ]\n" +
                        "[<A  ] [<B  ]\n" +
                        "[<C  ] [<D  ]\n" +
                        "[<Ms ] [<Mx ]\n" +
                        "[<F-  :     ]\n" +
                        "[<G-  :     ]\n" +
                        "[<H-  :     ]\n" +
                        "[<E-  :     ]\n" +
                        "[<X1-  :     ]\n" +
                        "[<X2  ] [<X3  ]\n" +
                        "[<X4  ] [<X5  ]\n" +
                        "[<X6  ] [<X7  ]\n" +
                        "[X10+****:]"
        ));
        p0.add(polygons, "X");
        p0.add(fillArea, "A");
        p0.add(drawArea, "B");
        p0.add(fillGrid, "C");
        p0.add(drawGrid, "D");
        p0.add(meshMin, "Ms");
        p0.add(meshMax, "Mx");
        p0.add(drawMain, "X1");
        p0.add(drawAttachX, "X2");
        p0.add(drawAttachY, "X3");
        p0.add(drawAttachNorth, "X4");
        p0.add(drawAttachSouth, "X5");
        p0.add(drawAttachEast, "X6");
        p0.add(drawAttachWest, "X7");
        p0.add(new JScrollPane(selectionTable), "X10");
        if (geometryList instanceof FractalAreaGeometryList) {
            JPanel p = new JPanel(new BorderLayout());
            p.setBorder(BorderFactory.createTitledBorder("Iteration K"));
            JSpinner ss = new JSpinner(new SpinnerNumberModel(1, 0, Integer.MAX_VALUE, 1));
            ss.setValue(((FractalAreaGeometryList) geometryList).getLevel());
            p.add(ss);
            ss.putClientProperty("FractalPolygon", geometryList);
            ss.addChangeListener(
                    new ChangeListener() {
                        public void stateChanged(ChangeEvent e) {
                            JSpinner ss = (JSpinner) e.getSource();
                            FractalAreaGeometryList polygon2DCollection = (FractalAreaGeometryList) ((FractalAreaGeometryList) ss.getClientProperty("FractalPolygon")).clone();
                            polygon2DCollection.setLevel(((Number) ss.getValue()).intValue());
                            if (getGpMeshAlgoRect() != null) {
                                MeshAlgoRect gpMeshAlgoRect = getGpMeshAlgoRect();
                                gpMeshAlgoRect.setMinRelativeSizeX(getMinRelativeSizeXValue());
                                gpMeshAlgoRect.setMinRelativeSizeY(getMinRelativeSizeYValue());
                                gpMeshAlgoRect.setMaxRelativeSizeX(getMaxRelativeSizeXValue());
                                gpMeshAlgoRect.setMaxRelativeSizeY(getMaxRelativeSizeYValue());
                            } else if (getMeshConsDesAlgo() != null) {
                                getMeshConsDesAlgo().getOption().setMaxTriangles(((Number) minRelativeSizeX.getValue()).intValue());
                            }
                            updatePolygonPainting();
                        }
                    });
            p0.add(p, "E");
        }

        JPanel p1 = new JPanel(new GridBagLayout2(
                "[<A  ][B+=  ][L1][F+=][<C  ]\n" +
                        "[<D  ][E+=  ][L2][G+=][C  ]\n"));
        p1.add(new JLabel("X : 1/2^"), "A");
        p1.add(new JLabel("Y : 1/2^"), "D");
        p1.add(minRelativeSizeX, "B");
        p1.add(minRelativeSizeY, "E");
        p1.add(maxRelativeSizeX, "F");
        p1.add(maxRelativeSizeY, "G");
        p1.add(gridSliderGlue, "C");
        p1.add(new JLabel(" - 1/2^"), "L1");
        p1.add(new JLabel(" - 1/2^"), "L2");
        p1.setBorder(BorderFactory.createTitledBorder("Grille"));
        p0.add(p1, "F");

        p1 = new JPanel(new GridBagLayout2(
                "[<A  ][B+=  ][<C  ]\n" +
                        "[<D  ][E+=  ][C  ]\n"
        ));
        p1.add(new JLabel("X"), "A");
        p1.add(new JLabel("Y"), "D");
        p1.add(zoomSpliderX, "B");
        p1.add(zoomSpliderY, "E");
        p1.add(zoomSpliderGlue, "C");
        p1.setBorder(BorderFactory.createTitledBorder("Zoom"));
        p0.add(p1, "G");

        p1 = new JPanel(new GridBagLayout2(
                "[<A  ][B+=  ]\n" +
                        "[<D  ][E+=  ]\n"
        ));
        p1.add(new JLabel("X"), "A");
        p1.add(new JLabel("Y"), "D");
        p1.add(translateSliderX, "B");
        p1.add(translateSpliderY, "E");
        p1.setBorder(BorderFactory.createTitledBorder("Translate"));
        p0.add(p1, "H");
        p0.setBorder(BorderFactory.createTitledBorder("Display"));

        JSplitPane jsp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, p0, polygonComponent);
        this.add(jsp, BorderLayout.CENTER);
//        this.add(b, BorderLayout.LINE_START);


        ItemListener someChangeListener = new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                someChange();
            }
        };
        drawMain.addItemListener(someChangeListener);
        drawAttachX.addItemListener(someChangeListener);
        drawAttachY.addItemListener(someChangeListener);
        drawAttachEast.addItemListener(someChangeListener);
        drawAttachWest.addItemListener(someChangeListener);
        drawAttachNorth.addItemListener(someChangeListener);
        drawAttachSouth.addItemListener(someChangeListener);

        fillGrid.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                int stateChange = e.getStateChange();
                fillGridChanged();
//                System.out.println(stateChange==ItemEvent.SELECTED?"SELECTED":"DESELECTED");
            }
        });
        fillArea.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                fillAreaChanged();
            }
        });
        gridSliderGlue.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                gridSliderGlueChanged();
            }
        });
        drawGrid.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                drawGridChanged();
            }
        });
        meshMin.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                meshMinChanged();
            }
        });
        meshMax.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                meshMaxChanged();
            }
        });
        drawArea.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                drawAreaChanged();
            }
        });
        polygons.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (ItemEvent.SELECTED == e.getStateChange()) {
                    polygonsChanged();
                }
            }
        });
        minRelativeSizeX.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                minRelativeSizeXChanged();
            }
        });

        minRelativeSizeY.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                minRelativeSizeYChanged();
            }
        });

        maxRelativeSizeX.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                maxRelativeSizeXChanged();
            }
        });

        maxRelativeSizeY.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                maxRelativeSizeYChanged();
            }
        });

        zoomSpliderX.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                zoomSpliderXChanged();
            }
        });

        zoomSpliderY.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                zoomSpliderYChanged();
            }
        });

        translateSliderX.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                translateSpliderXChanged();
            }
        });

        translateSpliderY.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                translateSpliderYChanged();
            }
        });

        setPolygonList(geometryList);
    }

    private boolean isInSetPolygonList = false;

    public void setPolygonList(GeometryList p) {
        currentGeometryList =p;
        updatePolygonList();
    }

    public void updatePolygonList() {
        try {
            isInSetPolygonList = true;
            Object[] items = new Object[currentGeometryList.size() + 1];
            items[0] = "All Polygons";
            for (int i = 1; i < items.length; i++) {
                items[i] = "Polygon " + (i);
            }
            polygons.setModel(new DefaultComboBoxModel(items));
            polygons.setSelectedIndex(0);
//        if (items.length > 1) {
//        }

            //noinspection LoopStatementThatDoesntLoop
            if (getGpMeshAlgoRect() != null) {
                setMinRelativeSizeXValue(getGpMeshAlgoRect().getMinRelativeSizeX());
                setMinRelativeSizeYValue(getGpMeshAlgoRect().getMinRelativeSizeY());
                setMaxRelativeSizeXValue(getGpMeshAlgoRect().getMaxRelativeSizeX());
                setMaxRelativeSizeYValue(getGpMeshAlgoRect().getMaxRelativeSizeY());
            } else if (getMeshConsDesAlgo() != null) {
                minRelativeSizeX.setValue(getMeshConsDesAlgo().getOption().getMaxTriangles());
            }

            updatePolygonPaintingConfigure();

            if (
                    minRelativeSizeX.getValue().equals(minRelativeSizeY.getValue())
                            && maxRelativeSizeX.getValue().equals(maxRelativeSizeY.getValue())
                    ) {
                gridSliderGlue.setSelected(true);
            }
        } finally {
            isInSetPolygonList = false;
        }
    }

    private MeshAlgoRect getGpMeshAlgoRect() {
        if (meshAlgo instanceof MeshAlgoRect) {
            return (MeshAlgoRect) meshAlgo;
        }
        return null;
    }

    private MeshConsDesAlgo getMeshConsDesAlgo() {
        if (meshAlgo instanceof MeshConsDesAlgo) {
            return (MeshConsDesAlgo) meshAlgo;
        }
        return null;
    }

    private void gridSliderGlueChanged() {
        minRelativeSizeY.setEnabled(!gridSliderGlue.isSelected());
        maxRelativeSizeY.setEnabled(!gridSliderGlue.isSelected());
        minRelativeSizeY.setValue(minRelativeSizeX.getValue());
        maxRelativeSizeY.setValue(maxRelativeSizeX.getValue());
        updatePolygonPainting();
        repaint();
    }

    private void someChange() {
        polygonComponent.setDrawMain(drawMain.isSelected());
        polygonComponent.setDrawAttachX(drawAttachX.isSelected());
        polygonComponent.setDrawAttachY(drawAttachY.isSelected());
        polygonComponent.setDrawAttachNorth(drawAttachNorth.isSelected());
        polygonComponent.setDrawAttachSouth(drawAttachSouth.isSelected());
        polygonComponent.setDrawAttachEast(drawAttachEast.isSelected());
        polygonComponent.setDrawAttachWest(drawAttachWest.isSelected());
        updatePolygonPainting();
        repaint();

    }

    private void fillGridChanged() {
        if (fillGrid.isSelected()) {
            gridForegroundColor = chooseColor(gridForegroundColor);
        } else {
            gridForegroundColor = null;
        }
        polygonComponent.setGridForegroundColor(gridForegroundColor);
        updatePolygonPainting();
        repaint();
    }

    private void fillAreaChanged() {
        polygonComponent.setPolygonForegroundColor(fillArea.isSelected() ? polygonForegroundColor = chooseColor(polygonForegroundColor) : null);
        updatePolygonPainting();
        repaint();
    }

    private void drawAreaChanged() {
        polygonComponent.setPolygonEdgeColor(drawArea.isSelected() ? polygonEdgeColor = chooseColor(polygonEdgeColor) : null);
        updatePolygonPainting();
        repaint();
    }

    private void drawGridChanged() {
        polygonComponent.setGridEdgeColor(drawGrid.isSelected() ? gridEdgeColor = chooseColor(gridEdgeColor) : null);
        updatePolygonPainting();
        repaint();
    }

    private void meshMinChanged() {
        polygonComponent.setGridMeshMinColor(meshMin.isSelected() ? gridMeshMinColor = chooseColor(gridMeshMinColor) : null);
        updatePolygonPainting();
        repaint();
    }

    private void meshMaxChanged() {
        polygonComponent.setGridMeshMaxColor(meshMax.isSelected() ? gridMeshMaxColor = chooseColor(gridMeshMaxColor) : null);
        updatePolygonPainting();
        repaint();
    }

    private Color chooseColor(Color color) {
        Color c = JColorChooser.showDialog(this, "Choose Color", color);
        if (c == null) {
            return color;
        }
        return c;
    }

    private void polygonsChanged() {
        if (getGpMeshAlgoRect() != null) {
            getGpMeshAlgoRect().setMinRelativeSizeX(getMinRelativeSizeXValue());
            getGpMeshAlgoRect().setMinRelativeSizeY(getMinRelativeSizeYValue());
            getGpMeshAlgoRect().setMaxRelativeSizeX(getMaxRelativeSizeXValue());
            getGpMeshAlgoRect().setMaxRelativeSizeY(getMaxRelativeSizeYValue());
        } else if (getMeshConsDesAlgo() != null) {
            getMeshConsDesAlgo().getOption().setMaxTriangles(((Number) minRelativeSizeX.getValue()).intValue());
        }
        updatePolygonPainting();
    }


    private Iterable<Geometry> getCurrentPolygons() {
        int index = polygons.getSelectedIndex();
        if (index == 0) {
            return polygonComponent.getPolygonList();
        }
        return Arrays.asList(polygonComponent.getPolygon(index - 1));
    }

    private void minRelativeSizeYChanged() {
        if (isInSetPolygonList) {
            return;
        }
        if (!gridSliderGlue.isSelected()) {
            if (getGpMeshAlgoRect() != null) {
                getGpMeshAlgoRect().setMinRelativeSizeY(((Number) minRelativeSizeY.getValue()).doubleValue());
            }
        }
        updatePolygonPaintingConfigure();
        repaint();
    }

    private void maxRelativeSizeYChanged() {
        if (isInSetPolygonList) {
            return;
        }
        if (!gridSliderGlue.isSelected()) {
            if (getGpMeshAlgoRect() != null) {
                getGpMeshAlgoRect().setMaxRelativeSizeY(((Number) maxRelativeSizeY.getValue()).doubleValue());
            }
        }
        updatePolygonPainting();
        repaint();
    }

    private void maxRelativeSizeXChanged() {
        if (isInSetPolygonList) {
            return;
        }
        if (gridSliderGlue.isSelected()) {
            maxRelativeSizeY.setValue(maxRelativeSizeX.getValue());
        }
        if (getGpMeshAlgoRect() != null) {
            getGpMeshAlgoRect().setMaxRelativeSizeX(getMaxRelativeSizeXValue());
            getGpMeshAlgoRect().setMaxRelativeSizeY(getMaxRelativeSizeYValue());
        }
        updatePolygonPainting();
        repaint();
    }

    private void minRelativeSizeXChanged() {
        if (isInSetPolygonList) {
            return;
        }
        if (gridSliderGlue.isSelected()) {
            minRelativeSizeY.setValue(minRelativeSizeX.getValue());
        }
        if (getGpMeshAlgoRect() != null) {
            getGpMeshAlgoRect().setMinRelativeSizeX(getMinRelativeSizeXValue());
            getGpMeshAlgoRect().setMinRelativeSizeY(getMinRelativeSizeYValue());
        } else if (getMeshConsDesAlgo() != null) {
            getMeshConsDesAlgo().getOption().setMaxTriangles(((Number) minRelativeSizeX.getValue()).intValue());
        }
        updatePolygonPaintingConfigure();
        repaint();
    }

    private void zoomSpliderXChanged() {
        polygonComponent.setZoomX(zoomSpliderX.getValue() / 100.0);
        if (zoomSpliderGlue.isSelected() /*&& !precisionSpliderX.getValueIsAdjusting()*/) {
            zoomSpliderY.setValue(zoomSpliderX.getValue());
        }
        updatePolygonPainting();
        repaint();
    }

    private void zoomSpliderYChanged() {
        polygonComponent.setZoomY(zoomSpliderY.getValue() / 100.0);
        if (zoomSpliderGlue.isSelected() /*&& !precisionSpliderX.getValueIsAdjusting()*/) {
            zoomSpliderX.setValue(zoomSpliderY.getValue());
        }
        updatePolygonPainting();
        repaint();
    }

    private void translateSpliderXChanged() {
        polygonComponent.setTranslateX(translateSliderX.getValue() / 1000.0 * polygonComponent.getPolygonList().getBounds().getXwidth());
        updatePolygonPainting();
        repaint();
    }

    private void translateSpliderYChanged() {
        polygonComponent.setTranslateY(translateSpliderY.getValue() / 1000.0 * polygonComponent.getPolygonList().getBounds().getYwidth());
        updatePolygonPainting();
        repaint();
    }

//    public void setValueMinRelativeSizeY(Polygon polygon) {
//        System.out.println("polygon.getMinRelativeSizeY() = " + polygon.getMinRelativeSizeY());
//        minRelativeSizeY.setValue(polygon.getMinRelativeSizeY());
//    }
//
//    public void setValueMinRelativeSizeX(Polygon polygon) {
//        System.out.println("polygon.getMinRelativeSizeX() = " + polygon.getMinRelativeSizeX());
//        minRelativeSizeX.setValue(polygon.getMinRelativeSizeX());
//    }
//
//
//    public void setValueMaxRelativeSizeY(Polygon polygon) {
//        System.out.println("polygon.getMaxRelativeSizeY() = " + polygon.getMaxRelativeSizeY());
//        maxRelativeSizeY.setValue((polygon.getMaxRelativeSizeY()));
//    }
//
//    public void setValueMaxRelativeSizeX(Polygon polygon) {
//        System.out.println("polygon.getMaxRelativeSizeX() = " + polygon.getMaxRelativeSizeX());
//        maxRelativeSizeX.setValue((polygon.getMaxRelativeSizeX()));
//    }

    //conversion

    public double getMaxRelativeSizeXValue() {
        Number d = (Number) maxRelativeSizeX.getValue();
        return 1 / Math.pow(2, d.doubleValue());
    }

    public double getMinRelativeSizeXValue() {
        Number d = (Number) minRelativeSizeX.getValue();
        return 1 / Math.pow(2, d.doubleValue());
    }

    public double getMaxRelativeSizeYValue() {
        Number d = (Number) maxRelativeSizeY.getValue();
        return 1 / Math.pow(2, d.doubleValue());
    }

    public double getMinRelativeSizeYValue() {
        Number d = (Number) minRelativeSizeY.getValue();
        return 1 / Math.pow(2, d.doubleValue());
    }

    public void setMaxRelativeSizeXValue(double v) {
        maxRelativeSizeX.setValue((int) Math.round(Math.log(1 / v) / Math.log(2)));
    }

    public void setMinRelativeSizeXValue(double v) {
        minRelativeSizeX.setValue((int) Math.round(Math.log(1 / v) / Math.log(2)));
    }

    public void setMaxRelativeSizeYValue(double v) {
        maxRelativeSizeY.setValue((int) Math.round(Math.log(1 / v) / Math.log(2)));
    }

    public void setMinRelativeSizeYValue(double v) {
        minRelativeSizeY.setValue((int) Math.round(Math.log(1 / v) / Math.log(2)));
    }

    public void updatePolygonPaintingConfigure() {
        PolygonListMeshInfo meshInfo = new PolygonListMeshInfo(currentGeometryList, globalDomain, meshAlgo, pattern);
        java.util.List<MeshZone> currentMeshZones= polygonComponent.getMeshZones();
        selected = new BitSet(currentMeshZones.size());
        if(currentMeshZones.size()>0) {
            selected.set(0, currentMeshZones.size() - 1, true);
        }
        for (int i = 0; i < currentMeshZones.size(); i++) {
            selected.set(i, true);
        }
        polygonComponent.setMeshInfo(meshInfo);
        selectionTable.setModel(selectionTable.getModel());
//        selectionTable.repaint();
        //this.currentGeometryList = p;
    }

    public void updatePolygonPainting() {
        polygonComponent.repaint();
    }

}
