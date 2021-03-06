package net.thevpc.scholar.hadruplot.console;

import net.thevpc.common.i18n.Str;
import net.thevpc.common.swing.file.FileDropListener;
import net.thevpc.common.swing.win.WindowInfo;
import net.thevpc.common.swing.win.WindowInfoListener;
import net.thevpc.common.swing.*;
import net.thevpc.common.swing.win.InternalWindowsHelper;
import net.thevpc.common.swing.win.WindowPath;
import net.thevpc.echo.WindowState;
import net.thevpc.echo.api.components.AppComponent;
import net.thevpc.echo.api.AppContainerChildren;
import net.thevpc.echo.impl.DefaultApplication;
import net.thevpc.swing.plaf.UIPlafManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import net.thevpc.common.props.Path;
import net.thevpc.common.swing.file.ExtensionFileChooserFilter;

import net.thevpc.echo.Application;
import net.thevpc.scholar.hadruplot.console.extension.PlotConsoleFileSupport;

public class DefaultPlotConsoleFrame2 implements PlotConsoleFrame {

    public static int openFrames = 0;
    PlotConsole console;
    JLabel globalLabel;
    InternalWindowsHelper wins = new InternalWindowsHelper();
    private Application app;

    @Override
    public void setTitle(String frameTitle) {
        app.mainFrame().get().title().set(Str.of(frameTitle));
    }

