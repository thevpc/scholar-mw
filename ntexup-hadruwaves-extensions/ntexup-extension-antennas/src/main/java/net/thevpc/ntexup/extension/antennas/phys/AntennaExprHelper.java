package net.thevpc.ntexup.extension.antennas.phys;

import net.thevpc.ntexup.api.renderer.NTxRendererContext;
import net.thevpc.nuts.expr.*;
import net.thevpc.nuts.math.NDoubleFunction;
import net.thevpc.nuts.text.NMsg;
import net.thevpc.nuts.util.NOptional;

import java.util.HashMap;
import java.util.Map;

public class AntennaExprHelper {

    /**
     * Compiles a mathematical expression into a function of theta (radians).
     * Available variables in the expression:
     * - theta: angle in radians
     * - deg: angle in degrees
     * - PI: Math.PI
     * - lambda: wavelength (if provided)
     * - d: element spacing (if provided)
     * - beta: phase constant (2*PI/lambda)
     * - alpha: progressive phase shift (if provided)
     */
    public static NDoubleFunction compileAfFunction(NTxRendererContext ctx, String formula, double lambda, double d, double alpha) {
        if (formula == null || formula.trim().isEmpty()) {
            return null;
        }

        try {
            // 1. Create a mutable context with global constants
            NExprContextBuilder baseCtx = NExprContextBuilder.of();
            baseCtx.declareBuiltins();
            baseCtx.declareMathConstants();
            baseCtx.declareMathFunctions();
            baseCtx.declarePhysicsConstants();
            // Add standard constants
            baseCtx.declareVar(NExprVar.ofConst("PI", Math.PI));
            baseCtx.declareVar(NExprVar.ofConst("pi", Math.PI));

            // Add antenna parameters if useful for the formula
            if (lambda > 0) {
                baseCtx.declareVar(NExprVar.ofConst("lambda", lambda));
                baseCtx.declareVar(NExprVar.ofConst("λ", lambda));
                baseCtx.declareVar(NExprVar.ofConst("beta", 2 * Math.PI / lambda));
                baseCtx.declareVar(NExprVar.ofConst("β", 2 * Math.PI / lambda));
            }
            if (d > 0) {
                baseCtx.declareVar(NExprVar.ofConst("d", d));
            }
            baseCtx.declareVar(NExprVar.ofConst("d", d));
            baseCtx.declareVar(NExprVar.ofConst("alpha", alpha));
            baseCtx.declareVar(NExprVar.ofConst("α", alpha));

            // 2. Parse the expression
            NExprContext exctx = baseCtx.build();
            NOptional<NExprNode> parsed = exctx.parse(formula);
            if (!parsed.isPresent()) {
                ctx.log(NMsg.ofC("Failed to parse AF formula '%s': %s", formula, parsed.message()));
                return null;
            }

            NExprNode node = parsed.get();

            // 3. Return a function that evaluates the node for a given theta
            return (thetaRad) -> {
                try {
                    // Create a child context for this specific evaluation
                    NExprContext evalCtx = exctx.childContext()
                            .declareVar(NExprVar.ofConst("theta", thetaRad))
                            .declareVar(NExprVar.ofConst("θ", thetaRad))
                            .declareVar(NExprVar.ofConst("deg", Math.toDegrees(thetaRad)))
                            .build();

                    NOptional<Object> result = node.eval(evalCtx);
                    if (result.isPresent()) {
                        Object val = result.get();
                        if (val instanceof Number) {
                            return ((Number) val).doubleValue();
                        }
                    }
                } catch (Exception e) {
                    // Log error but return 0 to avoid crashing the render
                    ctx.log(NMsg.ofC("Error evaluating AF at theta=%.2f: %s", thetaRad, e.getMessage()));
                }
                return 0.0;
            };

        } catch (Exception e) {
            ctx.log(NMsg.ofC("Compilation error for AF formula '%s': %s", formula, e.getMessage()));
            return null;
        }
    }
}
