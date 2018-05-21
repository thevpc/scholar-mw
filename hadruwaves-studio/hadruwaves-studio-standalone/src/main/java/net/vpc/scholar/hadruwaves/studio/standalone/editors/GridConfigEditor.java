package net.vpc.scholar.hadruwaves.studio.standalone.editors;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import net.vpc.lib.pheromone.application.swing.DataTypeEditor;
import net.vpc.lib.pheromone.application.swing.ECColorChooser;
import net.vpc.lib.pheromone.application.swing.ECNumericField;
import net.vpc.lib.pheromone.application.swing.ECRadioButton;
import net.vpc.lib.pheromone.application.swing.EditComponentsPanel;
import net.vpc.lib.pheromone.application.swing.NumberLayout;

import net.vpc.scholar.hadrumaths.util.config.Configuration;

/**
 * Created by IntelliJ IDEA.
 * User: TAHA
 * Date: 10 avr. 2004
 * Time: 22:13:43
 * To change this template use File | Settings | File Templates.
 */
public class GridConfigEditor extends JPanel {

    private MomProjectEditor structureEditor;
    private ECNumericField gridXComponent;
    private ECNumericField gridXcountComponent;
    private ECNumericField gridYComponent;
    private ECNumericField gridYcountComponent;
    private ECRadioButton dimType;
    private ECRadioButton countType;
    private ECRadioButton noGridType;

    public ECColorChooser getGridColorComponent() {
        return gridColorComponent;
    }

    public ECNumericField getGridXComponent() {
        return gridXComponent;
    }

    public ECNumericField getGridXcountComponent() {
        return gridXcountComponent;
    }

    public ECNumericField getGridYComponent() {
        return gridYComponent;
    }

    public ECNumericField getGridYcountComponent() {
        return gridYcountComponent;
    }
    private ECColorChooser gridColorComponent;
    private PropertyChangeListener handler = new PropertyChangeListener() {

        public void propertyChange(PropertyChangeEvent evt) {
            firePropertyChange("GridChanged", null, GridConfigEditor.this);
        }
    };

