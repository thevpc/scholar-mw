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

    private static java.lang.reflect.Method OF_NAMED_TUPLE;
    private static java.lang.reflect.Method OF_NAMED_UPLET;
    private static java.lang.reflect.Method OF_UPLET_NAME;
    private static java.lang.reflect.Method OF_TUPLE;
    private static java.lang.reflect.Method OF_UPLET;

    static {
        try {
            OF_NAMED_TUPLE = NElement.class.getMethod("ofNamedTuple", String.class, NElement[].class);
        } catch (Throwable ignored) {}
        try {
            OF_NAMED_UPLET = NElement.class.getMethod("ofNamedUplet", String.class, NElement[].class);
        } catch (Throwable ignored) {}
        try {
            OF_UPLET_NAME = NElement.class.getMethod("ofUplet", String.class, NElement[].class);
        } catch (Throwable ignored) {}
        try {
            OF_TUPLE = NElement.class.getMethod("ofTuple", NElement[].class);
        } catch (Throwable ignored) {}
        try {
            OF_UPLET = NElement.class.getMethod("ofUplet", NElement[].class);
        } catch (Throwable ignored) {}
    }

    public static NElement ofNamedTuple(String name, NElement... elements) {
        try {
            if (OF_NAMED_TUPLE != null) {
                return (NElement) OF_NAMED_TUPLE.invoke(null, name, elements);
            }
            if (OF_NAMED_UPLET != null) {
                return (NElement) OF_NAMED_UPLET.invoke(null, name, elements);
            }
            if (OF_UPLET_NAME != null) {
                return (NElement) OF_UPLET_NAME.invoke(null, name, elements);
            }
        } catch (Throwable ex) {
            // fallback
        }
        net.thevpc.nuts.elem.NObjectElementBuilder b = NElement.ofObjectBuilder(name);
        for (int i = 0; i < elements.length; i++) {
            b.add(String.valueOf(i), elements[i]);
        }
        return b.build();
    }

    public static NElement ofTuple(NElement... elements) {
        try {
            if (OF_TUPLE != null) {
                return (NElement) OF_TUPLE.invoke(null, (Object) elements);
            }
            if (OF_UPLET != null) {
                return (NElement) OF_UPLET.invoke(null, (Object) elements);
            }
        } catch (Throwable ex) {
            // fallback
        }
        return NElement.ofArray(elements);
    }
}
