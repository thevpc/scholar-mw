package net.thevpc.scholar.hadruwaves.mom.console.params;

import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadruplot.console.params.AbstractCParam;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 14 juil. 2005 11:44:11
 */
public class BoxHeightParam extends AbstractCParam implements Cloneable {
    public BoxHeightParam() {
        super("boxHeight");
    }

    public void configure(Object source,Object value) {
        MomStructure s = (MomStructure) source;
        Domain d = s.getDomain();
        s.setDomain(Domain.ofWidth(d.xmin(),d.xwidth(),d.ymin(),(Double)value));
    }
}
