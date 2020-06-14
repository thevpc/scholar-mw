package net.vpc.scholar.hadrumaths.expeval;

import net.vpc.common.jeep.JeepImported;
import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.symbolic.DomainExpr;
import net.vpc.scholar.hadrumaths.symbolic.ExprType;
import net.vpc.scholar.hadrumaths.symbolic.conv.Imag;
import net.vpc.scholar.hadrumaths.symbolic.conv.Real;
import net.vpc.scholar.hadrumaths.symbolic.polymorph.ParametrizedScalarProduct;
import net.vpc.scholar.hadrumaths.symbolic.polymorph.cond.Neg;
import net.vpc.scholar.hadrumaths.symbolic.polymorph.num.*;
import net.vpc.scholar.hadrumaths.symbolic.polymorph.trigo.*;

import java.lang.annotation.ElementType;


@JeepImported(ElementType.TYPE)
public class PlatformHelperAsExpr {
    public static Expr X=Maths.X;
    public static Expr Y=Maths.Y;
    public static Expr Z=Maths.Z;
    // neg

    public static Expr pow(double a, double b) {
        return Pow.of(__e(a), __e(b));
    }

    public static Expr pow(Complex a, Complex b) {
        return Pow.of(__e(a), __e(b));
    }

    public static Expr pow(Expr a, Expr b) {
        return Pow.of(a, b);
    }

    public static Expr neg(byte a) {
        return Neg.of(__e(a));
    }

    public static Expr neg(short a) {
        return Neg.of(__e(a));
    }

    public static Expr neg(int a) {
        return Neg.of(__e(a));
    }

    public static Expr neg(long a) {
        return Neg.of(__e(a));
    }

    public static Expr neg(float a) {
        return Neg.of(__e(a));
    }

    public static Expr neg(double a) {
        return Neg.of(__e(a));
    }

    public static Expr neg(Complex a) {
        return Neg.of(a);//Maths.neg(__expr(a));
    }

//    public static Expr neg(BigInteger a) {
//        return a.negate();
//    }
//
//    public static Expr neg(BigDecimal a) {
//        return a.negate();
//    }

//    public static Expr neg( Complex a) {
//        throw new IllegalArgumentException("Unsupported");
//    }
//
//    public static Expr neg( Expr a) {
//        throw new IllegalArgumentException("Unsupported");
//    }

    //  tilde

//    public static Expr tilde(byte a) {
//        return ~a;
//    }
//
//    public static Expr tilde(short a) {
//        return ~a;
//    }
//
//    public static Expr tilde(int a) {
//        return ~a;
//    }
//
//    public static Expr tilde(long a) {
//        return ~a;
//    }

//    public static Expr tilde( float a) {
//        return ~a;
//    }
//
//    public static Expr tilde( double a) {
//        return ~a;
//    }

//    public static Expr tilde(BigInteger a) {
//        // for negative BigInteger, top byte is negative
//        byte[] contents = a.toByteArray();
//
//        // prepend byte of opposite sign
//        byte[] result = new byte[contents.length + 1];
//        System.arraycopy(contents, 0, result, 1, contents.length);
//        result[0] = (contents[0] < 0) ? 0 : (byte) -1;
//
//        // this will be two's complement
//        return new BigInteger(result);
//    }

//    public static Expr tilde(BigDecimal a) {
//        return a.negate();
//    }
//
//    public static Expr neg( Complex b) {
//        throw new IllegalArgumentException("Unsupported");
//    }
//
//    public static Expr neg( Expr b) {
//        throw new IllegalArgumentException("Unsupported");
//    }


    // byte ADD

    public static Expr plus(byte a, byte b) {
        return Maths.add(__e(a), __e(b));
    }

    public static Expr plus(byte a, short b) {
        return Maths.add(__e(a), __e(b));
    }

    public static Expr plus(byte a, int b) {
        return Maths.add(__e(a), __e(b));
    }

    public static Expr plus(byte a, long b) {
        return Maths.add(__e(a), __e(b));
    }

    public static Expr plus(byte a, float b) {
        return Maths.add(__e(a), __e(b));
    }

    public static Expr plus(byte a, double b) {
        return Maths.add(__e(a), __e(b));
    }

//    public static Expr add(byte a, BigInteger b) {
//        return BigInteger.valueOf(a).add(b);
//    }
//
//    public static Expr add(byte a, BigDecimal b) {
//        return BigDecimal.valueOf(a).add(b);
//    }

    public static Expr plus(byte a, Complex b) {
        return __e(a).plus(b);
    }

    public static Expr plus(byte a, Expr b) {
        return __e(a).plus(b);
    }

    // short ADD

    public static Expr plus(short a, byte b) {
        return Maths.add(__e(a), __e(b));
    }

    public static Expr plus(short a, short b) {
        return Maths.add(__e(a), __e(b));
    }

    public static Expr plus(short a, int b) {
        return Maths.add(__e(a), __e(b));
    }

    public static Expr plus(short a, long b) {
        return Maths.add(__e(a), __e(b));
    }

    public static Expr plus(short a, float b) {
        return Maths.add(__e(a), __e(b));
    }

    public static Expr plus(short a, double b) {
        return Maths.add(__e(a), __e(b));
    }

//    public static Expr add(short a, BigInteger b) {
//        return BigInteger.valueOf(a).add(b);
//    }
//
//    public static Expr add(short a, BigDecimal b) {
//        return BigDecimal.valueOf(a).add(b);
//    }

    public static Expr plus(short a, Complex b) {
        return __e(a).plus(b);
    }

    public static Expr plus(short a, Expr b) {
        return __e(a).plus(b);
    }

    // int ADD

    public static Expr plus(int a, byte b) {
        return Maths.add(__e(a), __e(b));
    }

    public static Expr plus(int a, short b) {
        return Maths.add(__e(a), __e(b));
    }

    public static Expr plus(int a, int b) {
        return Maths.add(__e(a), __e(b));
    }

    public static Expr plus(int a, long b) {
        return Maths.add(__e(a), __e(b));
    }

    public static Expr plus(int a, float b) {
        return Maths.add(__e(a), __e(b));
    }

