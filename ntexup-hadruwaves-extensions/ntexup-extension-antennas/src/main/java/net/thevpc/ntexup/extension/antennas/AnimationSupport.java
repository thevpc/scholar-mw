package net.thevpc.ntexup.extension.antennas;

import net.thevpc.ntexup.api.document.node.NTxNode;
import net.thevpc.ntexup.api.renderer.NTxRendererContext;

import javax.swing.*;

/**
 * One-repaint-timer-per-node support for live (animated) presentation renders.
 *
 * - A timer is created only once per node (stored in the node render cache) and only while
 *   the renderer advertises the {@code animate} capability.
 * - The timer is stopped (and removed from the cache) as soon as rendering stops being
 *   animated (static PDF/image render, dry run, or when the node leaves a live screen) —
 *   no per-frame timer creation, no timer leak.
 * - {@code repaint()} is a no-op when the renderer context has no repainter (PDF/images),
 *   which also guarantees static renders are never re-scheduled.
 */
final class AnimationSupport {

    private static final String TIMER_CACHE = "ntexup-antennas.animate-timer";

    private AnimationSupport() {
    }

    /**
     * Ensures a repaint tick while {@code animated && ctx.isAnimate()}; otherwise stops and
     * drops any cached timer.
     *
     * @param ctx       renderer context
     * @param animated  whether this node instance wants animation at all (property flag)
     * @param periodMs  tick period in milliseconds
     */
    static void tick(NTxRendererContext ctx, boolean animated, long periodMs) {
        NTxNode node = ctx.node();
        if (!animated || !ctx.isAnimate()) {
            Object cached = node.getRenderCache(TIMER_CACHE).orNull();
            if (cached instanceof Timer) {
                ((Timer) cached).stop();
                node.setRenderCache(TIMER_CACHE, null);
            }
            return;
        }
        Object cached = node.getRenderCache(TIMER_CACHE).orNull();
        if (cached instanceof Timer) {
            Timer timer = (Timer) cached;
            if (!timer.isRunning()) {
                timer.start();
            }
            return;
        }
        Timer timer = new Timer((int) Math.max(16, periodMs), e -> {
            if (!ctx.isAnimate() || ctx.isDry()) {
                ((Timer) e.getSource()).stop();
                node.setRenderCache(TIMER_CACHE, null);
            } else {
                ctx.repaint();
            }
        });
        timer.setCoalesce(true);
        node.setRenderCache(TIMER_CACHE, timer);
        timer.start();
    }
}