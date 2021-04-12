/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.params.components;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.TreePath;

import net.thevpc.common.iconset.IconSets;
//import net.thevpc.common.iconset.PIconSet;
import net.thevpc.scholar.hadrumaths.units.ParamUnit;
import net.thevpc.scholar.hadrumaths.units.UnitType;
import net.thevpc.scholar.hadruwaves.project.parameter.HWParameterElement;
import net.thevpc.scholar.hadruwaves.project.parameter.HWParameterFolder;
import net.thevpc.scholar.hadruwaves.project.parameter.HWParameterValue;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.components.NumberExpressionDialog;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.components.StringExpressionDialog;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.util.DefaultCellEditor2;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.util.PValueViewPropertyE;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.renderer.DefaultTableRenderer;
import org.jdesktop.swingx.tree.DefaultXTreeCellRenderer;
import net.thevpc.echo.AppPropertiesNodeItem;
import net.thevpc.scholar.hadruwaves.project.EvalExprError;
import net.thevpc.scholar.hadruwaves.project.EvalExprResult;
import net.thevpc.scholar.hadruwaves.project.configuration.HWConfigurationRun;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.params.HWSProjectParametersTool;

/**
 *
 * @author vpc
 */
public class CustomJXTreeTable extends JXTreeTable {

    private HWSProjectParametersTool tool;

