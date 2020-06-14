/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.results.components;

import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JTree;

import net.vpc.common.app.AppIconSet;
import net.vpc.common.iconset.PIconSet;
import net.vpc.common.app.swing.core.swing.LazyTree;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.HadruwavesStudio;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.results.actions.HWSolverAction;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.results.results.HWSolverResult;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.util.CustomLazyNode;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.util.DefaultAppTreeCellRenderer;

/**
 *
 * @author vpc
 */
public class ResultsTreeCellRendererImpl extends DefaultAppTreeCellRenderer {
    
    public ResultsTreeCellRendererImpl(HadruwavesStudio hs) {
        super(hs);
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        CustomLazyNode ln = (CustomLazyNode) LazyTree.resolveLazyTreeNodeValue(value);
        if (ln != null) {
            AppIconSet iconSet = studio().app().iconSet();
            Object o = ln.getValue();
            if (o instanceof HWSolverAction) {
                HWSolverAction a = (HWSolverAction) o;
                String i = a.icon();
                if (i != null) {
                    ImageIcon v = iconSet.icon(i).get();
                    setIcon(v);
                }else{
                    ImageIcon v = iconSet.icon("Build").get();
                    setIcon(v);
                }
            }else if(o instanceof HWSolverResult){
                HWSolverResult a = (HWSolverResult) o;
                String i = a.icon();
                if (i != null) {
                    ImageIcon v = iconSet.icon(i).get();
                    setIcon(v);
                }else{
                    ImageIcon v = iconSet.icon("Chart").get();
                    setIcon(v);
                }
            }
        }
        return this;
    }
    
}
