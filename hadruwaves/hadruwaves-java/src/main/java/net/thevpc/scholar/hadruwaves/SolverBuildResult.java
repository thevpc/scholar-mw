/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwaves;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 *
 * @author vpc
 */
public class SolverBuildResult {

    private List<SolverMessage> messages = new ArrayList<SolverMessage>();
    private boolean error;
    private boolean warning;

    public void error(String message) {
        if (message != null) {
            message(new SolverMessage(Level.SEVERE, message));
        }
    }

    public void warning(String message) {
        if (message != null) {
            message(new SolverMessage(Level.SEVERE, message));
        }
    }

    public void message(SolverMessage message) {
        if (message != null) {
            if (message.getLevel().intValue() >= Level.SEVERE.intValue()) {
                error = true;
            } else if (message.getLevel().intValue() >= Level.WARNING.intValue()) {
                warning = true;
            }
            messages.add(message);
        }
    }

    public boolean isError() {
        return error;
    }

    public boolean isWarning() {
        return warning;
    }

    public List<SolverMessage> getMessages() {
        return messages;
    }

    public void requireNoError() {
        if (error) {
            throw new IllegalArgumentException("Build failed");
        }
    }

}
