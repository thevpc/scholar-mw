/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.props.components;

import java.awt.Color;
import java.util.ArrayList;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.TreePath;
import net.vpc.common.swings.ColorChooserEditor;
import net.vpc.common.app.AppPropertiesNodeItem;
import net.vpc.scholar.hadruwaves.project.configuration.HWConfigurationRun;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.HadruwavesStudio;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.components.NumberExpressionDialog;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.components.StringDialog;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.components.StringExpressionDialog;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.props.HWSProjectPropertiesTool;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.util.DefaultCellEditor2;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.util.PValueViewPropertyE;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.JXTreeTable;

/**
 *
 * @author vpc
 */
public class CustomJXTreeTableImpl extends JXTreeTable {

    private HadruwavesStudio studio;

    public CustomJXTreeTableImpl(HWSProjectPropertiesTool tool) {
        this.studio = tool.studio();
        setTreeCellRenderer(new PropsTreeCellRenderer(tool));
    }

    @Override
    public TableCellEditor getCellEditor(int row, int column) {
        if (column == 1) {
            TreePath p = this.getPathForRow(row);
            Object c = HWSProjectPropertiesTool.toITem(p.getLastPathComponent());
            if (c instanceof AppPropertiesNodeItem) {
                AppPropertiesNodeItem vp = (AppPropertiesNodeItem) c;
                if (vp instanceof PValueViewPropertyE) {
                    PValueViewPropertyE ee = (PValueViewPropertyE) vp;
                    if (ee.getExpressionType().equals(Boolean.class)) {
                        JComboBox v = new JComboBox(new Object[]{"true", "false"});
                        v.setEditable(true);
                        return new DefaultCellEditor(v);
                    } else if (ee.getExpressionType().isEnum()) {
                        java.util.List<String> vals = new ArrayList<String>();
                        for (Object object : ee.getExpressionType().getEnumConstants()) {
                            vals.add(object.toString());
                        }
                        JComboBox v = new JComboBox(vals.toArray(new Object[0]));
                        v.setEditable(true);
                        return new DefaultCellEditor(v);
                    } else if (ee.getExpressionType().equals(String.class)) {
                        DefaultCellEditor2 ed = new DefaultCellEditor2(new JTextField(), new DefaultCellEditor2.ComboAction() {
                            @Override
                            public void actionPerformed(DefaultCellEditor2.ComboActionContext e) {
                                StringExpressionDialog diag = new StringExpressionDialog(studio);
                                diag.setTitle(vp.name());
                                diag.setExpression(e.getObject() == null ? "" : e.getObject().toString());
                                if (diag.show()) {
                                    e.setObject(diag.getExpression());
                                }
                            }
                        });
                        return ed;
                    } else {
                        DefaultCellEditor2 ed = new DefaultCellEditor2(new JTextField(), new DefaultCellEditor2.ComboAction() {
                            @Override
                            public void actionPerformed(DefaultCellEditor2.ComboActionContext e) {

                                HWConfigurationRun configuration = studio.proc().selectedConfiguration().get();
                                NumberExpressionDialog diag = new NumberExpressionDialog(studio, ee.unitType(),
                                        configuration.project().get().units().defaultUnitValue(ee.unitType()),
                                        configuration);
                                diag.setTitle(vp.name());
                                diag.setExpression(e.getObject() == null ? "" : e.getObject().toString());
                                if (diag.show()) {
                                    e.setObject(diag.getExpression());
                                }
                            }
                        });
                        return ed;
                    }
                }
                if (Boolean.class.equals(vp.getType()) || Boolean.TYPE.equals(vp.getType())) {
                    JXTable.BooleanEditor b = new BooleanEditor();
                    JCheckBox component = (JCheckBox) b.getComponent();
                    component.setHorizontalAlignment(SwingConstants.LEFT);
                    return b;
                }
                if (String.class.equals(vp.getType())) {
                    if (vp.getValues() == null) {
                        DefaultCellEditor2 ed = new DefaultCellEditor2(new JTextField(), new DefaultCellEditor2.ComboAction() {
                            @Override
                            public void actionPerformed(DefaultCellEditor2.ComboActionContext e) {
                                StringDialog diag = new StringDialog(studio);
                                diag.setTitle(vp.name());
                                diag.setExpression(e.getObject() == null ? "" : e.getObject().toString());
                                if (diag.show()) {
                                    e.setObject(diag.getExpression());
                                }
                            }
                        });
                        return ed;
                    } else {
                        Object[] values = vp.getValues();
                        JComboBox v = new JComboBox(values);
                        v.setEditable(true);
                        v.setSelectedItem(vp.object());
                        return new DefaultCellEditor(v);
                    }
                }
                if (Color.class.equals(vp.getType())) {
                    return new ColorChooserEditor();
                }
                Object[] values = vp.getValues();
                if (values != null) {
                    JComboBox v = new JComboBox(values);
                    if (vp instanceof PValueViewPropertyE) {
                        if (vp.getType().isEnum()) {
                            v.setEditable(true);
                        }
                        v.setSelectedItem(vp.object());
                        return new DefaultCellEditor(v);
                    } else {
                        v.setSelectedItem(vp.object());
                        return new DefaultCellEditor(v);
                    }
                }
                //vp.object()
            }
        }
        return super.getCellEditor(row, column);
    }

    @Override
    public TableCellRenderer getCellRenderer(int row, int column) {
        if (column == 1) {
            TreePath p = this.getPathForRow(row);
            Object c = HWSProjectPropertiesTool.toITem(p.getLastPathComponent());
            if (c instanceof AppPropertiesNodeItem) {
                AppPropertiesNodeItem vp = (AppPropertiesNodeItem) c;
                if (vp instanceof PValueViewPropertyE) {
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
            Object c = HWSProjectPropertiesTool.toITem(p.getLastPathComponent());
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
        }
        return super.getCellRenderer(row, column);
    }

}