    public GridConfigEditor(MomProjectEditor strEditor) {
        super(new BorderLayout());
        this.structureEditor = strEditor;
        dimType = new ECRadioButton("By Dimension");
        dimType.setDescription("Specify Grid Dimensions");
        countType = new ECRadioButton("By Count");
        countType.setDescription("Specify Grid Tics");
        noGridType = new ECRadioButton("Invisible");
        noGridType.setDescription("No Grid");
        ButtonGroup bg = new ButtonGroup();
        bg.add(noGridType);
        bg.add(dimType);
        bg.add(countType);
        countType.setSelected(true);
        ItemListener itemListener = new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                updateDependecies();
            }
        };
        dimType.addItemListener(itemListener);
        countType.addItemListener(itemListener);
        gridXComponent = new ECNumericField("gridX", NumberLayout.POSITIVE, 16, Double.class, new Double(0), null, 3, 20, true);
        gridXComponent.setDescription(strEditor.getResources().get("gridX"));
        gridXComponent.addPropertyChangeListener(DataTypeEditor.EC_VALUE_PROPERTY, handler);
        gridXComponent.addPropertyChangeListener(DataTypeEditor.EC_VALUE_PROPERTY, new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                updateGridXcountComponent();
            }
        });

        gridXcountComponent = new ECNumericField("gridXcount", NumberLayout.POSITIVE, 16, Double.class, new Double(0), null, 3, 20, true);
        gridXcountComponent.setObject(20.0);
        gridXcountComponent.setDescription(strEditor.getResources().get("gridXcount"));
        gridXcountComponent.addPropertyChangeListener(DataTypeEditor.EC_VALUE_PROPERTY, new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                if (gridXcountComponent.getObject(false) != null) {
                    gridXComponent.setDouble(gridXcountComponent.getDouble() == 0 ? 0 : (structureEditor.getProject0().getDomain().getXwidth()/ (gridXcountComponent.getDouble() * structureEditor.getProject().getDimensionUnit())));
                }
            }
        });


        gridYComponent = new ECNumericField("gridY", NumberLayout.POSITIVE, 16, Double.class, new Double(0), null, 3, 20, true);
        gridYComponent.setDescription(strEditor.getResources().get("gridY"));
        gridYComponent.addPropertyChangeListener(DataTypeEditor.EC_VALUE_PROPERTY, handler);
        gridYComponent.addPropertyChangeListener(DataTypeEditor.EC_VALUE_PROPERTY, new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                updateGridYcountComponent();
            }
        });

        gridYcountComponent = new ECNumericField("gridYcount", NumberLayout.POSITIVE, 16, Double.class, new Double(0), null, 3, 20, true);
        gridYcountComponent.setObject(20.0);
        gridYcountComponent.setDescription(strEditor.getResources().get("gridYcount"));
        gridYcountComponent.addPropertyChangeListener(DataTypeEditor.EC_VALUE_PROPERTY, new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                if (gridYcountComponent.getObject(false) != null) {

                    gridYComponent.setDouble(gridYcountComponent.getDouble() == 0 ? 0 : (structureEditor.getProject0().getDomain().getYwidth()/ gridYcountComponent.getDouble() / structureEditor.getProject().getDimensionUnit()));
                }
            }
        });

        gridColorComponent = new ECColorChooser("gridColor", Color.BLUE, false);
        gridColorComponent.setDescription(strEditor.getResources().get("gridColor"));
        gridColorComponent.addPropertyChangeListener(DataTypeEditor.EC_VALUE_PROPERTY, handler);
        JPanel p = new EditComponentsPanel(new DataTypeEditor[]{
            noGridType,
            dimType,
            gridXComponent, gridYComponent,
            countType,
            gridXcountComponent, gridYcountComponent,
            gridColorComponent
        }, 1, false);
        p.setBorder(BorderFactory.createTitledBorder(strEditor.getResources().get("Dielectric.grid")));
        add(p);
        updateDependecies();
    }

    public void updateDependecies() {
        gridXComponent.setEnabled(dimType.isSelected());
        gridYComponent.setEnabled(dimType.isSelected());
        gridXcountComponent.setEnabled(countType.isSelected());
        gridYcountComponent.setEnabled(countType.isSelected());
        gridColorComponent.setEnabled(countType.isSelected() || dimType.isSelected());
    }

    public void updateGridXcountComponent() {
        if (gridXComponent.getObject(false) != null) {
            gridXcountComponent.setDouble(gridXComponent.getDouble() == 0 ? 0 : (structureEditor.getProject0().getDomain().getXwidth()/ (gridXComponent.getDouble() * structureEditor.getProject().getDimensionUnit())));
        }
    }

    public void updateGridYcountComponent() {
        if (gridYComponent.getObject(false) != null) {
            gridYcountComponent.setDouble(gridYComponent.getDouble() == 0 ? 0 : (structureEditor.getProject0().getDomain().getYwidth() / (gridYComponent.getDouble() * structureEditor.getProject().getDimensionUnit())));
        }
    }

    public void loadConfiguration(Configuration conf, String key) {
        this.dimType.setSelected("dimension".equals(conf.getString(key + ".strategy", "count")));
        this.countType.setSelected("count".equals(conf.getString(key + ".strategy", "count")));
        this.noGridType.setSelected("invisible".equals(conf.getString(key + ".strategy", "count")));
        this.gridXComponent.setText(conf.getString(key + ".gridX"));
        this.gridYComponent.setText(conf.getString(key + ".gridY"));
        this.gridXcountComponent.setText(conf.getString(key + ".gridXcount"));
        this.gridYcountComponent.setText(conf.getString(key + ".gridYcount"));
        this.gridColorComponent.setObject(conf.getColor(key + ".gridColor", Color.LIGHT_GRAY));
    }

    public void storeConfiguration(Configuration conf, String key) {
        conf.deleteTree(key, true);
        conf.setString(key + ".strategy", this.dimType.isSelected() ? "dimension" : this.countType.isSelected()? "count" : "invisible");
        conf.setString(key + ".gridX", this.gridXComponent.getText());
        conf.setString(key + ".gridY", this.gridYComponent.getText());
        conf.setString(key + ".gridXcount", this.gridXcountComponent.getText());
        conf.setString(key + ".gridYcount", this.gridYcountComponent.getText());
        conf.setColor(key + ".gridColor", this.gridColorComponent.getColor());
    }

    public double getGridXValue() {
        if (dimType.isSelected()) {
            double unit = structureEditor.getProject().getDimensionUnit();
            Number dd = (Number) this.gridXComponent.getObject();
            return dd == null ? 0 : dd.doubleValue() * unit;
        } else if(countType.isSelected()){
            Number dd0 = (Number) gridXcountComponent.getObject();
            if (dd0 == null) {
                return 0;
            }
            return (dd0.doubleValue() == 0 ? 0 : (structureEditor.getProject0().getDomain().getXwidth()/ dd0.doubleValue()));
        }else{
            return 0;
        }
    }

    public double getGridYValue() {
        if (dimType.isSelected()) {
            double unit = structureEditor.getProject().getDimensionUnit();
            Number dd = (Number) this.gridYComponent.getObject();
            return dd == null ? 0 : dd.doubleValue() * unit;
        } else if(countType.isSelected()){
            Number dd0 = (Number) gridYcountComponent.getObject();
            if (dd0 == null) {
                return 0;
            }
            return (dd0.doubleValue() == 0 ? 0 : (structureEditor.getProject0().getDomain().getYwidth()/ dd0.doubleValue()));
        }else{
            return 0;
        }
    }

    public double getGridXcountValue() {
        return this.gridXcountComponent.getDouble();
    }

    public double getGridYcountValue() {
        return this.gridYcountComponent.getDouble();
    }

    public Color getGridColorValue() {
        return this.gridColorComponent.getColor();
    }
}
