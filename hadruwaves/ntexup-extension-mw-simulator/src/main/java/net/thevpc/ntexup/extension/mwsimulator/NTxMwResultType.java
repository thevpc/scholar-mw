package net.thevpc.ntexup.extension.mwsimulator;

import net.thevpc.nuts.util.NOptional;
import net.thevpc.nuts.util.NStringUtils;

public enum NTxMwResultType {
    S11,
    ZIN,
    ;

    public static NOptional<NTxMwResultType> parse(String s) {
        switch (NStringUtils.trimToNull(s).toLowerCase()) {
                case "s11":
                case "sparameters":
                case "s-parameters":
                case "sparams":
                case "s-params":
                {
                    return NOptional.of(NTxMwResultType.S11);
                }
                case "zin":
                case "inputimpedance":
                case "input-impedance":
                case "input_impedance":
                {
                    return NOptional.of(NTxMwResultType.ZIN);
                }
        }
        try {
            return NOptional.of(valueOf(s == null ? "" : s.trim().toUpperCase()));
        } catch (Exception ex) {
            return NOptional.ofNamedEmpty(s);
        }
    }
}