    public static Expr plus(int a, double b) {
        return Maths.add(__e(a), __e(b));
    }

//    public static Expr add(int a, BigInteger b) {
//        return BigInteger.valueOf(a).add(b);
//    }
//
//    public static Expr add(int a, BigDecimal b) {
//        return BigDecimal.valueOf(a).add(b);
//    }

    public static Expr plus(int a, Complex b) {
        return __e(a).plus(b);
    }

    public static Expr plus(int a, Expr b) {
        return __e(a).plus(b);
    }

    // long ADD

    public static Expr plus(long a, byte b) {
        return Maths.add(__e(a), __e(b));
    }

    public static Expr plus(long a, short b) {
        return Maths.add(__e(a), __e(b));
    }

    public static Expr plus(long a, int b) {
        return Maths.add(__e(a), __e(b));
    }

    public static Expr plus(long a, long b) {
        return Maths.add(__e(a), __e(b));
    }

    public static Expr plus(long a, float b) {
        return Maths.add(__e(a), __e(b));
    }

    public static Expr plus(long a, double b) {
        return Maths.add(__e(a), __e(b));
    }

//    public static Expr add(long a, BigInteger b) {
//        return BigInteger.valueOf(a).add(b);
//    }
//
//    public static Expr add(long a, BigDecimal b) {
//        return BigDecimal.valueOf(a).add(b);
//    }

    public static Expr plus(long a, Complex b) {
        return __e(a).plus(b);
    }

    public static Expr plus(long a, Expr b) {
        return __e(a).plus(b);
    }

    // float ADD

    public static Expr plus(float a, byte b) {
        return Maths.add(__e(a), __e(b));
    }

    public static Expr plus(float a, short b) {
        return Maths.add(__e(a), __e(b));
    }

    public static Expr plus(float a, int b) {
        return Maths.add(__e(a), __e(b));
    }

    public static Expr plus(float a, long b) {
        return Maths.add(__e(a), __e(b));
    }

    public static Expr plus(float a, float b) {
        return Maths.add(__e(a), __e(b));
    }

    public static Expr plus(float a, double b) {
        return Maths.add(__e(a), __e(b));
    }

//    public static Expr add(float a, BigInteger b) {
//        return BigDecimal.valueOf(a).add(new BigDecimal(b));
//    }
//
//    public static Expr add(float a, BigDecimal b) {
//        return BigDecimal.valueOf(a).add(b);
//    }

    public static Expr plus(float a, Complex b) {
        return __e(a).plus(b);
    }

    public static Expr plus(float a, Expr b) {
        return __e(a).plus(b);
    }

    // double ADD

    public static Expr plus(double a, byte b) {
        return Maths.add(__e(a), __e(b));
    }

    public static Expr plus(double a, short b) {
        return Maths.add(__e(a), __e(b));
    }

    public static Expr plus(double a, int b) {
        return Maths.add(__e(a), __e(b));
    }

    public static Expr plus(double a, long b) {
        return Maths.add(__e(a), __e(b));
    }

    public static Expr plus(double a, float b) {
        return Maths.add(__e(a), __e(b));
    }

    public static Expr plus(double a, double b) {
        return Maths.add(__e(a), __e(b));
    }

//    public static Expr add(double a, BigInteger b) {
//        return BigDecimal.valueOf(a).add(new BigDecimal(b));
//    }
//
//    public static Expr add(double a, BigDecimal b) {
//        return BigDecimal.valueOf(a).add(b);
//    }

    public static Expr plus(double a, Complex b) {
        return __e(a).plus(b);
    }

    public static Expr plus(double a, Expr b) {
        return __e(a).plus(b);
    }

    // BigInteger ADD

//    public static Expr add(BigInteger a, byte b) {
//        return a.add(BigInteger.valueOf(b));
//    }
//
//    public static Expr add(BigInteger a, short b) {
//        return a.add(BigInteger.valueOf(b));
//    }
//
//    public static Expr add(BigInteger a, int b) {
//        return a.add(BigInteger.valueOf(b));
//    }
//
//    public static Expr add(BigInteger a, long b) {
//        return a.add(BigInteger.valueOf(b));
//    }

//    public static Expr add(BigInteger a, float b) {
//        return new BigDecimal(a).add(BigDecimal.valueOf(b));
//    }
//
//    public static Expr add(BigInteger a, double b) {
//        return new BigDecimal(a).add(BigDecimal.valueOf(b));
//    }
//
//    public static Expr add(BigInteger a, BigInteger b) {
//        return new BigDecimal(a).add(new BigDecimal(b));
//    }
//
//    public static Expr add(BigInteger a, BigDecimal b) {
//        return new BigDecimal(a).add(b);
//    }

//    public static Expr add(BigInteger a, Complex b) {
//        throw new IllegalArgumentException("Unsupported");
//    }
//
//    public static Expr add(BigInteger a, Expr b) {
//        throw new IllegalArgumentException("Unsupported");
//    }


    // BigDecimal ADD

//    public static Expr add(BigDecimal a, byte b) {
//        return a.add(BigDecimal.valueOf(b));
//    }
//
//    public static Expr add(BigDecimal a, short b) {
//        return a.add(BigDecimal.valueOf(b));
//    }
//
//    public static Expr add(BigDecimal a, int b) {
//        return a.add(BigDecimal.valueOf(b));
//    }
//
//    public static Expr add(BigDecimal a, long b) {
//        return a.add(BigDecimal.valueOf(b));
//    }
//
//    public static Expr add(BigDecimal a, float b) {
//        return a.add(BigDecimal.valueOf(b));
//    }
//
//    public static Expr add(BigDecimal a, double b) {
//        return a.add(BigDecimal.valueOf(b));
//    }
//
//    public static Expr add(BigDecimal a, BigInteger b) {
//        return a.add(new BigDecimal(b));
//    }
//
//    public static Expr add(BigDecimal a, BigDecimal b) {
//        return a.add(b);
//    }
//
//    public static Expr add(BigDecimal a, Complex b) {
//        throw new IllegalArgumentException("Unsupported");
//    }
//
//    public static Expr add(BigDecimal a, Expr b) {
//        throw new IllegalArgumentException("Unsupported");
//    }


