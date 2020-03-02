package net.vpc.scholar.hadruwaves.studio.standalone.v1;

import net.vpc.lib.pheromone.application.swing.ECTextField;

/**
 * Created by IntelliJ IDEA.
 * User: taha
 * Date: 8 juin 2004
 * Time: 00:31:35
 * To change this template use File | Settings | File Templates.
 */
public class ECExpressionField extends ECTextField {
    public ECExpressionField(String key, boolean nullable) {
        super(key, 16, 0, Integer.MAX_VALUE, nullable);
    }
}
