package net.vpc.scholar.hadrumaths.plot.console;

import net.vpc.common.util.Chronometer;
import net.vpc.scholar.hadrumaths.plot.CardPlotContainer;
import net.vpc.scholar.hadrumaths.plot.PlotComponent;
import net.vpc.scholar.hadrumaths.plot.PlotContainer;
import net.vpc.scholar.hadrumaths.plot.TabbedPlotContainer;
import net.vpc.scholar.hadrumaths.plot.swings.*;
import net.vpc.scholar.hadrumaths.plot.swings.FileDrop;
import net.vpc.scholar.hadrumaths.util.StringUtils;

import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyVetoException;
import java.io.File;
import java.util.*;
import java.util.List;

public class PlotConsoleFrame extends JFrame {
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

    public PlotConsoleFrame(PlotConsole console, String title) throws HeadlessException {
        this.console = console;
        setTitle(title);
        init();
    }

    public void prepare() {
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

    protected void prepareFileMenu(JMenu fileMenu) {
        fileMenu.add(createMenuItem("Properties", new PlotConsolePropertiesAction()));

        fileMenu.add(createMenuItem("Load", new LoadAction()));
        fileMenu.addSeparator();
        recentFilesMenu = new RecentFilesMenu("Recent Files", new RecentFilesPropertiesModel(new File(System.getProperty("user.dir") + "/.java/plotconsole.xml")));
        recentFilesMenu.addFileSelectedListener(
                new FileSelectedListener() {
                    public void fileSelected(FileEvent event) {
                        try {
                            console.loadFile(event.getFile());
                            recentFilesMenu.addFile(event.getFile());
                        } catch (Throwable e1) {
                            JOptionPane.showMessageDialog(PlotConsoleFrame.this, e1);
                        }
                    }
                });

        fileMenu.add(recentFilesMenu);
        fileMenu.addSeparator();
        fileMenu.add(createMenuItem("Close", new CloseAction()));

        fileMenu.add(createMenuItem("Exit", new ExitAction()));
    }

    protected void prepareToolsMenu(JMenu toolsWindowsMenu) {

    }

    protected JMenuBar createMenuBar() {
        menubar = new JMenuBar();

        fileMenu = new JMenu("File");
        prepareFileMenu(fileMenu);
        menubar.add(fileMenu);


        toolsWindowsMenu = new JMenu("Tools");
        menubar.add(toolsWindowsMenu);
        prepareToolsMenu(toolsWindowsMenu);

//        toolsWindowsMenu.add(createMenuItem("Locks", new LocksAction()));
        windowsMenu = new JMenu("Windows");
        menubar.add(windowsMenu);


        List<PlotConsoleMenuItem> menus = new ArrayList<>(Arrays.asList(console.getMenus()));
        Set<String> visitedMenuPos = new HashSet<>();
        Map<String, Integer> expectedMenuPos = new HashMap<>();
        for (int i = 0; i < menus.size(); i++) {
            PlotConsoleMenuItem mi = menus.get(i);
            visitedMenuPos.add(mi.getFullPath());
            if (!mi.getPath().equals("") && mi.getPath().equals("/")) {
                expectedMenuPos.put(mi.getPath(), i);
            }
        }
        List<PlotConsoleMenuItem> menusToCreate = new ArrayList<>();

        for (Map.Entry<String, Integer> ee : expectedMenuPos.entrySet()) {
            if (!visitedMenuPos.contains(ee.getKey())) {
                if (ee.getKey().equals("/")) {

                } else {
                    List<String> split = StringUtils.split(ee.getKey(), "/");
                    String nn = split.get(split.size() - 1);
                    split.remove(split.size() - 1);
                    String pp = StringUtils.toPath(split, "/");
                    PlotConsoleMenu e = new PlotConsoleMenu(nn, nn, pp, 0);
                    e.setInitialIndex(ee.getValue());
                    menusToCreate.add(e);
                }
            }
        }

        Collections.sort(menusToCreate, new Comparator<PlotConsoleMenuItem>() {
            @Override
            public int compare(PlotConsoleMenuItem o1, PlotConsoleMenuItem o2) {
                return Integer.compare(o2.getInitialIndex(), o1.getInitialIndex());
            }
        });
        for (PlotConsoleMenuItem plotConsoleMenuItem : menusToCreate) {
            menus.add(plotConsoleMenuItem.getInitialIndex(), plotConsoleMenuItem);
        }
        Map<String, PlotConsoleMenuItem> menuMap = new HashMap<>();
        Map<String, JMenuItem> menuMapToJ = new HashMap<>();
        for (int i = 0; i < menus.size(); i++) {
            PlotConsoleMenuItem mi = menus.get(i);
            mi.setInitialIndex(i);
            menuMap.put(mi.getFullPath(), mi);
        }
        Collections.sort(menus, new Comparator<PlotConsoleMenuItem>() {
            @Override
            public int compare(PlotConsoleMenuItem o1, PlotConsoleMenuItem o2) {
                String p1 = o1.getPath();
                String p2 = o2.getPath();
                if (p1.equals(p2)) {
                    int x = Integer.compare(o1.getOrder(), o2.getOrder());
                    if (x != 0) {
                        return x;
                    }
                    return Integer.compare(o1.getInitialIndex(), o2.getInitialIndex());
                } else {
                    if (p1.equals("/")) {
                        return -1;
                    }
                    if (p2.equals("/")) {
                        return 1;
                    }
                    PlotConsoleMenuItem m1 = menuMap.get(p1);
                    PlotConsoleMenuItem m2 = menuMap.get(p2);
                    return compare(m1, m2);
                }
            }
        });
        for (PlotConsoleMenuItem menu : menus) {
            JMenuItem jMenuItem = menuMapToJ.get(menu.getFullPath());
            if (jMenuItem == null) {
                if (menu.getPath().equals("/")) {
                    jMenuItem = new JMenu(menu.getName());
                    menubar.add(jMenuItem);
                } else {
                    JMenu jmenu = (JMenu) menuMapToJ.get(menu.getPath());
                    jMenuItem = new JMenuItem(menu.getName());
                    jMenuItem.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            menu.actionPerformed(console, e);
                        }
                    });
                    jmenu.add(jMenuItem);
                }
                menuMapToJ.put(menu.getFullPath(), jMenuItem);
            }
        }
        return menubar;
    }

    private void init() {
        setLayout(new BorderLayout());
        add(createMenuBar(), BorderLayout.NORTH);
        desktop = new JDesktopPane();
        new FileDrop(null, desktop, /*dragBorder,*/ new FileDrop.Listener() {
            public void filesDropped(java.io.File[] files) {
                try {
                    console.loadFiles(files);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e.toString());
                }

                // end for: through each dropped file
            }   // end filesDropped
        });
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
                PlotConsoleFrame.this.console.dispose();
            }

            public void windowClosed(WindowEvent e) {
                PlotConsoleFrame.this.console.dispose();
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

    public JInternalFrame addFrame(FrameInfo frame) {
        JInternalFrame f = new JInternalFrame(frame.getTitle(), frame.isResizable(), frame.isClosable(), frame.isMaximizable(), frame.isIconifiable());
        Component c = frame.getComponent();
        if (c == null) {
            c = new JLabel("...");
        }
        f.add(c);
        Dimension ps = frame.getPreferredSize();
        if (ps != null) {
            f.setPreferredSize(ps);
        }
        f.pack();
        f.setVisible(true);
        try {
            f.setIcon(frame.isIcon());
        } catch (PropertyVetoException ee) {
            ee.printStackTrace();
        }
        addFrame(f);
        return f;
    }

    public void addFrame(JInternalFrame jInternalFrame) {
        JMenuItem windowMenu = createWindowMenu(jInternalFrame);
        windowsMenu.add(windowMenu);
        String title = jInternalFrame.getTitle();
        jInternalFrame.addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosed(InternalFrameEvent e) {
                windowsMenu.remove(windowMenu);
                userWindows.remove(title);
            }
        });
        userWindows.put(title, jInternalFrame);
        onAddJInternalFrame(jInternalFrame);
    }

    public void addToolsFrame(JInternalFrame jInternalFrame) {
        addFrame(jInternalFrame);
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
                    if (inf.getParent() == null) {
                        onAddJInternalFrame(inf);
                    }
                    if (inf.isIcon()) {
                        inf.setIcon(false);
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                inf.moveToFront();
            }
        });
        return i;
    }

    private void onAddJInternalFrame(JInternalFrame inf) {
        //was closed!
        desktop.add(inf);
        inf.setVisible(true);
        try {
            inf.setIcon(false);
        } catch (PropertyVetoException ee) {
            ee.printStackTrace();
        }
        try {
            inf.setSelected(true);
        } catch (PropertyVetoException e) {
            //e.printStackTrace();
        }
        try {
            inf.setIcon(false);
        } catch (PropertyVetoException e) {
            //e.printStackTrace();
        }
        inf.moveToFront();
    }

    public void setGlobalInfo(String title, int value) {
        globalLabel.setText(title);
        globalProgress.setMaximum(value);
    }

    public void setGlobalProgress(int value, long spentTimeNano) {
        globalProgress.setValue(value);
        globalProgress.setString(
                String.valueOf(value) + "/" + String.valueOf(globalProgress.getMaximum())
                        + (spentTimeNano < 0 ? "" : (" | + " + Chronometer.formatPeriodNano(spentTimeNano)))
        );
    }

    public synchronized ConsoleWindow getWindow(WindowPath path) {
        if (path == null) {
            path = new WindowPath("");
        }
        String[] pp = path.toArray();
        String windowTitle = "";
//        String windowSupTitle = "";
        java.util.List<String> subTitles = new ArrayList<>();
        switch (pp.length) {
            case 0: {
                windowTitle = "DefaultProject";
//                windowSupTitle = "Plot";
                break;
            }
            case 1: {
                windowTitle = pp[0];
//                windowSupTitle = "Plot";
                break;
            }
            default: {
                windowTitle = pp[0];
                for (int i = 1; i < pp.length; i++) {
                    subTitles.add(pp[i]);
                }
                break;
            }
        }
        JInternalFrame internalFrame = userWindows.get(windowTitle);
        if (internalFrame == null) {
            internalFrame = addFrame(new FrameInfo()
                    .setClosable(true)
                    .setIcon(false)
                    .setIconifiable(true)
                    .setResizable(true)
                    .setMaximizable(true)
                    .setTitle(windowTitle)
                    .setComponent(new DummyLabel())
            );
        }
        Component[] components = internalFrame.getContentPane().getComponents();
        //JTabbedPane pane;
        PlotContainer tcontainer = null;
        if (components.length == 0) {
            tcontainer = new TabbedPlotContainer();
            internalFrame.getContentPane().add(tcontainer.toComponent());
        } else {
            Component old = components[0];
            if (old instanceof JComponent) {
                tcontainer = (PlotContainer) ((JComponent) old).getClientProperty(PlotComponent.class.getName());
            }
            if (tcontainer == null) {
                internalFrame.getContentPane().remove(old);
                tcontainer = new CardPlotContainer();// PanelPlotWindowContainerFactory.INSTANCE.create();
                internalFrame.getContentPane().add(tcontainer.toComponent());
            }
        }
        return new PlotContainerConsoleWindow(path, tcontainer);

//        for (int i = 0; i < pane.getTabCount(); i++) {
//            String titleAt = pane.getTitleAt(i);
//            if (titleAt.equals(windowSupTitle)) {
//                return new PlotContainerConsoleWindow(path, pane, i);
//            }
//        }
//        pane.addTab(windowSupTitle, new DummyLabel());
////        internalFrame.pack();
//        return new PlotContainerConsoleWindow(path, pane, pane.getTabCount() - 1);
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
            PlotConsoleFrame.this.setVisible(false);
            PlotConsoleFrame.this.dispose();
            System.exit(0);
        }
    }

    public class CloseAction implements SerializableActionListener {
        public void actionPerformed(ActionEvent e) {
            PlotConsoleFrame.this.setVisible(false);
            PlotConsoleFrame.this.dispose();
            if (openFrames <= 0) {
                System.exit(0);
            }
        }
    }

    public class PlotConsolePropertiesAction implements SerializableActionListener {

        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(PlotConsoleFrame.this, new PlotConsoleProperties(console), "Properties", JOptionPane.PLAIN_MESSAGE);
        }
    }

    public class LoadAction implements SerializableActionListener {

        public void actionPerformed(ActionEvent e) {
            JFileChooser c = new JFileChooser();
            c.setCurrentDirectory(console.getCurrentDirectory());
            c.addChoosableFileFilter(PlotConsole.CHOOSER_FILTER);
            for (PlotConsoleFileSupport plotConsoleFileSupport : console.getPlotConsoleFileSupports()) {
                c.addChoosableFileFilter(new ExtensionFileChooserFilter(plotConsoleFileSupport.getFileExtension(), plotConsoleFileSupport.getFileDesc()));
            }
            if (JFileChooser.APPROVE_OPTION == c.showOpenDialog(PlotConsoleFrame.this)) {
                File selectedFile = c.getSelectedFile();
                try {
                    console.loadFile(selectedFile);
                    recentFilesMenu.addFile(selectedFile);
                } catch (Throwable e1) {
                    console.getLog().error(e1);
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(PlotConsoleFrame.this, "unable to load " + selectedFile + "\n" + e1);
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
