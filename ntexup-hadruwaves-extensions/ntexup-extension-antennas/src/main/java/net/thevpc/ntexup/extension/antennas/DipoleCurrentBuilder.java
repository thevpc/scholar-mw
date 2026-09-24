package net.thevpc.ntexup.extension.antennas;

import net.thevpc.ntexup.api.document.elem2d.NTxBounds2D;
import net.thevpc.ntexup.api.document.elem2d.NTxPoint2D;
import net.thevpc.ntexup.api.engine.NTxNodeBuilderContext;
import net.thevpc.ntexup.api.eval.NTxValue;
import net.thevpc.ntexup.api.extension.NTxNodeBuilder;
import net.thevpc.ntexup.api.document.node.NTxNode;
import net.thevpc.ntexup.api.document.style.NTxPropName;
import net.thevpc.ntexup.api.renderer.NTxGraphics;
import net.thevpc.ntexup.api.renderer.NTxRendererContext;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.GeneralPath;

/**
 * Dipole current visualization component for ntexup-antennas extension.
 * Visualizes the sinusoidal standing current distribution of a center-fed dipole.
 */
public class DipoleCurrentBuilder implements NTxNodeBuilder {

    @Override
    public void build(NTxNodeBuilderContext builderContext) {
        builderContext
                .id("dipole-current")
                .parseParam()
                    .matchesNamedPair("length", "animate-period")
                .end()
                .renderComponent(this::render);
    }

    private void render(NTxRendererContext rendererContext) {
        NTxNode node = rendererContext.node();

        // Read properties with defaults
        double length = NTxValue.of(node.getPropertyValue("length")).asDouble().orElse(0.5);
        long animatePeriodMs = NTxValue.of(node.getPropertyValue("animate-period")).asDouble().orElse(2000.0).longValue();

        // Get colors
        Paint currentPaint = rendererContext.getLineColor(true); // Uses the 'color' common property
        Paint wirePaint = NTxValue.of(node.getPropertyValue("wire-color"))
                .asPaint()
                .orElse(Color.DARK_GRAY); // Sensible default for wire color

        // Animation logic - following the pattern from NTxPlot2DData.java
        double timeFactor = 1.0; // Default for print mode (cos(ωt) = 1)
        if (rendererContext.isAnimate()) {
            long pageStartTime = rendererContext.pageStartTime();
            long now = System.currentTimeMillis();
            long elapsed = now - pageStartTime;
            double phase = 2 * Math.PI * ((elapsed % animatePeriodMs) / (double) animatePeriodMs);
            timeFactor = Math.cos(phase); // Oscillates between -1 and 1
        }

        // Get bounds for positioning - use selfBounds2D for the component's display area
        NTxBounds2D bounds = rendererContext.selfBounds2D();
        double centerX = bounds.minX() + bounds.widthX() / 2.0;
        double centerY = bounds.minY() + bounds.widthY() / 2.0;

        // Scale factors - dipole length is in wavelengths
        // Map wavelength to a reasonable fraction of the component's height to ensure visibility
        double wavelength = bounds.widthY() * 0.6; // Use 60% of height for wavelength
        double dipoleLength = length * wavelength; // Actual length in pixel units

        // Half-length for symmetry
        double halfLength = dipoleLength / 2.0;

        // Create paths
        GeneralPath wirePath = new GeneralPath();
        GeneralPath currentPath = new GeneralPath();

        // Number of samples for smooth curve
        int samples = 60;

        // Build wire path (centered vertically in the component)
        wirePath.moveTo(centerX, centerY - halfLength);
        wirePath.lineTo(centerX, centerY + halfLength);

        // Build current envelope path
        boolean firstPoint = true;
        for (int i = 0; i <= samples; i++) {
            // Position along dipole from -halfLength to +halfLength
            double z = -halfLength + (double) i * dipoleLength / samples;

            // Calculate standing wave envelope I(z) = I0 * sin(k * (ℓ/2 - |z|))
            // where k = 2π/λ and ℓ is dipole length
            double k = 2 * Math.PI / wavelength; // wavenumber
            double envelope = Math.sin(k * (halfLength - Math.abs(z)));

            // Apply time oscillation: I(z,t) = I(z) * cos(ωt)
            // timeFactor oscillates between -1 and 1, creating the breathing effect
            double current = envelope * timeFactor;

            // Map to coordinates:
            // - y position varies along the wire (vertical position)
            // - x position varies with current strength (horizontal displacement from wire center)
            double y = centerY - z; // y increases downward in graphics, so subtract z to go up
            double offset = current * bounds.widthX() * 0.25; // 25% of width for max bulge
            double x = centerX + offset;

            if (firstPoint) {
                currentPath.moveTo(x, y);
                firstPoint = false;
            } else {
                currentPath.lineTo(x, y);
            }
        }

        // Render wire
        NTxGraphics g = rendererContext.graphics();
        g.setPaint(wirePaint);
        g.draw(wirePath);

        // Render current envelope
        g.setPaint(currentPaint);
        g.draw(currentPath);

        rendererContext.drawContour();
        if(rendererContext.isAnimate()) {
            startRepaintTick(rendererContext);
        }
    }
    private void startRepaintTick(NTxRendererContext rendererContext) {
        Timer timer = new Timer(40, e -> {
            rendererContext.repaint();
//            System.out.println("repaint");
        });
        timer.setCoalesce(true);
        timer.start();
    }
}