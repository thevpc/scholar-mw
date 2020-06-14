/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.explorer.components;

import net.vpc.common.app.swing.core.swing.LazyTreeBackend;
import net.vpc.common.app.swing.core.swing.LazyTreeNode;
import net.vpc.common.props.WritablePIndexedNode;
import net.vpc.common.props.WritablePNamedNode;
import net.vpc.scholar.hadruwaves.project.HWProject;
import net.vpc.scholar.hadruwaves.project.HWSolution;
import net.vpc.scholar.hadruwaves.project.HWSolutionFolder;
import net.vpc.scholar.hadruwaves.project.configuration.HWConfigurationFolder;
import net.vpc.scholar.hadruwaves.project.parameter.HWParameterFolder;
import net.vpc.scholar.hadruwaves.project.scene.*;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.HadruwavesStudio;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.explorer.HWSSolutionExplorerTool;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.util.HWProjectItem;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author vpc
 */
public class SolutionLazyTreeBackend implements LazyTreeBackend {

    HadruwavesStudio studio;

    public SolutionLazyTreeBackend(HadruwavesStudio studio) {
        this.studio = studio;
    }

    @Override
    public LazyTreeNode getRoot() {
        HWSolution solution = studio.proc().solution().get();
        if (solution != null) {
            return new LazyTreeNode(solution.name().get(), new HWProjectItem(studio, solution, null, solution), "/", true);
        }
        return new LazyTreeNode("No Solution is Open", new HWProjectItem(studio, solution, null, "/"), "/", true);
    }

