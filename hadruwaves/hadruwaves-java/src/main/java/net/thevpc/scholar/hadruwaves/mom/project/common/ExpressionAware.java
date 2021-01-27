package net.thevpc.scholar.hadruwaves.mom.project.common;

import net.thevpc.scholar.hadruwaves.mom.project.MomProject;

/**
 * Created by IntelliJ IDEA.
 * User: taha
 * Date: 8 juin 2004
 * Time: 22:49:25
 * To change this template use File | Settings | File Templates.
 */
public interface ExpressionAware {
    public MomProject getContext();

    public void setContext(MomProject context);

    public void recompile();
}
