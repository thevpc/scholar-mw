package net.vpc.scholar.hadruwaves.studio.standalone;


import net.vpc.lib.pheromone.application.swing.JOptionPane2;
import net.vpc.lib.pheromone.application.swing.Swings;

import javax.swing.*;
import java.awt.*;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.Matrix;

/**
 * @author Taha Ben Salah (thevpc@walla.com)
 * @creationtime 24 mai 2007 23:20:01
 */
public class MatrixDialog {
    public static Matrix showMatrix(String title, Matrix matrix) {
        CMatrixTableModel model;
        JTable table = new JTable(model = new CMatrixTableModel(Maths.matrix(matrix)));
        JPanel p = new JPanel(new BorderLayout());
        Box footer = Box.createVerticalBox();
        footer.add(Box.createHorizontalGlue());
        footer.add(new JLabel("Norme 1"));
        footer.add(new JTextField("" + matrix.norm1()));
        footer.add(new JLabel("Norme Inf"));
        footer.add(new JTextField("" + matrix.normInf()));
        footer.add(Box.createHorizontalGlue());

        p.add(new JScrollPane(table), BorderLayout.CENTER);
        p.add(footer, BorderLayout.SOUTH);
        JOptionPane2 optionPane = new JOptionPane2(Swings.DEFAULT_PARENT_COMPONENT,
                p,
                "Editeur de Matrices : "+title,
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.OK_CANCEL_OPTION,
                null,
                null);
        optionPane.getDialog().setResizable(true);
        int o = optionPane.showDialog();
        if (o == JOptionPane.OK_OPTION) {
            return model.getMatrix();
        } else {
            return null;
        }
    }
}