    public CustomJXTreeTable(HWSProjectParametersTool tool) {
        this.tool = tool;

        DefaultTableRenderer nbrResult = new DefaultTableRenderer();
        nbrResult.getComponentProvider().setHorizontalAlignment(SwingConstants.RIGHT);
        setDefaultRenderer(EvalExprResult.class, nbrResult);

        DefaultTableRenderer errorResultRenderer = new DefaultTableRenderer();
        errorResultRenderer.setBackground(new Color(242, 127, 144));
        setDefaultRenderer(EvalExprError.class, errorResultRenderer);
        setTreeCellRenderer(new DefaultXTreeCellRenderer() {
            @Override
            public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
                Component c = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
                value = HWSProjectParametersTool.toItem(value);
                if (value instanceof HWParameterElement) {
                    super.setText(((HWParameterElement) value).name().get());
                } else if (value instanceof HWConfigurationRun) {
                    HWConfigurationRun configuration = (HWConfigurationRun) value;
                    if (configuration.project().get() == null) {
                        super.setText("No Selection");
                    } else {
                        super.setText(configuration.project().get().name().get() + " :: " + configuration.name().get());
                    }
                } else {
                    super.setText(String.valueOf(value));
                }
                IconSets iconSet = tool.app().iconSets();
                if (!leaf || value instanceof HWConfigurationRun || value instanceof HWParameterFolder) {
                    if (value instanceof HWConfigurationRun) {
                        super.setIcon(iconSet.icon("Configuration").get());
                    } else {
                        super.setIcon(expanded ? iconSet.icon("OpenFolder").get() : iconSet.icon("Folder").get());
                    }
                } else {
                    super.setIcon(null);
                }
                return c;
            }
        });

    }

    @Override
    public TableCellRenderer getCellRenderer(int row, int column) {
        if (column == 1) {
            TreePath p = this.getPathForRow(row);
            Object c = HWSProjectParametersTool.toItem(p.getLastPathComponent());
            if (c instanceof AppPropertiesNodeItem) {
                AppPropertiesNodeItem vp = (AppPropertiesNodeItem) c;
                if (vp instanceof PValueViewPropertyE) {
                    Object yy = getValueAt(row, column);
                    PValueViewPropertyE ee = (PValueViewPropertyE) vp;
                    if (ee.getExpressionType().equals(Boolean.class)) {
                        Object s = ee.object();
                        if (s == null || s.toString().isEmpty() || s.toString().equals("true") || s.toString().equals("false")) {
                            TableCellRenderer b = getDefaultRenderer(Boolean.class);
                            if (b instanceof org.jdesktop.swingx.renderer.DefaultTableRenderer) {
                                org.jdesktop.swingx.renderer.DefaultTableRenderer dtr = (org.jdesktop.swingx.renderer.DefaultTableRenderer) b;
                                dtr.getComponentProvider().setHorizontalAlignment(JLabel.LEADING);
                            }
                            return b;
                        }
                    }
                }
                if (Boolean.class.equals(vp.getType()) || Boolean.TYPE.equals(vp.getType())) {
                    TableCellRenderer b = getDefaultRenderer(Boolean.class);
                    if (b instanceof org.jdesktop.swingx.renderer.DefaultTableRenderer) {
                        org.jdesktop.swingx.renderer.DefaultTableRenderer dtr = (org.jdesktop.swingx.renderer.DefaultTableRenderer) b;
                        dtr.getComponentProvider().setHorizontalAlignment(JLabel.LEADING);
                        //                                JCheckBox component = (JCheckBox) b.getComponent();
                        //                                component.setHorizontalAlignment(SwingConstants.LEFT);
                    }
                    return b;
                }
                if (Color.class.equals(vp.getType())) {
                    return getDefaultRenderer(Color.class);
                }
                return getDefaultRenderer(String.class);
            }
        } else if (column == 2) {
            TreePath p = this.getPathForRow(row);
            Object c = HWSProjectParametersTool.toItem(p.getLastPathComponent());
            if (c instanceof PValueViewPropertyE) {
                AppPropertiesNodeItem vp = (AppPropertiesNodeItem) c;
                if (Boolean.class.equals(vp.getType()) || Boolean.TYPE.equals(vp.getType())) {
                    TableCellRenderer b = getDefaultRenderer(Boolean.class);
                    if (b instanceof org.jdesktop.swingx.renderer.DefaultTableRenderer) {
                        org.jdesktop.swingx.renderer.DefaultTableRenderer dtr = (org.jdesktop.swingx.renderer.DefaultTableRenderer) b;
                        dtr.getComponentProvider().setHorizontalAlignment(JLabel.LEADING);
                        //                                JCheckBox component = (JCheckBox) b.getComponent();
                        //                                component.setHorizontalAlignment(SwingConstants.LEFT);
                    }
                    return b;
                }
                if (Color.class.equals(vp.getType())) {
                    return getDefaultRenderer(Color.class);
                }
                return getDefaultRenderer(String.class);
            }
        } else if (column == 3) {
            TreePath p = this.getPathForRow(row);
            Object c = HWSProjectParametersTool.toItem(p.getLastPathComponent());
            Object v = getValueAt(row, column);
            if (v instanceof EvalExprResult) {
                EvalExprResult res = (EvalExprResult) v;
                if (res.isError()) {
                    TableCellRenderer dr = getDefaultRenderer(EvalExprError.class);
                    return dr;
                } else if (res.getValue() instanceof Number) {
                    DefaultTableRenderer dtr = (DefaultTableRenderer) getDefaultRenderer(EvalExprResult.class);
                    dtr.getComponentProvider().setHorizontalAlignment(JLabel.TRAILING);
                    return dtr;
                } else {
                    DefaultTableRenderer dtr = (DefaultTableRenderer) getDefaultRenderer(EvalExprResult.class);
                    dtr.getComponentProvider().setHorizontalAlignment(JLabel.LEFT);
                    return dtr;
                }
            }
        }
        return super.getCellRenderer(row, column);
    }

    protected TableCellEditor getCellEditor0(int row, int column) {
        return super.getCellEditor(row, column);
    }

    @Override
    public TableCellEditor getCellEditor(int row, int column) {
        if (column == 2) {
            TreePath p = this.getPathForRow(row);
            Object c = HWSProjectParametersTool.toItem(p.getLastPathComponent());
            if (c instanceof HWParameterValue) {
                HWParameterValue vp = (HWParameterValue) c;
                UnitType ttype = vp.type().get();
                if (ttype == UnitType.String) {
                    DefaultCellEditor2 ed = new DefaultCellEditor2(new JTextField(), new DefaultCellEditor2.ComboAction() {
                        @Override
                        public void actionPerformed(DefaultCellEditor2.ComboActionContext e) {
                            StringExpressionDialog diag = new StringExpressionDialog(tool.studio());
                            diag.setTitle(vp.name().get());
                            diag.setExpression(e.getObject() == null ? "" : e.getObject().toString());
                            if (diag.show()) {
                                e.setObject(diag.getExpression());
                            }
                        }
                    });
                    return ed;
                }
                if (ttype == UnitType.Boolean) {
                    java.util.LinkedHashSet<String> possibleValues = new LinkedHashSet<String>();
                    possibleValues.add("true");
                    possibleValues.add("false");
                    List<String> boolParams = tool.configuration().project().get().parameters().toMap().values().stream().filter(x -> x.type() == UnitType.Boolean)
                            .map(x -> x.name().get()).collect(Collectors.toList());
                    possibleValues.addAll(boolParams);
                    JComboBox v = new JComboBox(possibleValues.toArray(new Object[0]));
                    v.setEditable(true);
                    return new DefaultCellEditor(v);
                }
                if (ttype instanceof UnitType.EnumUnitType) {
                    UnitType.EnumUnitType ee = (UnitType.EnumUnitType) ttype;
                    java.util.List<String> all = new ArrayList<String>();
                    for (Object en : ee.getEnumType().getEnumConstants()) {
                        all.add(en.toString());
                    }
                    List<String> byType = tool.configuration().project().get().parameters().toMap().values().stream().filter(x -> x.type() == ee)
                            .map(x -> x.name().get()).collect(Collectors.toList());
                    all.addAll(byType);

                    JComboBox v = new JComboBox(all.toArray(new String[0]));
                    v.setEditable(true);
                    return new DefaultCellEditor(v);
                }
                DefaultCellEditor2 ed = new DefaultCellEditor2(new JTextField(), new DefaultCellEditor2.ComboAction() {
                    @Override
                    public void actionPerformed(DefaultCellEditor2.ComboActionContext e) {
                        ParamUnit unit = vp.unit().get();
                        if(unit==null){
                            unit=tool.configuration().project().get().units().defaultUnitValue(ttype);
                        }
                        NumberExpressionDialog diag = new NumberExpressionDialog(tool.studio(), ttype, unit,tool.configuration());
                        diag.setTitle(vp.name().get());
                        diag.setExpression(e.getObject() == null ? "" : e.getObject().toString());
                        if (diag.show()) {
                            e.setObject(diag.getExpression());
                        }
                    }
                });
                return ed;
            }
        } else if (column == 1) {
            JComboBox c = new JComboBox(UnitType.values());
            return new DefaultCellEditor(c);
        } else if (column == 4) {
            TreePath p = this.getPathForRow(row);
            Object c = HWSProjectParametersTool.toItem(p.getLastPathComponent());
            if (c instanceof HWParameterValue) {
                HWParameterValue vp = (HWParameterValue) c;
                UnitType utb = vp.type().get();
                JComboBox combo = new JComboBox(utb.unitValues());
                return new DefaultCellEditor(combo);
            }
        }
        return super.getCellEditor(row, column);
    }

}
