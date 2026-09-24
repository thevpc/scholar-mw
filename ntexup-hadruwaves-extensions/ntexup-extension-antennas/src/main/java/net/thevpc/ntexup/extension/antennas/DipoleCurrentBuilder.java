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

        // Animation logic
        double timeFactor = 1.0; // Default for print mode (cos(ωt) = 1)
        if (rendererContext.isAnimate()) {
            long pageStartTime = rendererContext.pageStartTime();
            long now = System.currentTimeMillis();
            long elapsed = now - pageStartTime;
            double phase = 2 * Math.PI * ((elapsed % animatePeriodMs) / (double) animatePeriodMs);
            timeFactor = Math.cos(phase);
        }

        // Get bounds for positioning
        NTxBounds2D bounds = rendererContext.selfBounds2D();
        double centerX = bounds.minX() + bounds.widthX() / 2.0;
        double centerY = bounds.minY() + bounds.widthY() / 2.0;

        // Scale factors - dipole length is in wavelengths, we'll map to component height
        double wavelength = bounds.widthY(); // Use height as wavelength reference
        double dipoleLength = length * wavelength; // Actual length in pixel units

        // Half-length for symmetry
        double halfLength = dipoleLength / 2.0;

        // Create paths
        GeneralPath wirePath = new GeneralPath();
        GeneralPath currentPath = new GeneralPath();

        // Number of samples for smooth curve
        int samples = 60;

        // Build wire path (centered vertically)
        wirePath.moveTo(centerX, centerY - halfLength);
        wirePath.lineTo(centerX, centerY + halfLength);

        // Build current envelope path
        boolean firstPoint = true;
        for (int i = 0; i <= samples; i++) {
            // Position along dipole from -halfLength to +halfLength
            double z = -halfLength + (double) i * dipoleLength / samples;

            // Normalized position from -0.5 to +0.5
            double zNormalized = z / halfLength;

            // Calculate standing wave envelope I(z) = I0 * sin(k * (ℓ/2 - |z|))
            // where k = 2π/λ and ℓ is dipole length
            double k = 2 * Math.PI / wavelength; // wavenumber
            double envelope = Math.sin(k * (halfLength - Math.abs(z)));

            // Apply time oscillation: I(z,t) = I(z) * cos(ωt)
            double current = envelope * timeFactor;

            // Map to coordinates: x position varies with current, y position fixed along wire
            double offset = current * bounds.widthX() * 0.35; // 35% of width for max bulge
            double x = centerX + offset;
            double y = centerY - z; // Note: y increases downward in graphics, so we subtract z

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
    }
}