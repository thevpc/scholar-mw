package net.thevpc.scholar.hadrumaths.symbolic.double2double;

import net.thevpc.nuts.elem.NElement;
import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadrumaths.util.internal.IgnoreRandomGeneration;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToDouble;

import static net.thevpc.scholar.hadrumaths.Maths.*;

/**
 * Created by vpc on 5/7/14.
 */
public class SinSeqYZ extends RefDoubleToDouble {
    private static final long serialVersionUID = 1L;
    private boolean maxEast;
    private boolean maxSouth;
    private boolean maxWest;
    private DoubleParam m;
    private DoubleParam n;
    private Domain domain;
    private boolean maxNorth;
    private DoubleToDouble exprVal;

    public SinSeqYZ(boolean maxNorth, boolean maxEast, boolean maxSouth, boolean maxWest, DoubleParam m, DoubleParam n, Domain domain) {
        init(maxNorth, maxEast, maxSouth, maxWest, m, n, domain);
    }

    @Override
    public NElement toElement() {
        return NElement.ofNamedObject("SinSeqYZ");
    }

    private void init(boolean maxNorth, boolean maxEast, boolean maxSouth, boolean maxWest, DoubleParam m, DoubleParam n, Domain domain) {
        this.maxEast = maxEast;
        this.maxSouth = maxSouth;
        this.maxWest = maxWest;
        this.m = m;
        this.n = n;
        this.domain = domain;
        this.maxNorth = maxNorth;
        Expr fy = null;
        Expr one = expr(1);
        Expr two = expr(2);
        Expr pi = expr(PI);
        Expr width = expr(domain.ywidth());
        Expr height = expr(domain.zwidth());
        Expr ymin = expr(domain.ymin());
        Expr zmin = expr(domain.zmin());
        Expr pibyw = Maths.div(pi, width);
        Expr pibyh = Maths.div(pi, height);
        if (maxWest) {
            if (maxEast) {
                //cos(2 * m * (pi / width) * (X-min))
                fy = Maths.cos(Maths.prod(two, m, pibyw, Maths.minus(Y, ymin))); //ok
            } else {
                //cos((m * 2+1)/2 * pi / width * (X-min))
                fy = Maths.cos(Maths.prod(Maths.div(Maths.sum(Maths.mul(two, m), one), two), pibyw, Maths.minus(Y, ymin))); //ok
            }
        } else {
            if (maxEast) {
                //fx = sin(((2 * m + 1) / 2 * PI / domain.width) * (X - domain.xmin)); //ok
                fy = Maths.sin(Maths.prod(Maths.div(Maths.sum(Maths.mul(two, m), one), two), pibyw, Maths.minus(Y, ymin))); //ok
            } else {
//                fx = sin(((m+1) * PI / domain.width) * (X - domain.xmin)); //ok
                fy = Maths.sin(Maths.prod(Maths.sum(m, one), pibyw, Maths.minus(Y, ymin))); //ok
            }
        }
        Expr fz = null;
        if (maxNorth) {
            if (maxSouth) {
//                fy = cos((n * PI / domain.Height) * (Y - domain.Ymin)); //ok
                fz = Maths.cos(Maths.prod(n, pibyh, Maths.minus(Z, zmin))); //ok
            } else {
//                fy = cos(((2 * n + 1) / 2 * PI / domain.Height) * (Y - domain.Ymin)); //ok
                fz = Maths.cos(Maths.prod(Maths.div(Maths.sum(Maths.mul(two, n), one), two), pibyh, Maths.minus(Z, zmin))); //ok
            }
        } else {
            if (maxSouth) {
                //fy = sin(((2 * n + 1) / 2 * PI / domain.Height) * (Y - domain.Ymin)); //ok
                fz = Maths.sin(Maths.prod(Maths.div(Maths.sum(Maths.mul(two, n), one), two), pibyh, Maths.minus(Z, zmin))); //ok
            } else {
                //fy = sin(((n+1) * PI / domain.Height) * (Y - domain.Ymin)); //ok
                fz = Maths.cos(Maths.prod(Maths.sum(n, one), pibyh, Maths.minus(Z, zmin))); //ok
            }
        }
        exprVal = (Maths.prod(fy, fz, Maths.expr(domain))).toDD();
    }

    @IgnoreRandomGeneration()
    public SinSeqYZ(String borders, DoubleParam m, DoubleParam n, Domain domain) {
        boolean maxNorth = false;
        boolean maxEast = false;
        boolean maxSouth = false;
        boolean maxWest = false;
        if (borders != null) {
            for (char c : borders.toCharArray()) {
                switch (c) {
                    case 'N':
                    case 'n':
                    case '^': {
                        maxNorth = true;
                        break;
                    }
                    case 'S':
                    case 's':
                    case '_': {
                        maxSouth = true;
                        break;
                    }
                    case 'W':
                    case 'w':
                    case '<': {
                        maxWest = true;
                        break;
                    }
                    case 'E':
                    case 'e':
                    case '>': {
                        maxEast = true;
                        break;
                    }
                    default: {
                        throw new IllegalArgumentException("Expected N,n,^ for 'north max border' ; S,s,_ for 'south max border' ; W,w,< for 'west max border' ; E,e,> for 'east max border'");
                    }
                }
            }
        }
        init(maxNorth, maxEast, maxSouth, maxWest, m, n, domain);
    }

    @Override
    public DoubleToDouble getReference() {
        return exprVal;
    }

    public Domain getDomain() {
        return domain;
    }

    public boolean isMaxEast() {
        return maxEast;
    }

    public boolean isMaxSouth() {
        return maxSouth;
    }

    public boolean isMaxWest() {
        return maxWest;
    }

    public DoubleParam getM() {
        return m;
    }

    public DoubleParam getN() {
        return n;
    }

    public boolean isMaxNorth() {
        return maxNorth;
    }

    //    @Override
//    public Ref forObject(Expression obj) {
//        SinSeq s = new SinSeq(obj);
//        s.maxEast = maxEast;
//        s.maxSouth = maxSouth;
//        s.maxWest = maxWest;
//        s.m = m;
//        s.n = n;
//        s.domain = domain;
//        s.maxNorth = maxNorth;
//        return s;
//    }

    @Override
    public int hashCode() {
        int result = getClass().getName().hashCode();
        result = 31 * result + (maxEast ? 1 : 0);
        result = 31 * result + (maxSouth ? 1 : 0);
        result = 31 * result + (maxWest ? 1 : 0);
        result = 31 * result + (m != null ? m.hashCode() : 0);
        result = 31 * result + (n != null ? n.hashCode() : 0);
        result = 31 * result + (domain != null ? domain.hashCode() : 0);
        result = 31 * result + (maxNorth ? 1 : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SinSeqYZ sinSeq = (SinSeqYZ) o;

        if (maxEast != sinSeq.maxEast) return false;
        if (maxNorth != sinSeq.maxNorth) return false;
        if (maxSouth != sinSeq.maxSouth) return false;
        if (maxWest != sinSeq.maxWest) return false;
        if (domain != null ? !domain.equals(sinSeq.domain) : sinSeq.domain != null) return false;
        if (m != null ? !m.equals(sinSeq.m) : sinSeq.m != null) return false;
        return n != null ? n.equals(sinSeq.n) : sinSeq.n == null;
    }

    @Override
    public Expr newInstance(Expr... subExpressions) {
        return this;
    }
    @Override
    public String toLatex() {
        throw new UnsupportedOperationException("Not Implemented toLatex for "+getClass().getName());
    }

}
