package net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern;

import net.thevpc.nuts.elem.NElement;
import net.thevpc.nuts.util.NBlankable;
import net.thevpc.nuts.util.NOptional;
import net.thevpc.nuts.util.NStringUtils;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 12 juil. 2007 01:32:42
 */
public enum CellBoundaries {
    /**
     * Up to Down according to X
     * Up to Up according to Y
     * <pre>
     * 11111111111111111110
     * 1                  0
     * 1                  0
     * 11111111111111111110
     * </pre>
     */
    UDxUUy,
    DDxUUy,
    DUxUUy,
    UUxUUy,

    UDxDDy,
    DDxDDy,
    DUxDDy,
    UUxDDy,

    UDxDUy,
    DDxDUy,
    DUxDUy,
    UUxDUy,

    UDxUDy,
    DDxUDy,
    DUxUDy,
    UUxUDy;

    public static CellBoundaries eval(boolean east, boolean west, boolean north, boolean south) {
        return CellBoundaries.valueOf(
                ((east) ? "U" : "D") +
                        ((west) ? "U" : "D") +
                        "x" +
                        ((north) ? "U" : "D") +
                        ((south) ? "U" : "D") +
                        "y");
    }

    public static NOptional<CellBoundaries> parse(NElement pv) {
        if(pv==null || pv.isNull()){
            return NOptional.ofNamedEmpty("CellBoundaries");
        }
        if (pv.isAnyStringOrName()) {
            String s = pv.asStringValue().get().toLowerCase();
            if(NBlankable.isBlank(s)){
                return NOptional.ofNamedEmpty("CellBoundaries");
            }
            StringBuilder sb = new StringBuilder();
            for (char c : s.toCharArray()) {
                switch (c) {
                    case 'v':
                    case 'u':
                    case '+':
                    case '1': {
                        sb.append("U");
                        break;
                    }
                    case '0':
                    case 'z':
                    case 'd':
                    case '-': {
                        sb.append("D");
                        break;
                    }
                }
            }
            switch (sb.toString()) {
                case "UUUU":
                    return NOptional.of(CellBoundaries.UUxUUy);
                case "UUUD":
                    return NOptional.of(CellBoundaries.UUxUDy);
                case "UUDU":
                    return NOptional.of(CellBoundaries.UUxDUy);
                case "UUDD":
                    return NOptional.of(CellBoundaries.UUxDDy);
                case "UDUU":
                    return NOptional.of(CellBoundaries.UDxUUy);
                case "UDUD":
                    return NOptional.of(CellBoundaries.UDxUDy);
                case "UDDU":
                    return NOptional.of(CellBoundaries.UDxDUy);
                case "UDDD":
                    return NOptional.of(CellBoundaries.UDxDDy);
                case "DUUU":
                    return NOptional.of(CellBoundaries.DUxUUy);
                case "DUUD":
                    return NOptional.of(CellBoundaries.DUxUDy);
                case "DUDU":
                    return NOptional.of(CellBoundaries.DUxDUy);
                case "DUDD":
                    return NOptional.of(CellBoundaries.DUxDDy);
                case "DDUU":
                    return NOptional.of(CellBoundaries.DDxUUy);
                case "DDUD":
                    return NOptional.of(CellBoundaries.DDxUDy);
                case "DDDU":
                    return NOptional.of(CellBoundaries.DDxDUy);
                case "DDDD":
                    return NOptional.of(CellBoundaries.DDxDDy);
            }
        }
        return NOptional.ofNamedError(pv.toString());
    }

    public boolean isEast() {
        return this.toString().charAt(0) == 'U';
    }

    public boolean isWest() {
        return this.toString().charAt(1) == 'U';
    }

    public boolean isNorth() {
        return this.toString().charAt(3) == 'U';
    }

    public boolean isSouth() {
        return this.toString().charAt(4) == 'U';
    }
}
