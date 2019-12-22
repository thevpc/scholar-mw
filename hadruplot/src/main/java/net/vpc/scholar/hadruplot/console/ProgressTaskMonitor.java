package net.vpc.scholar.hadruplot.console;

import net.vpc.common.mon.ProgressMonitorCreator;

public interface ProgressTaskMonitor extends ProgressMonitorCreator {
    void addTask(PlotThread task);

    net.vpc.common.mon.ProgressMonitor addTask(net.vpc.common.mon.ProgressMonitor task, String windowTitle, String descLabel);

    net.vpc.common.mon.ProgressMonitor addTask(String windowTitle, String descLabel, ConsoleTask task);

    net.vpc.common.mon.ProgressMonitor addTask(String windowTitle, String descLabel);

    net.vpc.common.mon.ProgressMonitor createMonitor(String name, String description);

    void removeTask(ProgressMonitorTask component);

    ProgressMonitorTask[] getTasks();

    void resetMonitor();

    void killAll();

    void unfreeze();

    void ticMonitor();

    int getTasksCount();

    void waitForTask();
}
