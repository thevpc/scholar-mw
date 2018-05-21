package net.vpc.scholar.hadrumaths.plot.swings;

/**
 * @author vpc
 * Date: 21 janv. 2005
 * Time: 00:08:44
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.text.MessageFormat;

/**
 * Tracks Memory allocated & used, displayed as icon tray .
 *
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime 13 juil. 2006 22:14:21
 */
public class MemoryUseIconTray extends JComponent {
    private static int MO = 1024 * 1024;

    public Timer timer;
    private int iconTrayHeight = 20;
    private int iconTrayWidth = 12;
    private Dimension s = new Dimension(iconTrayWidth + 1, iconTrayHeight + 1);

    private Color textForeground = Color.WHITE;
    private int period = 1000;
    private float H = 134f / 240f;
    private float S = 203f / 240f;
    private float B = 125f / 240f;
    private boolean iconified = true;
    private boolean mouseOver;
    private boolean mouseClicked;
    private String popupFormat = "<HTML><CENTER><I><U>Memory Use</U></I></CENTER><BR>Used: <B>{0}M</B> of <B>{1}M</B><BR>Max : <B>{2}M</B><BR><B><Font color=blue>Double-click to free</Font></B></HTML>";
    private String memoryIconTrayFormat = "{0}M of {1}M";
//    private Font strfont = new Font("verdana", Font.PLAIN, 8);

    public void setIconified(boolean i) {
        iconified = i;
        if (iconified) {
            iconTrayHeight = 18;
            iconTrayWidth = 12;
            s = new Dimension(iconTrayWidth + 1, iconTrayHeight + 1);
        } else {
            iconTrayHeight = 18;
            iconTrayWidth = 12 * 8;
            s = new Dimension(iconTrayWidth + 1, iconTrayHeight + 1);
        }
        repaint();
    }

    public void start() {
        if (timer == null) {
            timer = new Timer(period, new Updater());
            timer.start();
        }
    }

    public synchronized void stop() {
        if (timer != null) {
            timer = null;
            notify();
        }
    }

    public void addNotify() {
        super.addNotify();
        start();
    }

    public void removeNotify() {
        super.removeNotify();
        //Configuration.getConfiguration().setIntProperty("memoryAnalyzer.period",period);
        stop();
    }

    public Dimension getPreferredSize() {
        return s;
    }

    public Dimension getMaximumSize() {
        return s;
    }

    public Dimension getMinimumSize() {
        return s;
    }

    public String getToolTipText(MouseEvent event) {
        Runtime runtime = Runtime.getRuntime();
        float _maxMem = runtime.maxMemory();
        float _totalMem = runtime.totalMemory();
        float _freeMem = runtime.freeMemory();
        float totalM = _maxMem / MO;
        return MessageFormat.format(popupFormat,
                ((int) ((_totalMem - _freeMem) / MO)),
                ((int) (_totalMem / MO)),
                ((int) totalM)
        );
    }

    public void paint(Graphics g) {
        Runtime runtime = Runtime.getRuntime();
        float _maxMem = runtime.maxMemory();
        float _totalMem = runtime.totalMemory();
        float _freeMem = runtime.freeMemory();
        float _globFreeMem = _freeMem + _maxMem - _totalMem;
        float rapport = _globFreeMem / _maxMem;
        float totalM = _maxMem / MO;

        Color baseColor = new Color(Color.HSBtoRGB(H * (rapport), S, B));
        Color brighterColor = new Color(Color.HSBtoRGB(H * (rapport), S, (B + 80f / 240f)));
        Color darkerColor = new Color(Color.HSBtoRGB(H * (rapport), S, (B - 80f / 240f)));
        g.setColor(baseColor);
        g.fillRect(1, 1, iconTrayWidth - 1, iconTrayHeight - 1);
        g.setColor(darkerColor);
        if (iconified) {
            int separation = (int) (1 + (iconTrayHeight - 2) * rapport);
            for (int i = 0; i < separation; i += 2) {
                g.drawLine(1, i, iconTrayWidth - 1, i);
            }
            g.setColor(brighterColor);
            int st = (int) (1 + (iconTrayHeight - 2) * rapport);
            g.fillRect(1, st, iconTrayWidth - 1, iconTrayHeight - st);
        } else {
            int separation = (int) (1 + (iconTrayWidth - 2) * rapport);
            int startingPoint = (int) (1 + (iconTrayWidth - 2) * (1 - rapport));
            for (int i = startingPoint; i < separation + startingPoint + 1; i += 2) {
                g.drawLine(+i, 1, i, iconTrayHeight - 1);
            }
            g.setColor(brighterColor);
            g.fillRect(1, 1, (int) (1 + (iconTrayWidth - 2) * (1 - rapport)), iconTrayHeight - 1);

            String str = MessageFormat.format(memoryIconTrayFormat, new Object[]{
                    String.valueOf((int) ((_totalMem - _freeMem) / MO)),
                    String.valueOf((int) (_totalMem / MO)),
                    String.valueOf((int) totalM)});
            Rectangle2D r = g.getFont().getStringBounds(str, ((Graphics2D) g).getFontRenderContext());
            int w = (int) r.getWidth();
            g.setColor(textForeground.darker().darker()/* Color.GRAY*/);
            g.drawString(str, (iconTrayWidth - w) / 2 + 1, 14 + 1);
            g.setColor(textForeground);
            g.drawString(str, (iconTrayWidth - w) / 2, 14);
        }
        if (mouseClicked) {
            g.setColor(Color.red);
            g.drawRect(0, 0, iconTrayWidth, iconTrayHeight);
        } else {
            if (mouseOver) {
                g.setColor(Color.white);
                g.drawRect(0, 0, iconTrayWidth, iconTrayHeight);
            } else {
                g.setColor(Color.black);
                g.drawRect(0, 0, iconTrayWidth, iconTrayHeight);
            }
        }
    }

    public MemoryUseIconTray(boolean icon) {
        super();
        ToolTipManager.sharedInstance().registerComponent(this);
        setIconified(icon);
        addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
                    Runtime.getRuntime().gc();
                } else if (e.getClickCount() == 2 && SwingUtilities.isRightMouseButton(e)) {
                    setIconified(!iconified);
                }
                repaint();
            }

            public void mousePressed(MouseEvent e) {
                if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
                    mouseClicked = true;
                }
                repaint();
            }

            public void mouseReleased(MouseEvent e) {
                mouseClicked = false;
                repaint();
            }

            public void mouseEntered(MouseEvent e) {
                mouseOver = true;
                repaint();
            }

            public void mouseExited(MouseEvent e) {
                mouseOver = false;
                repaint();
            }
        });
    }

    private class Updater implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            repaint();
        }
    }
}
