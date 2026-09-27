package net.thevpc.ntexup.extension.mwsimulator;

import java.util.Locale;

/**
 * Policy controlling what a solver adapter does when the native solver does not
 * produce a usable result. The default is {@link #ERROR}: a failed solver run
 * must surface as a failure instead of being silently replaced by an
 * analytical approximation.
 */
public enum NTxSolverFallbackPolicy {

    /**
     * Fail loudly. No approximation is substituted and no curve is emitted.
     */
    ERROR,

    /**
     * Explicitly opted-in degradation: substitute a declared analytical
     * approximation. Results computed this way must be labelled as
     * approximations by the caller.
     */
    ANALYTICAL;

    public static final String OPTION = "simulation-fallback";

    public static NTxSolverFallbackPolicy parse(String value, String owner) {
        if (value == null) {
            return ERROR;
        }
        String v = value.trim().toLowerCase(Locale.ROOT);
        if (v.isEmpty() || "error".equals(v) || "fail".equals(v) || "none".equals(v)) {
            return ERROR;
        }
        if ("analytical".equals(v) || "approximation".equals(v)) {
            return ANALYTICAL;
        }
        throw new IllegalArgumentException(owner + ": unsupported '" + OPTION
                + "' value '" + value + "'. Supported values are 'error' (default) and 'analytical'.");
    }

    /**
     * Fails the run unless degradation to an analytical approximation was
     * explicitly requested.
     */
    public void requireExplicitOptIn(String owner, String reason) {
        if (this != ANALYTICAL) {
            throw new IllegalStateException(owner + ": " + reason
                    + ". Set '" + OPTION + "': 'analytical' to explicitly accept an approximation.");
        }
    }
}
