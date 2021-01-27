package net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.tasks.components;

import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.common.mon.ProgressMonitors;
import net.thevpc.common.mon.TaskMonitor;
import net.thevpc.common.mon.TaskMonitorManager;
import net.thevpc.common.mon.TaskMonitorManagerListener;
import net.thevpc.scholar.hadruplot.console.SerializerThread;
import net.thevpc.scholar.hadruplot.console.TaskComponent;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.HadruwavesStudio;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.util.HWUtils;

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
public class HWTaskMonitorManagerComponent extends JPanel implements ActionListener {
    private Box list;
    private Map<Long, TaskData> idToComp = new HashMap<>();
//    private JButton incrementButton;
//    private JButton decrementButton;
//    private JTextField countField;
    private int parallelTasksCount = 1;
    private SerializerThread serializerThread = null;
    private TaskMonitorManager taskMonitorManager;
    private HadruwavesStudio studio;
    private static class TaskData{
        long id;
        TaskMonitor task;
        HWTaskComponent component;
        private Throwable stack;

        public TaskData(long id, TaskMonitor task, HWTaskComponent component) {
            this.id = id;
            this.task = task;
            this.component = component;
            this.stack = new Throwable();
        }
    }

    public HWTaskMonitorManagerComponent(HadruwavesStudio studio) {
        this.studio = studio;
        this.taskMonitorManager = studio.proc().getTaskMonitorManager();

        list = Box.createVerticalBox();

        this.setLayout(new BorderLayout());

        this.add(list, BorderLayout.NORTH);

        JToolBar south = new JToolBar(JToolBar.HORIZONTAL);
        south.setFloatable(false);
        this.add(south, BorderLayout.SOUTH);

//        incrementButton = new JButton("");
//        decrementButton = new JButton("");
//        countField = new JTextField(String.valueOf(parallelTasksCount));
//        countField.setEditable(false);
//        south.add(Box.createVerticalGlue());
//        south.add(Box.createHorizontalGlue());
//        south.add(incrementButton);
//        south.add(decrementButton);
//        south.add(countField);
//        incrementButton.addActionListener(this);
//        decrementButton.addActionListener(this);

        taskMonitorManager.addListener(new TaskMonitorManagerListener() {
            @Override
            public void taskAdded(TaskMonitorManager man,TaskMonitor task) {
                //plotConsole.start();
                HWTaskComponent comp = new HWTaskComponent(taskMonitorManager, task,studio.app());
                list.add(comp);
                idToComp.put(task.getId(), new TaskData(task.getId(),task, comp));
//                idToStack.put(task.getId(), new Throwable());
            }

            @Override
            public void taskRemoved(TaskMonitorManager man,TaskMonitor task) {
                TaskData comp = idToComp.remove(task.getId());
//                Throwable y = idToStack.remove(task.getId());
//                y.printStackTrace();
                if(comp!=null) {
                    list.remove(comp.component);
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
        Timer timer=new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (Map.Entry<Long, TaskData> ex : idToComp.entrySet().<Map.Entry<Long, TaskData>>toArray(new Map.Entry[0])) {
                    ex.getValue().component.updateComponentUI();
                }
                HWTaskMonitorManagerComponent c = HWTaskMonitorManagerComponent.this;
                c.repaint();
            }
        });
        timer.start();
        HWUtils.onLookChanged(studio, () -> onLookChanged());
        onLookChanged();
    }
    public Color getBackground1() {
        Color c1 = UIManager.getColor("Table.background");
        if (c1 == null) {
            c1 = Color.WHITE;
        }
        return c1;
    }

    public void onLookChanged() {
        this.setBackground(getBackground1());
//        incrementButton.setIcon(studio.app().iconSet().icon("Add").get());
//        decrementButton.setIcon(studio.app().iconSet().icon("Remove").get());
        for (Map.Entry<Long, TaskData> ex : idToComp.entrySet().<Map.Entry<Long, TaskData>>toArray(new Map.Entry[0])) {
            ex.getValue().component.updateComponentUIImpl();
        }
    }

    public void actionPerformed(ActionEvent e) {
//        Object source = e.getSource();
//        if (source == incrementButton) {
//            if (parallelTasksCount < 16) {
//                parallelTasksCount++;
//                if (parallelTasksCount > taskMonitorManager.getTasksCount()) {
//                    if (serializerThread != null) {
//                        serializerThread.softStop();
//                        serializerThread = null;
//                    }
//                }
//            }
//            testTask.setProgress(testTask.getProgress()+0.01);
//        } else if (source == decrementButton) {
//            if (parallelTasksCount > 1) {
//                parallelTasksCount--;
//            }
//        }
//        countField.setText(String.valueOf(parallelTasksCount));
    }


}
