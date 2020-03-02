package net.vpc.scholar.hadruwaves.studio.standalone.v2.win;

import net.vpc.common.prpbind.PropertyEvent;
import net.vpc.common.prpbind.PropertyListener;
import net.vpc.common.prpbind.WritablePIndexedNode;
import net.vpc.scholar.hadruwaves.project.HWProjectEnv;
import net.vpc.scholar.hadruwaves.project.configuration.HWSConfigurationRun;
import net.vpc.scholar.hadruwaves.project.parameter.HWSParameterElement;
import net.vpc.scholar.hadruwaves.project.parameter.HWSParameterFolder;
import net.vpc.scholar.hadruwaves.project.parameter.HWSParameterValue;
import net.vpc.scholar.hadruwaves.project.parameter.HWSParameters;
import net.vpc.scholar.hadruwaves.project.HWProject;
import net.vpc.scholar.hadruwaves.studio.standalone.v2.HadruwavesStudioV2;
import org.jdesktop.swingx.JXTreeTable;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class HWSProjectParametersTool extends JPanel {
    private HadruwavesStudioV2 application;

    public HWSProjectParametersTool(HadruwavesStudioV2 application) {
        super(new BorderLayout());
        this.application = application;
        JXTreeTable view = new JXTreeTable(new ParamsTableModel3(application));
        view.expandAll();
        add(new JScrollPane(view));
    }

    private static class ParamsTableModel3 extends net.vpc.lib.pheromone.application.swing.treetable.AbstractTreeTableModel
            implements org.jdesktop.swingx.treetable.TreeTableModel {
        private HadruwavesStudioV2 application;

        public ParamsTableModel3(HadruwavesStudioV2 application) {
            super(new NN(application.activeProject() == null ? null : application.activeProject().parameters()));
            this.application = application;
            this.application.proc().listeners().add(new PropertyListener() {
                @Override
                public void propertyUpdated(PropertyEvent event) {
                    if (event.getProperty().getPropertyName().equals("activeProject")) {

                        setRoot(new NN(application.activeProject() == null ? null : application.activeProject().parameters()));
                    }
                }
            });
        }

        @Override
        public int getHierarchicalColumn() {
            return 0;
        }

        @Override
        public int getColumnCount() {
            return 5;
        }

        @Override
        public String getColumnName(int column) {
            switch (column) {
                case 0: {
                    return "Parameter";
                }
                case 1: {
                    return "Type";
                }
                case 2: {
                    return "Expression";
                }
                case 3: {
                    return "PValue";
                }
                case 4: {
                    return "Unit";
                }
            }
            return null;
        }

        @Override
        public Object getValueAt(Object node, int column) {
            if (node instanceof LoadableMutableTreeNode) {
                node = ((LoadableMutableTreeNode) node).getUserObject();
            }
            if (column == 0) {
                if (node instanceof HWSParameters) {
                    return "Parameters";
                }
                if (node instanceof WritablePIndexedNode) {
                    Object o = ((WritablePIndexedNode) node).get();
                    if (o instanceof HWSParameterValue) {
                        return ((HWSParameterValue) o).name().get();
                    }
                    if (o instanceof HWSParameterFolder) {
                        return ((HWSParameterFolder) o).name().get();
                    }
                }
                return "Unknown";
            }
            if (node instanceof WritablePIndexedNode) {
                Object o = ((WritablePIndexedNode) node).get();
                if (o instanceof HWSParameterValue) {
                    HWSParameterValue v = (HWSParameterValue) o;
                    switch (column) {
                        case 1: {
                            return v.unit().get().type();
                        }
                        case 2: {
                            HWSConfigurationRun c = application.activeProjectConfiguration();
                            if (c == null) {
                                return null;
                            }
                            return c.parameters().get(v.name().get());
                        }
                        case 3: {
                            HWProject p = application.activeProject();
                            if (p == null) {
                                return null;
                            }
                            HWProjectEnv env = new HWProjectEnv(p, application.activeProjectConfiguration());
                            return env.evalParam(v.name().get());
                        }
                        case 4: {
                            return v.unit().get();
                        }
                    }
                }
            }
            return null;
        }
//
//        @Override
//        public boolean isCellEditable(Object node, int column) {
//            if (column == 0) {
//                if (node instanceof HWSParameters) {
//                    return false;
//                }
//                if (node instanceof WritablePIndexedNode) {
//                    Object o = ((WritablePIndexedNode) node).get();
//                    if (o instanceof HWSParameterValue) {
//                        return true;
//                    }
//                    if (o instanceof HWSParameterFolder) {
//                        return true;
//                    }
//                }
//                return false;
//            }
//            if (node instanceof WritablePIndexedNode) {
//                Object o = ((WritablePIndexedNode) node).get();
//                if (o instanceof HWSParameterValue) {
//                    HWSParameterValue v = (HWSParameterValue) o;
//                    switch (column) {
//                        case 1:
//                        case 2: {
//                            return true;
//                        }
//                    }
//                }
//            }
//            return false;
//        }
//
//        @Override
//        public void setValueAt(Object aValue, Object node, int column) {
//            //
//        }
//
//        @Override
//        public Object getChild(Object parent, int index) {
//            if (parent instanceof HWSParameters) {
//                return ((HWSParameters) parent).children().get(index);
//            }
//            if (parent instanceof WritablePIndexedNode) {
//                return ((WritablePIndexedNode<HWSParameterElement>) parent).children().get(index);
//            }
//            return null;
//        }
//
//        @Override
//        public int getChildCount(Object parent) {
//            if (parent instanceof HWSParameters) {
//                return ((HWSParameters) parent).children().size();
//            }
//            if (parent instanceof WritablePIndexedNode) {
//                return ((WritablePIndexedNode<HWSParameterElement>) parent).children().size();
//            }
//            return 0;
//        }
    }

    private static class NN extends LoadableMutableTreeNode {
        public NN(Object a) {
            setUserObject(a);
            prepareChildren0();
        }

        @Override
        protected void prepareChildren() {
            Object userObject = getUserObject();
            if (userObject instanceof HWSParameters) {
                if (children == null) {
                    children = new Vector();
                }
                for (WritablePIndexedNode<HWSParameterElement> child : ((HWSParameters) userObject).children()) {
                    children.add(new NN(child));
                }
            }
            if (userObject instanceof WritablePIndexedNode) {
                if (children == null) {
                    children = new Vector();
                }
                for (WritablePIndexedNode<HWSParameterElement> child : ((WritablePIndexedNode<HWSParameterElement>) userObject).children()) {
                    children.add(new NN(child));
                }
            }
        }
    }
}
