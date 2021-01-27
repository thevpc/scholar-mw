/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.results;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.results.results.HWSolverResult;

/**
 *
 * @author vpc
 */
public class ConfResults {

    private Map<String, ActionResults> byAction = new HashMap<>();

    public void clear(String id) {
        getActionResults(id).clear();
    }

    public void addActionResult(HWSolverResult r) {
        getActionResults(r.actionId()).add(r);
    }

    public List<HWSolverResult> getActionResults(String id) {
        return getActionResultsNode(id).all;
    }

    public ActionResults getActionResultsNode(String id) {
        ActionResults s = byAction.get(id);
        if (s == null) {
            s = new ActionResults(id);
            byAction.put(id, s);
        }
        return s;
    }

    public static class ActionResults {

        private String id;
        private List<HWSolverResult> all = new ArrayList<>();

        public ActionResults(String id) {
            this.id = id;
        }

    }
}
