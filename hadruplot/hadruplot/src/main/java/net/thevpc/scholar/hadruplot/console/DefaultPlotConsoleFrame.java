package net.thevpc.scholar.hadruplot.console;

import net.thevpc.common.swing.frame.JInternalFrameHelper;
import net.thevpc.common.swing.label.MemoryUseIconTray;
import net.thevpc.common.swing.file.RecentFilesPropertiesModel;
import net.thevpc.common.swing.file.RecentFilesMenu;
import net.thevpc.common.swing.file.RecentFileEvent;
import net.thevpc.common.swing.file.FileDropListener;
import net.thevpc.common.swing.file.FileSelectedListener;
import net.thevpc.common.swing.win.WindowInfo;
import net.thevpc.common.swing.win.WindowInfoListener;
import net.thevpc.common.strings.StringUtils;
import net.thevpc.common.swing.*;
import net.thevpc.common.swing.win.InternalWindowsHelper;
import net.thevpc.common.swing.win.WindowPath;
import net.thevpc.scholar.hadruplot.actions.AbstractPlotAction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.*;
import java.util.List;

public class DefaultPlotConsoleFrame extends JFrame implements PlotConsoleFrame{
    public static int openFrames = 0;
    PlotConsole console;
    JMenuBar menubar;
    JMenu fileMenu;
    JMenu windowsMenu;
    JMenu toolsWindowsMenu;
    RecentFilesMenu recentFilesMenu;
    JLabel globalLabel;
    long globalStartTime;
    InternalWindowsHelper wins=new InternalWindowsHelper();

    public DefaultPlotConsoleFrame(PlotConsole console, String title) throws HeadlessException {
        this.console = console;
        wins.addWindowInfoListener(new WindowInfoListener() {
            @Override
            public void onAddFrame(WindowInfo windowInfo) {
                JComponent component = (JComponent) windowInfo.getComponent();
                JInternalFrame internalFrame = (JInternalFrame) component.getClientProperty(JInternalFrame.class);
                final JMenuItem windowMenu = createWindowMenu(internalFrame);
                component.putClientProperty("menu",windowMenu);
                windowsMenu.add(windowMenu);
                onPostAddJInternalFrame(new JInternalFrameHelper(internalFrame), windowInfo);
            }

            @Override
            public void onCloseFrame(WindowInfo windowInfo) {
                JComponent component = (JComponent) windowInfo.getComponent();
                JMenuItem windowMenu=(JMenuItem) component.getClientProperty("menu");
                if(windowMenu!=null) {
                    windowsMenu.add(windowMenu);
                }
            }
        });
        setTitle(title);
        init();
        prepare();
        setVisible(true);

    }

    public void prepare() {
//        console.getTaskMonitor().getFrame().setClosed(true);
//        console.getLockMonitor().getFrame().setClosed(true);
    }

