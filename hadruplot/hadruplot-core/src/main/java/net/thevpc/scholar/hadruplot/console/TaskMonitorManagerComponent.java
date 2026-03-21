package net.thevpc.scholar.hadruplot.console;

import net.thevpc.common.mon.*;
import net.thevpc.common.swing.frame.JInternalFrameHelper;
import net.thevpc.common.swing.SwingUtilities3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime 6 janv. 2007 12:13:07
 */
public class TaskMonitorManagerComponent extends JPanel implements ActionListener {
    private Box list;
    private Map<Long, JComponent> idToComp = new HashMap<>();
    private Map<Long, Throwable> idToStack = new HashMap<>();
    private JButton incrementButton;
    private JButton decrementButton;
    private JTextField countField;
    private int parallelTasksCount = 1;
    private SerializerThread serializerThread = null;
    private JInternalFrame frame = null;
    private TaskMonitorManager taskMonitorManager;

    public TaskMonitorManagerComponent(TaskMonitorManager taskMonitorManager) {
        this.taskMonitorManager = taskMonitorManager;

        list = Box.createVerticalBox();

        this.setLayout(new BorderLayout());

        this.add(list, BorderLayout.NORTH);

        JToolBar south = new JToolBar(JToolBar.HORIZONTAL);
        south.setFloatable(false);
        this.add(south, BorderLayout.SOUTH);
        incrementButton = new JButton("");
        decrementButton = new JButton("");
        incrementButton.setIcon(SwingUtilities3.getScaledIcon(TaskMonitorManagerComponent.class.getResource("Plus.png"), 16, 16));
        decrementButton.setIcon(SwingUtilities3.getScaledIcon(TaskMonitorManagerComponent.class.getResource("Minus.png"), 16, 16));

        countField = new JTextField(String.valueOf(parallelTasksCount));
        countField.setEditable(false);
        south.add(Box.createVerticalGlue());
        south.add(Box.createHorizontalGlue());
        south.add(incrementButton);
        south.add(decrementButton);
        south.add(countField);
        incrementButton.addActionListener(this);
        decrementButton.addActionListener(this);
        taskMonitorManager.addListener(new TaskMonitorManagerListener() {
            @Override
            public void taskAdded(TaskMonitorManager man,TaskMonitor task) {
                //plotConsole.start();
                TaskComponent comp = new TaskComponent(taskMonitorManager, task);
                list.add(comp);
                idToComp.put(task.getId(), comp);
//                idToStack.put(task.getId(), new Throwable());
            }

            @Override
            public void taskRemoved(TaskMonitorManager man,TaskMonitor task) {
                JComponent comp = idToComp.remove(task.getId());
//                Throwable y = idToStack.remove(task.getId());
//                y.printStackTrace();
                if(comp!=null) {
                    list.remove(comp);
                }
            }

            @Override
            public void taskStarted(TaskMonitorManager manager, TaskMonitor task) {

            }

            @Override
            public void taskResumed(TaskMonitorManager manager, TaskMonitor task) {

            }

            @Override
            public void taskSuspended(TaskMonitorManager manager, TaskMonitor task) {

            }

            @Override
            public void taskCanceled(TaskMonitorManager manager, TaskMonitor task) {

            }

            @Override
            public void taskTerminated(TaskMonitorManager manager, TaskMonitor task) {

            }

            @Override
            public void taskReset(TaskMonitorManager manager, TaskMonitor task) {

            }

            @Override
            public void taskBlocked(TaskMonitorManager manager, TaskMonitor monitor) {

            }

            @Override
            public void taskUnblocked(TaskMonitorManager manager, TaskMonitor monitor) {

            }
        });
    }

    public JInternalFrameHelper getFrame() {
        return new JInternalFrameHelper(frame);
    }

    public void setFrame(JInternalFrame frame) {
        this.frame = frame;
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == incrementButton) {
            if (parallelTasksCount < 16) {
                parallelTasksCount++;
                if (parallelTasksCount > taskMonitorManager.getTasksCount()) {
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

}
