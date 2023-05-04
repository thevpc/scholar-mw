/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwavesstudio.standalone.v1.editors;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;

import net.thevpc.scholar.hadruwavesstudio.standalone.v1.editors.materials.AreaMaterialEditor;
import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadruwaves.mom.project.MomProject;
import net.thevpc.scholar.hadruwaves.mom.project.areamaterial.ModalSourceMaterial;
import net.thevpc.scholar.hadruwaves.mom.project.areamaterial.PlanarSourceMaterial;
import net.thevpc.scholar.hadruwaves.mom.project.areamaterial.SurfaceImpedanceMaterial;
import net.thevpc.scholar.hadruwaves.mom.project.common.Area;

/**
 *
 * @author vpc
 */
public class DefaultAreaRenderer implements AreaRenderer {
    private MomProjectEditor application;
    public DefaultAreaRenderer(MomProjectEditor application) {
        this.application=application;
    }

    
    public void paintArea(Area area, MomProject project, Graphics2D g2d, Dimension boxSize, boolean selected) {
        paintArea(area, project, g2d, boxSize, selected, 0.4f);
    }

    public void paintArea(Area area, MomProject project, Graphics2D g2d, Dimension boxSize, boolean selected, float transparency) {
        if (project == null) {
            return;
        }
        Dimension size = boxSize;
        int xsize = size.width - 8;
        int ysize = size.height - 8;
        Domain sd=project.getDomain();
        double dx = sd.getXMin();
        double dy = sd.getYMin();
        double dw = sd.getXwidth();
        double dh = sd.getYwidth();
        if (dw < 0) {
            dx = dx + dw;
            dw = -dw;
        }
        if (dh < 0) {
            dy = dy + dh;
            dh = -dh;
        }
        Domain dd = area.getDomain();
        double ax = dd.getXMin();
        double ay = dd.getYMin();
        double aw = dd.getXwidth();
        double ah = dd.getYwidth();
        if (aw < 0) {
            ax = ax + aw;
            aw = -aw;
        }
        if (ah < 0) {
            ay = ay + ah;
            ah = -ah;
        }
        Color cc = area.getColor();
        if (cc == null) {
            AreaMaterialEditor ed = (AreaMaterialEditor) application.getUIFactory(area.getMaterial().getId());
            cc = ed.getDefaultColor();
        }
        g2d.setColor(cc);
        //Stroke oldStroke = g2d.getStroke();

        //g2d.setStroke(selected ? STROKE_BOLD : STROKE_THIN);

        int x0 = (int) ((ax - dx) / dw * xsize);
        int y0 = (int) ((ay - dy) / dh * ysize);
        int w0 = (int) (aw / dw * xsize);
        int h0 = (int) (ah / dh * ysize);
        if ((area.getMaterial() instanceof PlanarSourceMaterial || area.getMaterial() instanceof ModalSourceMaterial)) {
            g2d.drawRect(x0, y0, w0, h0);
            Color c2 = cc.darker().darker();// new Color(Integer.MAX_VALUE - cc.getRGB());
            g2d.setPaint(new GradientPaint(x0, y0, cc, x0 + (w0 / 2), y0 + (h0 / 2), c2, true));
            for (int i = 0; i < 3; i++) {
                int jj = i * 3;
                if ((w0 - (2 * jj)) > 3 && (h0 - (2 * jj)) > 3) {
                    g2d.drawOval(x0 + jj, y0 + jj, w0 - (2 * +jj), h0 - (2 * +jj));
                }
            }
            Composite oldComposite = g2d.getComposite();
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparency));
            g2d.fillRect((int) ((ax - dx) / dw * xsize),
                    (int) ((ay - dy) / dh * ysize),
                    (int) (aw / dw * xsize),
                    (int) (ah / dh * ysize));
            g2d.setComposite(oldComposite);

        } else if (area.getMaterial() instanceof SurfaceImpedanceMaterial) {
            int w1 = 8;
            int h1 = 8;
            BufferedImage bufferedImage =
                    new BufferedImage(w1, h1,
                    BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d2 = bufferedImage.createGraphics();
            g2d2.setColor(cc.darker());
            //g2d2.drawString("z",0,0);
            g2d2.drawRect(0, 0, w1, h1);
            g2d2.drawPolygon(
                    new int[]{0, w1 / 2, w1, w1 / 2},
                    new int[]{h1 / 2, 0, h1 / 2, h1},
                    4);

            TexturePaint texture =new TexturePaint(bufferedImage,new Rectangle(0, 0, w1, h1));
            g2d.setPaint(texture);
            int corners = 8;
            g2d.fillRoundRect(x0, y0, w0, h0, corners, corners);
            g2d2.setColor(cc.darker());
            g2d.drawRoundRect(x0, y0, w0, h0, corners, corners);
//            for (int i = 0; i < 3; i++) {
//                int jj = i * 3;
//                if ((w0 - jj) > 3 && (h0 - jj) > 3) {
//                    g2d.drawPolygon(
//                            new int[]{x0 + jj, x0 + w0 / 2, x0 + w0, x0 + w0 / 2},
//                            new int[]{y0 + h0 / 2, y0 + jj, y0 + h0 / 2, y0 + h0},
//                            4);
//                }
//            }

        } else {
            g2d.drawRect(x0, y0, w0, h0);
            Composite oldComposite = g2d.getComposite();
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparency));
            g2d.fillRect((int) ((ax - dx) / dw * xsize),
                    (int) ((ay - dy) / dh * ysize),
                    (int) (aw / dw * xsize),
                    (int) (ah / dh * ysize));
            g2d.setComposite(oldComposite);
        }
        g2d.setColor(cc);
        if (selected) {
            g2d.drawRect((int) ((ax - dx) / dw * xsize) + 1,
                    (int) ((ay - dy) / dh * ysize) + 1,
                    (int) (aw / dw * xsize) - 2,
                    (int) (ah / dh * ysize) - 2);
        }
    }
}
