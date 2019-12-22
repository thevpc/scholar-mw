package net.vpc.scholar.hadrumaths.expeval;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.symbolic.DomainExpr;
import net.vpc.scholar.hadrumaths.symbolic.ParametrizedScalarProduct;


public class PlatformHelperAsExpr {

    // neg

    public static Expr neg(byte a) {
        return MathsBase.neg(MathsBase.expr(a));
    }

    public static Expr neg(short a) {
        return MathsBase.neg(MathsBase.expr(a));
    }

    public static Expr neg(int a) {
        return MathsBase.neg(MathsBase.expr(a));
    }

    public static Expr neg(long a) {
        return MathsBase.neg(MathsBase.expr(a));
    }

    public static Expr neg(float a) {
        return MathsBase.neg(MathsBase.expr(a));
    }

    public static double neg(double a) {
        return -a;//MathsBase.neg(MathsBase.expr(a));
    }
    public static Complex neg(Complex a) {
        return a.neg();//MathsBase.neg(MathsBase.expr(a));
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

    public static Expr add(byte a, byte b) {
        return MathsBase.add(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr add(byte a, short b) {
        return MathsBase.add(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr add(byte a, int b) {
        return MathsBase.add(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr add(byte a, long b) {
        return MathsBase.add(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr add(byte a, float b) {
        return MathsBase.add(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr add(byte a, double b) {
        return MathsBase.add(MathsBase.expr(a),MathsBase.expr(b));
    }

//    public static Expr add(byte a, BigInteger b) {
//        return BigInteger.valueOf(a).add(b);
//    }
//
//    public static Expr add(byte a, BigDecimal b) {
//        return BigDecimal.valueOf(a).add(b);
//    }

    public static Expr add(byte a, Complex b) {
        return MathsBase.expr(a).add(b);
    }

    public static Expr add(byte a, Expr b) {
        return MathsBase.expr(a).add(b);
    }

    // short ADD

    public static Expr add(short a, byte b) {
        return MathsBase.add(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr add(short a, short b) {
        return MathsBase.add(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr add(short a, int b) {
        return MathsBase.add(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr add(short a, long b) {
        return MathsBase.add(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr add(short a, float b) {
        return MathsBase.add(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr add(short a, double b) {
        return MathsBase.add(MathsBase.expr(a),MathsBase.expr(b));
    }

//    public static Expr add(short a, BigInteger b) {
//        return BigInteger.valueOf(a).add(b);
//    }
//
//    public static Expr add(short a, BigDecimal b) {
//        return BigDecimal.valueOf(a).add(b);
//    }

    public static Expr add(short a, Complex b) {
        return MathsBase.expr(a).add(b);
    }

    public static Expr add(short a, Expr b) {
        return MathsBase.expr(a).add(b);
    }

    // int ADD

    public static Expr add(int a, byte b) {
        return MathsBase.add(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr add(int a, short b) {
        return MathsBase.add(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr add(int a, int b) {
        return MathsBase.add(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr add(int a, long b) {
        return MathsBase.add(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr add(int a, float b) {
        return MathsBase.add(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr add(int a, double b) {
        return MathsBase.add(MathsBase.expr(a),MathsBase.expr(b));
    }

//    public static Expr add(int a, BigInteger b) {
//        return BigInteger.valueOf(a).add(b);
//    }
//
//    public static Expr add(int a, BigDecimal b) {
//        return BigDecimal.valueOf(a).add(b);
//    }

    public static Expr add(int a, Complex b) {
        return MathsBase.expr(a).add(b);
    }

    public static Expr add(int a, Expr b) {
        return MathsBase.expr(a).add(b);
    }

    // long ADD

    public static Expr add(long a, byte b) {
        return MathsBase.add(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr add(long a, short b) {
        return MathsBase.add(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr add(long a, int b) {
        return MathsBase.add(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr add(long a, long b) {
        return MathsBase.add(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr add(long a, float b) {
        return MathsBase.add(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr add(long a, double b) {
        return MathsBase.add(MathsBase.expr(a),MathsBase.expr(b));
    }

//    public static Expr add(long a, BigInteger b) {
//        return BigInteger.valueOf(a).add(b);
//    }
//
//    public static Expr add(long a, BigDecimal b) {
//        return BigDecimal.valueOf(a).add(b);
//    }

    public static Expr add(long a, Complex b) {
        return MathsBase.expr(a).add(b);
    }

    public static Expr add(long a, Expr b) {
        return MathsBase.expr(a).add(b);
    }

    // float ADD

    public static Expr add(float a, byte b) {
        return MathsBase.add(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr add(float a, short b) {
        return MathsBase.add(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr add(float a, int b) {
        return MathsBase.add(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr add(float a, long b) {
        return MathsBase.add(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr add(float a, float b) {
        return MathsBase.add(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr add(float a, double b) {
        return MathsBase.add(MathsBase.expr(a),MathsBase.expr(b));
    }

//    public static Expr add(float a, BigInteger b) {
//        return BigDecimal.valueOf(a).add(new BigDecimal(b));
//    }
//
//    public static Expr add(float a, BigDecimal b) {
//        return BigDecimal.valueOf(a).add(b);
//    }

    public static Expr add(float a, Complex b) {
        return MathsBase.expr(a).add(b);
    }

    public static Expr add(float a, Expr b) {
        return MathsBase.expr(a).add(b);
    }

    // double ADD

    public static Expr add(double a, byte b) {
        return MathsBase.add(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr add(double a, short b) {
        return MathsBase.add(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr add(double a, int b) {
        return MathsBase.add(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr add(double a, long b) {
        return MathsBase.add(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr add(double a, float b) {
        return MathsBase.add(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr add(double a, double b) {
        return MathsBase.add(MathsBase.expr(a),MathsBase.expr(b));
    }

//    public static Expr add(double a, BigInteger b) {
//        return BigDecimal.valueOf(a).add(new BigDecimal(b));
//    }
//
//    public static Expr add(double a, BigDecimal b) {
//        return BigDecimal.valueOf(a).add(b);
//    }

    public static Expr add(double a, Complex b) {
        return MathsBase.expr(a).add(b);
    }

    public static Expr add(double a, Expr b) {
        return MathsBase.expr(a).add(b);
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

    public static Expr sub(byte a, byte b) {
        return MathsBase.sub(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr sub(byte a, short b) {
        return MathsBase.sub(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr sub(byte a, int b) {
        return MathsBase.sub(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr sub(byte a, long b) {
        return MathsBase.sub(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr sub(byte a, float b) {
        return MathsBase.sub(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr sub(byte a, double b) {
        return MathsBase.sub(MathsBase.expr(a),MathsBase.expr(b));
    }

//    public static Expr sub(byte a, BigInteger b) {
//        return BigInteger.valueOf(a).subtract(b);
//    }
//
//    public static Expr sub(byte a, BigDecimal b) {
//        return BigDecimal.valueOf(a).subtract(b);
//    }

    public static Expr sub(byte a, Complex b) {
        return MathsBase.expr(a).sub(b);
    }

    public static Expr sub(byte a, Expr b) {
        return MathsBase.expr(a).sub(b);
    }

    // short sub

    public static Expr sub(short a, byte b) {
        return MathsBase.sub(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr sub(short a, short b) {
        return MathsBase.sub(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr sub(short a, int b) {
        return MathsBase.sub(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr sub(short a, long b) {
        return MathsBase.sub(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr sub(short a, float b) {
        return MathsBase.sub(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr sub(short a, double b) {
        return MathsBase.sub(MathsBase.expr(a),MathsBase.expr(b));
    }

//    public static Expr sub(short a, BigInteger b) {
//        return BigInteger.valueOf(a).subtract(b);
//    }
//
//    public static Expr sub(short a, BigDecimal b) {
//        return BigDecimal.valueOf(a).subtract(b);
//    }

    public static Expr sub(short a, Complex b) {
        return MathsBase.expr(a).sub(b);
    }

    public static Expr sub(short a, Expr b) {
        return MathsBase.expr(a).sub(b);
    }

    // int sub

    public static Expr sub(int a, byte b) {
        return MathsBase.sub(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr sub(int a, short b) {
        return MathsBase.sub(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr sub(int a, int b) {
        return MathsBase.sub(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr sub(int a, long b) {
        return MathsBase.sub(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr sub(int a, float b) {
        return MathsBase.sub(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr sub(int a, double b) {
        return MathsBase.sub(MathsBase.expr(a),MathsBase.expr(b));
    }

//    public static Expr sub(int a, BigInteger b) {
//        return BigInteger.valueOf(a).subtract(b);
//    }
//
//    public static Expr sub(int a, BigDecimal b) {
//        return BigDecimal.valueOf(a).subtract(b);
//    }

    public static Expr sub(int a, Complex b) {
        return MathsBase.expr(a).sub(b);
    }

    public static Expr sub(int a, Expr b) {
        return MathsBase.expr(a).sub(b);
    }

    // long sub

    public static Expr sub(long a, byte b) {
        return MathsBase.sub(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr sub(long a, short b) {
        return MathsBase.sub(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr sub(long a, int b) {
        return MathsBase.sub(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr sub(long a, long b) {
        return MathsBase.sub(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr sub(long a, float b) {
        return MathsBase.sub(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr sub(long a, double b) {
        return MathsBase.sub(MathsBase.expr(a),MathsBase.expr(b));
    }

//    public static Expr sub(long a, BigInteger b) {
//        return BigInteger.valueOf(a).subtract(b);
//    }
//
//    public static Expr sub(long a, BigDecimal b) {
//        return BigDecimal.valueOf(a).subtract(b);
//    }

    public static Expr sub(long a, Complex b) {
        return MathsBase.expr(a).sub(b);
    }

    public static Expr sub(long a, Expr b) {
        return MathsBase.expr(a).sub(b);
    }

    // float sub

    public static Expr sub(float a, byte b) {
        return MathsBase.sub(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr sub(float a, short b) {
        return MathsBase.sub(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr sub(float a, int b) {
        return MathsBase.sub(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr sub(float a, long b) {
        return MathsBase.sub(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr sub(float a, float b) {
        return MathsBase.sub(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr sub(float a, double b) {
        return MathsBase.sub(MathsBase.expr(a),MathsBase.expr(b));
    }

//    public static Expr sub(float a, BigInteger b) {
//        return BigDecimal.valueOf(a).subtract(new BigDecimal(b));
//    }
//
//    public static Expr sub(float a, BigDecimal b) {
//        return BigDecimal.valueOf(a).subtract(b);
//    }

    public static Expr sub(float a, Complex b) {
        return MathsBase.expr(a).sub(b);
    }

    public static Expr sub(float a, Expr b) {
        return MathsBase.expr(a).sub(b);
    }

    // double sub

    public static Expr sub(double a, byte b) {
        return MathsBase.sub(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr sub(double a, short b) {
        return MathsBase.sub(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr sub(double a, int b) {
        return MathsBase.sub(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr sub(double a, long b) {
        return MathsBase.sub(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr sub(double a, float b) {
        return MathsBase.sub(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr sub(double a, double b) {
        return MathsBase.sub(MathsBase.expr(a),MathsBase.expr(b));
    }

//    public static Expr sub(double a, BigInteger b) {
//        return BigDecimal.valueOf(a).subtract(new BigDecimal(b));
//    }
//
//    public static Expr sub(double a, BigDecimal b) {
//        return BigDecimal.valueOf(a).subtract(b);
//    }

    public static Expr sub(double a, Complex b) {
        return MathsBase.expr(a).sub(b);
    }

    public static Expr sub(double a, Expr b) {
        return MathsBase.expr(a).sub(b);
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
        return mul(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr mul(byte a, short b) {
        return mul(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr mul(byte a, int b) {
        return mul(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr mul(byte a, long b) {
        return mul(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr mul(byte a, float b) {
        return mul(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr mul(byte a, double b) {
        return mul(MathsBase.expr(a),MathsBase.expr(b));
    }

//    public static Expr mul(byte a, BigInteger b) {
//        return BigInteger.valueOf(a).multiply(b);
//    }
//
//    public static Expr mul(byte a, BigDecimal b) {
//        return BigDecimal.valueOf(a).multiply(b);
//    }

    public static Expr mul(byte a, Complex b) {
        return MathsBase.expr(a).mul(b);
    }

    public static Expr mul(byte a, Expr b) {
        return MathsBase.expr(a).mul(b);
    }

    // short mul

    public static Expr mul(short a, byte b) {
        return mul(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr mul(short a, short b) {
        return mul(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr mul(short a, int b) {
        return mul(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr mul(short a, long b) {
        return mul(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr mul(short a, float b) {
        return mul(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr mul(short a, double b) {
        return mul(MathsBase.expr(a),MathsBase.expr(b));
    }

//    public static Expr mul(short a, BigInteger b) {
//        return BigInteger.valueOf(a).multiply(b);
//    }
//
//    public static Expr mul(short a, BigDecimal b) {
//        return BigDecimal.valueOf(a).multiply(b);
//    }

    public static Expr mul(short a, Complex b) {
        return MathsBase.expr(a).mul(b);
    }

    public static Expr mul(short a, Expr b) {
        return MathsBase.expr(a).mul(b);
    }

    // int mul

    public static Expr mul(int a, byte b) {
        return mul(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr mul(int a, short b) {
        return mul(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr mul(int a, int b) {
        return mul(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr mul(int a, long b) {
        return mul(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr mul(int a, float b) {
        return mul(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr mul(int a, double b) {
        return mul(MathsBase.expr(a),MathsBase.expr(b));
    }

//    public static Expr mul(int a, BigInteger b) {
//        return BigInteger.valueOf(a).multiply(b);
//    }
//
//    public static Expr mul(int a, BigDecimal b) {
//        return BigDecimal.valueOf(a).multiply(b);
//    }

    public static Expr mul(int a, Complex b) {
        return MathsBase.expr(a).mul(b);
    }

    public static Expr mul(int a, Expr b) {
        return MathsBase.expr(a).mul(b);
    }

    // long mul

    public static Expr mul(long a, byte b) {
        return mul(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr mul(long a, short b) {
        return mul(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr mul(long a, int b) {
        return mul(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr mul(long a, long b) {
        return mul(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr mul(long a, float b) {
        return mul(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr mul(long a, double b) {
        return mul(MathsBase.expr(a),MathsBase.expr(b));
    }

//    public static Expr mul(long a, BigInteger b) {
//        return BigInteger.valueOf(a).multiply(b);
//    }
//
//    public static Expr mul(long a, BigDecimal b) {
//        return BigDecimal.valueOf(a).multiply(b);
//    }

    public static Expr mul(long a, Complex b) {
        return MathsBase.expr(a).mul(b);
    }

    public static Expr mul(long a, Expr b) {
        return MathsBase.expr(a).mul(b);
    }

    // float mul

    public static Expr mul(float a, byte b) {
        return mul(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr mul(float a, short b) {
        return mul(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr mul(float a, int b) {
        return mul(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr mul(float a, long b) {
        return mul(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr mul(float a, float b) {
        return mul(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr mul(float a, double b) {
        return mul(MathsBase.expr(a),MathsBase.expr(b));
    }

//    public static Expr mul(float a, BigInteger b) {
//        return BigDecimal.valueOf(a).multiply(new BigDecimal(b));
//    }
//
//    public static Expr mul(float a, BigDecimal b) {
//        return BigDecimal.valueOf(a).multiply(b);
//    }

    public static Expr mul(float a, Complex b) {
        return MathsBase.expr(a).mul(b);
    }

    public static Expr mul(float a, Expr b) {
        return MathsBase.expr(a).mul(b);
    }

    // double mul

    public static Expr mul(double a, byte b) {
        return mul(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr mul(double a, short b) {
        return mul(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr mul(double a, int b) {
        return mul(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr mul(double a, long b) {
        return mul(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr mul(double a, float b) {
        return mul(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr mul(double a, double b) {
        return mul(MathsBase.expr(a),MathsBase.expr(b));
    }

//    public static Expr mul(double a, BigInteger b) {
//        return BigDecimal.valueOf(a).multiply(new BigDecimal(b));
//    }
//
//    public static Expr mul(double a, BigDecimal b) {
//        return BigDecimal.valueOf(a).multiply(b);
//    }

    public static Expr mul(double a, Complex b) {
        return MathsBase.expr(a).mul(b);
    }

    public static Expr mul(double a, Expr b) {
        return MathsBase.expr(a).mul(b);
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

    public static Expr div(byte a, byte b) {
        return MathsBase.div(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr div(byte a, short b) {
        return MathsBase.div(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr div(byte a, int b) {
        return MathsBase.div(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr div(byte a, long b) {
        return MathsBase.div(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr div(byte a, float b) {
        return MathsBase.div(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr div(byte a, double b) {
        return MathsBase.div(MathsBase.expr(a),MathsBase.expr(b));
    }

//    public static Expr div(byte a, BigInteger b) {
//        return BigInteger.valueOf(a).multiply(b);
//    }
//
//    public static Expr div(byte a, BigDecimal b) {
//        return BigDecimal.valueOf(a).multiply(b);
//    }

    public static Expr div(byte a, Complex b) {
        return MathsBase.expr(a).div(b);
    }

    public static Expr div(byte a, Expr b) {
        return MathsBase.expr(a).div(b);
    }

    // short  div

    public static Expr div(short a, byte b) {
        return MathsBase.div(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr div(short a, short b) {
        return MathsBase.div(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr div(short a, int b) {
        return MathsBase.div(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr div(short a, long b) {
        return MathsBase.div(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr div(short a, float b) {
        return MathsBase.div(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr div(short a, double b) {
        return MathsBase.div(MathsBase.expr(a),MathsBase.expr(b));
    }

//    public static Expr div(short a, BigInteger b) {
//        return BigInteger.valueOf(a).multiply(b);
//    }
//
//    public static Expr div(short a, BigDecimal b) {
//        return BigDecimal.valueOf(a).multiply(b);
//    }

    public static Expr div(short a, Complex b) {
        return MathsBase.expr(a).div(b);
    }

    public static Expr div(short a, Expr b) {
        return MathsBase.expr(a).div(b);
    }

    // int  div

    public static Expr div(int a, byte b) {
        return MathsBase.div(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr div(int a, short b) {
        return MathsBase.div(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr div(int a, int b) {
        return MathsBase.div(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr div(int a, long b) {
        return MathsBase.div(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr div(int a, float b) {
        return MathsBase.div(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr div(int a, double b) {
        return MathsBase.div(MathsBase.expr(a),MathsBase.expr(b));
    }

//    public static Expr div(int a, BigInteger b) {
//        return BigInteger.valueOf(a).multiply(b);
//    }
//
//    public static Expr div(int a, BigDecimal b) {
//        return BigDecimal.valueOf(a).multiply(b);
//    }

    public static Expr div(int a, Complex b) {
        return MathsBase.expr(a).div(b);
    }

    public static Expr div(int a, Expr b) {
        return MathsBase.expr(a).div(b);
    }

    // long  div

    public static Expr div(long a, byte b) {
        return MathsBase.div(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr div(long a, short b) {
        return MathsBase.div(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr div(long a, int b) {
        return MathsBase.div(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr div(long a, long b) {
        return MathsBase.div(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr div(long a, float b) {
        return MathsBase.div(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr div(long a, double b) {
        return MathsBase.div(MathsBase.expr(a),MathsBase.expr(b));
    }

//    public static Expr div(long a, BigInteger b) {
//        return BigInteger.valueOf(a).multiply(b);
//    }
//
//    public static Expr div(long a, BigDecimal b) {
//        return BigDecimal.valueOf(a).multiply(b);
//    }

    public static Expr div(long a, Complex b) {
        return MathsBase.expr(a).div(b);
    }

    public static Expr div(long a, Expr b) {
        return MathsBase.expr(a).div(b);
    }

    // float  div

    public static Expr div(float a, byte b) {
        return MathsBase.div(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr div(float a, short b) {
        return MathsBase.div(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr div(float a, int b) {
        return MathsBase.div(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr div(float a, long b) {
        return MathsBase.div(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr div(float a, float b) {
        return MathsBase.div(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr div(float a, double b) {
        return MathsBase.div(MathsBase.expr(a),MathsBase.expr(b));
    }

//    public static Expr div(float a, BigInteger b) {
//        return BigDecimal.valueOf(a).multiply(new BigDecimal(b));
//    }
//
//    public static Expr div(float a, BigDecimal b) {
//        return BigDecimal.valueOf(a).multiply(b);
//    }

    public static Expr div(float a, Complex b) {
        return MathsBase.expr(a).div(b);
    }

    public static Expr div(float a, Expr b) {
        return MathsBase.expr(a).div(b);
    }

    // double  div

    public static Expr div(double a, byte b) {
        return MathsBase.div(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr div(double a, short b) {
        return MathsBase.div(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr div(double a, int b) {
        return MathsBase.div(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr div(double a, long b) {
        return MathsBase.div(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr div(double a, float b) {
        return MathsBase.div(MathsBase.expr(a),MathsBase.expr(b));
    }

    public static Expr div(double a, double b) {
        return MathsBase.div(MathsBase.expr(a),MathsBase.expr(b));
    }

//    public static Expr div(double a, BigInteger b) {
//        return BigDecimal.valueOf(a).multiply(new BigDecimal(b));
//    }
//
//    public static Expr div(double a, BigDecimal b) {
//        return BigDecimal.valueOf(a).multiply(b);
//    }

    public static Expr div(double a, Complex b) {
        return MathsBase.expr(a).div(b);
    }

    public static Expr div(double a, Expr b) {
        return MathsBase.expr(a).div(b);
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

    public static Expr mul(Expr a, double b) {
        return mul(a,MathsBase.expr(b));
    }
    public static Expr mul(Expr a, Complex b) {
        return mul(a,(Expr) b);
    }
    public static Expr add(Expr a, Expr b) {
        return MathsBase.add(a,b);
    }
    public static Expr sub(Expr a, Expr b) {
        return MathsBase.sub(a,b);
    }
    public static Expr mul(Expr a, Expr b) {
        return MathsBase.mul(a,b);
    }
    public static Expr div(Expr a, Expr b) {
        return MathsBase.div(a,b);
    }
    public static Expr neg(Expr a) {
        return MathsBase.neg(a);
    }
    public static Expr cos(Expr a) {
        return MathsBase.cos(a);
    }
    public static Expr sin(Expr a) {
        return MathsBase.sin(a);
    }
    public static Expr tan(Expr a) {
        return MathsBase.tan(a);
    }
    public static Expr atan(Expr a) {
        return MathsBase.atan(a);
    }
    public static Expr cotan(Expr a) {
        return MathsBase.cotan(a);
    }
    public static Expr acotan(Expr a) {
        return MathsBase.acotan(a);
    }
    public static Expr acos(Expr a) {
        return MathsBase.acos(a);
    }
    public static Expr asin(Expr a) {
        return MathsBase.asin(a);
    }
    public static Expr cosh(Expr a) {
        return MathsBase.cosh(a);
    }
    public static Expr sinh(Expr a) {
        return MathsBase.sinh(a);
    }
    public static Expr tanh(Expr a) {
        return MathsBase.tanh(a);
    }
    public static Expr cotanh(Expr a) {
        return MathsBase.cotanh(a);
    }

    public static Expr scalarProduct(Expr a,Expr b) {
        return new ParametrizedScalarProduct(a,b);
    }

    public static <A, B> RightArrowUplet2<A, B> rightArrow(A a, B b) {
        return new RightArrowUplet2<A,B>(a, b);
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
        return Domain.forBounds(u.getFirst(), u.getSecond());
    }
    public static Expr domain(RightArrowUplet2.Double ux, RightArrowUplet2.Double uy) {
        return Domain.forBounds(ux.getFirst(), ux.getSecond(), uy.getFirst(), uy.getSecond());
    }

    public static Expr domain(RightArrowUplet2.Double ux, RightArrowUplet2.Double uy, RightArrowUplet2.Double uz) {
        return Domain.forBounds(ux.getFirst(), ux.getSecond(), uy.getFirst(), uy.getSecond(), uz.getFirst(), uz.getSecond());
    }

    public static Expr domain(RightArrowUplet2<Expr, Expr> u) {
        if (u.getFirst().isDouble() && u.getSecond().isDouble()) {
            return Domain.forBounds(u.getFirst().toDouble(), u.getSecond().toDouble());
        }
        return DomainExpr.forBounds(u.getFirst(), u.getSecond());
    }

    public static Expr domain(RightArrowUplet2<Expr, Expr> ux, RightArrowUplet2<Expr, Expr> uy) {
        if (ux.getFirst().isDouble() && ux.getSecond().isDouble() && uy.getFirst().isDouble() && uy.getSecond().isDouble()) {
            return Domain.forBounds(ux.getFirst().toDouble(), ux.getSecond().toDouble(), uy.getFirst().toDouble(), uy.getSecond().toDouble());
        }
        return DomainExpr.forBounds(ux.getFirst(), ux.getSecond(), uy.getFirst(), uy.getSecond());
    }

    public static Expr domain(RightArrowUplet2<Expr, Expr> ux, RightArrowUplet2<Expr, Expr> uy, RightArrowUplet2<Expr, Expr> uz) {
        if (ux.getFirst().isDouble() && ux.getSecond().isDouble() && uy.getFirst().isDouble() && uy.getSecond().isDouble() && uz.getFirst().isDouble() && uz.getSecond().isDouble()) {
            return Domain.forBounds(ux.getFirst().toDouble(), ux.getSecond().toDouble(), uy.getFirst().toDouble(), uy.getSecond().toDouble(), uz.getFirst().toDouble(), uz.getSecond().toDouble());
        }
        return DomainExpr.forBounds(ux.getFirst(), ux.getSecond(), uy.getFirst(), uy.getSecond(), uz.getFirst(), uz.getSecond());
    }

    public static DomainExpr xdomain(Expr min, Expr max) {
        return DomainExpr.forBounds(min, max);
    }

    public static Domain xdomain(double min, double max) {
        return Domain.forBounds(min, max);
    }

    public static Domain ydomain(double min, double max) {
        return Domain.forBounds(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, min, max);
    }

    public static DomainExpr ydomain(DomainExpr min, DomainExpr max) {
        return DomainExpr.forBounds(Complex.NEGATIVE_INFINITY, Complex.POSITIVE_INFINITY, min, max);
    }

    public static Domain zdomain(double min, double max) {
        return Domain.forBounds(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, min, max);
    }

    public static DomainExpr zdomain(Expr min, Expr max) {
        return DomainExpr.forBounds(Complex.NEGATIVE_INFINITY, Complex.POSITIVE_INFINITY, Complex.NEGATIVE_INFINITY, Complex.POSITIVE_INFINITY, min, max);
    }

    //    public static Domain domain(double min,double max){
//        return Domain.forBounds(min,max);
//    }
//
    public static Domain domain(double xmin, double xmax, double ymin, double ymax) {
        return Domain.forBounds(xmin, xmax, ymin, ymax);
    }

    public static DomainExpr domain(Expr xmin, Expr xmax, Expr ymin, Expr ymax) {
        return DomainExpr.forBounds(xmin, xmax, ymin, ymax);
    }

    public static Domain xyzdomain(double xmin, double xmax, double ymin, double ymax, double zmin, double zmax) {
        return Domain.forBounds(xmin, xmax, ymin, ymax, zmin, zmax);
    }

    public static DomainExpr xyzdomain(Expr xmin, Expr xmax, Expr ymin, Expr ymax, Expr zmin, Expr zmax) {
        return DomainExpr.forBounds(xmin, xmax, ymin, ymax, zmin, zmax);
    }

    public static Expr sqrt(Expr a) {
        return MathsBase.sqrt(a);
    }

    public static Complex sqrt(Complex a) {
        return MathsBase.sqrt(a);
    }

    public static Expr sqr(Expr a) {
        return MathsBase.sqr(a);
    }

    public static Complex sqr(Complex a) {
        return MathsBase.sqr(a);
    }

}
