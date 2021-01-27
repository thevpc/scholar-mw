package net.thevpc.scholar.hadrumaths.symbolic.double2double;

import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.symbolic.ExpressionsDebug;
import net.thevpc.scholar.hadrumaths.util.internal.DoubleValidator;

/**
 * amp * cos(a*X+b) * cos(c*Y+d)
 * User: taha
 * Date: 2 juil. 2003
 * Time: 11:51:13
 */
public final class CosXCosY extends AbstractDoubleToDouble {
    private static final long serialVersionUID = 1L;
    private final Domain domain;
    private final double amp;
    //    public static final int HASHCODE = 1;
    private final double a;
    private final double b;
    private final double c;
    private final double d;
    private final int eagerHashCode;

    public static void main(String[] args) {
        System.out.println("net.thevpc.scholar.hadrumaths.symbolic.double2double.Linear".hashCode());
    }

    public CosXCosY(@DoubleValidator(NaN = false) double amp,
                    @DoubleValidator(NaN = false) double a,
                    @DoubleValidator(NaN = false) double b,
                    @DoubleValidator(NaN = false) double c,
                    @DoubleValidator(NaN = false) double d, Domain domain) {
        this.domain = detectDomain(amp, c, domain);
        if(ExpressionsDebug.DEBUG) {
//            if (Double.isNaN(amp)) {
//                throw new IllegalArgumentException("CosXCosY amp=NaN");
//            }
//            if (Double.isNaN(a)) {
//                throw new IllegalArgumentException("CosXCosY a=NaN");
//            }
//            if (Double.isNaN(b)) {
//                throw new IllegalArgumentException("CosXCosY b=NaN");
//            }
//            if (Double.isNaN(c)) {
//                throw new IllegalArgumentException("CosXCosY c=NaN");
//            }
//            if (Double.isNaN(d)) {
//                throw new IllegalArgumentException("CosXCosY d=NaN");
//            }
        }
        this.amp = amp;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
//        name=(((a != 0 || b != 0) ? "cos(ax+b)" : "") + ((c != 0 || d != 0) ? "cos(cy+d)" : ""));

        //eager calculation of hashcode


        int hash = 431416456;//getClass().getName().hashCode();
        hash = 31 * hash + Double.hashCode(amp);
        hash = 31 * hash + Double.hashCode(a);
        hash = 31 * hash + Double.hashCode(b);
        hash = 31 * hash + Double.hashCode(c);
        hash = 31 * hash + Double.hashCode(d);
        hash = 31 * hash + domain.hashCode();
        this.eagerHashCode = hash;
    }

    protected static Domain detectDomain(double amp, double c, Domain domain) {
        if (domain == null) {
            if (amp == 0 || c == 0) {
                return Domain.FULLXY;
            }
            return Domain.FULLX;
        }
        if (domain.getDomain().getDimension() <= 2) {
            return domain;
        }
        return domain.toDomain(2);
    }

    /**
     * En posant x'=x+deltaX et y'=y+deltaY
     * f'(x',y')=f(x,y)=f(x'-deltaX,y'-delatY)
     *
     * @param deltaX
     * @param deltaY
     * @return <code>f(x+deltaX,y+deltaY)</code>
     */
    @Override
    public CosXCosY translate(double deltaX, double deltaY) {
        return new CosXCosY(amp, a, -a * deltaX + b, c, -c * deltaY + d, domain.translate(deltaX, deltaY));
    }

    @Override
    public AbstractDoubleToDouble getSymmetricX() {
        //return new DCosCosFunctionXY(amp, -a, b + a * (domain.xmax + domain.xmin), c, d, domain);
        return new CosXCosY(amp, -a, b + a * (domain.xmin() + domain.xmax()), c, d, domain);
    }

    @Override
    public CosXCosY getSymmetricY() {
//        return new DCosCosFunctionXY(amp,-a,b+a*(domain.xmax +domain.xmin),c,d,domain);
//        return new DCosCosFunctionXY(amp, a, b, -c, d + c * (domain.ymax + domain.ymin), domain);
        //return new DCosCosFunctionXY(amp, a, b, -c, d + c * (domain.height), domain);
        return new CosXCosY(amp, a, b, -c, d + c * (domain.ymax() + domain.ymin()), domain);
    }

