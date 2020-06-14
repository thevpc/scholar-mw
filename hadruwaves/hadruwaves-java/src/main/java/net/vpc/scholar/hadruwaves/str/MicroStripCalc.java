package net.vpc.scholar.hadruwaves.str;

import net.vpc.scholar.hadrumaths.Maths;
import static net.vpc.scholar.hadrumaths.Maths.C;

/**
 * https://www.researchgate.net/publication/305705158_Design_of_rectangular_microstrip_patch_antenna
 * http://janielectronics.com/szamitasok/Transmission Line/Microstrip Line Calculator/janilab.php
 */
public class MicroStripCalc {
    public final double height;
    public final double freq;
    public final double epsr;
    public final double width;
    public final double deltaLength;
    public final double epsrEff;
    public final double effLength;
    public final double length;
    public static void main(String[] args) {
        MicroStripCalc v = calculate(3*Maths.GHZ, 2.2, 0.1*Maths.MM);
        System.out.println(v);
    }
    private MicroStripCalc(double height, double freq, double epsr, double width, double deltaLength, double epsrEff, double effLength, double length) {
        this.height = height;
        this.freq = freq;
        this.epsr = epsr;
        this.width = width;
        this.deltaLength = deltaLength;
        this.epsrEff = epsrEff;
        this.effLength = effLength;
        this.length = length;
    }

    public static MicroStripCalc calculate(double f, double epsr, double h) {
        double w = C / (2 * f) * Math.sqrt(2 / (epsr + 1));
        double deltaL = h * 0.412 * (epsr + 0.3) * (w / h + 0.265) / (epsr - 0.258) / (w / h + 0.8);
        double epsrEff = (epsr + 1) / 2 + (epsr - 1) / 2 * (1 / Math.sqrt(1 + 2 * h / w));
        double Leff = C / (2 * f * Math.sqrt(epsrEff));
        double L = Leff - 2 * deltaL;
        return new MicroStripCalc(h, f, epsr, w, deltaL, epsrEff, Leff, L);
    }

    @Override
    public String toString() {
        return "MicroStripCalc{" + "height=" + height + ", freq=" + freq + ", epsr=" + epsr + ", width=" + width + ", deltaLength=" + deltaLength + ", epsrEff=" + epsrEff + ", effLength=" + effLength + ", length=" + length + '}';
    }
    

}