    // byte SUB

    public static Expr minus(byte a, byte b) {
        return Maths.minus(__e(a), __e(b));
    }

    public static Expr minus(byte a, short b) {
        return Maths.minus(__e(a), __e(b));
    }

    public static Expr minus(byte a, int b) {
        return Maths.minus(__e(a), __e(b));
    }

    public static Expr minus(byte a, long b) {
        return Maths.minus(__e(a), __e(b));
    }

    public static Expr minus(byte a, float b) {
        return Maths.minus(__e(a), __e(b));
    }

    public static Expr minus(byte a, double b) {
        return Maths.minus(__e(a), __e(b));
    }

//    public static Expr sub(byte a, BigInteger b) {
//        return BigInteger.valueOf(a).subtract(b);
//    }
//
//    public static Expr sub(byte a, BigDecimal b) {
//        return BigDecimal.valueOf(a).subtract(b);
//    }

    public static Expr minus(byte a, Complex b) {
        return __e(a).sub(b);
    }

    public static Expr minus(byte a, Expr b) {
        return __e(a).sub(b);
    }

    // short sub

    public static Expr minus(short a, byte b) {
        return Maths.minus(__e(a), __e(b));
    }

    public static Expr minus(short a, short b) {
        return Maths.minus(__e(a), __e(b));
    }

    public static Expr minus(short a, int b) {
        return Maths.minus(__e(a), __e(b));
    }

    public static Expr minus(short a, long b) {
        return Maths.minus(__e(a), __e(b));
    }

    public static Expr minus(short a, float b) {
        return Maths.minus(__e(a), __e(b));
    }

    public static Expr minus(short a, double b) {
        return Maths.minus(__e(a), __e(b));
    }

//    public static Expr sub(short a, BigInteger b) {
//        return BigInteger.valueOf(a).subtract(b);
//    }
//
//    public static Expr sub(short a, BigDecimal b) {
//        return BigDecimal.valueOf(a).subtract(b);
//    }

    public static Expr minus(short a, Complex b) {
        return __e(a).sub(b);
    }

    public static Expr minus(short a, Expr b) {
        return __e(a).sub(b);
    }

    // int sub

    public static Expr minus(int a, byte b) {
        return Maths.minus(__e(a), __e(b));
    }

    public static Expr minus(int a, short b) {
        return Maths.minus(__e(a), __e(b));
    }

    public static Expr minus(int a, int b) {
        return Maths.minus(__e(a), __e(b));
    }

    public static Expr minus(int a, long b) {
        return Maths.minus(__e(a), __e(b));
    }

    public static Expr minus(int a, float b) {
        return Maths.minus(__e(a), __e(b));
    }

    public static Expr minus(int a, double b) {
        return Maths.minus(__e(a), __e(b));
    }

//    public static Expr sub(int a, BigInteger b) {
//        return BigInteger.valueOf(a).subtract(b);
//    }
//
//    public static Expr sub(int a, BigDecimal b) {
//        return BigDecimal.valueOf(a).subtract(b);
//    }

    public static Expr minus(int a, Complex b) {
        return __e(a).sub(b);
    }

    public static Expr minus(int a, Expr b) {
        return __e(a).sub(b);
    }

    // long sub

    public static Expr minus(long a, byte b) {
        return Maths.minus(__e(a), __e(b));
    }

    public static Expr minus(long a, short b) {
        return Maths.minus(__e(a), __e(b));
    }

    public static Expr minus(long a, int b) {
        return Maths.minus(__e(a), __e(b));
    }

    public static Expr minus(long a, long b) {
        return Maths.minus(__e(a), __e(b));
    }

    public static Expr minus(long a, float b) {
        return Maths.minus(__e(a), __e(b));
    }

    public static Expr minus(long a, double b) {
        return Maths.minus(__e(a), __e(b));
    }

//    public static Expr sub(long a, BigInteger b) {
//        return BigInteger.valueOf(a).subtract(b);
//    }
//
//    public static Expr sub(long a, BigDecimal b) {
//        return BigDecimal.valueOf(a).subtract(b);
//    }

    public static Expr minus(long a, Complex b) {
        return __e(a).sub(b);
    }

    public static Expr minus(long a, Expr b) {
        return __e(a).sub(b);
    }

    // float sub

    public static Expr minus(float a, byte b) {
        return Maths.minus(__e(a), __e(b));
    }

    public static Expr minus(float a, short b) {
        return Maths.minus(__e(a), __e(b));
    }

    public static Expr minus(float a, int b) {
        return Maths.minus(__e(a), __e(b));
    }

    public static Expr minus(float a, long b) {
        return Maths.minus(__e(a), __e(b));
    }

    public static Expr minus(float a, float b) {
        return Maths.minus(__e(a), __e(b));
    }

    public static Expr minus(float a, double b) {
        return Maths.minus(__e(a), __e(b));
    }

//    public static Expr sub(float a, BigInteger b) {
//        return BigDecimal.valueOf(a).subtract(new BigDecimal(b));
//    }
//
//    public static Expr sub(float a, BigDecimal b) {
//        return BigDecimal.valueOf(a).subtract(b);
//    }

    public static Expr minus(float a, Complex b) {
        return __e(a).sub(b);
    }

    public static Expr minus(float a, Expr b) {
        return __e(a).sub(b);
    }

    // double sub

    public static Expr minus(double a, byte b) {
        return Maths.minus(__e(a), __e(b));
    }

    public static Expr minus(double a, short b) {
        return Maths.minus(__e(a), __e(b));
    }

    public static Expr minus(double a, int b) {
        return Maths.minus(__e(a), __e(b));
    }

    public static Expr minus(double a, long b) {
        return Maths.minus(__e(a), __e(b));
    }

    public static Expr minus(double a, float b) {
        return Maths.minus(__e(a), __e(b));
    }

    public static Expr minus(double a, double b) {
        return Maths.minus(__e(a), __e(b));
    }

//    public static Expr sub(double a, BigInteger b) {
//        return BigDecimal.valueOf(a).subtract(new BigDecimal(b));
//    }
//
//    public static Expr sub(double a, BigDecimal b) {
//        return BigDecimal.valueOf(a).subtract(b);
//    }

