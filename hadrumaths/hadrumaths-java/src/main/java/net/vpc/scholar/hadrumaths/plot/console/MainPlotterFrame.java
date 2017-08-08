package net.vpc.scholar.hadrumaths.plot.console;

import net.vpc.scholar.hadrumaths.Chronometer;
import net.vpc.scholar.hadrumaths.util.swingext.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyVetoException;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class MainPlotterFrame extends JFrame {
    public static int openFrames = 0;
    PlotConsole console;
    JDesktopPane desktop;
    JMenuBar menubar;
    JMenu fileMenu;
    JMenu windowsMenu;
    JMenu toolsWindowsMenu;
    RecentFilesMenu recentFilesMenu;
    JLabel globalLabel;
    JProgressBar globalProgress;
    long globalStartTime;
    Map<String, JInternalFrame> userWindows = new HashMap<String, JInternalFrame>();

    public MainPlotterFrame(PlotConsole console, String title) throws HeadlessException {
        this.console = console;
        setTitle(title);
        init();
    }

    public void prepare(){
        try {
            console.getTaskMonitor().getFrame().setClosed(true);
            console.getLockMonitor().getFrame().setClosed(true);
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
    }

    public void setVisible(boolean b) {
        boolean oldV = isVisible();
        super.setVisible(b);
        boolean newV = isVisible();
        if (oldV != newV) {
            if (newV) {
                openFrames++;
            } else {
                openFrames--;
                if (openFrames <= 0) {
                    System.exit(0);
                }
            }
        }
    }

    protected JMenuItem createMenuItem(String name, ActionListener action) {
        JMenuItem jmi;
        jmi = new JMenuItem(name);
        jmi.addActionListener(action);
        return jmi;
    }

    protected JMenuBar createMenuBar() {
        menubar = new JMenuBar();

        fileMenu = new JMenu("File");
        menubar.add(fileMenu);


        toolsWindowsMenu = new JMenu("Tools");
        menubar.add(toolsWindowsMenu);
//        toolsWindowsMenu.add(createMenuItem("Locks", new LocksAction()));
        windowsMenu = new JMenu("Windows");
        menubar.add(windowsMenu);


        fileMenu.add(createMenuItem("Properties", new PlotConsolePropertiesAction()));

        fileMenu.add(createMenuItem("Load", new LoadAction()));
        fileMenu.addSeparator();
        recentFilesMenu = new RecentFilesMenu("Recent Files", new RecentFilesPropertiesModel(new File(System.getProperty("user.dir") + "/.java/plotconsole.xml")));
        recentFilesMenu.addFileSelectedListener(
                new FileSelectedListener() {
                    public void fileSelected(FileEvent event) {
                        try {
                            console.setFrameTitle(event.getFile().getName());
                            console.loadFile(event.getFile());
                            recentFilesMenu.addFile(event.getFile());
                        } catch (Throwable e1) {
                            JOptionPane.showMessageDialog(MainPlotterFrame.this, e1);
                        }
                    }
                });

        fileMenu.add(recentFilesMenu);
        fileMenu.addSeparator();
        fileMenu.add(createMenuItem("Close", new CloseAction()));

        fileMenu.add(createMenuItem("Exit", new ExitAction()));

        return menubar;
    }

    private void init() {
        setLayout(new BorderLayout());
        add(createMenuBar(), BorderLayout.NORTH);
        desktop = new JDesktopPane();
//        desktop.setOpaque(false);
        add(desktop, BorderLayout.CENTER);
        JToolBar b = new JToolBar(JToolBar.HORIZONTAL);
        b.setFloatable(false);

        globalProgress = new JProgressBar(0, 100);
        globalProgress.setStringPainted(true);
        globalLabel = new JLabel();
        b.add(globalLabel);
        b.addSeparator();
        b.add(globalProgress);
        b.addSeparator();

        b.addSeparator();

        b.addSeparator();
        b.add(new MemoryUseIconTray(false));
        add(b, BorderLayout.SOUTH);
        setPreferredSize(new Dimension(800, 600));
        setSize(getPreferredSize());
        addWindowListener(new WindowListener() {
            public void windowOpened(WindowEvent e) {
            }

            public void windowClosing(WindowEvent e) {
                MainPlotterFrame.this.console.dispose();
            }

            public void windowClosed(WindowEvent e) {
                MainPlotterFrame.this.console.dispose();
            }

            public void windowIconified(WindowEvent e) {
            }

            public void windowDeiconified(WindowEvent e) {
            }

            public void windowActivated(WindowEvent e) {
            }

            public void windowDeactivated(WindowEvent e) {
            }
        });
    }

    public void addWindow(JInternalFrame jInternalFrame) {
        desktop.add(jInternalFrame);
        windowsMenu.add(createWindowMenu(jInternalFrame));
        jInternalFrame.setVisible(true);
    }

    public void addSystemWindow(JInternalFrame jInternalFrame) {
        desktop.add(jInternalFrame);
        jInternalFrame.setVisible(true);
        toolsWindowsMenu.add(createWindowMenu(jInternalFrame));
    }

    private JMenuItem createWindowMenu(JInternalFrame jInternalFrame) {
        JMenuItem i = new JMenuItem(jInternalFrame.getTitle());
        i.putClientProperty("JInternalFrame", jInternalFrame);
        i.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JComponent component1 = ((JComponent) e.getSource());
                JInternalFrame inf = (JInternalFrame) (component1.getClientProperty("JInternalFrame"));
                try {
                    if(inf.getParent()==null){
                        //was closed!
                        desktop.add(inf);
                        inf.setVisible(true);
                    }
                    inf.setIcon(false);
                } catch (PropertyVetoException e1) {
                    e1.printStackTrace();
                }
                inf.moveToFront();
            }
        });
        return i;
    }

    public void setGlobalInfo(String title, int value) {
        globalLabel.setText(title);
        globalProgress.setMaximum(value);
    }

    public void setGlobalProgress(int value, long spentTime) {
        globalProgress.setValue(value);
        globalProgress.setString(
                String.valueOf(value) + "/" + String.valueOf(globalProgress.getMaximum())
                        + (spentTime < 0 ? "" : (" | + " + Chronometer.formatPeriod(spentTime)))
        );
    }

    public synchronized ConsoleWindow getWindow(WindowPath path) {
        if (path == null) {
            path = new WindowPath("");
        }
        String[] pp = path.toArray();
        String windowTitle = "";
        String windowSupTitle = "";
        switch (pp.length) {
            case 0: {
                windowTitle = "DefaultProject";
                windowSupTitle = "Plot";
                break;
            }
            case 1: {
                windowTitle = pp[0];
                windowSupTitle = "Plot";
                break;
            }
            default: {
                windowTitle = pp[0];
                windowSupTitle = pp[0];
                break;
            }
        }
        JInternalFrame internalFrame = userWindows.get(windowTitle);
        if (internalFrame == null) {
            internalFrame = new JInternalFrame();
            internalFrame.setPreferredSize(new Dimension(600, 400));
            internalFrame.setClosable(false);
            internalFrame.setIconifiable(true);
            internalFrame.setResizable(true);
            internalFrame.setMaximizable(true);
            internalFrame.setTitle(windowTitle);
            internalFrame.getContentPane().add(new DummyLabel());
            internalFrame.pack();
            addWindow(internalFrame);
            userWindows.put(windowTitle, internalFrame);
//            internalFrame.moveToFront();
        }
        Component[] components = internalFrame.getContentPane().getComponents();
        JTabbedPane pane;
        if (components.length == 0) {
            pane = new JTabbedPane();
            internalFrame.getContentPane().add(pane);
        } else if (components[0] instanceof DummyLabel) {
            internalFrame.getContentPane().remove(components[0]);
            pane = new JTabbedPane();
            internalFrame.getContentPane().add(pane);
        } else {
            pane = (JTabbedPane) components[0];
        }
        for (int i = 0; i < pane.getTabCount(); i++) {
            String titleAt = pane.getTitleAt(i);
            if (titleAt.equals(windowSupTitle)) {
                return new JTabbedPaneConsoleWindow(path, pane, i);
            }
        }
        pane.addTab(windowSupTitle, new DummyLabel());
//        internalFrame.pack();
        return new JTabbedPaneConsoleWindow(path, pane, pane.getTabCount() - 1);
    }

    public synchronized void removeWindow(WindowPath path) {
        String[] strings = path.toArray();
        if (strings.length != 2) {
            throw new IllegalArgumentException("bad");
        }
        JInternalFrame internalFrame = userWindows.get(strings[0]);
        if (internalFrame != null) {
            Component[] components = internalFrame.getContentPane().getComponents();
            JTabbedPane pane;
            if (components.length == 0) {
                //
            } else if (components[0] instanceof DummyLabel) {
                //
            } else {
                pane = (JTabbedPane) components[0];
                for (int i = 0; i < pane.getTabCount(); i++) {
                    String titleAt = pane.getTitleAt(i);
                    if (titleAt.equals(strings[1])) {
                        pane.removeTabAt(i);
                        return;
                    }
                }
            }
        }
    }

    public synchronized void removeWindow(Component comp) {
        for (JInternalFrame internalFrame : desktop.getAllFrames()) {
            Component[] components = internalFrame.getContentPane().getComponents();
            JTabbedPane pane;
            if (components.length == 0) {
                //
            } else if (components[0] instanceof DummyLabel) {
                //
            } else {
                pane = (JTabbedPane) components[0];
                for (int i = 0; i < pane.getTabCount(); i++) {
                    Component c = pane.getComponentAt(i);
                    if (c == comp) {
                        pane.removeTabAt(i);
                        return;
                    }
                }
            }
        }
    }

    public class ExitAction implements SerializableActionListener {
        public void actionPerformed(ActionEvent e) {
            MainPlotterFrame.this.setVisible(false);
            MainPlotterFrame.this.dispose();
            System.exit(0);
        }
    }

    public class CloseAction implements SerializableActionListener {
        public void actionPerformed(ActionEvent e) {
            MainPlotterFrame.this.setVisible(false);
            MainPlotterFrame.this.dispose();
            if (openFrames <= 0) {
                System.exit(0);
            }
        }
    }

    public class PlotConsolePropertiesAction implements SerializableActionListener {

        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(MainPlotterFrame.this, new PlotConsoleProperties(console), "Properties", JOptionPane.PLAIN_MESSAGE);
        }
    }

    public class LoadAction implements SerializableActionListener {

        public void actionPerformed(ActionEvent e) {
            JFileChooser c = new JFileChooser();
            c.setCurrentDirectory(console.getCurrentDirectory());
            c.addChoosableFileFilter(PlotConsole.CHOOSER_FILTER);
            if (JFileChooser.APPROVE_OPTION == c.showOpenDialog(MainPlotterFrame.this)) {
                File selectedFile = c.getSelectedFile();
                try {
                    console.setFrameTitle(selectedFile.getName());
                    console.loadFile(selectedFile);
                    recentFilesMenu.addFile(selectedFile);
                } catch (Throwable e1) {
                    console.getLog().error(e1);
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(MainPlotterFrame.this, "unable to load " + selectedFile + "\n" + e1);
                }
            }

        }
    }

//    public class LocksAction implements SerializableActionListener {
//        public void actionPerformed(ActionEvent e) {
//            console.getLockMonitor().setVisible(true);
//        }
//    }

}
