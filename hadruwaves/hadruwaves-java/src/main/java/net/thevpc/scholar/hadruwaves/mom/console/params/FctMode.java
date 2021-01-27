package net.thevpc.scholar.hadruwaves.mom.console.params;

import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadruwaves.mom.ModeFunctions;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 14 juil. 2005 11:43:30
 */
public class FctMode {
    public int[] count;
    public ModeFunctions type;

    public FctMode(int value, ModeFunctions fnModeType) {
        this.count = new int[]{value};
        this.type = fnModeType;
    }

    public FctMode(int min, int max, int step, ModeFunctions fnModeType) {
        this.count = Maths.isteps(min, max, step);
        this.type = fnModeType;
    }

    public FctMode(int min, int max, ModeFunctions fnModeType) {
        this.count = Maths.isteps(min, max, 50);
        this.type = fnModeType;
    }
}
