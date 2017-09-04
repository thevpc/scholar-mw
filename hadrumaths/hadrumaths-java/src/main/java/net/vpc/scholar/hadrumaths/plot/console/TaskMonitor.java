package net.vpc.scholar.hadrumaths.plot.console;

import net.vpc.scholar.hadrumaths.ProgressMonitorFactory;
import net.vpc.scholar.hadrumaths.util.ProgressMonitor;
import net.vpc.scholar.hadrumaths.util.EnhancedProgressMonitor;
import net.vpc.scholar.hadrumaths.util.SwingUtils;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;
import java.util.ArrayList;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
* @creationtime 6 janv. 2007 12:13:07
*/
public class TaskMonitor extends JPanel implements ActionListener {
    Box list;
    JButton incrementButton;
    JButton decrementButton;
    JTextField countField;
    private PlotConsole plotConsole;
    private int parallelTasksCount = 1;
    private SerializerThread serializerThread = null;
    private JInternalFrame frame = null;

    public TaskMonitor(PlotConsole plotConsole) {
        this.plotConsole = plotConsole;

        list = Box.createVerticalBox();

        this.setLayout(new BorderLayout());

        this.add(list, BorderLayout.NORTH);
        
        JToolBar south = new JToolBar(JToolBar.HORIZONTAL);
        south.setFloatable(false);
        this.add(south, BorderLayout.SOUTH);
        incrementButton = new JButton("+");
        decrementButton = new JButton("-");
        incrementButton.setIcon(new ImageIcon(getClass().getResource("MoreTasks.gif")));
        decrementButton.setIcon(new ImageIcon(getClass().getResource("LessTasks.gif")));

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

    public JInternalFrame getFrame() {
        return frame;
    }

    public void setFrame(JInternalFrame frame) {
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

    public void addTask(PlotThread task) {
        plotConsole.start();
        String windowTitle=(task.getWindowTitle());
        String descLabel=(task.getSerieTitle().toString());
        list.add(new TaskComponent(this, ProgressMonitorFactory.enhance(task),windowTitle,descLabel));
        task.start();
    }

    public ProgressMonitor addTask(ProgressMonitor task, String windowTitle, String descLabel) {
        plotConsole.start();
        EnhancedProgressMonitor m= ProgressMonitorFactory.enhance(task);
        list.add(new TaskComponent(this, m, windowTitle, descLabel));
        return m;
    }

    public ProgressMonitor addTask(String windowTitle, String descLabel) {
        plotConsole.start();
        EnhancedProgressMonitor m= ProgressMonitorFactory.logger(null,plotConsole.getLog()).temporize(5000);
        list.add(new TaskComponent(this, m, windowTitle, descLabel));
        return m;
    }

    public void removeTask(final TaskComponent component) {
        plotConsole.start();
        list.remove(component);
        if (serializerThread != null) {
            if (parallelTasksCount > getTasksCount()) {
                serializerThread.softStop();
                serializerThread = null;
            }
        }
    }

    public TaskComponent[] getTasks() {
        Component[] components = list.getComponents();
        ArrayList<TaskComponent> a = new ArrayList<TaskComponent>();
        for (Component component1 : components) {
            if (component1 instanceof TaskComponent) {
                a.add((TaskComponent) component1);
            }
        }
        return a.toArray(new TaskComponent[a.size()]);
    }

    public void resetMonitor() {
        for (TaskComponent t : getTasks()) {
            t.resetMonitor();
        }
    }

    public void killAll() {
        for (TaskComponent t : getTasks()) {
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

    public void unfreeze(){
        if (serializerThread != null) {
            serializerThread.softStop();
            serializerThread = null;
        }
    }

    public void ticMonitor() {
        for (final TaskComponent t : getTasks()) {
            SwingUtils.invokeLater(new Runnable() {
                public void run() {
                    t.ticMonitor();
                }
            });
        }
    }

    public int getTasksCount() {
        return getTasks().length;
    }


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
