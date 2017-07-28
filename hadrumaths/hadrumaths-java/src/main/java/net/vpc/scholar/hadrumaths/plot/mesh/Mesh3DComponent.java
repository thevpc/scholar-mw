package net.vpc.scholar.hadrumaths.plot.mesh;


import javax.swing.*;
import java.awt.*;

class Mesh3DComponent extends JComponent {

    DefaultMesh3DModel md;
    Image backBuffer;
    Graphics backGC;
    Dimension prefSize;
    Dimension minSize;
    boolean painted;

    Mesh3DComponent(DefaultMesh3DModel model3d) {
        if (model3d == null) {
            return;
        } else {
            md = model3d;
            prefSize = new Dimension(model3d.width, model3d.height);
            minSize = new Dimension(model3d.width / 2, model3d.height / 2);
            return;
        }
    }

    public Dimension getMinimumSize() {
        return minSize;
    }

    public Dimension getPreferredSize() {
        return prefSize;
    }

    public void update(Graphics g) {
        paint(g);
    }

    public void paint(Graphics g) {
        backGC.setColor(getBackground());
        backGC.fillRect(0, 0, getSize().width, getSize().height);
        if (md != null)
            md.paint(backGC);
        g.drawImage(backBuffer, 0, 0, this);
        setPainted();
    }

    synchronized void setPainted() {
        painted = true;
        notifyAll();
    }

    synchronized void waitPainted() {
        while (!painted)
            try {
                wait();
            } catch (Exception _ex) {
            }
        painted = false;
    }

    public void setBounds(int i, int j, int k, int l) {
        super.setBounds(i, j, k, l);
        if (k <= 0) {
            k = 1;
        }
        if (k <= 0) {
            l = 1;
        }
        int theMin = 30;
        backBuffer = createImage(k < theMin ? theMin : k, l < theMin ? theMin : l);
        backGC = backBuffer.getGraphics();
        if (md != null)
            md.resize(k, l);
    }

    void releaseModel() {
        md = null;
    }
}