    public static Expr minus(double a, Complex b) {
        return __e(a).sub(b);
    }

    public static Expr minus(double a, Expr b) {
        return __e(a).sub(b);
    }

    // BigInteger sub

//    public static Expr sub(BigInteger a, byte b) {
//        return a.subtract(BigInteger.valueOf(b));
//    }
//
//    public static Expr sub(BigInteger a, short b) {
//        return a.subtract(BigInteger.valueOf(b));
//    }
//
//    public static Expr sub(BigInteger a, int b) {
//        return a.subtract(BigInteger.valueOf(b));
//    }
//
//    public static Expr sub(BigInteger a, long b) {
//        return a.subtract(BigInteger.valueOf(b));
//    }

//    public static Expr sub(BigInteger a, float b) {
//        return new BigDecimal(a).subtract(BigDecimal.valueOf(b));
//    }
//
//    public static Expr sub(BigInteger a, double b) {
//        return new BigDecimal(a).subtract(BigDecimal.valueOf(b));
//    }
//
//    public static Expr sub(BigInteger a, BigInteger b) {
//        return new BigDecimal(a).subtract(new BigDecimal(b));
//    }
//
//    public static Expr sub(BigInteger a, BigDecimal b) {
//        return new BigDecimal(a).subtract(b);
//    }

//    public static Expr sub(BigInteger a, Complex b) {
//        throw new IllegalArgumentException("Unsupported");
//    }
//
//    public static Expr sub(BigInteger a, Expr b) {
//        throw new IllegalArgumentException("Unsupported");
//    }


    // BigDecimal sub

//    public static Expr sub(BigDecimal a, byte b) {
//        return a.subtract(BigDecimal.valueOf(b));
//    }
//
//    public static Expr sub(BigDecimal a, short b) {
//        return a.subtract(BigDecimal.valueOf(b));
//    }
//
//    public static Expr sub(BigDecimal a, int b) {
//        return a.subtract(BigDecimal.valueOf(b));
//    }
//
//    public static Expr sub(BigDecimal a, long b) {
//        return a.subtract(BigDecimal.valueOf(b));
//    }
//
//    public static Expr sub(BigDecimal a, float b) {
//        return a.subtract(BigDecimal.valueOf(b));
//    }
//
//    public static Expr sub(BigDecimal a, double b) {
//        return a.subtract(BigDecimal.valueOf(b));
//    }
//
//    public static Expr sub(BigDecimal a, BigInteger b) {
//        return a.subtract(new BigDecimal(b));
//    }
//
//    public static Expr sub(BigDecimal a, BigDecimal b) {
//        return a.subtract(b);
//    }
//
//    public static Expr sub(BigDecimal a, Complex b) {
//        throw new IllegalArgumentException("Unsupported");
//    }
//
//    public static Expr sub(BigDecimal a, Expr b) {
//        throw new IllegalArgumentException("Unsupported");
//    }


    // byte mul

    public static Expr mul(byte a, byte b) {
        return mul(__e(a), __e(b));
    }

    public static Expr mul(Expr a, Expr b) {
        return a.mul( b);
    }

    public static Expr mul(byte a, short b) {
        return mul(__e(a), __e(b));
    }

    public static Expr mul(byte a, int b) {
        return mul(__e(a), __e(b));
    }

    public static Expr mul(byte a, long b) {
        return mul(__e(a), __e(b));
    }

    public static Expr mul(byte a, float b) {
        return mul(__e(a), __e(b));
    }

//    public static Expr mul(byte a, BigInteger b) {
//        return BigInteger.valueOf(a).multiply(b);
//    }
//
//    public static Expr mul(byte a, BigDecimal b) {
//        return BigDecimal.valueOf(a).multiply(b);
//    }

    public static Expr mul(byte a, double b) {
        return mul(__e(a), __e(b));
    }

    public static Expr mul(byte a, Complex b) {
        return __e(a).mul(b);
    }

    // short mul

    public static Expr mul(byte a, Expr b) {
        return __e(a).mul(b);
    }

    public static Expr mul(short a, byte b) {
        return mul(__e(a), __e(b));
    }

    public static Expr mul(short a, short b) {
        return mul(__e(a), __e(b));
    }

    public static Expr mul(short a, int b) {
        return mul(__e(a), __e(b));
    }

    public static Expr mul(short a, long b) {
        return mul(__e(a), __e(b));
    }

    public static Expr mul(short a, float b) {
        return mul(__e(a), __e(b));
    }

//    public static Expr mul(short a, BigInteger b) {
//        return BigInteger.valueOf(a).multiply(b);
//    }
//
//    public static Expr mul(short a, BigDecimal b) {
//        return BigDecimal.valueOf(a).multiply(b);
//    }

    public static Expr mul(short a, double b) {
        return mul(__e(a), __e(b));
    }

    public static Expr mul(short a, Complex b) {
        return __e(a).mul(b);
    }

    // int mul

    public static Expr mul(short a, Expr b) {
        return __e(a).mul(b);
    }

    public static Expr mul(int a, byte b) {
        return mul(__e(a), __e(b));
    }

    public static Expr mul(int a, short b) {
        return mul(__e(a), __e(b));
    }

    public static Expr mul(int a, int b) {
        return mul(__e(a), __e(b));
    }

    public static Expr mul(int a, long b) {
        return mul(__e(a), __e(b));
    }

    public static Expr mul(int a, float b) {
        return mul(__e(a), __e(b));
    }

//    public static Expr mul(int a, BigInteger b) {
//        return BigInteger.valueOf(a).multiply(b);
//    }
//
//    public static Expr mul(int a, BigDecimal b) {
//        return BigDecimal.valueOf(a).multiply(b);
//    }

    public static Expr mul(int a, double b) {
        return mul(__e(a), __e(b));
    }

    public static Expr mul(int a, Complex b) {
        return __e(a).mul(b);
    }

    // long mul

    public static Expr mul(int a, Expr b) {
        return __e(a).mul(b);
    }

