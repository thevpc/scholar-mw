/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.results.results;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author vpc
 */
public class HWSolverActionResultRegistry {

    public static final HWSolverActionResultRegistry DEFAULT = new HWSolverActionResultRegistry();

    private Map<String, Class<? extends HWSolverResult>> results = new HashMap<>();

    public void register(String id, Class<? extends HWSolverResult> resultType) {
        results.put(id, resultType);
    }

    public HWSolverResult createResult(String id) {
        Class r = results.get(id);
        if (r == null) {
            throw new IllegalArgumentException("Unsupported Result Id " + id);
        }
        try {
            return (HWSolverResult) r.newInstance();
        } catch (Exception ex) {
            throw new IllegalArgumentException("Unsupported Result Id " + id + " : " + ex);
        }
    }
}
