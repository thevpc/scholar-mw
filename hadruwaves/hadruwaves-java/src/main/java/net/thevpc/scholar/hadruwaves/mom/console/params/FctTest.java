package net.thevpc.scholar.hadruwaves.mom.console.params;

import net.thevpc.scholar.hadrumaths.Maths;

public class FctTest {
    public int[] count;
    public net.thevpc.scholar.hadruwaves.mom.TestFunctions type;

    public FctTest(int value, net.thevpc.scholar.hadruwaves.mom.TestFunctions type) {
        this.count = new int[]{value};
        this.type = type;
    }

    public FctTest(int min, int max, net.thevpc.scholar.hadruwaves.mom.TestFunctions type) {
        this.count = Maths.isteps(min, max, 1);
        this.type = type;
    }
}
