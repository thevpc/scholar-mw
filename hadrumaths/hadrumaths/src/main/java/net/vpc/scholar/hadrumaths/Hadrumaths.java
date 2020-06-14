package net.vpc.scholar.hadrumaths;

import net.vpc.hadralang.stdlib.JExports;

@JExports({
        //import static methods
        "net.vpc.scholar.hadrumaths.Maths.*",
        "net.vpc.scholar.hadrumaths.internal.hl.HLMaths.*",
        //import api package, may be should enumerate all of them
        //anyways i think i should refactor the library to better distinguish
        //between api and impl!
        "net.vpc.scholar.hadrumaths.*"
})
public final class Hadrumaths {
    private Hadrumaths() {
    }
}
