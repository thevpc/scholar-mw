package net.vpc.scholar.hadrumaths.util;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

public class SwingUtils {
    public static void invokeAndWait(final Runnable doRun) {
        if (SwingUtilities.isEventDispatchThread()) {
            doRun.run();
        } else {
            try {
                SwingUtilities.invokeAndWait(doRun);
            } catch (InterruptedException e) {
                throw new IllegalArgumentException(e);
            } catch (InvocationTargetException e) {
                throw new IllegalArgumentException(e);
            }
        }
    }

    public static void invokeLater(final Runnable doRun) {
//        if(SwingUtilities.isEventDispatchThread()){
//            doRun.run();
//        }else{
        SwingUtilities.invokeLater(doRun);
//        }
    }
}
