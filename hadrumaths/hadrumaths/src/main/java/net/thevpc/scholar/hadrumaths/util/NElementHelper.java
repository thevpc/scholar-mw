package net.thevpc.scholar.hadrumaths.util;

import net.thevpc.nuts.elem.NElement;
import net.thevpc.nuts.elem.NElements;
import net.thevpc.nuts.elem.NToElement;
import net.thevpc.scholar.hadrumaths.Maths;

public class NElementHelper {
    public static NElement elem(Object e) {
        if (e instanceof NElement) {
            return (NElement) e;
        }
        if (e == null) {
            return NElement.ofNull();
        }
        if (e instanceof NToElement) {
            return ((NToElement) e).toElement();
        }
        NElements elemsStore = Maths.Config.getElements();
        return elemsStore.toElement(e);
    }
}
