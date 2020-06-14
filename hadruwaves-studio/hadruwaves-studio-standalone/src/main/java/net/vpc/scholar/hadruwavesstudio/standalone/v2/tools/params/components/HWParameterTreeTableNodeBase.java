/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.params.components;

import net.vpc.scholar.hadruwaves.project.configuration.HWConfigurationRun;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;

/**
 *
 * @author vpc
 */
public class HWParameterTreeTableNodeBase extends DefaultMutableTreeTableNode {

    private final HWConfigurationRun configuration;
    private final CustomJXTreeTable tree;

    public HWParameterTreeTableNodeBase(Object pe, boolean folder, HWConfigurationRun configuration, CustomJXTreeTable model) {
        super(pe, folder);
        this.configuration = configuration;
        this.tree = model;
    }

    public HWConfigurationRun configuration() {
        return configuration;
    }

    protected void refreshModel() {
        configuration.reset();
        CustomDefaultTreeTableModelImpl model = (CustomDefaultTreeTableModelImpl) tree.getTreeTableModel();
//        TreeTableNode root = create(configuration, configuration, model);
//        model.setRoot(root);
//        tree.setRootVisible(true);
//        tree.expandAll();
        tree.invalidate();
        tree.revalidate();
        tree.repaint();
    
    }

    @Override
    public final int getColumnCount() {
        return 7;
    }
}
