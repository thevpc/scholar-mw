package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Maths;

import static net.vpc.scholar.hadrumaths.Maths.*;

/**
 * Created by vpc on 5/7/14.
 */
public class SinSeqXZ extends Ref implements Cloneable{
    private boolean maxEast;
    private boolean maxSouth;
    private boolean maxWest;
    private DoubleParam m;
    private DoubleParam n;
    private Domain domain;
    private boolean maxNorth;

    public SinSeqXZ(boolean maxNorth, boolean maxEast, boolean maxSouth, boolean maxWest, DoubleParam m, DoubleParam n, Domain domain) {
        init(maxNorth, maxEast, maxSouth, maxWest, m, n, domain);
    }

    public SinSeqXZ(String borders, DoubleParam m, DoubleParam n, Domain domain) {
        boolean maxNorth=false;
        boolean maxEast=false;
        boolean maxSouth=false;
        boolean maxWest=false;
        if(borders!=null){
            for (char c : borders.toCharArray()) {
                switch (c){
                    case 'N':
                    case 'n':
                    case '^':
                    {
                        maxNorth=true;
                        break;
                    }
                    case 'S':
                    case 's':
                    case '_':
                    {
                        maxSouth=true;
                        break;
                    }
                    case 'W':
                    case 'w':
                    case '<':
                    {
                        maxWest=true;
                        break;
                    }
                    case 'E':
                    case 'e':
                    case '>':
                    {
                        maxEast=true;
                        break;
                    }
                    default:{
                        throw new IllegalArgumentException("Expected N,n,^ for 'north max border' ; S,s,_ for 'south max border' ; W,w,< for 'west max border' ; E,e,> for 'east max border'");
                    }
                }
            }
        }
        init(maxNorth, maxEast, maxSouth, maxWest, m, n, domain);
    }

    private void init(boolean maxNorth, boolean maxEast, boolean maxSouth, boolean maxWest, DoubleParam m, DoubleParam n, Domain domain){
        this.maxEast = maxEast;
        this.maxSouth = maxSouth;
        this.maxWest = maxWest;
        this.m = m;
        this.n = n;
        this.domain = domain;
        this.maxNorth = maxNorth;
        Expr fx = null;
        Expr one = expr(1);
        Expr two = expr(2);
        Expr pi = expr(PI);
        Expr width = expr(domain.xwidth());
        Expr height = expr(domain.zwidth());
        Expr xmin = expr(domain.xmin());
        Expr zmin = expr(domain.zmin());
        Expr pibyw = div(pi, width);
        Expr pibyh = div(pi, height);
        if (maxWest) {
            if (maxEast) {
                //cos(2 * m * (pi / width) * (X-min))
                fx = cos(mul(two, m, pibyw, sub(X, xmin))); //ok
            } else {
                //cos((m * 2+1)/2 * pi / width * (X-min))
                fx = cos(mul(div(sum(mul(two, m), one), two), pibyw, sub(X, xmin))); //ok
            }
        } else {
            if (maxEast) {
                //fx = sin(((2 * m + 1) / 2 * PI / domain.width) * (X - domain.xmin)); //ok
                fx = sin(mul(div(sum(mul(two, m), one), two), pibyw, sub(X, xmin))); //ok
            } else {
//                fx = sin(((m+1) * PI / domain.width) * (X - domain.xmin)); //ok
                fx = sin(mul(sum(m, one), pibyw, sub(X, xmin))); //ok
            }
        }
        Expr fz = null;
        if (maxNorth) {
            if (maxSouth) {
//                fy = cos((n * PI / domain.Height) * (Y - domain.Ymin)); //ok
                fz = cos(mul(n, pibyh, sub(Z, zmin))); //ok
            } else {
//                fy = cos(((2 * n + 1) / 2 * PI / domain.Height) * (Y - domain.Ymin)); //ok
                fz = cos(mul(div(sum(mul(two, n), one), two), pibyh, sub(Z, zmin))); //ok
            }
        } else {
            if (maxSouth) {
                //fy = sin(((2 * n + 1) / 2 * PI / domain.Height) * (Y - domain.Ymin)); //ok
                fz = sin(mul(div(sum(mul(two, n), one), two), pibyh, sub(Z, zmin))); //ok
            } else {
                //fy = sin(((n+1) * PI / domain.Height) * (Y - domain.Ymin)); //ok
                fz = cos(mul(sum(n, one), pibyh, sub(Z, zmin))); //ok
            }
        }
        init(mul(fx, fz, Maths.expr(domain)));
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

    public Domain getDomainImpl() {
        return domain;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        SinSeqXZ sinSeq = (SinSeqXZ) o;

        if (maxEast != sinSeq.maxEast) return false;
        if (maxNorth != sinSeq.maxNorth) return false;
        if (maxSouth != sinSeq.maxSouth) return false;
        if (maxWest != sinSeq.maxWest) return false;
        if (domain != null ? !domain.equals(sinSeq.domain) : sinSeq.domain != null) return false;
        if (m != null ? !m.equals(sinSeq.m) : sinSeq.m != null) return false;
        if (n != null ? !n.equals(sinSeq.n) : sinSeq.n != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (maxEast ? 1 : 0);
        result = 31 * result + (maxSouth ? 1 : 0);
        result = 31 * result + (maxWest ? 1 : 0);
        result = 31 * result + (m != null ? m.hashCode() : 0);
        result = 31 * result + (n != null ? n.hashCode() : 0);
        result = 31 * result + (domain != null ? domain.hashCode() : 0);
        result = 31 * result + (maxNorth ? 1 : 0);
        return result;
    }

}