    public DefaultPlotConsoleFrame2(PlotConsole console, String title) throws HeadlessException {
        this.console = console;
        app = new DefaultApplication();
        app.mainFrame().set(new net.thevpc.echo.Frame(app));
        UIPlafManager.INSTANCE.apply("FlatLight");
        app.i18n().bundles().add("net.thevpc.scholar.hadruplot.console.HadrumathsPlot");
        app.start();

        wins.addWindowInfoListener(new WindowInfoListener() {
            @Override
            public void onAddFrame(WindowInfo windowInfo) {
                JComponent component = (JComponent) windowInfo.getComponent();
                JInternalFrame internalFrame = (JInternalFrame) component.getClientProperty(JInternalFrame.class);
                final JMenuItem windowMenu = createWindowMenu(internalFrame);
                component.putClientProperty("menu", windowMenu);
                app.components().add(
                        new net.thevpc.echo.Button(null, Str.of(windowInfo.getTitle()), () -> wins.ensureVisible(internalFrame), app),
                        Path.of("/mainFrame/menuBar/Windows/*")
                );
            }

            @Override
            public void onCloseFrame(WindowInfo windowInfo) {
                JComponent component = (JComponent) windowInfo.getComponent();
                JMenuItem windowMenu = (JMenuItem) component.getClientProperty("menu");
                if (windowMenu != null) {
                    //windowsMenu.add(windowMenu);
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
        boolean oldV = app.mainFrame().get().state().is(WindowState.ACTIVATED);
        app.mainFrame().get().state().add(b ? WindowState.ACTIVATED : WindowState.CLOSED);
//        super.setVisible(b);
        boolean newV = app.mainFrame().get().state().is(WindowState.ACTIVATED);
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
    protected void prepareFileMenu() {
        AppContainerChildren<AppComponent> tools = app.components();
        tools.addFolder(Path.of("/mainFrame/menuBar/File"));
        //;
        tools.add(
                new net.thevpc.echo.Button("Properties", Str.of("Properties"),
                        () -> JOptionPane.showMessageDialog(frameComponent(), new PlotConsoleProperties(getConsole()), "Properties", JOptionPane.PLAIN_MESSAGE),
                        app
                ), Path.of("/mainFrame/menuBar/File/*")
        );
        tools.addSeparator(Path.of("/mainFrame/menuBar/File/*"));
        tools.add(new net.thevpc.echo.Button("Open",()->loadPlot(),app),Path.of("/mainFrame/menuBar/File/LoadAction"));
        tools.addSeparator(Path.of("/mainFrame/menuBar/File/*"));
        tools.add(new net.thevpc.echo.Button("Close",()->closeFrame(),app),Path.of("/mainFrame/menuBar/File/CloseAction"));
        tools.add(new net.thevpc.echo.Button("Exit",()->exitFrame(),app),Path.of("/mainFrame/menuBar/File/ExitAction"));
        //Applications.Helper.addViewActions(app);

//        Applications.Helper.addWindowsActions(app,wins.getDesktop());
        tools.addSeparator(Path.of("/mainFrame/menuBar/Windows/Separator"));

//        recentFilesMenu = new RecentFilesMenu("Recent Files", new RecentFilesPropertiesModel(new File(System.getProperty("user.dir") + "/.java/plotconsole.xml")));
//        recentFilesMenu.addFileSelectedListener(
//                new FileSelectedListener() {
//                    public void fileSelected(FileEvent event) {
//                        try {
//                            console.loadFile(event.getFile());
//                            recentFilesMenu.addFile(event.getFile());
//                        } catch (Throwable e1) {
//                            JOptionPane.showMessageDialog(DefaultPlotConsoleFrame2.this.frameComponent(), e1);
//                        }
//                    }
//                });
    }

//    protected JMenuBar createMenuBar() {
//        menubar = new JMenuBar();
//
//        fileMenu = new JMenu("File");
//        prepareFileMenu(fileMenu);
//        menubar.add(fileMenu);
//
//
//        toolsWindowsMenu = new JMenu("Tools");
//        menubar.add(toolsWindowsMenu);
//        prepareToolsMenu(toolsWindowsMenu);
//
////        toolsWindowsMenu.add(createMenuItem("Locks", new LocksAction()));
//        windowsMenu = new JMenu("Windows");
////        JMenuItem tile_windows = new JMenuItem("Tile Windows");
////        tile_windows.setAction();
//        windowsMenu.add(new AbstractPlotAction("Tile Windows") {
//            {
//                putValue(SMALL_ICON, SwingUtilities3.getScaledIcon(DefaultPlotConsoleFrame2.class.getResource("TileWindows.png"),16,16));
//            }
//
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                tileFrames();
//            }
//        });
//        windowsMenu.add(new AbstractPlotAction("Icon Windows") {
//            {
//                putValue(SMALL_ICON, SwingUtilities3.getScaledIcon(DefaultPlotConsoleFrame2.class.getResource("IconifyWindows.png"),16,16));
//            }
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                iconifyFrames();
//            }
//        });
//        windowsMenu.add(new AbstractPlotAction("De-Icon Windows") {
//            {
//                putValue(SMALL_ICON, SwingUtilities3.getScaledIcon(DefaultPlotConsoleFrame2.class.getResource("DeiconifyWindows.png"),16,16));
//            }
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                deiconifyFrames();
//            }
//        });
//        windowsMenu.add(new AbstractPlotAction("Close Windows") {
//            {
//                putValue(SMALL_ICON, SwingUtilities3.getScaledIcon(DefaultPlotConsoleFrame2.class.getResource("CloseWindows.png"),16,16));
//            }
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                closeFrames();
//            }
//        });
//        windowsMenu.addSeparator();
//        menubar.add(windowsMenu);
//
//
//        List<PlotConsoleMenuItem> menus = new ArrayList<>(Arrays.asList(console.getMenus()));
//        Set<String> visitedMenuPos = new HashSet<>();
//        Map<String, Integer> expectedMenuPos = new HashMap<>();
//        for (int i = 0; i < menus.size(); i++) {
//            PlotConsoleMenuItem mi = menus.get(i);
//            visitedMenuPos.add(mi.getFullPath());
//            if (!mi.getPath().equals("") && mi.getPath().equals("/")) {
//                expectedMenuPos.put(mi.getPath(), i);
//            }
//        }
//        List<PlotConsoleMenuItem> menusToCreate = new ArrayList<>();
//
//        for (Map.Entry<String, Integer> ee : expectedMenuPos.entrySet()) {
//            if (!visitedMenuPos.contains(ee.getKey())) {
//                if (ee.getKey().equals("/")) {
//
//                } else {
//                    List<String> split = new ArrayList<>(Arrays.asList(StringUtils.split(ee.getKey(), "/")));
//                    String nn = split.get(split.size() - 1);
//                    split.remove(split.size() - 1);
//                    String pp = StringUtils.join( "/",split);
//                    PlotConsoleMenu e = new PlotConsoleMenu(nn, nn, pp, 0);
//                    e.setInitialIndex(ee.getValue());
//                    menusToCreate.add(e);
//                }
//            }
//        }
//
//        Collections.sort(menusToCreate, new Comparator<PlotConsoleMenuItem>() {
//            @Override
//            public int compare(PlotConsoleMenuItem o1, PlotConsoleMenuItem o2) {
//                return Integer.compare(o2.getInitialIndex(), o1.getInitialIndex());
//            }
//        });
//        for (PlotConsoleMenuItem plotConsoleMenuItem : menusToCreate) {
//            menus.add(plotConsoleMenuItem.getInitialIndex(), plotConsoleMenuItem);
//        }
//        final Map<String, PlotConsoleMenuItem> menuMap = new HashMap<>();
//        Map<String, JMenuItem> menuMapToJ = new HashMap<>();
//        for (int i = 0; i < menus.size(); i++) {
//            PlotConsoleMenuItem mi = menus.get(i);
//            mi.setInitialIndex(i);
//            menuMap.put(mi.getFullPath(), mi);
//        }
//        Collections.sort(menus, new Comparator<PlotConsoleMenuItem>() {
//            @Override
//            public int compare(PlotConsoleMenuItem o1, PlotConsoleMenuItem o2) {
//                String p1 = o1.getPath();
//                String p2 = o2.getPath();
//                if (p1.equals(p2)) {
//                    int x = Integer.compare(o1.getOrder(), o2.getOrder());
//                    if (x != 0) {
//                        return x;
//                    }
//                    return Integer.compare(o1.getInitialIndex(), o2.getInitialIndex());
//                } else {
//                    if (p1.equals("/")) {
//                        return -1;
//                    }
//                    if (p2.equals("/")) {
//                        return 1;
//                    }
//                    PlotConsoleMenuItem m1 = menuMap.get(p1);
//                    PlotConsoleMenuItem m2 = menuMap.get(p2);
//                    return compare(m1, m2);
//                }
//            }
//        });
//        for (final PlotConsoleMenuItem menu : menus) {
//            JMenuItem jMenuItem = menuMapToJ.get(menu.getFullPath());
//            if (jMenuItem == null) {
//                if (menu.getPath().equals("/")) {
//                    jMenuItem = new JMenu(menu.getName());
//                    menubar.add(jMenuItem);
//                } else {
//                    JMenu jmenu = (JMenu) menuMapToJ.get(menu.getPath());
//                    jMenuItem = new JMenuItem(menu.getName());
//                    jMenuItem.addActionListener(new ActionListener() {
//                        @Override
//                        public void actionPerformed(ActionEvent e) {
//                            menu.actionPerformed(console, e);
//                        }
//                    });
//                    jmenu.add(jMenuItem);
//                }
//                menuMapToJ.put(menu.getFullPath(), jMenuItem);
//            }
//        }
//        return menubar;
//    }
    private void init() {
        prepareFileMenu();

//        try {
//            setIconImage(new ImageIcon(PlotConsole.class.getResource("PlotConsole.png")).getImage());
//        }catch (Exception ex){
//            //
//        }
        SwingUtilities3.addFileDropListener(wins.getDesktop(), /*dragBorder,*/ new FileDropListener() {
            public void filesDropped(File[] files) {
                try {
                    console.loadFiles(files);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e.toString());
                }

                // end for: through each dropped file
            }   // end filesDropped
        });
//        desktop.setOpaque(false);
        globalLabel = new JLabel();
//        b.add(new MemoryUseIconTray(false));
//        add(b, BorderLayout.SOUTH);
//        setPreferredSize(new Dimension(800, 600));
//        setSize(getPreferredSize());
//        addWindowListener(new WindowListener() {
//            public void windowOpened(WindowEvent e) {
//            }
//
//            public void windowClosing(WindowEvent e) {
//                DefaultPlotConsoleFrame2.this.console.dispose();
//            }
//
//            public void windowClosed(WindowEvent e) {
//                DefaultPlotConsoleFrame2.this.console.dispose();
//            }
//
//            public void windowIconified(WindowEvent e) {
//            }
//
//            public void windowDeiconified(WindowEvent e) {
//            }
//
//            public void windowActivated(WindowEvent e) {
//            }
//
//            public void windowDeactivated(WindowEvent e) {
//            }
//        });
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
        return (Component) app.mainFrame().get().peer().toolkitComponent();
    }

    @Override
    public void addRecentFile(File selectedFile) {
//        recentFilesMenu.addFile(selectedFile);
    }

    public JInternalFrame addToolsFrame(WindowInfo fino) {
        JInternalFrame jInternalFrame = wins.addFrame(fino);
        //toolsWindowsMenu.add(createWindowMenu(jInternalFrame));
        return jInternalFrame;
    }

    private JMenuItem createWindowMenu(JInternalFrame jInternalFrame) {
        JMenuItem i = new JMenuItem(jInternalFrame.getTitle());
        i.setIcon(jInternalFrame.getFrameIcon());
        i.putClientProperty("JInternalFrame", jInternalFrame);
        i.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JComponent component1 = ((JComponent) e.getSource());
                wins.ensureVisible((JInternalFrame) (component1.getClientProperty("JInternalFrame")));
            }
        });
        return i;
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
    public void exitFrame() {
        closeFrame();
    }

    public void closeFrame() {
        setVisible(false);
        app.mainFrame().get().state().add(WindowState.CLOSED);
        if (openFrames <= 0) {
            System.exit(0);
        }
    }

    @Override
    public void setDefaultCloseOperation(CloseOption closeOption) {
        if (closeOption == null) {
            closeOption = CloseOption.EXIT;
        }
//        switch (closeOption) {
//            case EXIT: {
//                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//                break;
//            }
//            case CLOSE: {
//                setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//                break;
//            }
//            case NOTHING: {
//                setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
//                break;
//            }
//        }
    }

    @Override
    public void removeWindow(JComponent toComponent) {
        wins.removeWindow(toComponent);
    }

    private void loadPlot() {
        JFileChooser c = new JFileChooser();
        PlotConsole console = getConsole();
        c.setCurrentDirectory(getConsole().getCurrentDirectory());
        c.addChoosableFileFilter(PlotConsole.CHOOSER_FILTER);
        for (PlotConsoleFileSupport plotConsoleFileSupport : console.getPlotConsoleFileSupports()) {
            c.addChoosableFileFilter(new ExtensionFileChooserFilter(plotConsoleFileSupport.getFileExtension(), plotConsoleFileSupport.getFileDesc()));
        }
        if (JFileChooser.APPROVE_OPTION == c.showOpenDialog(frameComponent())) {
            File selectedFile = c.getSelectedFile();
            try {
                console.loadFile(selectedFile);
                addRecentFile(selectedFile);
            } catch (Throwable e1) {
                console.getLog().error(e1);
                e1.printStackTrace();
                JOptionPane.showMessageDialog(frameComponent(), "unable to load " + selectedFile + "\n" + e1);
            }
        }
    }
}
