package net.vpc.scholar.hadrumaths.plot.console;

import net.vpc.scholar.hadrumaths.Chronometer;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;
import java.text.DecimalFormat;

import net.vpc.scholar.hadrumaths.util.EnhancedProgressMonitor;
import net.vpc.scholar.hadrumaths.util.swingext.GridBagLayout2;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
* @creationtime 6 janv. 2007 12:23:22
*/
public class TaskComponent extends JPanel implements ActionListener {
    private JLabel windowTitle;
    private JLabel descLabel;
    private JLabel yLabel;
    private JLabel timeDurLabel;
    private JLabel timeDurValue;
    private JLabel timeElapLabel;
    private JLabel timeElapValue;
    private JLabel timeRemLabel;
    private JLabel timeRemValue;
    private JProgressBar yProgress;
    private EnhancedProgressMonitor thread;
    private JToggleButton pause;
    private JButton detach;
    private JButton kill;
    private TaskMonitor taskMonitor;

    public TaskComponent(TaskMonitor taskMonitor, EnhancedProgressMonitor thread, String windowTitleString, String descString) {
        setOpaque(true);
        setBackground(Color.WHITE);
        thread.start("Task Starting...");
        this.taskMonitor = taskMonitor;
        this.thread = thread;
        yProgress = new JProgressBar(0, 100);
        yProgress.setStringPainted(true);
        windowTitle = new JLabel();
        windowTitle.setForeground(Color.BLUE);
        yProgress.setOpaque(true);
        yProgress.setBackground(Color.WHITE);
        yLabel = new JLabel();

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

        descLabel = new JLabel();
        pause = new JToggleButton("");
        pause.setOpaque(true);
        pause.setBackground(Color.WHITE);
        pause.setIcon(new ImageIcon(getClass().getResource("PauseTask.gif")));
        pause.setToolTipText("Pause");
        pause.addActionListener(this);
        detach = new JButton(new ImageIcon(getClass().getResource("NextTask.gif")));
        detach.setOpaque(true);
        detach.setBackground(Color.WHITE);
        detach.setToolTipText("Start Next");
        detach.addActionListener(this);
        kill = new JButton("");
        kill.setOpaque(true);
        kill.setBackground(Color.WHITE);
        kill.setIcon(new ImageIcon(getClass().getResource("StopTask.gif")));
        kill.setToolTipText("Kill");
        kill.addActionListener(this);
        setLayout(new GridBagLayout2(
                        "[<^windowTitle+  :                  :                 :               :               :               ] \n" +
                                "[<^yLabel=-      :                  :                 :               :               :               ]\n" +
                                "[<^descLabel-=    :                :                 :                 :               :               ]\n" +
                                "[<^timeDurLabel][<^timeDurValue-=][<^timeElapLabel][<^timeElapValue-=][<^timeRemLabel][<^timeRemValue-=]\n" +
                                "[<^yProgress-=    :               :                   :               :                ][<^toolbar     ]"
                )
                        .setInsets(".*", new Insets(1, 1, 1, 1))
        );
        setBorder(BorderFactory.createEtchedBorder());
        add(windowTitle,"windowTitle");
        add(yLabel,"yLabel");
        add(timeDurLabel,"timeDurLabel");
        add(timeDurValue,"timeDurValue");
        add(timeElapLabel,"timeElapLabel");
        add(timeElapValue,"timeElapValue");
        add(timeRemLabel,"timeRemLabel");
        add(timeRemValue, "timeRemValue");
        add(descLabel, "descLabel");
        add(yProgress, "yProgress");
        JToolBar jToolBar=new JToolBar(JToolBar.HORIZONTAL);
        jToolBar.setBorderPainted(false);
        jToolBar.setRollover(true);
        jToolBar.setBackground(Color.WHITE);
        jToolBar.setOpaque(true);
        jToolBar.setFloatable(false);
        jToolBar.add(pause);
        jToolBar.add(kill);
        jToolBar.add(detach);
        add(jToolBar, "toolbar");
        windowTitle.setText(windowTitleString);
        descLabel.setText(descString);
    }


    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == detach) {
            taskMonitor.unfreeze();
            detach.setEnabled(false);
        } else if (source == pause) {
            if (thread != null) {
                if (pause.isSelected()) {
                    setBackground(Color.LIGHT_GRAY);
                    yProgress.setBackground(Color.LIGHT_GRAY);
                    detach.setBackground(Color.LIGHT_GRAY);
                    kill.setBackground(Color.LIGHT_GRAY);
                    pause.setBackground(Color.LIGHT_GRAY);
                    thread.suspend();
                } else {
                    setBackground(Color.WHITE);
                    yProgress.setBackground(Color.WHITE);
                    detach.setBackground(Color.WHITE);
                    kill.setBackground(Color.WHITE);
                    pause.setBackground(Color.WHITE);
                    thread.resume();
                }
            }
        } else if (source == kill) {
            kill();
        }
    }

    public void kill() {
        if (thread != null) {
            try {
                thread.cancel();
            } catch (ThreadDeath e) {
                //e.printStackTrace();
            }
            kill.setEnabled(false);
        }
    }

    public void resetMonitor() {
        updateProgress(0, null);
    }

    public void ticMonitor(int index, int maxIndex) {
        if (thread != null) {
            double progressValue = thread.getProgressValue();
            if(thread.getProgressMessage().getText().length()>0) {
                descLabel.setText(thread.getProgressMessage().getText());
            }
            if (!thread.isTerminated() && !pause.isSelected()) {
                double d = thread.getProgressValue();
                if(d<0 || d>1.0){
                    System.err.println("%= "+d+"????????????");
                    d=d/100;
                }
                double d100;
                if (Double.isNaN(d)) {
                    d = 100;
                    d100 = 100;
                }else{
                    d100=d*100;
                }
                Chronometer chrono = thread.getChronometer();
                long spent = chrono.isStarted() ? chrono.getTime():0;
                long remaining = spent == 0 ? -1 : (long) ((spent / d) * (1 - d));
                long approx = spent == 0 ? -1 : (long) ((spent / d));
                timeDurLabel.setText("Duration :");
                timeDurValue.setText(Chronometer.formatPeriodNano(approx, Chronometer.DatePart.s));
                timeElapLabel.setText("Elapsed :");
                timeElapValue.setText(Chronometer.formatPeriodNano(spent, Chronometer.DatePart.s));
                timeRemLabel.setText("Remaining :");
                timeRemValue.setText(Chronometer.formatPeriodNano(remaining, Chronometer.DatePart.s));
                detach.setEnabled(index<maxIndex);
                updateProgress(d100, thread.getProgressMessage().toString());
            } else if(thread.isTerminated()){
                windowTitle.setForeground(Color.RED);
                yLabel.setForeground(Color.RED);
                descLabel.setForeground(Color.RED);
                detach.setEnabled(false);
                kill.setEnabled(false);
                pause.setEnabled(false);
                thread = null;
            }
        } else {
            taskMonitor.removeTask(this);
        }
    }
    private static final DecimalFormat df=new DecimalFormat("00.00");

    public void updateProgress(double progress, String message) {
        yLabel.setText(message == null ? "" : message);
        yProgress.setValue((int) progress);
        StringBuilder sb = new StringBuilder(df.format(progress));
        sb.append("%");
//        if (spentTime > 0) {
//            sb.append(" | + ").append(Chronometer.formatPeriod(spentTime,Chronometer.DatePart.s));
//        }
//        if (remainingTime > 0) {
//            sb.append(" | - ").append(Chronometer.formatPeriod(remainingTime,Chronometer.DatePart.s));
//        }
        yProgress.setString(sb.toString());
    }

    @Override
    public String toString() {
        return "TaskComponent{" +
                "windowTitle=" + windowTitle.getText() +
                ", descLabel=" + descLabel.getText() +
                '}';
    }
}
