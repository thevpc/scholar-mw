package net.vpc.scholar.hadruplot.console;

import net.vpc.common.swings.JInternalFrameHelper;
import net.vpc.common.swings.SwingUtilities3;
import net.vpc.common.mon.ProgressMonitor;
import net.vpc.common.mon.ProgressMonitorFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime 6 janv. 2007 12:13:07
 */
public class ProgressTaskMonitorImpl extends JPanel implements ActionListener, ProgressTaskMonitor {
    Box list;
    JButton incrementButton;
    JButton decrementButton;
    JTextField countField;
    private PlotConsole plotConsole;
    private int parallelTasksCount = 1;
    private SerializerThread serializerThread = null;
    private JInternalFrame frame = null;

    public ProgressTaskMonitorImpl(PlotConsole plotConsole) {
        this.plotConsole = plotConsole;

        list = Box.createVerticalBox();

        this.setLayout(new BorderLayout());

        this.add(list, BorderLayout.NORTH);

        JToolBar south = new JToolBar(JToolBar.HORIZONTAL);
        south.setFloatable(false);
        this.add(south, BorderLayout.SOUTH);
        incrementButton = new JButton("");
        decrementButton = new JButton("");
        incrementButton.setIcon(SwingUtilities3.getScaledIcon(ProgressTaskMonitorImpl.class.getResource("Plus.png"),32,32));
        decrementButton.setIcon(SwingUtilities3.getScaledIcon(ProgressTaskMonitorImpl.class.getResource("Minus.png"),32,32));

        countField = new JTextField(String.valueOf(parallelTasksCount));
        countField.setEditable(false);
        south.add(Box.createVerticalGlue());
        south.add(Box.createHorizontalGlue());
        south.add(incrementButton);
        south.add(decrementButton);
        south.add(countField);
        incrementButton.addActionListener(this);
        decrementButton.addActionListener(this);
    }

    public JInternalFrameHelper getFrame() {
//        System.out.println("taskMonitor.getFrame");
        return new JInternalFrameHelper(frame);
    }

    public void setFrame(JInternalFrame frame) {
//        System.out.println("taskMonitor.setFrame");
        this.frame = frame;
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == incrementButton) {
            if (parallelTasksCount < 16) {
                parallelTasksCount++;
                if (parallelTasksCount > plotConsole.getTaskMonitor().getTasksCount()) {
                    if (serializerThread != null) {
                        serializerThread.softStop();
                        serializerThread = null;
                    }
                }
            }
        } else if (source == decrementButton) {
            if (parallelTasksCount > 1) {
                parallelTasksCount--;
            }
        }
        countField.setText(String.valueOf(parallelTasksCount));
    }

    @Override
    public ProgressMonitor createMonitor(String name, String description) {
        return addTask(name,description);
    }

    @Override
    public void addTask(PlotThread task) {
        plotConsole.start();
        String windowTitle = (task.getWindowTitle());
        String descLabel = (task.getSerieTitle().toString());
        list.add(new TaskComponent(this, ProgressMonitorFactory.nonnull(task), windowTitle, descLabel));
        task.start();
    }

    @Override
    public net.vpc.common.mon.ProgressMonitor addTask(net.vpc.common.mon.ProgressMonitor task, String windowTitle, String descLabel) {
        plotConsole.start();
        ProgressMonitor m = ProgressMonitorFactory.nonnull(task);
        list.add(new TaskComponent(this, m, windowTitle, descLabel));
        return m;
    }

    @Override
    public net.vpc.common.mon.ProgressMonitor addTask(String windowTitle, String descLabel, final ConsoleTask task) {
        plotConsole.start();//new TLogProgressMonitor(null, printStream)
        final ProgressMonitor m = new PlotConsoleLogProgressMonitor(null, plotConsole.getLog()).temporize(5000);
        list.add(new TaskComponent(this, m, windowTitle, descLabel));
        new Thread(new Runnable() {
            @Override
            public void run() {
                task.run(m);
            }
        }).start();
        return m;
    }

    @Override
    public net.vpc.common.mon.ProgressMonitor addTask(String windowTitle, String descLabel) {
        plotConsole.start();
        ProgressMonitor m = new PlotConsoleLogProgressMonitor(null, plotConsole.getLog()).temporize(5000);
        list.add(new TaskComponent(this, m, windowTitle, descLabel));
        return m;
    }

    @Override
    public void removeTask(final ProgressMonitorTask component) {
        plotConsole.start();
        list.remove((Component)component);
        if (serializerThread != null) {
            if (parallelTasksCount > getTasksCount()) {
                serializerThread.softStop();
                serializerThread = null;
            }
        }
    }

    @Override
    public ProgressMonitorTask[] getTasks() {
        Component[] components = list.getComponents();
        ArrayList<TaskComponent> a = new ArrayList<TaskComponent>();
        for (Component component1 : components) {
            if (component1 instanceof TaskComponent) {
                a.add((TaskComponent) component1);
            }
        }
        return a.toArray(new TaskComponent[0]);
    }

    @Override
    public void resetMonitor() {
        for (ProgressMonitorTask t : getTasks()) {
            t.resetMonitor();
        }
    }

    @Override
    public void killAll() {
        for (ProgressMonitorTask t : getTasks()) {
            t.kill();
        }
        try {
            if (serializerThread != null) {
                serializerThread.softStop();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void unfreeze() {
        if (serializerThread != null) {
            serializerThread.softStop();
            serializerThread = null;
        }
    }

    @Override
    public void ticMonitor() {
        final ProgressMonitorTask[] tasks = getTasks();
        for (int i = 0; i < tasks.length; i++) {
            final TaskComponent t = (TaskComponent)tasks[i];
            final int finalI = i;
            SwingUtilities3.invokeLater(new Runnable() {
                public void run() {
                    t.ticMonitor(finalI, tasks.length - 1);
                }
            });
        }
    }

    @Override
    public int getTasksCount() {
        return getTasks().length;
    }


    @Override
    public void waitForTask() {
        if (getParallelTasksCount() <= getTasksCount()) {
//                                    System.out.println("starting synchronous "+windowTitle+"/" +yParam);
            if (serializerThread != null) {
                System.out.println(">>>>>>>>>>serializerThread!=null");
            }
            serializerThread = new SerializerThread();
            serializerThread.serialize();
            serializerThread = null;
        } else {
//                                    System.out.println("starting asynchronous "+windowTitle+"/" +yParam);
        }
    }

    public int getParallelTasksCount() {
        return parallelTasksCount;
    }
}
