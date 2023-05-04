package net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.tasks.components;

import net.thevpc.echo.Application;
import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.common.mon.TaskListener;
import net.thevpc.common.mon.TaskMonitor;
import net.thevpc.common.mon.TaskMonitorManager;
import net.thevpc.common.strings.StringUtils;
import net.thevpc.common.swing.layout.GridBagLayout2;
import net.thevpc.common.time.DatePart;
import net.thevpc.common.time.TimeDuration;
import net.thevpc.echo.swing.icons.SwingAppImage;
import net.thevpc.scholar.hadruplot.util.PlotUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime 6 janv. 2007 12:23:22
 */
public class HWTaskComponent extends JPanel implements ActionListener {
    private static final DecimalFormat df = new DecimalFormat("00.00");
    private JLabel windowTitle;
    private JLabel progressMessageLabel;
    private JLabel timeDurLabel;
    private JLabel timeDurValue;
    private JLabel timeElapLabel;
    private JLabel timeElapValue;
    private JLabel timeRemLabel;
    private JLabel timeRemValue;
    private JProgressBar yProgress;
    private TaskMonitor monitor;
    private JToggleButton pause;
//    private JButton detach;
    private JButton kill;
    private TaskMonitorManager taskMonitor;
    private JToolBar jToolBar;
    private TaskListener listener;
    private Application app;

//    @Override
//    public ProgressMonitor getProgressMonitor() {
//        return monitor;
//    }