    public static Expr mul(long a, byte b) {
        return mul(__e(a), __e(b));
    }

    public static Expr mul(long a, short b) {
        return mul(__e(a), __e(b));
    }

    public static Expr mul(long a, int b) {
        return mul(__e(a), __e(b));
    }

    public static Expr mul(long a, long b) {
        return mul(__e(a), __e(b));
    }

    public static Expr mul(long a, float b) {
        return mul(__e(a), __e(b));
    }

//    public static Expr mul(long a, BigInteger b) {
//        return BigInteger.valueOf(a).multiply(b);
//    }
//
//    public static Expr mul(long a, BigDecimal b) {
//        return BigDecimal.valueOf(a).multiply(b);
//    }

    public static Expr mul(long a, double b) {
        return mul(__e(a), __e(b));
    }

    public static Expr mul(long a, Complex b) {
        return __e(a).mul(b);
    }

    // float mul

    public static Expr mul(long a, Expr b) {
        return __e(a).mul(b);
    }

    public static Expr mul(float a, byte b) {
        return mul(__e(a), __e(b));
    }

    public static Expr mul(float a, short b) {
        return mul(__e(a), __e(b));
    }

    public static Expr mul(float a, int b) {
        return mul(__e(a), __e(b));
    }

    public static Expr mul(float a, long b) {
        return mul(__e(a), __e(b));
    }

    public static Expr mul(float a, float b) {
        return mul(__e(a), __e(b));
    }

//    public static Expr mul(float a, BigInteger b) {
//        return BigDecimal.valueOf(a).multiply(new BigDecimal(b));
//    }
//
//    public static Expr mul(float a, BigDecimal b) {
//        return BigDecimal.valueOf(a).multiply(b);
//    }

    public static Expr mul(float a, double b) {
        return mul(__e(a), __e(b));
    }

    public static Expr mul(float a, Complex b) {
        return __e(a).mul(b);
    }

    // double mul

    public static Expr mul(float a, Expr b) {
        return __e(a).mul(b);
    }

    public static Expr mul(double a, byte b) {
        return mul(__e(a), __e(b));
    }

    public static Expr mul(double a, short b) {
        return mul(__e(a), __e(b));
    }

    public static Expr mul(double a, int b) {
        return mul(__e(a), __e(b));
    }

    public static Expr mul(double a, long b) {
        return mul(__e(a), __e(b));
    }

    public static Expr mul(double a, float b) {
        return mul(__e(a), __e(b));
    }

//    public static Expr mul(double a, BigInteger b) {
//        return BigDecimal.valueOf(a).multiply(new BigDecimal(b));
//    }
//
//    public static Expr mul(double a, BigDecimal b) {
//        return BigDecimal.valueOf(a).multiply(b);
//    }

    public static Expr mul(double a, double b) {
        return mul(__e(a), __e(b));
    }

    public static Expr mul(double a, Complex b) {
        return __e(a).mul(b);
    }

    // BigInteger mul

//    public static Expr mul(BigInteger a, byte b) {
//        return a.multiply(BigInteger.valueOf(b));
//    }
//
//    public static Expr mul(BigInteger a, short b) {
//        return a.multiply(BigInteger.valueOf(b));
//    }
//
//    public static Expr mul(BigInteger a, int b) {
//        return a.multiply(BigInteger.valueOf(b));
//    }
//
//    public static Expr mul(BigInteger a, long b) {
//        return a.multiply(BigInteger.valueOf(b));
//    }
//
//    public static Expr mul(BigInteger a, float b) {
//        return new BigDecimal(a).multiply(BigDecimal.valueOf(b));
//    }
//
//    public static Expr mul(BigInteger a, double b) {
//        return new BigDecimal(a).multiply(BigDecimal.valueOf(b));
//    }
//
//    public static Expr mul(BigInteger a, BigInteger b) {
//        return new BigDecimal(a).multiply(new BigDecimal(b));
//    }
//
//    public static Expr mul(BigInteger a, BigDecimal b) {
//        return new BigDecimal(a).multiply(b);
//    }

//    public static Expr mul(BigInteger a, Complex b) {
//        throw new IllegalArgumentException("Unsupported");
//    }
//
//    public static Expr mul(BigInteger a, Expr b) {
//        throw new IllegalArgumentException("Unsupported");
//    }


    // BigDecimal mul

//    public static Expr mul(BigDecimal a, byte b) {
//        return a.multiply(BigDecimal.valueOf(b));
//    }
//
//    public static Expr mul(BigDecimal a, short b) {
//        return a.multiply(BigDecimal.valueOf(b));
//    }
//
//    public static Expr mul(BigDecimal a, int b) {
//        return a.multiply(BigDecimal.valueOf(b));
//    }
//
//    public static Expr mul(BigDecimal a, long b) {
//        return a.multiply(BigDecimal.valueOf(b));
//    }
//
//    public static Expr mul(BigDecimal a, float b) {
//        return a.multiply(BigDecimal.valueOf(b));
//    }
//
//    public static Expr mul(BigDecimal a, double b) {
//        return a.multiply(BigDecimal.valueOf(b));
//    }
//
//    public static Expr mul(BigDecimal a, BigInteger b) {
//        return a.multiply(new BigDecimal(b));
//    }
//
//    public static Expr mul(BigDecimal a, BigDecimal b) {
//        return a.multiply(b);
//    }
//
//    public static Expr mul(BigDecimal a, Complex b) {
//        throw new IllegalArgumentException("Unsupported");
//    }
//
//    public static Expr mul(BigDecimal a, Expr b) {
//        throw new IllegalArgumentException("Unsupported");
//    }


    // byte div

    public static Expr mul(double a, Expr b) {
        return __e(a).mul(b);
    }

    public static Expr div(byte a, byte b) {
        return Div.of(__e(a), __e(b));
    }

    public static Expr div(byte a, short b) {
        return Div.of(__e(a), __e(b));
    }

    public static Expr div(byte a, int b) {
        return Div.of(__e(a), __e(b));
    }

    public static Expr div(byte a, long b) {
        return Div.of(__e(a), __e(b));
    }

