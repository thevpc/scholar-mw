package net.thevpc.scholar.hadruwavesstudio.standalone.v2.util;

import javax.swing.tree.TreePath;

import net.thevpc.scholar.hadruwavesstudio.standalone.v2.HadruwavesStudio;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.props.trees.HWProjectElementBrickPropertiesAware;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.props.trees.MaterialPropertiesAware;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.props.trees.HWProjectElementPropertiesAware;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.props.trees.MaterialBrickFacePropertiesAware;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.props.trees.HWMaterialTemplatePropertiesAware;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.props.trees.HWProjectElementMaterialPropertiesAware;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.props.trees.HWSolutionPropertiesAware;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.props.trees.HWProjectPropertiesAware;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.props.trees.HWSolutionElementPropertiesAware;
import net.thevpc.common.props.ObservableValue;
import net.thevpc.scholar.hadruwaves.Material;
import net.thevpc.scholar.hadruwaves.project.*;
import net.thevpc.scholar.hadruwaves.project.scene.HWMaterialTemplate;
import net.thevpc.scholar.hadruwaves.project.scene.AbstractHWProjectComponentMaterial;

import net.thevpc.scholar.hadruwaves.project.scene.HWProjectElementBrickFace;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.explorer.components.HWProjectFolder;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.props.trees.HWProjectConfigurationPropertiesAware;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.props.trees.HWProjectFolderPropertiesAware;
import net.thevpc.scholar.hadruwaves.project.configuration.HWConfigurationElement;
import net.thevpc.echo.api.AppPropertiesTree;
import net.thevpc.scholar.hadruwaves.project.HWProjectComponent;
import net.thevpc.scholar.hadruwaves.project.scene.HWProjectComponentGroup;
import net.thevpc.scholar.hadruwaves.project.scene.HWProjectPiece;

public class HWProjectItem {

    private HadruwavesStudio studio;
    private HWSolution solution;
    private HWProject project;
    private Object item;
    public TreePath treePath;

    public HWProjectItem(HadruwavesStudio application, HWSolution solution, HWProject project, Object item) {
        this.studio = application;
        this.solution = solution;
        this.project = project;
        this.item = item;
    }

    public TreePath getTreePath() {
        return treePath;
    }

    public Object getItemValue() {
        Object item = this.item;
        if (item instanceof ObservableValue) {
            item = ((ObservableValue) item).get();
        }
        return item;
    }

    public boolean isProjectFolder(String name) {
        return getItemValue() instanceof HWProjectFolder && ((HWProjectFolder) (getItemValue())).path.equals("/" + name);
    }

    public boolean isElementGroup() {
        return getItemValue() instanceof HWProjectComponentGroup;
    }

    public HadruwavesStudio getApplication() {
        return getStudio();
    }

    public HWSolution getSolution() {
        return solution;
    }

    public HWProject getProject() {
        return project;
    }

    public Object getItem() {
        return item;
    }

    public AppPropertiesTree toPropertiesAware() {
        Object item = this.item;
        if (item instanceof ObservableValue) {
            item = ((ObservableValue) item).get();
        }
        if (item instanceof HWProject) {
            HWProject wp = (HWProject) item;
            return new HWProjectPropertiesAware(wp, this);
        }
        if (item instanceof HWSolution) {
            HWSolution wp = (HWSolution) item;
            return new HWSolutionPropertiesAware(wp, this);
        }
        if (item instanceof Material) {
            Material wp = (Material) item;
            return new MaterialPropertiesAware(wp, this);
        }
        if (item instanceof HWMaterialTemplate) {
            HWMaterialTemplate wp = (HWMaterialTemplate) item;
            return new HWMaterialTemplatePropertiesAware(wp, this);
        }
        if (item instanceof HWProjectPiece) {
            HWProjectPiece wp = (HWProjectPiece) item;
            return new HWProjectElementBrickPropertiesAware(wp, this);
        }
        if (item instanceof HWProjectElementBrickFace) {
            HWProjectElementBrickFace wp = (HWProjectElementBrickFace) item;
            return new MaterialBrickFacePropertiesAware(wp, this);
        }
        if (item instanceof AbstractHWProjectComponentMaterial) {
            AbstractHWProjectComponentMaterial wp = (AbstractHWProjectComponentMaterial) item;
            return new HWProjectElementMaterialPropertiesAware(wp, this);
        }
        if (item instanceof HWProjectComponent) {
            HWProjectComponent wp = (HWProjectComponent) item;
            return new HWProjectElementPropertiesAware(wp, this);
        }
        if (item instanceof HWSolutionElement) {
            HWSolutionElement wp = (HWSolutionElement) item;
            return new HWSolutionElementPropertiesAware(wp, this);
        }
        if (item instanceof HWConfigurationElement) {
            HWConfigurationElement wp = (HWConfigurationElement) item;
            return new HWProjectConfigurationPropertiesAware(wp, this);
        }
        if (item instanceof HWProjectFolder) {
            HWProjectFolder wp = (HWProjectFolder) item;
            return new HWProjectFolderPropertiesAware(wp, this);
        }
        return null;
    }

    /**
     * @return the studio
     */
    public HadruwavesStudio getStudio() {
        return studio;
    }

    /**
     * @param studio the studio to set
     */
    public void setStudio(HadruwavesStudio studio) {
        this.studio = studio;
    }

}
