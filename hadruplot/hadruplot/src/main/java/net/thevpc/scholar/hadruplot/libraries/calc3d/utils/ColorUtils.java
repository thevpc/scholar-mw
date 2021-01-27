package net.thevpc.scholar.hadruplot.libraries.calc3d.utils;

import net.thevpc.scholar.hadruplot.libraries.calc3d.core.LocMessages;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Paint;

public class ColorUtils {
    // adds alpha to a color

    static public Color changeAlpha(Color c, int alpha) {
        return new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha);
    }

    // blends two colors... this is NOT symmetric
    static public Color blendColors(Paint c1, Color c2, double amt) {
        if (c1 instanceof Color) {
            return blendColors((Color) c1, c2, amt);
        }
        return c2;
    }

    // blends two colors... this is NOT symmetric
    static public Color blendColors(Color c1, Paint c2, double amt) {
        if (c2 instanceof Color) {
            return blendColors(c1, (Color) c2, amt);
        }
        return c1;
    }

    /**
     * Returns a foreground color (for text) given a background color by
     * examining the brightness of the background color.
     *
     * @param color the foreground color
     * @return Color
     */
    public static final Color getForegroundColorFromBackgroundColor(Paint color) {
        int brightness = getBrightness(color);
        if (brightness < 130) {
            return Color.WHITE;
        } else {
            return Color.BLACK;
        }
    }

    static public Color blendColors(Color c1, Color c2, double amt) {
        if (c1 == null || c2 == null) {
            return null;
        }
        int red = (int) (c2.getRed() * amt + c1.getRed() * (1 - amt));
        int green = (int) (c2.getGreen() * amt + c1.getGreen() * (1 - amt));
        int blue = (int) (c2.getBlue() * amt + c1.getBlue() * (1 - amt));
        int alpha = c1.getAlpha();    // keep first color's alpha
        return new Color(red, green, blue, alpha);
    }

    // blends two colors... this is NOT symmetric
    static public Color addColors(Color c1, Color c2, double amt) {
        if (c1 == null || c2 == null) {
            return null;
        }
        int red = (int) (c2.getRed() * amt + c1.getRed());
        if (red > 255) {
            red = 255;
        }
        int green = (int) (c2.getGreen() * amt + c1.getGreen());
        if (green > 255) {
            green = 255;
        }
        int blue = (int) (c2.getBlue() * amt + c1.getBlue());
        if (blue > 255) {
            blue = 255;
        }
        int alpha = c1.getAlpha();    // keep first color's alpha
        return new Color(red, green, blue, alpha);
    }

    static double getBlendAmt(Double dist) {
        double blendAmt;
        double fogStart = 0, fogEnd = 2;
        blendAmt = (dist - fogStart) / (fogEnd - fogStart);
        if (blendAmt > 1) {
            return 1;
        } else if (blendAmt < 0) {
            return 0;
        } else {
            return blendAmt;
        }
    }

    /**
     * Returns an array of color components for the given Color object.
     *
     * @param color the color object
     * @return float[]
     */
    public static final float[] convertColor(Color color) {
        return color.getRGBComponents(null);
    }

    /**
     * Returns a new Color object given the color components in the given array.
     *
     * @param color the color components in RGB or RGBA
     * @return Color
     */
    public static final Color convertColor(float[] color) {
        if (color.length == 3) {
            return new Color(color[0], color[1], color[2]);
        } else if (color.length == 4) {
            return new Color(color[0], color[1], color[2], color[3]);
        } else {
            throw new IllegalArgumentException(LocMessages.getString("exception.color.notEnoughComponents"));
        }
    }

    /**
     * Places the RGBA values from the given Color object into the destination
     * array.
     *
     * @param color the color to convert
     * @param destination the array to hold the RGBA values; length 4
     */
    public static final void convertColor(Color color, float[] destination) {
        color.getRGBComponents(destination);
    }

    public static final int getBrightness(Paint color) {
        if (color instanceof Color) {
            return getBrightness((Color) color);
        }
        if (color instanceof GradientPaint) {
            GradientPaint p1 = (GradientPaint) color;
            return getBrightness(blendColors(p1.getColor1(), p1.getColor2(), 0.5));
        }
        return getBrightness(Color.gray);
    }

    /**
     * Uses the method described at http://alienryderflex.com/hsp.html to get
     * the <u>perceived</u> brightness of a color.
     *
     * @param color the color
     * @return int brightness on the scale of 0 to 255
     */
    public static final int getBrightness(Color color) {
        // original coefficients
        final double cr = 0.241;
        final double cg = 0.691;
        final double cb = 0.068;
        // another set of coefficients
//		final double cr = 0.299;
//		final double cg = 0.587;
//		final double cb = 0.114;

        double r, g, b;
        r = color.getRed();
        g = color.getGreen();
        b = color.getBlue();

        // compute the weighted distance
        double result = Math.sqrt(cr * r * r + cg * g * g + cb * b * b);

        return (int) result;
    }

    /**
     * Returns a foreground color (for text) given a background color by
     * examining the brightness of the background color.
     *
     * @param color the foreground color
     * @return Color
     */
    public static final Color getForegroundColorFromBackgroundColor(Color color) {
        int brightness = getBrightness(color);
        if (brightness < 130) {
            return Color.WHITE;
        } else {
            return Color.BLACK;
        }
    }

    /**
     * Returns a foreground color (for text) given a background color by
     * examining the brightness of the background color.
     *
     * @param color the foreground color
     * @return Color
     */
    public static final Color getInvertColor(Color color) {
        return new Color(255 - color.getRed(), 255 - color.getBlue(), 255 - color.getGreen(), color.getAlpha());
    }

    /**
     * Returns a random color given the offset and alpha values.
     *
     * @param offset the offset between 0.0 and 1.0
     * @param alpha the alpha value between 0.0 and 1.0
     * @return Color
     */
    public static final Color getRandomColor(float offset, float alpha) {
        final float max = 1.0f;
        final float min = 0.0f;
        // make sure the offset is valid
        if (offset > max) {
            offset = min;
        }
        if (offset < min) {
            offset = min;
        }
        // use the offset to calculate the color
        float multiplier = max - offset;
        // compute the rgb values
        float r = (float) Math.random() * multiplier + offset;
        float g = (float) Math.random() * multiplier + offset;
        float b = (float) Math.random() * multiplier + offset;

        return new Color(r, g, b, alpha);
    }

    /**
     * Returns a new color that is darker or lighter than the given color by the
     * given factor.
     *
     * @param color the color to modify
     * @param factor 0.0 &le; factor &le; 1.0 darkens; 1.0 &lt; factor brightens
     * @return Color
     * @since 1.0.1
     */
    public static final Color getColor(Color color, float factor) {
        float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        hsb[2] = hsb[2] * factor;
        return new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2] * factor));
    }

    public static Color paintToColor(Paint color) {
        if (color instanceof Color) {
            return ((Color) color);
        }
        if (color instanceof GradientPaint) {
            GradientPaint p1 = (GradientPaint) color;
            return blendColors(p1.getColor1(), p1.getColor2(), 0.5);
        }
        return (Color.gray);
    }

}
