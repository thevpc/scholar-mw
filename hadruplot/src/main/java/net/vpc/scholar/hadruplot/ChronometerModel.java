package net.vpc.scholar.hadruplot;

import net.vpc.common.util.Chronometer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ChronometerModel extends BasePlotModel implements PlotPanelFactory {
    private Chronometer chronometer;

    public ChronometerModel(Chronometer ch) {
        this.chronometer = ch;
    }

    public Chronometer getChronometer() {
        return chronometer;
    }

    @Override
    public PlotPanel create(PlotModel model) {
        BasePlotPanel p = new ChronometerPlotPanel();
        p.setModel(model);
        return p;
    }

    private static class ChronometerPlotPanel extends BasePlotPanel {
        JLabel title = new JLabel("???");
        JLabel label = new JLabel("???");
        Timer t;

        {
            title.setHorizontalAlignment(SwingConstants.CENTER);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            add(title, BorderLayout.NORTH);
            add(label, BorderLayout.CENTER);
            addComponentListener(new ComponentAdapter() {
                @Override
                public void componentShown(ComponentEvent e) {
                    System.out.println("componentShown");
                    createTimer();
                }

                @Override
                public void componentHidden(ComponentEvent e) {
                    System.out.println("componentHidden");
//                        killTimer();
                }
            });
            createTimer();
        }

        public ChronometerPlotPanel() {
            super(new BorderLayout());
        }

        public ChronometerModel getChronometerModel() {
            return (ChronometerModel) getModel();
        }

        @Override
        protected void modelChanged() {
            PlotModel m = getModel();
            title.setText(m==null?"":m.getPreferredTitle());

        }

        private void createTimer() {
            if (t == null) {
                t = new Timer(500, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        ChronometerModel m = getChronometerModel();
                        if(m!=null && m.getChronometer()!=null) {
                            label.setText(m.getChronometer().toString());
                        }else{
                            label.setText("");
                        }
                    }
                });
                t.start();
            }
        }

        private void killTimer() {
            if (t != null) {
                t.stop();
                t = null;
            }
        }

        @Override
        protected void finalize() throws Throwable {
            super.finalize();
            killTimer();
        }


    }
}
