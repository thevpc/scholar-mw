package net.vpc.scholar.hadruwaves.studio.standalone.v2;

import net.vpc.common.swings.SwingUtilities3;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HWUtils {
    public static AbstractAction action(String id, String title, String icon, ActionListener listener) {
        AbstractAction a = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (listener != null) {
                    listener.actionPerformed(e);
                }
            }
        };
        a.putValue(Action.ACTION_COMMAND_KEY, id);
        a.putValue(Action.NAME, title);
        a.putValue(Action.SHORT_DESCRIPTION, title);
        a.putValue(Action.SMALL_ICON, iconFor(icon));
        return a;
    }

    public static ImageIcon iconFor(String name) {
        if (name == null) {
            return null;
        }
        return SwingUtilities3.getScaledIcon(
                HadruwavesStudioV2.class.getResource("/net/vpc/scholar/hadruwaves/studio/standalone/v2/images/" + name + ".png"),
                16, 16
        );
    }
}
