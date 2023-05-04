package net.thevpc.scholar.hadruplot.model.custom;

import net.thevpc.common.time.Chronometer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import net.thevpc.scholar.hadruplot.component.BasePlotPanel;
import net.thevpc.scholar.hadruplot.PlotPanel;
import net.thevpc.scholar.hadruplot.extension.PlotPanelFactory;
import net.thevpc.scholar.hadruplot.model.BasePlotModel;
import net.thevpc.scholar.hadruplot.model.PlotModel;

public class ChronometerModel extends BasePlotModel implements PlotPanelFactory {
    private Chronometer chronometer;

    public ChronometerModel(Chronometer ch) {
        this.chronometer = ch;
    }

    public Chronometer getChronometer() {
        return chronometer;
    }

    @Override
    public PlotPanel create() {
        BasePlotPanel p = new ChronometerPlotPanel();
        p.setModel(this);
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
                    createTimer();
                }

                @Override
                public void componentHidden(ComponentEvent e) {
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