    public static Expr div(byte a, float b) {
        return Div.of(__e(a), __e(b));
    }

//    public static Expr div(byte a, BigInteger b) {
//        return BigInteger.valueOf(a).multiply(b);
//    }
//
//    public static Expr div(byte a, BigDecimal b) {
//        return BigDecimal.valueOf(a).multiply(b);
//    }

    public static Expr div(byte a, double b) {
        return Div.of(__e(a), __e(b));
    }

    public static Expr div(byte a, Complex b) {
        return __e(a).div(b);
    }

    // short  div

    public static Expr div(byte a, Expr b) {
        return __e(a).div(b);
    }

    public static Expr div(short a, byte b) {
        return Div.of(__e(a), __e(b));
    }

    public static Expr div(short a, short b) {
        return Div.of(__e(a), __e(b));
    }

    public static Expr div(short a, int b) {
        return Div.of(__e(a), __e(b));
    }

    public static Expr div(short a, long b) {
        return Div.of(__e(a), __e(b));
    }

    public static Expr div(short a, float b) {
        return Div.of(__e(a), __e(b));
    }

//    public static Expr div(short a, BigInteger b) {
//        return BigInteger.valueOf(a).multiply(b);
//    }
//
//    public static Expr div(short a, BigDecimal b) {
//        return BigDecimal.valueOf(a).multiply(b);
//    }

    public static Expr div(short a, double b) {
        return Div.of(__e(a), __e(b));
    }

    public static Expr div(short a, Complex b) {
        return __e(a).div(b);
    }

    // int  div

    public static Expr div(short a, Expr b) {
        return __e(a).div(b);
    }

    public static Expr div(int a, byte b) {
        return Div.of(__e(a), __e(b));
    }

    public static Expr div(int a, short b) {
        return Div.of(__e(a), __e(b));
    }

    public static Expr div(int a, int b) {
        return Div.of(__e(a), __e(b));
    }

    public static Expr div(int a, long b) {
        return Div.of(__e(a), __e(b));
    }

    public static Expr div(int a, float b) {
        return Div.of(__e(a), __e(b));
    }

//    public static Expr div(int a, BigInteger b) {
//        return BigInteger.valueOf(a).multiply(b);
//    }
//
//    public static Expr div(int a, BigDecimal b) {
//        return BigDecimal.valueOf(a).multiply(b);
//    }

    public static Expr div(int a, double b) {
        return Div.of(__e(a), __e(b));
    }

    public static Expr div(int a, Complex b) {
        return Div.of(__e(a),__e(b));
    }

    // long  div

    public static Expr div(int a, Expr b) {
        return Div.of(__e(a),b);
    }

    public static Expr div(long a, byte b) {
        return Div.of(__e(a), __e(b));
    }

    public static Expr div(long a, short b) {
        return Div.of(__e(a), __e(b));
    }

    public static Expr div(long a, int b) {
        return Div.of(__e(a), __e(b));
    }

    public static Expr div(long a, long b) {
        return Div.of(__e(a), __e(b));
    }

    public static Expr div(long a, float b) {
        return Div.of(__e(a), __e(b));
    }

//    public static Expr div(long a, BigInteger b) {
//        return BigInteger.valueOf(a).multiply(b);
//    }
//
//    public static Expr div(long a, BigDecimal b) {
//        return BigDecimal.valueOf(a).multiply(b);
//    }

    public static Expr div(long a, double b) {
        return Div.of(__e(a), __e(b));
    }

    public static Expr div(long a, Complex b) {
        return __e(a).div(b);
    }

    // float  div

    public static Expr div(long a, Expr b) {
        return __e(a).div(b);
    }

    public static Expr div(float a, byte b) {
        return Div.of(__e(a), __e(b));
    }

    public static Expr div(float a, short b) {
        return Div.of(__e(a), __e(b));
    }

    public static Expr div(float a, int b) {
        return Div.of(__e(a), __e(b));
    }

    public static Expr div(float a, long b) {
        return Div.of(__e(a), __e(b));
    }

    public static Expr div(float a, float b) {
        return Div.of(__e(a), __e(b));
    }

//    public static Expr div(float a, BigInteger b) {
//        return BigDecimal.valueOf(a).multiply(new BigDecimal(b));
//    }
//
//    public static Expr div(float a, BigDecimal b) {
//        return BigDecimal.valueOf(a).multiply(b);
//    }

    public static Expr div(float a, double b) {
        return Div.of(__e(a), __e(b));
    }

    public static Expr div(float a, Complex b) {
        return Div.of(__e(a),__e(b));
    }

    // double  div

    public static Expr div(float a, Expr b) {
        return Div.of(__e(a),b);
    }

    public static Expr div(double a, byte b) {
        return Div.of(__e(a), __e(b));
    }

    public static Expr div(double a, short b) {
        return Div.of(__e(a), __e(b));
    }

    public static Expr div(double a, int b) {
        return Div.of(__e(a), __e(b));
    }

    public static Expr div(double a, long b) {
        return Div.of(__e(a), __e(b));
    }

    public static Expr div(double a, float b) {
        return Div.of(__e(a), __e(b));
    }

//    public static Expr div(double a, BigInteger b) {
//        return BigDecimal.valueOf(a).multiply(new BigDecimal(b));
//    }
//
//    public static Expr div(double a, BigDecimal b) {
//        return BigDecimal.valueOf(a).multiply(b);
//    }

    public static Expr div(double a, double b) {
        return Div.of(__e(a), __e(b));
    }

    public static Expr div(double a, Complex b) {
        return Div.of(__e(a),__e(b));
    }

