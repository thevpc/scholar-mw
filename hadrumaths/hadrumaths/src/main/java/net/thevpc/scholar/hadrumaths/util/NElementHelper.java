package net.thevpc.scholar.hadrumaths.util;

import net.thevpc.nuts.elem.NArrayElementBuilder;
import net.thevpc.nuts.elem.NElement;
import net.thevpc.nuts.elem.NElements;
import net.thevpc.nuts.elem.NToElement;
import net.thevpc.scholar.hadrumaths.Maths;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;
import java.util.function.Predicate;

public class NElementHelper {

    public static final Predicate<NElement> BLANK_ELEMENT_PREDICATE = z -> (z != null && !z.isNull() && !(z.isListContainer() && z.asListContainer().get().isEmpty()));

    public static Predicate<NElement> blankPredicate() {
        return BLANK_ELEMENT_PREDICATE;
    }

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
        if(e instanceof Collection){
            return NElement.ofArray(((Collection<?>) e).stream().map(NElementHelper::elem).toArray(NElement[]::new));
        }
        if(e instanceof Map){
            return NElement.ofObject(((Map<?,?>) e).entrySet().stream().map(ee-> NElement.ofPair(
                    elem(ee.getKey()),
                    elem(ee.getValue())
            )).toArray(NElement[]::new));
        }
        if(e.getClass().isArray()){
            NArrayElementBuilder b = NElement.ofArrayBuilder();
            int len = Array.getLength(e);
            for (int i = 0; i < len; i++) {
                b.add(elem(Array.get(e, i)));
            }
            return b.build();
        }
        return elemsStore.toElement(e);
    }
}
