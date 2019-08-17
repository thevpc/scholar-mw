package net.vpc.scholar.hadruwaves.mom.console.params;

import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadruplot.console.params.AbstractParam;
import net.vpc.scholar.hadruwaves.mom.MomStructure;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 14 juil. 2005 11:44:11
 */
public class BoxHeightParam extends AbstractParam implements Cloneable {
    public BoxHeightParam() {
        super("boxHeight");
    }

    public void configure(Object source,Object value) {
        MomStructure s = (MomStructure) source;
        Domain d = s.getDomain();
        s.setDomain(Domain.forWidth(d.xmin(),d.xwidth(),d.ymin(),(Double)value));
    }
}
