package net.thevpc.scholar.hadrumaths;
import net.hl.lang.*;
@JExports({
        //import static methods
        "net.thevpc.scholar.hadrumaths.Maths.*",
        "net.thevpc.scholar.hadrumaths.internal.hl.HLMaths.*",
        //import api package, may be should enumerate all of them
        //anyways i think i should refactor the library to better distinguish
        //between api and impl!
        "net.thevpc.scholar.hadrumaths.*"
})
public final class Hadrumaths {
    private Hadrumaths() {
    }
}
