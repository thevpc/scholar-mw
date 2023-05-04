/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruplot.extension;

import net.thevpc.scholar.hadruplot.console.PlotConsole;
import net.thevpc.scholar.hadruplot.console.PlotConsoleWindowManager;
import net.thevpc.scholar.hadruplot.containers.CustomContainerWindowManager;
import net.thevpc.scholar.hadruplot.containers.FrameWindowManager;
import net.thevpc.scholar.hadruplot.PlotWindowManager;
import net.thevpc.scholar.hadruplot.containers.ScatteredFramesWindowManager;

/**
 * @author vpc
 */
public class PlotWindowManagerFactory {

    public static PlotWindowManager create(String name) {
        if (name == null) {
            name = "";
        }
        name = name.toLowerCase().trim();
        switch (name) {
            case "win":
            case "window":
            case "frame": {
                return new FrameWindowManager();
            }
            case "wins":
            case "windows":
            case "frames":
            case "scattered":
            case "scatter": {
                return new FrameWindowManager("wins");
            }
            case "console":
            case "con": {
                return new PlotConsoleWindowManager();
            }
        }
        return new FrameWindowManager(name);
    }
}
