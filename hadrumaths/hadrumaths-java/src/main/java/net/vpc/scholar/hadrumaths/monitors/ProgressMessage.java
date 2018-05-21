package net.vpc.scholar.hadrumaths.monitors;

import java.util.logging.Level;

/**
 * Created by vpc on 3/20/17.
 */
public interface ProgressMessage {
    Level getLevel();

    String getText();
}
