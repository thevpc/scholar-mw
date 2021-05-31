/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.results.components;

import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JTree;

//import net.thevpc.common.iconset.PIconSet;
import net.thevpc.echo.iconset.IconSets;
import net.thevpc.echo.swing.helpers.tree.LazyTree;
import net.thevpc.echo.swing.icons.SwingAppImage;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.HadruwavesStudio;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.results.actions.HWSolverAction;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.results.results.HWSolverResult;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.util.CustomLazyNode;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.util.DefaultAppTreeCellRenderer;

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
            IconSets iconSet = studio().app().iconSets();
            Object o = ln.getValue();
            if (o instanceof HWSolverAction) {
                HWSolverAction a = (HWSolverAction) o;
                String i = a.icon();
                if (i != null) {
                    ImageIcon v = SwingAppImage.imageIconOf(iconSet.icon(i).get());
                    setIcon(v);
                }else{
                    ImageIcon v = SwingAppImage.imageIconOf(iconSet.icon("Build").get());
                    setIcon(v);
                }
            }else if(o instanceof HWSolverResult){
                HWSolverResult a = (HWSolverResult) o;
                String i = a.icon();
                if (i != null) {
                    ImageIcon v = SwingAppImage.imageIconOf(iconSet.icon(i).get());
                    setIcon(v);
                }else{
                    ImageIcon v = SwingAppImage.imageIconOf(iconSet.icon("Chart").get());
                    setIcon(v);
                }
            }
        }
        return this;
    }
    
}