    // BigInteger  div

//    public static Expr div(BigInteger a, byte b) {
//        return a.multiply(BigInteger.valueOf(b));
//    }
//
//    public static Expr div(BigInteger a, short b) {
//        return a.multiply(BigInteger.valueOf(b));
//    }
//
//    public static Expr div(BigInteger a, int b) {
//        return a.multiply(BigInteger.valueOf(b));
//    }
//
//    public static Expr div(BigInteger a, long b) {
//        return a.multiply(BigInteger.valueOf(b));
//    }
//
//    public static Expr div(BigInteger a, float b) {
//        return new BigDecimal(a).multiply(BigDecimal.valueOf(b));
//    }
//
//    public static Expr div(BigInteger a, double b) {
//        return new BigDecimal(a).multiply(BigDecimal.valueOf(b));
//    }
//
//    public static Expr div(BigInteger a, BigInteger b) {
//        return new BigDecimal(a).multiply(new BigDecimal(b));
//    }
//
//    public static Expr div(BigInteger a, BigDecimal b) {
//        return new BigDecimal(a).multiply(b);
//    }

//    public static Expr  div(BigInteger a, Complex b) {
//        throw new IllegalArgumentException("Unsupported");
//    }
//
//    public static Expr  div(BigInteger a, Expr b) {
//        throw new IllegalArgumentException("Unsupported");
//    }


    // BigDecimal  div

//    public static Expr div(BigDecimal a, byte b) {
//        return a.multiply(BigDecimal.valueOf(b));
//    }
//
//    public static Expr div(BigDecimal a, short b) {
//        return a.multiply(BigDecimal.valueOf(b));
//    }
//
//    public static Expr div(BigDecimal a, int b) {
//        return a.multiply(BigDecimal.valueOf(b));
//    }
//
//    public static Expr div(BigDecimal a, long b) {
//        return a.multiply(BigDecimal.valueOf(b));
//    }
//
//    public static Expr div(BigDecimal a, float b) {
//        return a.multiply(BigDecimal.valueOf(b));
//    }
//
//    public static Expr div(BigDecimal a, double b) {
//        return a.multiply(BigDecimal.valueOf(b));
//    }
//
//    public static Expr div(BigDecimal a, BigInteger b) {
//        return a.multiply(new BigDecimal(b));
//    }
//
//    public static Expr div(BigDecimal a, BigDecimal b) {
//        return a.multiply(b);
//    }
//
//    public static Expr  div(BigDecimal a, Complex b) {
//        throw new IllegalArgumentException("Unsupported");
//    }
//
//    public static Expr  div(BigDecimal a, Expr b) {
//        throw new IllegalArgumentException("Unsupported");
//    }

    public static Expr div(double a, Expr b) {
        return Div.of(__e(a),b);
    }

    public static Expr mul(Expr a, double b) {
        return Mul.of(a, __e(b));
    }

    public static Expr mul(Expr a, Complex b) {
        return Mul.of(a, (Expr) b);
    }

    public static Expr plus(Expr a, Expr b) {
        return Plus.of(a, b);
    }

    public static Expr minus(Expr a, Expr b) {
        return Minus.of(a, b);
    }

    public static Expr div(Expr a, Expr b) {
        return Div.of(a, b);
    }

    public static Expr neg(Expr a) {
        return Neg.of(a);
    }

    public static Expr cos(Expr a) {
        return Cos.of(a);
    }

    public static Expr cos(double a) {
        return Cos.of(__e(a));
    }

    public static Expr sin(Expr a) {
        return Sin.of(a);
    }

    public static Expr sin(double a) {
        return Sin.of(__e(a));
    }

    public static Expr tan(Expr a) {
        return Tan.of(a);
    }

    public static Expr tan(double a) {
        return Tan.of(__e(a));
    }

    public static Expr atan(Expr a) {
        return Atan.of(a);
    }

    public static Expr atan(double a) {
        return Atan.of(__e(a));
    }

    public static Expr cotan(Expr a) {
        return Cotan.of(a);
    }

    public static Expr cotan(double a) {
        return Cotan.of(__e(a));
    }

    public static Expr acotan(Expr a) {
        return Acotan.of(a);
    }

    public static Expr acotan(double a) {
        return Acotan.of(__e(a));
    }

    public static Expr acos(Expr a) {
        return Acos.of(a);
    }

    public static Expr acos(double a) {
        return Acos.of(__e(a));
    }

    public static Expr asin(Expr a) {
        return Asin.of(a);
    }

    public static Expr asin(double a) {
        return Asin.of(__e(a));
    }

    public static Expr cosh(Expr a) {
        return Cosh.of(a);
    }

    public static Expr cosh(double a) {
        return Cosh.of(__e(a));
    }

    public static Expr sinh(Expr a) {
        return Sinh.of(a);
    }

    public static Expr sinh(double a) {
        return Sinh.of(__e(a));
    }

    public static Expr tanh(Expr a) {
        return Tanh.of(a);
    }

    public static Expr tanh(double a) {
        return Tanh.of(__e(a));
    }

    public static Expr cotanh(Expr a) {
        return Cotanh.of(a);
    }

    public static Expr cotanh(double a) {
        return Cotanh.of(__e(a));
    }

    public static Expr scalarProduct(Expr a, Expr b) {
        return ParametrizedScalarProduct.of(a, b);
    }

    public static <A, B> RightArrowUplet2<A, B> rightArrow(A a, B b) {
        return new RightArrowUplet2<A, B>(a, b);
    }

    public static RightArrowUplet2.Double rightArrow(double a, double b) {
        return new RightArrowUplet2.Double(a, b);
    }

    public static RightArrowUplet2.Complex rightArrow(Complex a, Complex b) {
        return new RightArrowUplet2.Complex(a, b);
    }

    public static RightArrowUplet2.Expr rightArrow(Expr a, Expr b) {
        return new RightArrowUplet2.Expr(a, b);
    }

    public static Expr domain(RightArrowUplet2.Double u) {
        return Domain.ofBounds(u.getFirst(), u.getSecond());
    }

    public static Expr domain(RightArrowUplet2.Double ux, RightArrowUplet2.Double uy) {
        return Domain.ofBounds(ux.getFirst(), ux.getSecond(), uy.getFirst(), uy.getSecond());
    }

    public static Expr domain(double ux, double uy) {
        return Domain.ofBounds(ux, uy);
    }

    public static Expr domain(Expr ux, Expr uy) {
        return DomainExpr.ofBounds(ux, uy);
    }