    @Override
    public LazyTreeNode[] getChildren(LazyTreeNode parent) {
        HWProjectItem oi = (HWProjectItem) parent.getValue();
        Object ov = oi.getItemValue();
        Object o = oi.getItem();
        String parentPath = parent.getPath();
        HWSolution solution = studio.proc().solution().get();
        if (ov instanceof HWProjectFolder) {
            HWProjectFolder pfn = (HWProjectFolder) ov;
            switch (pfn.path) {
                case "/Materials": {
                    HWProject project = pfn.project;
                    LinkedHashMap<String, LazyTreeNode> all = new LinkedHashMap<>();
                    if (project != null) {
                        for (HWMaterialTemplate value : project.materials().values()) {
                            if (!all.containsKey(value.name().get())) {
                                all.put(value.name().get(), HWSSolutionExplorerTool.createLazyTreeNode(value, solution, project, studio, parentPath));
                            }
                        }
                    }
                    return all.values().toArray(new LazyTreeNode[0]);
                }
                case "/Sources": {
                    HWProject project = pfn.project;
                    if (project != null) {
                        HWProjectScene s = project.scene().get();
                        if (s != null) {
                            return s.findDeepComponents((x) -> x instanceof HWProjectPort).stream().map((x) -> HWSSolutionExplorerTool.createLazyTreeNode(x, solution, project, studio, parentPath)).toArray(LazyTreeNode[]::new);
                        }
                    }
                    return new LazyTreeNode[0];
                }
                case "/Volumes": {
                    HWProject project = pfn.project;
                    if (project != null) {
                        HWProjectScene s = project.scene().get();
                        if (s != null) {
                            return s.components().findAll((x)
                                    -> (x instanceof HWProjectElementMaterialVolume)
                            ).stream().map((x) -> HWSSolutionExplorerTool.createLazyTreeNode(x, solution, project, studio, parentPath)).toArray(LazyTreeNode[]::new);
                        }
                    }
                    return new LazyTreeNode[0];
                }
                case "/Surfaces": {
                    HWProject project = pfn.project;
                    if (project != null) {
                        HWProjectScene s = project.scene().get();
                        if (s != null) {
                            return s.components().findAll((x)
                                    -> (x instanceof HWProjectElementMaterialSurface)
                            ).stream().map((x) -> HWSSolutionExplorerTool.createLazyTreeNode(x, solution, project, studio, parentPath)).toArray(LazyTreeNode[]::new);
                        }
                    }
                    return new LazyTreeNode[0];
                }
                case "/Curves": {
                    HWProject project = pfn.project;
                    if (project != null) {
                        HWProjectScene s = project.scene().get();
                        if (s != null) {
                            return s.components().findAll((x)
                                    -> (x instanceof HWProjectElementMaterialCurve)
                            ).stream().map((x) -> HWSSolutionExplorerTool.createLazyTreeNode(x, solution, project, studio, parentPath)).toArray(LazyTreeNode[]::new);
                        }
                    }
                    return new LazyTreeNode[0];
                }
                case "/Configurations": {
                    HWProject project = pfn.project;
                    if (project != null) {
                        return project.configurations().children().values().stream().map((x) -> HWSSolutionExplorerTool.createLazyTreeNode(x, solution, project, studio, parentPath)).toArray(LazyTreeNode[]::new);
                    }
                    return new LazyTreeNode[0];
                }
                case "/Code": {
                    HWProject project = pfn.project;
                    if (project != null) {
                        if(project.isPersistent()){
                            String fr = project.filePath().get();
                            File[] children = new File(new File(fr).getParentFile(), "code").listFiles();
                            List<LazyTreeNode> nn=new ArrayList<>();
                            if(children!=null){
                                for (File child : children) {
                                    if(!child.isHidden() && !child.getName().startsWith(".")){
                                        HWProjectSource value = HWProjectSource.of("/" + child.getName(), project, child,studio);
                                        nn.add(HWSSolutionExplorerTool.createLazyTreeNode(value,solution,project, studio,parentPath));
                                    }
                                }
                            }
                            return nn.toArray(new LazyTreeNode[0]);
                        }
                    }
                    return new LazyTreeNode[0];
                }
            }
        }
        if (ov instanceof HWProjectSource) {
            HWProjectSource s=(HWProjectSource) ov;
            List<LazyTreeNode> nn=new ArrayList<>();
            if(s.file().isDirectory()) {
                File[] children = s.file().listFiles();
                if (children != null) {
                    for (File child : children) {
                        if (!child.isHidden() && !child.getName().startsWith(".")) {
                            HWProjectSource value = HWProjectSource.of("/" + child.getName(), s.project(), child,studio);
                            nn.add(HWSSolutionExplorerTool.createLazyTreeNode(value, solution, s.project(), studio, parentPath));
                        }
                    }
                }
            }
            return nn.toArray(new LazyTreeNode[0]);
        }
        if (ov instanceof HWProjectComponentGroup) {
            return ((HWProjectComponentGroup) ov).children().stream().map((x) -> HWSSolutionExplorerTool.createLazyTreeNode(x, solution, oi.getProject(), studio, parentPath)).toArray(LazyTreeNode[]::new);
        }
        if (ov instanceof HWProjectBrick) {
            return Arrays.stream(((HWProjectBrick) ov).faces())
                    .map((x) -> HWSSolutionExplorerTool.createLazyTreeNode(x, solution, oi.getProject(), studio, parentPath)).toArray(LazyTreeNode[]::new);
        }
        if (ov instanceof HWConfigurationFolder) {
            return ((HWConfigurationFolder) ov).children().values().stream().map((x) -> HWSSolutionExplorerTool.createLazyTreeNode(x, solution, oi.getProject(), studio, parentPath)).toArray(LazyTreeNode[]::new);
        }
        if (ov instanceof HWSolutionFolder) {
            return ((HWSolutionFolder) ov).children().values().stream().map((x) -> HWSSolutionExplorerTool.createLazyTreeNode(x, solution, oi.getProject(), studio, parentPath)).toArray(LazyTreeNode[]::new);
        }
        if (ov instanceof HWParameterFolder) {
            return ((HWParameterFolder) ov).children().values().stream().map((x) -> HWSSolutionExplorerTool.createLazyTreeNode(x, solution, oi.getProject(), studio, parentPath)).toArray(LazyTreeNode[]::new);
        }
        if (ov instanceof HWSolution) {
            //WritablePIndexedNode<HWSolutionElement> child :
            return ((HWSolution) o).children().values().stream().map((x) -> HWSSolutionExplorerTool.createLazyTreeNode(x, solution, oi.getProject(), studio, parentPath)).toArray(LazyTreeNode[]::new);
        }
        if (ov instanceof HWProject) {
            HWProject project = (HWProject) ov;
            return new LazyTreeNode[]{
                    new LazyTreeNode("Volumes", new HWProjectItem(studio, solution, project, new HWProjectFolder("/Volumes", project)), parentPath + "/Volumes", true),
                    new LazyTreeNode("Surfaces", new HWProjectItem(studio, solution, project, new HWProjectFolder("/Surfaces", project)), parentPath + "/Surfaces", true),
                    new LazyTreeNode("Curves", new HWProjectItem(studio, solution, project, new HWProjectFolder("/Curves", project)), parentPath + "/Curves", true),
                    new LazyTreeNode("Sources", new HWProjectItem(studio, solution, project, new HWProjectFolder("/Sources", project)), parentPath + "/Sources", true),
                    new LazyTreeNode("Materials", new HWProjectItem(studio, solution, project, new HWProjectFolder("/Materials", project)), parentPath + "/Materials", true),
                    new LazyTreeNode("Code", new HWProjectItem(studio, solution, project, new HWProjectFolder("/Code", project)), parentPath + "/Code", true),
                    new LazyTreeNode("Configurations", new HWProjectItem(studio, solution, project, new HWProjectFolder("/Configurations", project)), parentPath + "/Configurations", true)
            };
        }
        if (o instanceof WritablePIndexedNode) {
            WritablePIndexedNode<Object> node = (WritablePIndexedNode) o;
            return node.children().stream().map((x) -> HWSSolutionExplorerTool.createLazyTreeNode(x, solution, oi.getProject(), studio, parentPath)).toArray(LazyTreeNode[]::new);
        }
        if (o instanceof WritablePNamedNode) {
            WritablePNamedNode<Object> node = (WritablePNamedNode) o;
            return node.children().values().stream().map((x) -> HWSSolutionExplorerTool.createLazyTreeNode(x, solution, oi.getProject(), studio, parentPath)).toArray(LazyTreeNode[]::new);
        }
        return new LazyTreeNode[0];
    }

}
