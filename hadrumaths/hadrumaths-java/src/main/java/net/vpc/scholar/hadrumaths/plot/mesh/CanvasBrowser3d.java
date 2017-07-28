package net.vpc.scholar.hadrumaths.plot.mesh;


import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

class CanvasBrowser3d extends Mesh3DComponent
        implements MouseListener, MouseMotionListener {

    Mesh3DPlot br;
    int prevx;
    int prevy;
    float xtheta;
    float ytheta;

    CanvasBrowser3d(Mesh3DPlot meshbrowser) {
        super(meshbrowser.model3d);
        br = meshbrowser;
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public void mousePressed(MouseEvent mouseevent) {
        prevx = mouseevent.getX();
        prevy = mouseevent.getY();
    }


    private void rotateXY(MouseEvent mouseevent) {
        int i = mouseevent.getX();
        int j = mouseevent.getY();

        double xrot0 = (double) (prevy - j) * (360D / (double) getSize().width);
        double yrot0 = (double) (i - prevx) * (360D / (double) getSize().height);
        double zrot0 = 0;
        super.md.addRotation(xrot0, yrot0, zrot0);
    }

    private void rotateZ(MouseEvent mouseevent) {
//        int i = mouseevent.getX();
        int j = mouseevent.getY();

        double xrot0 = 0;
        double yrot0 = 0;
        double zrot0 = (double) (prevy - j) * (360D / (double) getSize().width);
        super.md.addRotation(xrot0, yrot0, zrot0);
    }

    private void rotateY(MouseEvent mouseevent) {
        int i = mouseevent.getX();
//        int j = mouseevent.getY();

        double xrot0 = 0;
        double yrot0 = (double) (i - prevx) * (360D / (double) getSize().height);
        double zrot0 = 0;
        super.md.addRotation(xrot0, yrot0, zrot0);
    }

    private void rotateX(MouseEvent mouseevent) {
//        int i = mouseevent.getX();
        int j = mouseevent.getY();

        double xrot0 = (double) (prevy - j) * (360D / (double) getSize().width);
        double yrot0 = 0;
        double zrot0 = 0;
        super.md.addRotation(xrot0, yrot0, zrot0);
    }

    private void rotateYZ(MouseEvent mouseevent) {
        int i = mouseevent.getX();
        int j = mouseevent.getY();

        double xrot0 = 0;
        double yrot0 = (double) (prevy - j) * (360D / (double) getSize().width);
        double zrot0 = (double) (i - prevx) * (360D / (double) getSize().height);
        super.md.addRotation(xrot0, yrot0, zrot0);
    }

    private void rotateXZ(MouseEvent mouseevent) {
        int i = mouseevent.getX();
        int j = mouseevent.getY();

        double xrot0 = (double) (prevy - j) * (360D / (double) getSize().width);
        double yrot0 = 0;
        double zrot0 = (double) (i - prevx) * (360D / (double) getSize().height);
        super.md.addRotation(xrot0,
                yrot0,
                zrot0);
    }

    private void translate(MouseEvent mouseevent) {
        int i = mouseevent.getX();
        int j = mouseevent.getY();

        super.md.addTranslation(((double) (i - prevx) / (double) getSize().width) * super.md.bb.getMaxSize(), // getWidth(),
                ((double) (prevy - j) / (double) getSize().height) * super.md.bb.getMaxSize(), ////getHeight(),
                0.0D);
    }

    private void zoom(MouseEvent mouseevent) {
        int i = mouseevent.getX();

        double d1 = 1.0D + (double) (i - prevx) / (double) getSize().width;
        if (super.md.persp) {
            super.md.minScale *= d1;
            super.md.maxScale *= d1;
            super.md.computeMatrix();
        } else {
            super.md.addScale(d1, d1, d1);
        }
    }

    public void mouseDragged(MouseEvent mouseevent) {
        int i = mouseevent.getX();
        int j = mouseevent.getY();
        boolean rightMouseButton = SwingUtilities.isRightMouseButton(mouseevent);
        boolean leftMouseButton = SwingUtilities.isLeftMouseButton(mouseevent);
//        boolean middleMouseButton = SwingUtilities.isMiddleMouseButton(mouseevent);
        int modifiersEx = mouseevent.getModifiersEx();
        if (
                leftMouseButton &&
                        (modifiersEx & MouseEvent.SHIFT_DOWN_MASK) == 0
                        &&
                        (modifiersEx & MouseEvent.CTRL_DOWN_MASK) == 0
                        &&
                        (modifiersEx & MouseEvent.ALT_DOWN_MASK) == 0
                ) {
            translate(mouseevent);
        } else if (

                (modifiersEx & MouseEvent.SHIFT_DOWN_MASK) == 0
                        &&
                        (modifiersEx & MouseEvent.CTRL_DOWN_MASK) == MouseEvent.CTRL_DOWN_MASK
                        &&
                        (modifiersEx & MouseEvent.ALT_DOWN_MASK) == 0
                ) {
            if (rightMouseButton) {
                rotateX(mouseevent);
            } else {
                rotateXY(mouseevent);
            }
        } else if (
                (modifiersEx & MouseEvent.SHIFT_DOWN_MASK) == MouseEvent.SHIFT_DOWN_MASK
                        &&
                        (modifiersEx & MouseEvent.CTRL_DOWN_MASK) == MouseEvent.CTRL_DOWN_MASK
                        &&
                        (modifiersEx & MouseEvent.ALT_DOWN_MASK) == 0
                ) {
            if (rightMouseButton) {
                rotateY(mouseevent);
            } else {
                rotateYZ(mouseevent);
            }
        } else if (
                (modifiersEx & MouseEvent.SHIFT_DOWN_MASK) == MouseEvent.SHIFT_DOWN_MASK
                        &&
                        (modifiersEx & MouseEvent.CTRL_DOWN_MASK) == MouseEvent.CTRL_DOWN_MASK
                        &&
                        (modifiersEx & MouseEvent.ALT_DOWN_MASK) == MouseEvent.ALT_DOWN_MASK
                ) {
            if (rightMouseButton) {
                rotateZ(mouseevent);
            } else {
                rotateXZ(mouseevent);
            }
        } else if (
                leftMouseButton &&
                        (modifiersEx & MouseEvent.SHIFT_DOWN_MASK) == MouseEvent.SHIFT_DOWN_MASK
                        &&
                        (modifiersEx & MouseEvent.CTRL_DOWN_MASK) == 0
                        &&
                        (modifiersEx & MouseEvent.ALT_DOWN_MASK) == 0
                ) {
            zoom(mouseevent);
        }
        if (super.painted) {
            super.painted = false;
            repaint();
        }
        prevx = i;
        prevy = j;
    }

    public void mouseClicked(MouseEvent mouseevent) {
    }

    public void mouseReleased(MouseEvent mouseevent) {
    }

    public void mouseEntered(MouseEvent mouseevent) {
    }

    public void mouseExited(MouseEvent mouseevent) {
    }

    public void mouseMoved(MouseEvent mouseevent) {
    }
}