    public static Expr domain(RightArrowUplet2.Double ux, RightArrowUplet2.Double uy, RightArrowUplet2.Double uz) {
        return Domain.ofBounds(ux.getFirst(), ux.getSecond(), uy.getFirst(), uy.getSecond(), uz.getFirst(), uz.getSecond());
    }

    public static Expr domain(RightArrowUplet2<Expr, Expr> u) {
        if (u.getFirst().isNarrow(ExprType.DOUBLE_NBR) && u.getSecond().isNarrow(ExprType.DOUBLE_NBR)) {
            return Domain.ofBounds(u.getFirst().toDouble(), u.getSecond().toDouble());
        }
        return DomainExpr.ofBounds(u.getFirst(), u.getSecond());
    }

    public static Expr domain(RightArrowUplet2<Expr, Expr> ux, RightArrowUplet2<Expr, Expr> uy) {
        Expr xfirst = ux.getFirst();
        Expr xsecond = ux.getSecond();
        Expr yfirst = uy.getFirst();
        Expr ysecond = uy.getSecond();
        if (xfirst.isNarrow(ExprType.DOUBLE_NBR) && xsecond.isNarrow(ExprType.DOUBLE_NBR) && yfirst.isNarrow(ExprType.DOUBLE_NBR) && ysecond.isNarrow(ExprType.DOUBLE_NBR)) {
            return Domain.ofBounds(xfirst.toDouble(), xsecond.toDouble(), yfirst.toDouble(), ysecond.toDouble());
        }
        return DomainExpr.ofBounds(xfirst, xsecond, yfirst, ysecond);
    }

    public static Expr domain(RightArrowUplet2<Expr, Expr> ux, RightArrowUplet2<Expr, Expr> uy, RightArrowUplet2<Expr, Expr> uz) {
        if (ux.getFirst().isNarrow(ExprType.DOUBLE_NBR) && ux.getSecond().isNarrow(ExprType.DOUBLE_NBR) && uy.getFirst().isNarrow(ExprType.DOUBLE_NBR) && uy.getSecond().isNarrow(ExprType.DOUBLE_NBR) && uz.getFirst().isNarrow(ExprType.DOUBLE_NBR) && uz.getSecond().isNarrow(ExprType.DOUBLE_NBR)) {
            return Domain.ofBounds(ux.getFirst().toDouble(), ux.getSecond().toDouble(), uy.getFirst().toDouble(), uy.getSecond().toDouble(), uz.getFirst().toDouble(), uz.getSecond().toDouble());
        }
        return DomainExpr.ofBounds(ux.getFirst(), ux.getSecond(), uy.getFirst(), uy.getSecond(), uz.getFirst(), uz.getSecond());
    }

    public static DomainExpr xdomain(Expr min, Expr max) {
        return DomainExpr.ofBounds(min, max);
    }

    public static Domain xdomain(double min, double max) {
        return Domain.ofBounds(min, max);
    }

    public static Domain ydomain(double min, double max) {
        return Domain.ofBounds(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, min, max);
    }

    public static DomainExpr ydomain(DomainExpr min, DomainExpr max) {
        return DomainExpr.ofBounds(Complex.NEGATIVE_INFINITY, Complex.POSITIVE_INFINITY, min, max);
    }

    public static Domain zdomain(double min, double max) {
        return Domain.ofBounds(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, min, max);
    }

    public static DomainExpr zdomain(Expr min, Expr max) {
        return DomainExpr.ofBounds(Complex.NEGATIVE_INFINITY, Complex.POSITIVE_INFINITY, Complex.NEGATIVE_INFINITY, Complex.POSITIVE_INFINITY, min, max);
    }

    //    public static Domain domain(double min,double max){
//        return Domain.forBounds(min,max);
//    }
//
    public static Domain domain(double xmin, double xmax, double ymin, double ymax) {
        return Domain.ofBounds(xmin, xmax, ymin, ymax);
    }

    public static Domain domain(double xmin, double xmax, double ymin, double ymax, double zmin, double zmax) {
        return Domain.ofBounds(xmin, xmax, ymin, ymax, zmin, zmax);
    }

    public static DomainExpr domain(Expr xmin, Expr xmax, Expr ymin, Expr ymax) {
        return DomainExpr.ofBounds(xmin, xmax, ymin, ymax);
    }

    public static DomainExpr domain(Expr xmin, Expr xmax, Expr ymin, Expr ymax, Expr zmin, Expr zmax) {
        return DomainExpr.ofBounds(xmin, xmax, ymin, ymax, zmin, zmax);
    }

    public static Domain xyzdomain(double xmin, double xmax, double ymin, double ymax, double zmin, double zmax) {
        return Domain.ofBounds(xmin, xmax, ymin, ymax, zmin, zmax);
    }

    public static DomainExpr xyzdomain(Expr xmin, Expr xmax, Expr ymin, Expr ymax, Expr zmin, Expr zmax) {
        return DomainExpr.ofBounds(xmin, xmax, ymin, ymax, zmin, zmax);
    }

    public static Expr sqrt(Expr a) {
        return Sqrt.of(a);
    }

    public static Expr sqrt(Complex a) {
        return Sqrt.of(a);
    }

    public static Expr sqr(Expr a) {
        return Sqr.of(a);
    }

    public static Expr sqr(Complex a) {
        return Sqr.of(a);
    }

    public static Expr sqr(int a) {
        return Sqr.of(__e(a));
    }

    public static Expr sqr(float a) {
        return Sqr.of(__e(a));
    }

    public static Expr sqr(long a) {
        return Sqr.of(__e(a));
    }

    public static Expr sqr(double a) {
        return Sqr.of(__e(a));
    }

    public static Expr real(double a) {
        return Real.of(__e(a));
    }

    public static Expr real(Complex a) {
        return Real.of(a);
    }

    public static Expr real(Expr a) {
        return Real.of(a);
    }

    public static Expr imag(double a) {
        return Imag.of(__e(0));
    }

    public static Expr imag(Complex a) {
        return Imag.of(a);
    }

    public static Expr imag(Expr a) {
        return Imag.of(a);
    }

    private static Expr __e(Complex d){
        return d;
    }

    private static Expr __e(double d){
        return Maths.expr(d);
    }
}
