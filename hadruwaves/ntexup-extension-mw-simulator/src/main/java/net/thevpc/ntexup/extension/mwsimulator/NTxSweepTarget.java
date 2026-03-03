package net.thevpc.ntexup.extension.mwsimulator;

import net.thevpc.nuts.util.NOptional;
import net.thevpc.nuts.util.NStringUtils;

public enum NTxSweepTarget {
    FREQ;
    public static NOptional<NTxSweepTarget> parse(String s) {
        switch (NStringUtils.trimToNull(s).toLowerCase()) {
            case "freq":
            case "frequency":
            case "frequencies":
            {
                return NOptional.of(NTxSweepTarget.FREQ);
            }
        }
        try {
            return NOptional.of(valueOf(s == null ? "" : s.trim().toUpperCase()));
        } catch (Exception ex) {
            return NOptional.ofNamedEmpty(s);
        }
    }
}
