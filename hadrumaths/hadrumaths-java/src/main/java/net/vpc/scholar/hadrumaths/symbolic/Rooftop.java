package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.*;

import static net.vpc.scholar.hadrumaths.Maths.*;

/**
 * Created by vpc on 5/7/14.
 */
public class Rooftop extends Ref implements Cloneable{
    private static final long serialVersionUID = 1L;
    private boolean maxEast;
    private boolean maxSouth;
    private boolean maxWest;
    private boolean xside;
    private boolean yside;
    private int nx;
    private int ny;
    private Domain d;
    private boolean maxNorth;

    public Rooftop(boolean maxNorth, boolean maxEast, boolean maxSouth, boolean maxWest, boolean xside, boolean yside, int nx, int ny, Domain domain) {
        init(maxEast, maxEast, maxSouth, maxWest, xside,yside,nx, ny, domain);
    }

    @TestInfo("DisableRandomCalls")
    public Rooftop(String borders, int nx, int ny, Domain domain) {
        boolean maxNorth = false;
        boolean maxEast = false;
        boolean maxSouth = false;
        boolean maxWest = false;
        boolean x = false;
        boolean y = false;
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
                    case 'X':
                    case 'x': {
                        x = true;
                        break;
                    }
                    case 'Y':
                    case 'y': {
                        y = true;
                        break;
                    }
                    default: {
                        throw new IllegalArgumentException("Expected N,n,^ for 'north max border' ; S,s,_ for 'south max border' ; W,w,< for 'west max border' ; E,e,> for 'east max border'");
                    }
                }
            }
        }
        init(maxNorth, maxEast, maxSouth, maxWest, x, y, nx, ny, domain);
    }


    private void init(boolean maxNorth, boolean maxEast, boolean maxSouth, boolean maxWest, boolean xside, boolean yside, int nx, int ny, Domain d) {
        //  def rooftopSequence(pattern: String, d: DomainXY, nx: Int, ny: Int): ExprArrayList = {
        this.maxNorth=maxNorth;
        this.maxSouth=maxSouth;
        this.maxWest=maxWest;
        this.maxEast=maxEast;
        this.xside=xside;
        this.nx=nx;
        this.ny=ny;
        this.d=d;
        TList<Expr> list = Maths.elist();
        if (xside && yside) {
            throw new Error("Not yet supported");
        } else if (xside) {
            int m0 = 0;
            double w = d.xwidth() * 2.0 / (nx + 1);
            if (maxWest) {
                m0 = (-1);
                Domain d1 = Domain.forBounds((m0 / 2.0 * w + d.xmin()), ((m0 / 2.0 + 1) * w + d.xmin()), d.ymin(), d.ymax());
                Expr rooftop = rooftopPartX(false, d1).setProperties(
                        MapUtils.<String,Object>linkedMap(
                                "SequenceType", "Rooftop",
                                "SequenceIndex", "" + (list.size()),
                                "SequencePattern", "E"
                        )
                );
                list.append(rooftop);
            }
            m0 = 0;
            while (m0 < nx) {
                Domain d0 = Domain.forBounds(m0 / 2.0 * w + d.xmin(), ((m0 / 2.0 + 1) * w + d.xmin()), d.ymin(), d.ymax());
                Expr rooftop = rooftop(true, false, d0).setProperties(
                        MapUtils.<String,Object>linkedMap(
                                "SequenceType", "Rooftop",
                                "SequenceIndex", "" + (list.size()+1),
                                "SequencePattern", "X"
                        )
                );
                list.append(rooftop);
                m0 += 1;
            }
            if (maxEast) {
                m0 = nx;
                Domain d1 = Domain.forBounds((m0 / 2.0 * w + d.xmin()), ((m0 / 2.0 + 1) * w + d.xmin()), d.ymin(), d.ymax());
                Expr rooftop = rooftopPartX(true, d1).setProperties(
                        MapUtils.<String,Object>linkedMap(
                                "SequenceType", "Rooftop",
                                "SequenceIndex", "" + (list.size()+1),
                                "SequencePattern", "E"
                        )
                );
                list.append(rooftop);
            }
        } else if (yside) {
            int m0 = 0;
            double h = d.ywidth() * 2.0 / (ny + 1);
            if (maxNorth) {
                m0 = (-1);
                Domain d1 = Domain.forBounds(d.xmin(), d.xmax(), (m0 / 2.0 * h + d.ymin()), ((m0 / 2.0 + 1) * h + d.ymin()));
                Expr rooftop = rooftopPartY(false, d1).setProperties(
                        MapUtils.<String,Object>linkedMap(
                                "SequenceType", "Rooftop",
                                "SequenceIndex", "" + (list.size()+1),
                                "SequencePattern", "N"
                        )
                );
                list.append(rooftop);
            }
            m0 = 0;
            while (m0 < ny) {
                Domain d0 = Domain.forBounds(d.xmin(), d.xmax(), (m0 / 2.0 * h + d.ymin()), ((m0 / 2.0 + 1) * h + d.ymin()));
                Expr rooftop = rooftop(false, true, d0).setProperties(
                        MapUtils.<String,Object>linkedMap(
                                "SequenceType", "Rooftop",
                                "SequenceIndex", "" + (list.size()+1),
                                "SequencePattern", "Y"
                        )
                );
                list.append(rooftop);
                m0 += 1;
            }
            if (maxSouth) {
                m0 = ny;
                Domain d1 = Domain.forBounds(d.xmin(), d.xmax(), (m0 / 2.0 * h + d.ymin()), ((m0 / 2.0 + 1) * h + d.ymin()));
                Expr rooftop = rooftopPartY(true, d1).setProperties(
                        MapUtils.<String,Object>linkedMap(
                                "SequenceType", "Rooftop",
                                "SequenceIndex", "" + (list.size()+1),
                                "SequencePattern", "S"
                        )
                );
                list.append(rooftop);
            }

        } else {
            throw new RuntimeException("Rooftop Pattern should include either X or Y");
        }
        init(Maths.esum(list));
    }

    private Expr rooftopPartX(boolean asc, Domain domain) {
        Expr amp = expr(1);

        Expr wo2a = any((domain.xwidth() / 2)).mul(amp);
        if (asc) {
            return ((any(X).sub(domain.xmin())).div(wo2a)).mul(expr(Domain.forBounds(domain.xmin(), domain.getCenterX(), domain.ymin(), domain.ymax())));
        } else {
            return ((any(domain.xmax()).sub(X)).div(wo2a)).mul(expr(Domain.forBounds(domain.getCenterX(), domain.xmax(), domain.ymin(), domain.ymax())));
        }
    }

    private Expr rooftopPartY(boolean asc, Domain domain) {
        Expr amp = expr(1);

        Expr wo2a = any((domain.ywidth() / 2)).mul(amp);
        if (asc) {
            return ((any(Y).sub(domain.ymin())).div(wo2a)).mul(expr(Domain.forBounds(domain.xmin(), domain.xmax(), domain.ymin(), domain.getCenterY())));
        } else {
            return ((any(domain.ymax()).sub(Y)).div(wo2a)).mul(expr(Domain.forBounds(domain.xmin(), domain.xmax(), domain.getCenterY(), domain.ymax())));
        }
    }

    private Expr rooftop(boolean x, boolean y, Domain domain) {
        Expr px = Maths.sum(rooftopPartX(true, domain), rooftopPartX(false, domain));
        Expr py = Maths.sum(rooftopPartY(true, domain), rooftopPartY(false, domain));
        if (x && y) {
            return Maths.mul(px, py);
        } else if (x) {
            return px;
        } else if (y) {
            return py;
        }
        throw new IllegalArgumentException("Unsupported");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Rooftop rooftop = (Rooftop) o;

        if (maxEast != rooftop.maxEast) return false;
        if (maxNorth != rooftop.maxNorth) return false;
        if (maxSouth != rooftop.maxSouth) return false;
        if (maxWest != rooftop.maxWest) return false;
        if (nx != rooftop.nx) return false;
        if (ny != rooftop.ny) return false;
        if (xside != rooftop.xside) return false;
        if (yside != rooftop.yside) return false;
        if (d != null ? !d.equals(rooftop.d) : rooftop.d != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (maxEast ? 1 : 0);
        result = 31 * result + (maxSouth ? 1 : 0);
        result = 31 * result + (maxWest ? 1 : 0);
        result = 31 * result + (xside ? 1 : 0);
        result = 31 * result + (yside ? 1 : 0);
        result = 31 * result + nx;
        result = 31 * result + ny;
        result = 31 * result + (d != null ? d.hashCode() : 0);
        result = 31 * result + (maxNorth ? 1 : 0);
        return result;
    }

}
