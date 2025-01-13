/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwaves;

import java.util.logging.Level;

/**
 *
 * @author vpc
 */
public class SolverMessage {

    private final Level level;
    private final String message;

    public SolverMessage(Level level, String message) {
        this.level = level;
        this.message = message;
    }

    public Level getLevel() {
        return level;
    }

    public String getMessage() {
        return message;
    }

}
