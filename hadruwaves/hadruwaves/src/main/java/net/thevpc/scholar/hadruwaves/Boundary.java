package net.thevpc.scholar.hadruwaves;

import net.thevpc.nuts.elem.NElement;
import net.thevpc.nuts.util.NNameFormat;
import net.thevpc.nuts.util.NOptional;
import net.thevpc.nuts.util.NStringUtils;

public enum Boundary {
    ELECTRIC,
    MAGNETIC,
    PERIODIC,
    // Matched Load
    INFINITE,
    //Open
    OPEN,
    //Open
    NOTHING,
    ;

    public static NOptional<Boundary> parse(NElement any) {
        return parse(any.asStringValue().orElse(""));
    }

    public static NOptional<Boundary> parse(String any) {
        switch (NNameFormat.LOWER_KEBAB_CASE.format(NStringUtils.trim(any))) {
            case "magnetic":
            case "m": {
                return NOptional.of(Boundary.MAGNETIC);
            }
            case "electric":
            case "e": {
                return NOptional.of(Boundary.ELECTRIC);
            }
            case "periodic":
            case "p": {
                return NOptional.of(Boundary.PERIODIC);
            }
            case "nothing":
            case "n": {
                return NOptional.of(Boundary.NOTHING);
            }
            case "open":
            case "o": {
                return NOptional.of(Boundary.OPEN);
            }
            case "infinite":
            case "∞": {
                return NOptional.of(Boundary.INFINITE);
            }
        }
        return NOptional.<Boundary>ofNamedEmpty("boundary " + any).withDefault(() -> Boundary.NOTHING);
    }

    public static Boundary of(char c) {
        switch (c) {
            case 'M':
                return Boundary.MAGNETIC;
            case 'E':
                return Boundary.ELECTRIC;
            case 'P':
                return Boundary.PERIODIC;
            case 'I':
                return Boundary.INFINITE;
            case 'O':
                return Boundary.OPEN;
            case 'N':
                return Boundary.NOTHING;
        }
        throw new IllegalArgumentException("Unknown Wall " + c);
    }

    public static char idChar(Boundary w) {
        switch (w) {
            case ELECTRIC:
                return 'E';
            case MAGNETIC:
                return 'M';
            case PERIODIC:
                return 'P';
            case INFINITE:
                return 'I';
            case OPEN:
                return 'O';
            case NOTHING:
                return 'N';
        }
        throw new IllegalArgumentException("Wall " + w + " not supported");
    }

    public boolean is(char c) {
        return idChar() == c;
    }

    public char idChar() {
        return idChar(this);
    }
}