    @Override
    public PlotConsole getConsole() {
        return console;
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

//    protected JMenuItem createMenuItem(String name, Action action) {
//        JMenuItem jmi;
//        jmi = new JMenuItem(name);
//        jmi.addActionListener(action);
//        return jmi;
//    }

    protected void prepareFileMenu(JMenu fileMenu) {
        fileMenu.add(new JMenuItem(new PlotConsolePropertiesAction(this)));

        fileMenu.add(new JMenuItem(new LoadAction(this)));
        fileMenu.addSeparator();
        recentFilesMenu = new RecentFilesMenu("Recent Files", new RecentFilesPropertiesModel((System.getProperty("user.dir") + "/.java/plotconsole.xml")));
        recentFilesMenu.addFileSelectedListener(
                new FileSelectedListener() {
                    public void fileSelected(RecentFileEvent event) {
                        try {
                            console.loadFile(new File(event.getFile()));
                            recentFilesMenu.addFile(event.getFile());
                        } catch (Throwable e1) {
                            JOptionPane.showMessageDialog(DefaultPlotConsoleFrame.this, e1);
                        }
                    }
                });

        fileMenu.add(recentFilesMenu);
        fileMenu.addSeparator();
        fileMenu.add(new JMenuItem(new CloseAction(this)));

        fileMenu.add(new JMenuItem(new ExitAction(this)));
    }

    protected void prepareToolsMenu(JMenu toolsWindowsMenu) {

    }

    public synchronized void tileFrames() {
        wins.tileFrames();
    }
    public synchronized void iconifyFrames() {
        wins.iconifyFrames();
    }
    public synchronized void deiconifyFrames() {
        wins.deiconifyFrames();
    }
    public synchronized void closeFrames() {
        wins.closeFrames();
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
//        JMenuItem tile_windows = new JMenuItem("Tile Windows");
//        tile_windows.setAction();
        windowsMenu.add(new AbstractPlotAction("Tile Windows") {
            {
                putValue(SMALL_ICON, SwingUtilities3.getScaledIcon(DefaultPlotConsoleFrame.class.getResource("TileWindows.png"),16,16));
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                tileFrames();
            }
        });
        windowsMenu.add(new AbstractPlotAction("Icon Windows") {
            {
                putValue(SMALL_ICON, SwingUtilities3.getScaledIcon(DefaultPlotConsoleFrame.class.getResource("IconifyWindows.png"),16,16));
            }
            @Override
            public void actionPerformed(ActionEvent e) {
                iconifyFrames();
            }
        });
        windowsMenu.add(new AbstractPlotAction("De-Icon Windows") {
            {
                putValue(SMALL_ICON, SwingUtilities3.getScaledIcon(DefaultPlotConsoleFrame.class.getResource("DeiconifyWindows.png"),16,16));
            }
            @Override
            public void actionPerformed(ActionEvent e) {
                deiconifyFrames();
            }
        });
        windowsMenu.add(new AbstractPlotAction("Close Windows") {
            {
                putValue(SMALL_ICON, SwingUtilities3.getScaledIcon(DefaultPlotConsoleFrame.class.getResource("CloseWindows.png"),16,16));
            }
            @Override
            public void actionPerformed(ActionEvent e) {
                closeFrames();
            }
        });
        windowsMenu.addSeparator();
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
                    List<String> split = new ArrayList<>(Arrays.asList(StringUtils.split(ee.getKey(), "/")));
                    String nn = split.get(split.size() - 1);
                    split.remove(split.size() - 1);
                    String pp = StringUtils.join( "/",split);
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
        final Map<String, PlotConsoleMenuItem> menuMap = new HashMap<>();
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
        for (final PlotConsoleMenuItem menu : menus) {
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
        try {
            setIconImage(new ImageIcon(PlotConsole.class.getResource("PlotConsole.png")).getImage());
        }catch (Exception ex){
            //
        }
        setLayout(new BorderLayout());
        add(createMenuBar(), BorderLayout.NORTH);
        SwingUtilities3.addFileDropListener(wins.getDesktop(), /*dragBorder,*/ new FileDropListener() {
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
        add(wins.getDesktop(), BorderLayout.CENTER);
        JToolBar b = new JToolBar(JToolBar.HORIZONTAL);
        b.setFloatable(false);

        globalLabel = new JLabel();
        b.add(globalLabel);
        b.addSeparator();

        b.add(Box.createHorizontalGlue());

        b.addSeparator();
        b.add(new MemoryUseIconTray(false));
        add(b, BorderLayout.SOUTH);
        setPreferredSize(new Dimension(800, 600));
        setSize(getPreferredSize());
        addWindowListener(new WindowListener() {
            public void windowOpened(WindowEvent e) {
            }

            public void windowClosing(WindowEvent e) {
                DefaultPlotConsoleFrame.this.console.dispose();
            }

            public void windowClosed(WindowEvent e) {
                DefaultPlotConsoleFrame.this.console.dispose();
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

    @Override
    public void addPlotConsoleListener(WindowInfoListener listener) {
        wins.addWindowInfoListener(listener);
    }

    @Override
    public void removePlotConsoleListener(WindowInfoListener listener) {
        wins.removeWindowInfoListener(listener);
    }

    @Override
    public Component frameComponent() {
        return this;
    }

    @Override
    public void addRecentFile(File selectedFile) {
        recentFilesMenu.addFile(selectedFile.getPath());
    }

    public JInternalFrame addToolsFrame(WindowInfo fino) {
        JInternalFrame jInternalFrame = wins.addFrame( fino);
        toolsWindowsMenu.add(createWindowMenu(jInternalFrame));
        return jInternalFrame;
    }

    private JMenuItem createWindowMenu(JInternalFrame jInternalFrame) {
        JMenuItem i = new JMenuItem(jInternalFrame.getTitle());
        i.setIcon(jInternalFrame.getFrameIcon());
        i.putClientProperty("JInternalFrame", jInternalFrame);
        i.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JComponent component1 = ((JComponent) e.getSource());
                JInternalFrameHelper inf = new JInternalFrameHelper((JInternalFrame) (component1.getClientProperty("JInternalFrame")));
                if (inf.getFrame().getParent() == null) {
                    wins.getDesktop().add(inf.getFrame());
                }
                onPostAddJInternalFrame(inf,null);
            }
        });
        return i;
    }

    private void onPostAddJInternalFrame(JInternalFrameHelper h, WindowInfo fino) {
        if(fino!=null) {
            h.setVisible(true);
            h.setIcon(fino.isClosable() && fino.isIcon());
            if(!fino.isIcon()) {
                h.moveToFront();
                h.setSelected(true);
            }
            h.getFrame().pack();
            h.getFrame().requestFocus(true);
        }else{
            h.setVisible(true);
            if(!h.setIcon(false)){
                System.out.println("Cant deiconify!");
            }
            h.moveToFront();
            h.setSelected(true);
            h.getFrame().pack();
            h.getFrame().requestFocus(true);
        }

//        System.out.println(h.getFrame().getTitle()+" :: "
//                + (h.getFrame().isIcon() ? "icon;" : "")
//                + (h.getFrame().isClosed() ? "closed;" : "")
//                + (h.getFrame().isSelected() ? "selected;" : "")
//                + (h.getFrame().isMaximum() ? "maximum;" : "")
//                + (h.getFrame().isVisible() ? "visible;" : "")
//        );
    }
    public void setGlobalInfo(String title) {
        globalLabel.setText(title);
    }

    public synchronized ConsoleWindow getWindow(WindowPath path) {
        return new PlotContainerConsoleWindow(path, wins.getWindow(path));
    }

    public synchronized void removeWindow(WindowPath path) {
        wins.removeWindow(path);
    }



    @Override
    public void exitFrame(){
        closeFrame();
    }
    public void closeFrame(){
        setVisible(false);
        dispose();
        if (openFrames <= 0) {
            System.exit(0);
        }
    }

    @Override
    public void setDefaultCloseOperation(CloseOption closeOption) {
        if (closeOption == null) {
            closeOption = CloseOption.EXIT;
        }
        switch (closeOption) {
            case EXIT: {
                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                break;
            }
            case CLOSE: {
                setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                break;
            }
            case NOTHING: {
                setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                break;
            }
        }
    }
    //    public class LocksAction implements SerializableActionListener {
//        public void actionPerformed(ActionEvent e) {
//            console.getLockMonitor().setVisible(true);
//        }
//    }


    @Override
    public void removeWindow(JComponent toComponent) {
        wins.removeWindow(toComponent);
    }
}
