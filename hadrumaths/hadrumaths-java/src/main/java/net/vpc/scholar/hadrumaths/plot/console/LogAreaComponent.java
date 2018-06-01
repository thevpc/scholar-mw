package net.vpc.scholar.hadrumaths.plot.console;

import net.vpc.common.swings.SwingUtilities3;

import javax.swing.*;
import java.awt.*;

public class LogAreaComponent {
    private JTextArea area = new JTextArea();
    private int logAreaLinesCount = 0;
    private int logAreaMaxLinesCount = 4096;

    public LogAreaComponent() {
        this(null, -1);
    }

    public LogAreaComponent(int maxLinesCount) {
        this(null, maxLinesCount);
    }

    public LogAreaComponent(JTextArea area, int maxLinesCount) {
        if (maxLinesCount <= 0) {
            maxLinesCount = 4096;
        }
        this.logAreaMaxLinesCount = maxLinesCount;
        this.area = area;
        if (this.area == null) {
            this.area = new JTextArea();
            this.area.setFont(new Font("Monospaced", 0, 12));
            this.area.setEditable(false);
        }
    }

    public JComponent toComponent() {
        return area;
    }

    public void append(final String msg) {
        SwingUtilities3.invokeLater(new Runnable() {
            public void run() {
                String[] c = msg.split("\n");
                logAreaLinesCount += c.length;
                area.append(msg + "\n");
                if (logAreaLinesCount > logAreaMaxLinesCount) {
                    char[] s = area.getText().toCharArray();
                    int i = 0;
                    int v = 0;
                    int r = logAreaLinesCount - logAreaMaxLinesCount;
                    boolean found = false;
                    while (i < s.length && v < r) {
                        if (s[i] == '\n') {
                            v++;
                            if (v == r) {
                                i++;
                                found = true;
                                break;
                            }
                        }
                        i++;
                    }
                    if (found) {
                        if (i == s.length) {
                            area.setText("");
                        } else {
                            String s2 = new String(s, i, s.length - i);
                            area.setText(s2);
                        }
                        logAreaLinesCount = logAreaMaxLinesCount;
                    } else {
                        System.out.println("Why");
                    }
                    //getLogArea().setText("");
                }
            }
        });
    }
}