    public HWTaskComponent(TaskMonitorManager taskMonitor, TaskMonitor monitor,Application app) {
        setOpaque(true);
        Color white = getBackground1();
        setBackground(white);
        this.taskMonitor = taskMonitor;
        this.monitor = monitor;
        this.app = app;
        listener = new TaskListener() {
            @Override
            public void taskStarted(TaskMonitor monitor) {
                updateComponentUI();
            }

            @Override
            public void taskResumed(TaskMonitor monitor) {
                updateComponentUI();
            }

            @Override
            public void taskSuspended(TaskMonitor monitor) {
                updateComponentUI();
            }

            @Override
            public void taskCanceled(TaskMonitor monitor) {
                updateComponentUI();
                monitor.removeListener(listener);
            }

            @Override
            public void taskTerminated(TaskMonitor monitor) {
                updateComponentUI();
                monitor.removeListener(listener);
            }

            @Override
            public void taskReset(TaskMonitor task) {
                updateComponentUI();
            }

            @Override
            public void taskBlocked(TaskMonitor monitor) {
                updateComponentUI();
            }

            @Override
            public void taskUnblocked(TaskMonitor monitor) {
                updateComponentUI();
            }
        };
        this.monitor.addListener(listener);
        yProgress = new JProgressBar(0, 100);
        yProgress.setStringPainted(true);
        windowTitle = new JLabel();
        windowTitle.setForeground(Color.BLUE);
        yProgress.setOpaque(true);
        yProgress.setBackground(white);
        progressMessageLabel = new JLabel();

        timeDurLabel = new JLabel();
        timeDurLabel.setFont(timeDurLabel.getFont().deriveFont(Font.BOLD));
        timeDurValue = new JLabel();
        timeDurValue.setFont(timeDurValue.getFont().deriveFont(Font.PLAIN));

        timeElapLabel = new JLabel();
        timeElapLabel.setFont(timeElapLabel.getFont().deriveFont(Font.BOLD));
        timeElapValue = new JLabel();
        timeElapValue.setFont(timeElapValue.getFont().deriveFont(Font.PLAIN));

        timeRemLabel = new JLabel();
        timeRemLabel.setFont(timeRemLabel.getFont().deriveFont(Font.BOLD));
        timeRemValue = new JLabel();
        timeRemValue.setFont(timeRemValue.getFont().deriveFont(Font.PLAIN));

        pause = new JToggleButton("");
        pause.setOpaque(true);
        pause.setBackground(white);
        pause.setToolTipText("Pause");
        pause.addActionListener(this);
//        detach = new JButton(PlotUtils.getScaledImageIcon(TaskComponent.class.getResource("NextTask.png"), 16, 16));
//        detach.setOpaque(true);
//        detach.setBackground(Color.WHITE);
//        detach.setToolTipText("Start Next");
//        detach.addActionListener(this);
        kill = new JButton("");
        kill.setOpaque(true);
        kill.setBackground(white);
        kill.setToolTipText("Kill");
        kill.addActionListener(this);

        jToolBar = new JToolBar(JToolBar.HORIZONTAL);
        jToolBar.setBorderPainted(false);
        jToolBar.setRollover(true);
        jToolBar.setBackground(white);
        jToolBar.setOpaque(true);
        jToolBar.setFloatable(false);
        jToolBar.add(pause);
        jToolBar.add(kill);

        JPanel row1=new JPanel(new BorderLayout());
        row1.add(windowTitle,BorderLayout.CENTER);
        row1.setOpaque(false);

        JPanel row2=new JPanel(new BorderLayout());
        row2.add(progressMessageLabel,BorderLayout.CENTER);
        row2.setOpaque(false);

        JPanel row3=new JPanel(new GridLayout(1,3));

        Box cell31=Box.createHorizontalBox();
        cell31.add(timeDurLabel);
        cell31.add(timeDurValue);
        row3.add(cell31);
        row3.setOpaque(false);

        Box cell32=Box.createHorizontalBox();
        cell32.add(timeElapLabel);
        cell32.add(timeElapValue);
        row3.add(cell32);

        Box cell33=Box.createHorizontalBox();
        cell33.add(timeRemLabel);
        cell33.add(timeRemValue);
        row3.add(cell33);

        JPanel row4=new JPanel(new BorderLayout());
        row4.add(yProgress,BorderLayout.CENTER);
        row4.add(jToolBar,BorderLayout.LINE_END);
        row4.setOpaque(false);

        setLayout(new GridLayout(-1,1));
        setBorder(BorderFactory.createEtchedBorder());
        add(row1);
        add(row2);
        add(row3);
        add(row4);
//        add(windowTitle, "windowTitle");
//        add(progressMessageLabel, "yLabel");
//        add(timeDurLabel, "timeDurLabel");
//        add(timeDurValue, "timeDurValue");
//        add(timeElapLabel, "timeElapLabel");
//        add(timeElapValue, "timeElapValue");
//        add(timeRemLabel, "timeRemLabel");
//        add(timeRemValue, "timeRemValue");
//        add(yProgress, "yProgress");
//        add(jToolBar, "toolbar");
        windowTitle.setText(monitor.getName());
        progressMessageLabel.setText(monitor.getDesc());
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == pause) {
            if (monitor != null) {
                if (pause.isSelected()) {
                    monitor.suspend();
                } else {
                    monitor.resume();
                }
            }
        } else if (source == kill) {
            terminate();
        }
        updateComponentUIImpl();
    }

    //    @Override
    public void terminate() {
        monitor.terminate();
//        if (monitor != null) {
//            try {
//                monitor.cancel();
//            } catch (ThreadDeath e) {
//                //e.printStackTrace();
//            }
//        }
//        updateComponentUIImpl();
    }

    //    @Override
    public void resetMonitor() {
        updateProgress(0, null, false);
    }

    public Color getDisabledColor() {
        Color c1 = UIManager.getColor("ToggleButton.disabledSelectedBackground");
        if (c1 == null) {
            c1 = Color.LIGHT_GRAY;
        }
        return c1;
    }
    public Color getBackground1() {
        Color c1 = UIManager.getColor("Table.background");
        if (c1 == null) {
            c1 = Color.WHITE;
        }
        return c1;
    }

    public void updateComponentUIImpl() {
        pause.setIcon(SwingAppImage.imageIconOf(app.iconSets().icon("Pause").get()));
        kill.setIcon(SwingAppImage.imageIconOf(app.iconSets().icon("Delete").get()));
        updateComponentUI();
    }

    public void updateComponentUI() {
//        pause.setSelectedIcon(app.iconSet().icon("Stop").get());
        if (monitor != null) {
            TimeDuration spent = TimeDuration.ofMillis(monitor.getDuration());
            TimeDuration remaining = null;
            TimeDuration approx = null;
            double d = Double.NaN;
            boolean indeterminate = true;
            double d100 = 0;
            if (monitor instanceof ProgressMonitor) {
                ProgressMonitor pmonitor = (ProgressMonitor) this.monitor;
                d = pmonitor.getProgress();
                if (Double.isNaN(d)) {
                    d100 = 0;
                    indeterminate = true;
                } else {
                    if (d < 0) {
                        d = 0;
                    } else if (d > 1.0) {
                        d = 1;
                    }
                    if(d>0){
                        remaining = TimeDuration.ofMillis(pmonitor.getEstimatedRemainingDuration());
                        approx = TimeDuration.ofMillis(pmonitor.getEstimatedTotalDuration());
                    }
                    d100 = d * 100;
                    indeterminate = false;
                }
            } else {
                indeterminate = true;
                d100 = 0;
            }
            String message = monitor.getMessage().toString();
            if(StringUtils.isBlank(message)){
                if(!monitor.isStarted() && StringUtils.isBlank(message)){
                    message="Not yet started...";
                }else{
                    message="Running...";
                }
            }
            if (monitor.isBlocked()) {
                windowTitle.setForeground(Color.DARK_GRAY);
                progressMessageLabel.setForeground(Color.DARK_GRAY);
                timeDurLabel.setText("Blocked....");
                timeDurValue.setText("");
                timeElapLabel.setText("");
                timeElapValue.setText("");
                timeRemLabel.setText("");
                timeRemValue.setText("");
            } else if (monitor.isTerminated()) {
                windowTitle.setForeground(Color.RED);
                progressMessageLabel.setForeground(Color.RED);
                timeDurLabel.setText("Duration : ");
                timeDurValue.setText(approx == null ? "?" : approx.toString(DatePart.SECOND));
                timeElapLabel.setText("Elapsed : ");
                timeElapValue.setText(spent.toString(DatePart.SECOND));
                timeRemLabel.setText("(Terminated)");
                timeRemValue.setText("");
            } else {
                windowTitle.setForeground(Color.BLUE);
                progressMessageLabel.setForeground(Color.BLACK);
                timeDurLabel.setText("Duration : ");
                timeDurValue.setText(approx == null ? "?" : approx.toString(DatePart.SECOND));
                timeElapLabel.setText("Elapsed : ");
                timeElapValue.setText(spent.toString(DatePart.SECOND));
                timeRemLabel.setText("Remaining : ");
                timeRemValue.setText(remaining == null ? "?" : remaining.toString(DatePart.SECOND));
            }
            updateProgress(d100, message, indeterminate);
        } else {
            windowTitle.setForeground(Color.RED);
            progressMessageLabel.setForeground(Color.RED);
        }
        kill.setEnabled(monitor != null && (!monitor.isCanceled() && !monitor.isTerminated()));
        pause.setEnabled(monitor != null && (!monitor.isCanceled() && !monitor.isSuspended() && !monitor.isTerminated() && monitor.isStarted()));
        if (monitor == null || monitor.isBlocked() || monitor.isSuspended()) {
            Color c1 = getDisabledColor();
            setBackground(c1);
            yProgress.setBackground(c1);
            kill.setBackground(c1);
            pause.setBackground(c1);
            jToolBar.setBackground(c1);
        } else {
            Color c1 = getBackground1();
            setBackground(c1);
            yProgress.setBackground(c1);
            kill.setBackground(c1);
            pause.setBackground(c1);
            jToolBar.setBackground(c1);
        }
    }

    public void updateProgress(double progress100, String message, boolean indeterminate) {
        progressMessageLabel.setText(message == null ? "" : message);
        if (Double.isNaN(progress100)) {
            yProgress.setString("");
        } else {
            yProgress.setString(df.format(progress100) + "%");
            yProgress.setValue((int) progress100);
        }
        yProgress.setIndeterminate(indeterminate);
    }

    @Override
    public String toString() {
        return "TaskComponent{" +
                "title=" + windowTitle.getText() +
                ", message=" + progressMessageLabel.getText() +
                '}';
    }
}
