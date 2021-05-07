package net.thevpc.scholar.hadruplot.console;

import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.common.mon.TaskListener;
import net.thevpc.common.mon.TaskMonitor;
import net.thevpc.common.mon.TaskMonitorManager;
import net.thevpc.common.swing.layout.GridBagLayout2;
import net.thevpc.common.time.DatePart;
import net.thevpc.common.time.TimeDuration;
import net.thevpc.scholar.hadruplot.util.PlotUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.stream.Stream;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime 6 janv. 2007 12:23:22
 */
public class TaskComponent extends JPanel implements ActionListener {
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

//    @Override
//    public ProgressMonitor getProgressMonitor() {
//        return monitor;
//    }

    public TaskComponent(TaskMonitorManager taskMonitor, TaskMonitor monitor) {
        setOpaque(true);
        setBackground(Color.WHITE);
        this.taskMonitor = taskMonitor;
        this.monitor = monitor;
        listener = new TaskListener() {
            @Override
            public void taskStarted(TaskMonitor monitor) {
                updateComponentUIImpl();
            }

            @Override
            public void taskResumed(TaskMonitor monitor) {
                updateComponentUIImpl();
            }

            @Override
            public void taskSuspended(TaskMonitor monitor) {
                updateComponentUIImpl();
            }

            @Override
            public void taskCanceled(TaskMonitor monitor) {
                updateComponentUIImpl();
                monitor.removeListener(listener);
            }

            @Override
            public void taskTerminated(TaskMonitor monitor) {
                updateComponentUIImpl();
                monitor.removeListener(listener);
            }

            @Override
            public void taskReset(TaskMonitor task) {
                updateComponentUIImpl();
            }

            @Override
            public void taskBlocked(TaskMonitor monitor) {
                updateComponentUIImpl();
            }

            @Override
            public void taskUnblocked(TaskMonitor monitor) {
                updateComponentUIImpl();
            }
        };
        this.monitor.addListener(listener);
        yProgress = new JProgressBar(0, 100);
        yProgress.setStringPainted(true);
        windowTitle = new JLabel();
        windowTitle.setForeground(Color.BLUE);
        yProgress.setOpaque(true);
        yProgress.setBackground(Color.WHITE);
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
        pause.setBackground(Color.WHITE);
        pause.setToolTipText("Pause");
        pause.addActionListener(this);
//        detach = new JButton(PlotUtils.getScaledImageIcon(TaskComponent.class.getResource("NextTask.png"), 16, 16));
//        detach.setOpaque(true);
//        detach.setBackground(Color.WHITE);
//        detach.setToolTipText("Start Next");
//        detach.addActionListener(this);
        kill = new JButton("");
        kill.setOpaque(true);
        kill.setBackground(Color.WHITE);
        kill.setToolTipText("Kill");
        kill.addActionListener(this);
        setLayout(new GridBagLayout2(
                        "[<^windowTitle+  :                  :                 :               :               :               ] \n" +
                                "[<^yLabel=-      :                  :                 :               :               :               ]\n" +
                                "[<^timeDurLabel][<^timeDurValue-=][<^timeElapLabel][<^timeElapValue-=][<^timeRemLabel][<^timeRemValue-=]\n" +
                                "[<^yProgress-=    :               :                   :               :                ][<^toolbar     ]"
                )
                        .setInsets(".*", new Insets(1, 1, 1, 1))
        );
        setBorder(BorderFactory.createEtchedBorder());
        add(windowTitle, "windowTitle");
        add(progressMessageLabel, "yLabel");
        add(timeDurLabel, "timeDurLabel");
        add(timeDurValue, "timeDurValue");
        add(timeElapLabel, "timeElapLabel");
        add(timeElapValue, "timeElapValue");
        add(timeRemLabel, "timeRemLabel");
        add(timeRemValue, "timeRemValue");
        add(yProgress, "yProgress");
        jToolBar = new JToolBar(JToolBar.HORIZONTAL);
        jToolBar.setBorderPainted(false);
        jToolBar.setRollover(true);
        jToolBar.setBackground(Color.WHITE);
        jToolBar.setOpaque(true);
        jToolBar.setFloatable(false);
        jToolBar.add(pause);
        jToolBar.add(kill);
        add(jToolBar, "toolbar");
        windowTitle.setText(monitor.getName());
        progressMessageLabel.setText(monitor.getDesc());
        pause.setIcon(PlotUtils.getScaledImageIcon(TaskComponent.class.getResource("PauseTask.png"), 16, 16));
        kill.setIcon(PlotUtils.getScaledImageIcon(TaskComponent.class.getResource("StopTask.png"), 16, 16));
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

    public void updateComponentUIImpl() {
        if (monitor != null) {
            TimeDuration spent = new TimeDuration(monitor.getDuration());
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
                        remaining = new TimeDuration(pmonitor.getEstimatedRemainingDuration());
                        approx = new TimeDuration(pmonitor.getEstimatedTotalDuration());
                    }
                    d100 = d * 100;
                    indeterminate = false;
                }
            } else {
                indeterminate = true;
                d100 = 0;
            }
            String message = monitor.getMessage().toString();
            if(!monitor.isStarted() && message==null || message.isEmpty()){
                message="Not yet started...";
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
                timeDurLabel.setText("Duration :");
                timeDurValue.setText(approx == null ? "?" : approx.toString(DatePart.SECOND));
                timeElapLabel.setText("Elapsed :");
                timeElapValue.setText(spent.toString(DatePart.SECOND));
                timeRemLabel.setText("(Terminated)");
                timeRemValue.setText("");
            } else {
                windowTitle.setForeground(Color.BLUE);
                progressMessageLabel.setForeground(Color.BLACK);
                timeDurLabel.setText("Duration :");
                timeDurValue.setText(approx == null ? "?" : approx.toString(DatePart.SECOND));
                timeElapLabel.setText("Elapsed :");
                timeElapValue.setText(spent.toString(DatePart.SECOND));
                timeRemLabel.setText("Remaining :");
                timeRemValue.setText(remaining == null ? "?" : remaining.toString(DatePart.SECOND));
            }
            updateProgress(d100, message, indeterminate);
        } else {
            windowTitle.setForeground(Color.RED);
            progressMessageLabel.setForeground(Color.RED);
        }
        kill.setEnabled(monitor != null && (!monitor.isCanceled() && !monitor.isTerminated() && monitor.isStarted()));
        pause.setEnabled(monitor != null && (!monitor.isCanceled() && !monitor.isSuspended() && !monitor.isTerminated() && monitor.isStarted()));
        if (monitor == null || monitor.isBlocked() || monitor.isSuspended()) {
            setBackground(Color.LIGHT_GRAY);
            yProgress.setBackground(Color.LIGHT_GRAY);
            kill.setBackground(Color.LIGHT_GRAY);
            pause.setBackground(Color.LIGHT_GRAY);
            jToolBar.setBackground(Color.LIGHT_GRAY);
        } else {
            setBackground(Color.WHITE);
            yProgress.setBackground(Color.WHITE);
            kill.setBackground(Color.WHITE);
            pause.setBackground(Color.WHITE);
            jToolBar.setBackground(Color.WHITE);
        }
    }

    public void ticMonitor(int index, int maxIndex) {
        updateComponentUIImpl();
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