    @Override
    public AbstractDoubleToDouble mul(double factor, Domain newDomain) {
        return new CosXCosY(amp * factor, a, b, c, d,
                newDomain == null ? domain : domain.intersect(newDomain)
        );
    }

    @Override
    public AbstractDoubleToDouble toXOpposite() {
        return new CosXCosY(amp, -a, b, c, d, domain);
    }

    @Override
    public AbstractDoubleToDouble toYOpposite() {
        return new CosXCosY(amp, a, b, -c, d, domain);
    }

    public boolean isSymmetric(AxisXY axis) {
        switch (axis) {
            case X: {
                //TODO implement isSymmetric
                return (amp == 0 || a == 0);
            }
            case Y: {
                //TODO implement isSymmetric
                return (amp == 0 || c == 0);
            }
            case XY: {
                return isSymmetric(AxisXY.X) && isSymmetric(AxisXY.Y);
            }
        }
        throw new IllegalArgumentException("Unsupported");
    }

    @Override
    public Domain getDomain() {
        return domain;
    }

    @Override
    public boolean isInvariant(Axis axis) {
        switch (axis) {
            case X: {
                return (amp == 0 || a == 0);
            }
            case Y: {
                return (amp == 0 || c == 0);
            }
            case Z: {
                return true;
            }
        }
        throw new UnsupportedOperationException("[" + getClass().getName() + "]" + "Not supported yet.");
    }

    @Override
    public boolean isZero() {
        return amp == 0 ||
                (a == 0 && (Maths.cos2(b) == 0 || ((Math.abs(b) * 2 / Maths.PI) % 2 == 1)))
                ||
                (c == 0 && (Maths.cos2(d) == 0 || ((Math.abs(d) * 2 / Maths.PI) % 2 == 1)));
    }

//    public DFunctionXY toTranslation(double xDelta) {
//        // TODO a verifier !!
//        // was return new DCosCosFunction(amp,a,b-a*xDelta,c,d,domain);
//        return new DCosCosFunctionXY(amp,a,b-a*xDelta,c,d,domain.translate(xDelta,0));
//    }

    @Override
    public boolean isNaN() {
        return
                Double.isNaN(amp)
                        || Double.isNaN(a)
                        || Double.isNaN(b)
                        || Double.isNaN(c)
                        || Double.isNaN(d);
    }

    @Override
    public Expr setParam(String name, Expr value) {
        return this;
    }

//    @Override
//    public String toString() {
//        return "{" + amp + "}.cos({" + a + "}.x+{" + b + "})" + ".cos({" + c + "}.y+{" + d + "})";
//    }

    @Override
    public boolean isInfinite() {
        return
                Double.isInfinite(amp)
                        || Double.isInfinite(a)
                        || Double.isInfinite(b)
                        || Double.isInfinite(c)
                        || Double.isInfinite(d);
    }

    @Override
    public Expr mul(Complex other) {
        if (other.isReal()) {
            return mul(other.toDouble());
        }
        return other.mul(this);
    }

    @Override
    public boolean isSmartMulDouble() {
        return true;
    }

    @Override
    public boolean isSmartMulComplex() {
        return false;
    }

    @Override
    public boolean isSmartMulDomain() {
        return true;
    }

