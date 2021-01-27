package net.thevpc.scholar.hadruwaves.mom.project.series;

import net.thevpc.scholar.hadrumaths.Axis;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToComplex;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToDouble;
import net.thevpc.scholar.hadrumaths.Domain;

import static net.thevpc.scholar.hadrumaths.Expressions.symmetric;

/**
 * Created by IntelliJ IDEA. User: vpc Date: 11 août 2005 Time: 14:49:16 To
 * change this template use File | Settings | File Templates.
 */
public class CSerieSymmetricXY extends CSerieXY implements Cloneable {

    private CSerieXY base;

    public CSerieSymmetricXY(Domain domain, CSerieXY base) {
        super(domain);
        this.base = base;
    }

    public DoubleToComplex getFunction(int n) {
        DoubleToComplex f = base.getFunction(n);
        DoubleToDouble r = symmetric(f.getRealDD(), Axis.X, getDomain());
        DoubleToDouble i = symmetric(f.getImagDD(), Axis.X, getDomain());
        return Maths.sum(f, Maths.complex(r, i)).toDC();
    }

    public int getMinIndex() {
        return base.getMaxIndex();
    }

    public int getMaxIndex() {
        return base.getMaxIndex();
    }

    public int getMaxXIndex() {
        return base.getMaxXIndex();
    }

    public void setMaxXIndex(int x) {
        base.setMaxXIndex(x);
    }

    public int getMaxYIndex() {
        return base.getMaxYIndex();
    }

    public void setMaxYIndex(int y) {
        base.setMaxYIndex(y);
    }
}
