package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Maths;

/**
 * User: taha
 * Date: 2 juil. 2003
 * Time: 11:51:13
 */
public final class CosXCosY extends AbstractDoubleToDouble implements Cloneable{
    private static final long serialVersionUID = -1010101010101001040L;
    public double amp;
    //    public static final int HASHCODE = 1;
    public double a;
    public double b;
    public double c;
    public double d;

    public CosXCosY(double amp, double a, double b, double c, double d, Domain domain) {
        super(domain);
        if (Double.isNaN(amp)) {
            throw new IllegalArgumentException("DCosCosFunctionXY amp=NaN");
        }
        if (Double.isNaN(a)) {
            throw new IllegalArgumentException("DCosCosFunctionXY a=NaN");
        }
        if (Double.isNaN(b)) {
            throw new IllegalArgumentException("DCosCosFunctionXY b=NaN");
        }
        if (Double.isNaN(c)) {
            throw new IllegalArgumentException("DCosCosFunctionXY c=NaN");
        }
        if (Double.isNaN(d)) {
            throw new IllegalArgumentException("DCosCosFunctionXY d=NaN");
        }
        this.amp = amp;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
//        name=(((a != 0 || b != 0) ? "cos(ax+b)" : "") + ((c != 0 || d != 0) ? "cos(cy+d)" : ""));
    }


    @Override
    public boolean isZeroImpl() {
        return amp == 0 ||
                (a == 0 && (Maths.cos2(b) == 0 || ((Math.abs(b) * 2 / Math.PI) % 2 == 1)))
                ||
                (c == 0 && (Maths.cos2(d) == 0 || ((Math.abs(d) * 2 / Math.PI) % 2 == 1)));
    }

    @Override
    public boolean isNaNImpl() {
        return
                Double.isNaN(amp)
                        || Double.isNaN(a)
                        || Double.isNaN(b)
                        || Double.isNaN(c)
                        || Double.isNaN(d);
    }

    @Override
    public boolean isInfiniteImpl() {
        return
                Double.isInfinite(amp)
                        || Double.isInfinite(a)
                        || Double.isInfinite(b)
                        || Double.isInfinite(c)
                        || Double.isInfinite(d);
    }

    @Override
    public boolean isInvariantImpl(Axis axis) {
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
        throw new UnsupportedOperationException("Not supported yet.");
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

    @Override
    public AbstractDoubleToDouble getSymmetricX() {
        //return new DCosCosFunctionXY(amp, -a, b + a * (domain.xmax + domain.xmin), c, d, domain);
        return new CosXCosY(amp, -a, b + a * (domain.xmin ()+ domain.xmax()), c, d, domain);
    }

//    public DFunctionXY toTranslation(double xDelta) {
//        // TODO a verifier !!
//        // was return new DCosCosFunction(amp,a,b-a*xDelta,c,d,domain);
//        return new DCosCosFunctionXY(amp,a,b-a*xDelta,c,d,domain.translate(xDelta,0));
//    }

    @Override
    public CosXCosY getSymmetricY() {
//        return new DCosCosFunctionXY(amp,-a,b+a*(domain.xmax +domain.xmin),c,d,domain);
//        return new DCosCosFunctionXY(amp, a, b, -c, d + c * (domain.ymax + domain.ymin), domain);
        //return new DCosCosFunctionXY(amp, a, b, -c, d + c * (domain.height), domain);
        return new CosXCosY(amp, a, b, -c, d + c * (domain.ymax ()+ domain.ymin()), domain);
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

//    @Override
//    public String toString() {
//        return "{" + amp + "}.cos({" + a + "}.x+{" + b + "})" + ".cos({" + c + "}.y+{" + d + "})";
//    }

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
    public Expr setParam(String name, Expr value) {
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CosXCosY)) return false;
        if (!super.equals(o)) return false;

        CosXCosY cosXCosY = (CosXCosY) o;

        if (Double.compare(cosXCosY.a, a) != 0) return false;
        if (Double.compare(cosXCosY.amp, amp) != 0) return false;
        if (Double.compare(cosXCosY.b, b) != 0) return false;
        if (Double.compare(cosXCosY.c, c) != 0) return false;
        if (Double.compare(cosXCosY.d, d) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        long temp;
        temp = Double.doubleToLongBits(amp);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(a);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(b);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(c);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(d);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    protected double computeDouble0(double x) {
        throw new IllegalArgumentException("Missing y");
    }

    protected double computeDouble0(double x,double y) {
        return amp * Maths.cos2(a * x + b) * Maths.cos2(c * y + d);
    }

    protected double computeDouble0(double x,double y,double z) {
        return amp * Maths.cos2(a * x + b) * Maths.cos2(c * y + d);
    }

}
