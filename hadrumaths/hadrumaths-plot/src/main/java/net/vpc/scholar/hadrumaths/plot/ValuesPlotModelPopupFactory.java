package net.vpc.scholar.hadrumaths.plot;

import net.vpc.scholar.hadruplot.extension.PlotModelPopupFactory;
import net.vpc.scholar.hadruplot.model.ValuesPlotModel;
import net.vpc.common.strings.StringUtils;
import net.vpc.common.swings.GridBagLayout2;
import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.ComplexMatrix;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.util.ArrayUtils;
import net.vpc.scholar.hadrumaths.util.PlatformUtils;
import net.vpc.scholar.hadruplot.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.ToDoubleFunction;

public class ValuesPlotModelPopupFactory implements PlotModelPopupFactory {

    @Override
    public void preparePopup(PlotModelPopupFactoryContext context) {
        final ValuesPlotModel model = (ValuesPlotModel) context.getModel();
        if (context.getFunctionsMenu() != null) {
            ButtonGroup g = new ButtonGroup();
            for (ToDoubleFunction<Object> toDoubleConverter : PlotDoubleConverter.values()) {
                JCheckBoxMenuItem f = new JCheckBoxMenuItem(new Plot.DoubleTypeAction(context.getModelProvider(), StringUtils.toCapitalized(toDoubleConverter.toString()), toDoubleConverter));
                f.setSelected(PlatformUtils.notnull(model.getConverter(), PlotDoubleConverter.ABS).equals(toDoubleConverter));
                g.add(f);
                context.getFunctionsMenu().add(f);
            }
        }
        context.getExtProperties().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Complex[][] z1 = ArrayUtils.toComplex(model.getZ());
                if (z1 != null) {
                    ComplexMatrix c = Maths.matrix(z1);
                    double n1 = Double.NaN;
                    double n2 = Double.NaN;
                    double n3 = Double.NaN;
                    double n4 = Double.NaN;
                    double cd1 = Double.NaN;
                    double cd2 = Double.NaN;
                    double cd3 = Double.NaN;
                    Complex d1 = Complex.NaN;
                    int m1 = c.getRowCount();
                    int m2 = c.getColumnCount();
                    try {
                        n1 = c.norm1();
                    } catch (Exception e1) {
                        //
                    }
                    try {
                        n2 = c.norm2();
                    } catch (Exception e1) {
                        //
                    }
                    try {
                        n3 = c.norm3();
                    } catch (Exception e1) {
                        //
                    }
                    try {
                        n4 = c.normInf();
                    } catch (Exception e1) {
                        //
                    }
                    try {
                        cd1 = c.cond();
                    } catch (Exception e1) {
                        //
                    }
                    try {
                        cd2 = c.cond2();
                    } catch (Exception e1) {
                        //
                    }
                    try {
                        cd3 = c.condHadamard();
                    } catch (Exception e1) {
                        //
                    }
                    try {
                        d1 = c.det();
                    } catch (Exception e1) {
                        //
                    }
                    JPanel p = new JPanel(new GridBagLayout2().addLine("[<N1L][<+=N1]").addLine("[<N2L][<+=N2]").addLine("[<N3L][<+=N3]").addLine("[<N4L][<+=N4]").addLine("[<C1L][<+=C1]").addLine("[<C2L][<+=C2]").addLine("[<C3L][<+=C3]").addLine("[<D1L][<+=D1]").addLine("[<M1L][<+=M1]").addLine("[<M2L][<+=M2]").setInsets(".*", new Insets(3, 3, 3, 3)));
                    p.add(new JLabel("Norm1"), "N1L");
                    p.add(new JLabel(String.valueOf(n1)), "N1");
                    p.add(new JLabel("Norm2"), "N2L");
                    p.add(new JLabel(String.valueOf(n2)), "N2");
                    p.add(new JLabel("Norm3"), "N3L");
                    p.add(new JLabel(String.valueOf(n3)), "N3");
                    p.add(new JLabel("NormInf"), "N4L");
                    p.add(new JLabel(String.valueOf(n4)), "N4");
                    p.add(new JLabel("Cond"), "C1L");
                    p.add(new JLabel(String.valueOf(cd1)), "C1");
                    p.add(new JLabel("Cond2"), "C2L");
                    p.add(new JLabel(String.valueOf(cd2)), "C2");
                    p.add(new JLabel("CondH"), "C3L");
                    p.add(new JLabel(String.valueOf(cd3)), "C3");
                    p.add(new JLabel("Det"), "D1L");
                    p.add(new JLabel(String.valueOf(d1)), "D1");
                    p.add(new JLabel("Rows"), "M1L");
                    p.add(new JLabel(String.valueOf(m1)), "M1");
                    p.add(new JLabel("Cols"), "M2L");
                    p.add(new JLabel(String.valueOf(m2)), "M2");
                    JOptionPane.showMessageDialog(null, p);
                }
            }
        });
    }
}
