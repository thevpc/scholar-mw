//package net.vpc.scholar.hadruwaves.studio.standalone.v2.win;
//
//import net.vpc.common.strings.StringUtils;
//import net.vpc.common.swings.app.impl.swing.LazyTree;
//import net.vpc.common.swings.app.impl.swing.LazyTreeBackend;
//import net.vpc.common.swings.app.impl.swing.LazyTreeNode;
//import net.vpc.scholar.hadruwaves.Material;
//import net.vpc.scholar.hadruwaves.project.HWProject;
//import net.vpc.scholar.hadruwaves.project.scene.HWMaterialTemplate;
//import net.vpc.scholar.hadruwaves.project.scene.HWScene;
//import net.vpc.scholar.hadruwaves.project.scene.StructureNodeGroup;
//import net.vpc.scholar.hadruwaves.project.scene.StructureNodePort;
//import net.vpc.scholar.hadruwaves.studio.standalone.v2.HadruwavesStudioV2;
//import org.jdesktop.swingx.JXTree;
//
//import javax.swing.*;
//import java.awt.*;
//import java.util.ArrayList;
//import java.util.LinkedHashMap;
//
//public class HWSProjectNavigationTreeTool extends JPanel {
//    private HadruwavesStudioV2 application;
//
//    public HWSProjectNavigationTreeTool(HadruwavesStudioV2 application) {
//        super(new BorderLayout());
//        this.application = application;
//
//        JTree tree = LazyTree.of(new JXTree(), new LazyTreeBackend() {
//                    @Override
//                    public LazyTreeNode[] getChildren(LazyTreeNode parent) {
//                        switch (parent.getPath()) {
//                            case "/": {
//                                return new LazyTreeNode[]{
//                                        new LazyTreeNode("Components", "/Components", "/Components", true),
//                                        new LazyTreeNode("Sources", "/Sources", "/Sources", true),
//                                        new LazyTreeNode("Materials", "/Materials", "/Materials", true),
//                                };
//                            }
//                            case "/Materials": {
//                                HWProject p = application.proc().project().get();
//                                LinkedHashMap<String, LazyTreeNode> all = new LinkedHashMap<>();
//                                all.put("Vacuum", new LazyTreeNode("Vacuum", Material.VACUUM, parent.getPath() + "/Vacuum", false));
//                                all.put("PEC", new LazyTreeNode("PEC", Material.PEC, parent.getPath() + "/PEC", false));
//                                all.put("PMC", new LazyTreeNode("PMC", Material.PMC, parent.getPath() + "/PMC", false));
//                                if (p != null) {
//                                    for (HWMaterialTemplate value : p.materials().values()) {
//                                        if (all.containsKey(value.name().get())) {
//                                            all.put(
//                                                    value.name().get(),
//                                                    new LazyTreeNode(value.name().get(), value, parent.getPath() + "/" + value.name().get(), false)
//                                            );
//                                        }
//                                    }
//                                }
//                                return all.values().toArray(new LazyTreeNode[0]);
//                            }
//                            case "/Sources": {
//                                HWProject p = application.proc().project().get();
//                                java.util.List<LazyTreeNode> all = new ArrayList<>();
//                                if (p != null) {
//                                    HWScene s = p.scene().get();
//                                    if (s != null) {
//                                        return s.findDeepComponents(x -> x instanceof StructureNodePort)
//                                                .stream()
//                                                .map(x -> new LazyTreeNode(x.getClass().getSimpleName(), x, parent.getPath() + "/" + x.getClass().getSimpleName(), x instanceof StructureNodeGroup))
//                                                .toArray(LazyTreeNode[]::new);
//                                    }
//                                }
//                                return new LazyTreeNode[0];
//                            }
//                            case "/Components": {
//                                HWProject p = application.proc().project().get();
//                                java.util.List<LazyTreeNode> all = new ArrayList<>();
//                                if (p != null) {
//                                    HWScene s = p.scene().get();
//                                    if (s != null) {
//                                        return s.components().findAll(x -> !(x instanceof StructureNodePort))
//                                                .stream()
//                                                .map(x -> new LazyTreeNode(StringUtils.isBlank(x.name().get())?x.getClass().getSimpleName():x.name().get(),
//                                                        x, parent.getPath() + "/" + x.getClass().getSimpleName(),
//                                                        x instanceof StructureNodeGroup)
//                                                )
//                                                .toArray(LazyTreeNode[]::new);
//                                    }
//                                    return new LazyTreeNode[0];
//                                }
//                            }
//                        }
//                        if (parent.getValue() instanceof StructureNodeGroup) {
//                            return ((StructureNodeGroup) parent.getValue()).children()
//                                    .stream()
//                                    .map(x -> new LazyTreeNode(x.getClass().getSimpleName(), x, parent.getPath() + "/" + x.getClass().getSimpleName(), x instanceof StructureNodeGroup))
//                                    .toArray(LazyTreeNode[]::new);
//                        }
//                        return new LazyTreeNode[0];
//                    }
//                }, new LazyTreeNode("Project Layout", "/", "/", true)
//        );
//
//        JPanel pane = new JPanel(new BorderLayout());
////        pane.add(toolbar,BorderLayout.PAGE_START);
//        tree.setBorder(null);
//        JScrollPane jsp = new JScrollPane(tree);
//        jsp.setBorder(null);
//        pane.add(jsp, BorderLayout.CENTER);
//        add(pane);
//    }
//}
