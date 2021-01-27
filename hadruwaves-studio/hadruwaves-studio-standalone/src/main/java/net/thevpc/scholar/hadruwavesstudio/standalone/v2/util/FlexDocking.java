package net.thevpc.scholar.hadruwavesstudio.standalone.v2.util;

//package net.thevpc.scholar.hadruwavesstudio.standalone.v2.win;
//
//import org.flexdock.docking.Dockable;
//import org.flexdock.docking.DockingConstants;
//import org.flexdock.view.View;
//import org.flexdock.view.Viewport;
//
//import javax.swing.*;
//import javax.swing.border.LineBorder;
//import java.awt.*;
//
//import static org.flexdock.docking.DockingConstants.CENTER_REGION;
//
//public class FlexDocking {
//    public static JComponent create() {
//        Viewport viewport = new Viewport();
////        p.add(viewport, BorderLayout.CENTER);
//        View startPage = createStartPage();
//
//        View view1 = createProjectsView();
//        View view2 = createStructure();
//        View view3 = createResults();
//        View view4 = createMessages();
//        View view5 = createTasks();
//        View view6 = createLocks();
//
//        viewport.dock(startPage);
//        view1.dock(view4);
//        viewport.dock((Dockable) view1, DockingConstants.WEST_REGION);
//        viewport.dock((Dockable) view2, DockingConstants.SOUTH_REGION);
//        viewport.dock((Dockable) view3, DockingConstants.EAST_REGION);
//        viewport.dock((Dockable) view5, DockingConstants.WEST_REGION);
//        viewport.dock((Dockable) view6, DockingConstants.WEST_REGION);
////                        viewport.dock((Dockable) view4, DockingConstants.SOUTH_REGION);
//
////                        startPage.dock(view1, DockingConstants.WEST_REGION, .3f);
////                        startPage.dock(view2, DockingConstants.SOUTH_REGION, .3f);
////                        startPage.dock(view3, DockingConstants.EAST_REGION, .3f);
////                        startPage.dock(view5, DockingConstants.WEST_REGION, .3f);
////                        startPage.dock(view6, DockingConstants.WEST_REGION, .3f);
////                        startPage.dock(view4, DockingConstants.SOUTH_REGION, .3f);
//        return viewport;
//    }
//
//    protected static View createStructure() {
//        View view = new View("Structure", "Structure");
//        view.addAction(DockingConstants.CLOSE_ACTION);
//        view.addAction(DockingConstants.PIN_ACTION);
//
//        JPanel p = new JPanel();
//        //                p.setBackground(Color.WHITE);
//        p.setBorder(new LineBorder(Color.GRAY, 1));
//
//        JTextArea a = new JTextArea();
//        JScrollPane jsp = new JScrollPane(a);
//
//        p.add(jsp);
//
//        view.setContentPane(p);
//        return view;
//    }
//
//    protected static View createResults() {
//        View view = new View("Results", "Results");
//        view.addAction(DockingConstants.CLOSE_ACTION);
//        view.addAction(DockingConstants.PIN_ACTION);
//
//        JPanel p = new JPanel();
//        //                p.setBackground(Color.WHITE);
//        p.setBorder(new LineBorder(Color.GRAY, 1));
//
//        JTextArea a = new JTextArea();
//        JScrollPane jsp = new JScrollPane(a);
//
//        p.add(jsp);
//        view.setContentPane(p);
//        return view;
//    }
//
//    protected static View createMessages() {
//        View view = new View("Messages", "Messages");
//        view.addAction(DockingConstants.CLOSE_ACTION);
//        view.addAction(DockingConstants.PIN_ACTION);
//
//        JPanel p = new JPanel();
//        //                p.setBackground(Color.WHITE);
//        p.setBorder(new LineBorder(Color.GRAY, 1));
//
//        JTextArea a = new JTextArea();
//        JScrollPane jsp = new JScrollPane(a);
//
//        p.add(jsp);
//
//        view.setContentPane(p);
//        return view;
//    }
//
//    protected static View createTasks() {
//        View view = new View("Tasks", "Tasks");
//        view.addAction(DockingConstants.CLOSE_ACTION);
//        view.addAction(DockingConstants.PIN_ACTION);
//
//        JPanel p = new JPanel();
//        //                p.setBackground(Color.WHITE);
//        p.setBorder(new LineBorder(Color.GRAY, 1));
//
//        JTextArea a = new JTextArea();
//        JScrollPane jsp = new JScrollPane(a);
//
//        p.add(jsp);
//
//        view.setContentPane(p);
//        return view;
//    }
//
//    protected static View createLocks() {
//        View view = new View("Locks", "Locks");
//        view.addAction(DockingConstants.CLOSE_ACTION);
//        view.addAction(DockingConstants.PIN_ACTION);
//
//        JPanel p = new JPanel();
//        //                p.setBackground(Color.WHITE);
//        p.setBorder(new LineBorder(Color.GRAY, 1));
//
//        JTextArea a = new JTextArea();
//        JScrollPane jsp = new JScrollPane(a);
//
//        p.add(jsp);
//
//        view.setContentPane(p);
//        return view;
//    }
//
//    protected static View createStartPage() {
//        View startPage = new View("startPage", null, null);
//        startPage.setTerritoryBlocked(CENTER_REGION, true);
//        startPage.setTitlebar(null);
//        JDesktopPane desktop = new JDesktopPane();
//        startPage.setContentPane(desktop);
//        return startPage;
//    }
//
//    protected static View createProjectsView() {
//        View view = new View("Projects", "Projects");
//        view.addAction(DockingConstants.CLOSE_ACTION);
//        view.addAction(DockingConstants.PIN_ACTION);
//
//        JPanel p = new JPanel(new BorderLayout());
//        //                p.setBackground(Color.WHITE);
//        p.setBorder(new LineBorder(Color.GRAY, 1));
//
//        JTextArea a = new JTextArea();
//        JScrollPane jsp = new JScrollPane(a);
//        p.add(jsp);
//
//        view.setContentPane(p);
//        return view;
//    }
//}