    //NEWWWWW
//    public double leftScalarProduct(DFunction other) {
//        Domain d=this.domain.intersect(other.getDomain());
//        if(!d.isEmpty()){
//            return ScalarProductFactory.COSCOS_COSCOS.compute(d,this,other);
//        }else{
//            return 0;
//        }
//    }
//
//    public double rightScalarProduct(DFunction other) {
//        Domain d=this.domain.intersect(other.getDomain());
//        if(!d.isEmpty()){
//            return ScalarProductFactory.COSCOS_COSCOS.compute(d,other,this);
//        }else{
//            return 0;
//        }
//    }

//    public double leftScalarProduct(DFunction other) {
//        Domain d=this.domain.intersect(other.getDomain());
//        if(!d.isEmpty()){
//            if(other instanceof DCstFunction){
//                return ScalarProductFactory.COSCOS_CST.compute(d,this,other);
//            }else if(other instanceof DCosCosFunction){
//                return ScalarProductFactory.COSCOS_COSCOS.compute(d,this,other);
//            }else if(other instanceof DLinearFunction){
//                return ScalarProductFactory.COSCOS_LINEAR.compute(d,this,other);
//            }else{
//                throw new IllegalArgumentException("Unhandled <COSCOS,"+other.getClass().getName()+">");
//            }
//        }else{
//            return 0;
//        }
//    }
//
//    public double rightScalarProduct(DFunction other) {
//        Domain d=this.domain.intersect(other.getDomain());
//        if(!d.isEmpty()){
//            if(other instanceof DCstFunction){
//                return ScalarProductFactory.COSCOS_CST.compute(d,this,other);
//            }else if(other instanceof DCosCosFunction){
//                return ScalarProductFactory.COSCOS_COSCOS.compute(d,this,other);
//            }else if(other instanceof DLinearFunction){
//                return ScalarProductFactory.COSCOS_LINEAR.compute(d,this,other);
//            }else{
//                throw new IllegalArgumentException("Unhandled <COSCOS,"+other.getClass().getName()+">");
//            }
//        }else{
//            return 0;
//        }
//    }

    @Override
    public Expr mul(Domain domain) {
        return new CosXCosY(amp, a, b, c, d, this.domain.intersect(domain));
    }

    @Override
    public Expr mul(double other) {
        return new CosXCosY(amp * other, a, b, c, d, this.domain);
    }

    @Override
    public Expr newInstance(Expr... subExpressions) {
        return this;
    }

    public double getA() {
        return a;
    }

    public double getAmp() {
        return amp;
    }

    public double getB() {
        return b;
    }

    public double getC() {
        return c;
    }

    public double getD() {
        return d;
    }

    @Override
    public int hashCode() {
        return eagerHashCode;
//        int result = super.hashCode();
//        long temp;
//        temp = Double.doubleToLongBits(amp);
//        result = 31 * result + (int) (temp ^ (temp >>> 32));
//        temp = Double.doubleToLongBits(a);
//        result = 31 * result + (int) (temp ^ (temp >>> 32));
//        temp = Double.doubleToLongBits(b);
//        result = 31 * result + (int) (temp ^ (temp >>> 32));
//        temp = Double.doubleToLongBits(c);
//        result = 31 * result + (int) (temp ^ (temp >>> 32));
//        temp = Double.doubleToLongBits(d);
//        result = 31 * result + (int) (temp ^ (temp >>> 32));
//        result = 31 * result + domain.hashCode();
//        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CosXCosY)) return false;

        CosXCosY cosXCosY = (CosXCosY) o;

        if (cosXCosY.eagerHashCode != eagerHashCode) return false;
        if (!cosXCosY.domain.equals(domain)) return false;
        if (Double.compare(cosXCosY.a, a) != 0) return false;
        if (Double.compare(cosXCosY.amp, amp) != 0) return false;
        if (Double.compare(cosXCosY.b, b) != 0) return false;
        if (Double.compare(cosXCosY.c, c) != 0) return false;
        return Double.compare(cosXCosY.d, d) == 0;
    }

    public double evalDouble(double x, double y, double z, BooleanMarker defined) {
        if (contains(x, y, z)) {
            defined.set();
            return amp * Maths.cos2(a * x + b) * Maths.cos2(c * y + d);
        }
        return 0;
    }

    public double evalDouble(double x, BooleanMarker defined) {
        if (c == 0) {
            if (contains(x)) {
                defined.set();
                return amp * Maths.cos2(a * x + b) * Maths.cos2(d);
            }
            return 0;
        }
        throw new MissingAxisException(Axis.Y);
    }

    public double evalDouble(double x, double y, BooleanMarker defined) {
        if (contains(x, y)) {
            defined.set();
            return amp * Maths.cos2(a * x + b) * Maths.cos2(c * y + d);
        }
        return 0;
    }

    @Override
    public String toLatex() {
        throw new UnsupportedOperationException("Not Implemented toLatex for "+getClass().getName());
    }

}
