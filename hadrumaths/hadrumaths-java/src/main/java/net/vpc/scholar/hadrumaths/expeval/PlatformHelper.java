package net.vpc.scholar.hadrumaths.expeval;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;

import java.math.BigDecimal;
import java.math.BigInteger;

public class PlatformHelper {

    // BigDecimal neg

    public static int neg(byte a) {
        return -a;
    }

    public static int neg(short a) {
        return -a;
    }

    public static int neg(int a) {
        return -a;
    }

    public static long neg(long a) {
        return -a;
    }

    public static float neg(float a) {
        return -a;
    }

    public static double neg(double a) {
        return -a;
    }

    public static BigInteger neg(BigInteger a) {
        return a.negate();
    }

    public static BigDecimal neg(BigDecimal a) {
        return a.negate();
    }

//    public static Complex neg( Complex a) {
//        throw new IllegalArgumentException("Unsupported");
//    }
//
//    public static Expr neg( Expr a) {
//        throw new IllegalArgumentException("Unsupported");
//    }

    // BigDecimal tilde

    public static int tilde(byte a) {
        return ~a;
    }

    public static int tilde(short a) {
        return ~a;
    }

    public static int tilde(int a) {
        return ~a;
    }

    public static long tilde(long a) {
        return ~a;
    }

//    public static float tilde( float a) {
//        return ~a;
//    }
//
//    public static double tilde( double a) {
//        return ~a;
//    }

    public static BigInteger tilde(BigInteger a) {
        // for negative BigInteger, top byte is negative
        byte[] contents = a.toByteArray();

        // prepend byte of opposite sign
        byte[] result = new byte[contents.length + 1];
        System.arraycopy(contents, 0, result, 1, contents.length);
        result[0] = (contents[0] < 0) ? 0 : (byte) -1;

        // this will be two's complement
        return new BigInteger(result);
    }

//    public static BigDecimal tilde(BigDecimal a) {
//        return a.negate();
//    }
//
//    public static Complex neg( Complex b) {
//        throw new IllegalArgumentException("Unsupported");
//    }
//
//    public static Expr neg( Expr b) {
//        throw new IllegalArgumentException("Unsupported");
//    }


    // byte ADD

    public static int add(byte a, byte b) {
        return a + b;
    }

    public static int add(byte a, short b) {
        return a + b;
    }

    public static int add(byte a, int b) {
        return a + b;
    }

    public static long add(byte a, long b) {
        return a + b;
    }

    public static float add(byte a, float b) {
        return a + b;
    }

    public static double add(byte a, double b) {
        return a + b;
    }

    public static BigInteger add(byte a, BigInteger b) {
        return BigInteger.valueOf(a).add(b);
    }

    public static BigDecimal add(byte a, BigDecimal b) {
        return BigDecimal.valueOf(a).add(b);
    }

    public static Complex add(byte a, Complex b) {
        return Complex.valueOf(a).add(b);
    }

    public static Expr add(byte a, Expr b) {
        return Complex.valueOf(a).add(b);
    }

    // short ADD

    public static int add(short a, byte b) {
        return a + b;
    }

    public static int add(short a, short b) {
        return a + b;
    }

    public static int add(short a, int b) {
        return a + b;
    }

    public static long add(short a, long b) {
        return a + b;
    }

    public static float add(short a, float b) {
        return a + b;
    }

    public static double add(short a, double b) {
        return a + b;
    }

    public static BigInteger add(short a, BigInteger b) {
        return BigInteger.valueOf(a).add(b);
    }

    public static BigDecimal add(short a, BigDecimal b) {
        return BigDecimal.valueOf(a).add(b);
    }

    public static Complex add(short a, Complex b) {
        return Complex.valueOf(a).add(b);
    }

    public static Expr add(short a, Expr b) {
        return Complex.valueOf(a).add(b);
    }

    // int ADD

    public static int add(int a, byte b) {
        return a + b;
    }

    public static int add(int a, short b) {
        return a + b;
    }

    public static int add(int a, int b) {
        return a + b;
    }

    public static long add(int a, long b) {
        return a + b;
    }

    public static float add(int a, float b) {
        return a + b;
    }

    public static double add(int a, double b) {
        return a + b;
    }

    public static BigInteger add(int a, BigInteger b) {
        return BigInteger.valueOf(a).add(b);
    }

    public static BigDecimal add(int a, BigDecimal b) {
        return BigDecimal.valueOf(a).add(b);
    }

    public static Complex add(int a, Complex b) {
        return Complex.valueOf(a).add(b);
    }

    public static Expr add(int a, Expr b) {
        return Complex.valueOf(a).add(b);
    }

    // long ADD

    public static long add(long a, byte b) {
        return a + b;
    }

    public static long add(long a, short b) {
        return a + b;
    }

    public static long add(long a, int b) {
        return a + b;
    }

    public static long add(long a, long b) {
        return a + b;
    }

    public static float add(long a, float b) {
        return a + b;
    }

    public static double add(long a, double b) {
        return a + b;
    }

    public static BigInteger add(long a, BigInteger b) {
        return BigInteger.valueOf(a).add(b);
    }

    public static BigDecimal add(long a, BigDecimal b) {
        return BigDecimal.valueOf(a).add(b);
    }

    public static Complex add(long a, Complex b) {
        return Complex.valueOf(a).add(b);
    }

    public static Expr add(long a, Expr b) {
        return Complex.valueOf(a).add(b);
    }

    // float ADD

    public static float add(float a, byte b) {
        return a + b;
    }

    public static float add(float a, short b) {
        return a + b;
    }

    public static float add(float a, int b) {
        return a + b;
    }

    public static float add(float a, long b) {
        return a + b;
    }

    public static float add(float a, float b) {
        return a + b;
    }

    public static double add(float a, double b) {
        return a + b;
    }

    public static BigDecimal add(float a, BigInteger b) {
        return BigDecimal.valueOf(a).add(new BigDecimal(b));
    }

    public static BigDecimal add(float a, BigDecimal b) {
        return BigDecimal.valueOf(a).add(b);
    }

    public static Complex add(float a, Complex b) {
        return Complex.valueOf(a).add(b);
    }

    public static Expr add(float a, Expr b) {
        return Complex.valueOf(a).add(b);
    }

    // double ADD

    public static double add(double a, byte b) {
        return a + b;
    }

    public static double add(double a, short b) {
        return a + b;
    }

    public static double add(double a, int b) {
        return a + b;
    }

    public static double add(double a, long b) {
        return a + b;
    }

    public static double add(double a, float b) {
        return a + b;
    }

    public static double add(double a, double b) {
        return a + b;
    }

    public static BigDecimal add(double a, BigInteger b) {
        return BigDecimal.valueOf(a).add(new BigDecimal(b));
    }

    public static BigDecimal add(double a, BigDecimal b) {
        return BigDecimal.valueOf(a).add(b);
    }

    public static Complex add(double a, Complex b) {
        return Complex.valueOf(a).add(b);
    }

    public static Expr add(double a, Expr b) {
        return Complex.valueOf(a).add(b);
    }

    // BigInteger ADD

    public static BigInteger add(BigInteger a, byte b) {
        return a.add(BigInteger.valueOf(b));
    }

    public static BigInteger add(BigInteger a, short b) {
        return a.add(BigInteger.valueOf(b));
    }

    public static BigInteger add(BigInteger a, int b) {
        return a.add(BigInteger.valueOf(b));
    }

    public static BigInteger add(BigInteger a, long b) {
        return a.add(BigInteger.valueOf(b));
    }

    public static BigDecimal add(BigInteger a, float b) {
        return new BigDecimal(a).add(BigDecimal.valueOf(b));
    }

    public static BigDecimal add(BigInteger a, double b) {
        return new BigDecimal(a).add(BigDecimal.valueOf(b));
    }

    public static BigDecimal add(BigInteger a, BigInteger b) {
        return new BigDecimal(a).add(new BigDecimal(b));
    }

    public static BigDecimal add(BigInteger a, BigDecimal b) {
        return new BigDecimal(a).add(b);
    }

//    public static Complex add(BigInteger a, Complex b) {
//        throw new IllegalArgumentException("Unsupported");
//    }
//
//    public static Expr add(BigInteger a, Expr b) {
//        throw new IllegalArgumentException("Unsupported");
//    }


    // BigDecimal ADD

    public static BigDecimal add(BigDecimal a, byte b) {
        return a.add(BigDecimal.valueOf(b));
    }

    public static BigDecimal add(BigDecimal a, short b) {
        return a.add(BigDecimal.valueOf(b));
    }

    public static BigDecimal add(BigDecimal a, int b) {
        return a.add(BigDecimal.valueOf(b));
    }

    public static BigDecimal add(BigDecimal a, long b) {
        return a.add(BigDecimal.valueOf(b));
    }

    public static BigDecimal add(BigDecimal a, float b) {
        return a.add(BigDecimal.valueOf(b));
    }

    public static BigDecimal add(BigDecimal a, double b) {
        return a.add(BigDecimal.valueOf(b));
    }

    public static BigDecimal add(BigDecimal a, BigInteger b) {
        return a.add(new BigDecimal(b));
    }

    public static BigDecimal add(BigDecimal a, BigDecimal b) {
        return a.add(b);
    }
//
//    public static Complex add(BigDecimal a, Complex b) {
//        throw new IllegalArgumentException("Unsupported");
//    }
//
//    public static Expr add(BigDecimal a, Expr b) {
//        throw new IllegalArgumentException("Unsupported");
//    }


    // byte SUB

    public static int sub(byte a, byte b) {
        return a - b;
    }

    public static int sub(byte a, short b) {
        return a - b;
    }

    public static int sub(byte a, int b) {
        return a - b;
    }

    public static long sub(byte a, long b) {
        return a - b;
    }

    public static float sub(byte a, float b) {
        return a - b;
    }

    public static double sub(byte a, double b) {
        return a - b;
    }

    public static BigInteger sub(byte a, BigInteger b) {
        return BigInteger.valueOf(a).subtract(b);
    }

    public static BigDecimal sub(byte a, BigDecimal b) {
        return BigDecimal.valueOf(a).subtract(b);
    }

    public static Complex sub(byte a, Complex b) {
        return Complex.valueOf(a).sub(b);
    }

    public static Expr sub(byte a, Expr b) {
        return Complex.valueOf(a).sub(b);
    }

    // short sub

    public static int sub(short a, byte b) {
        return a - b;
    }

    public static int sub(short a, short b) {
        return a - b;
    }

    public static int sub(short a, int b) {
        return a - b;
    }

    public static long sub(short a, long b) {
        return a - b;
    }

    public static float sub(short a, float b) {
        return a - b;
    }

    public static double sub(short a, double b) {
        return a - b;
    }

    public static BigInteger sub(short a, BigInteger b) {
        return BigInteger.valueOf(a).subtract(b);
    }

    public static BigDecimal sub(short a, BigDecimal b) {
        return BigDecimal.valueOf(a).subtract(b);
    }

    public static Complex sub(short a, Complex b) {
        return Complex.valueOf(a).sub(b);
    }

    public static Expr sub(short a, Expr b) {
        return Complex.valueOf(a).sub(b);
    }

    // int sub

    public static int sub(int a, byte b) {
        return a - b;
    }

    public static int sub(int a, short b) {
        return a - b;
    }

    public static int sub(int a, int b) {
        return a - b;
    }

    public static long sub(int a, long b) {
        return a - b;
    }

    public static float sub(int a, float b) {
        return a - b;
    }

    public static double sub(int a, double b) {
        return a - b;
    }

    public static BigInteger sub(int a, BigInteger b) {
        return BigInteger.valueOf(a).subtract(b);
    }

    public static BigDecimal sub(int a, BigDecimal b) {
        return BigDecimal.valueOf(a).subtract(b);
    }

    public static Complex sub(int a, Complex b) {
        return Complex.valueOf(a).sub(b);
    }

    public static Expr sub(int a, Expr b) {
        return Complex.valueOf(a).sub(b);
    }

    // long sub

    public static long sub(long a, byte b) {
        return a - b;
    }

    public static long sub(long a, short b) {
        return a - b;
    }

    public static long sub(long a, int b) {
        return a - b;
    }

    public static long sub(long a, long b) {
        return a - b;
    }

    public static float sub(long a, float b) {
        return a - b;
    }

    public static double sub(long a, double b) {
        return a - b;
    }

    public static BigInteger sub(long a, BigInteger b) {
        return BigInteger.valueOf(a).subtract(b);
    }

    public static BigDecimal sub(long a, BigDecimal b) {
        return BigDecimal.valueOf(a).subtract(b);
    }

    public static Complex sub(long a, Complex b) {
        return Complex.valueOf(a).sub(b);
    }

    public static Expr sub(long a, Expr b) {
        return Complex.valueOf(a).sub(b);
    }

    // float sub

    public static float sub(float a, byte b) {
        return a - b;
    }

    public static float sub(float a, short b) {
        return a - b;
    }

    public static float sub(float a, int b) {
        return a - b;
    }

    public static float sub(float a, long b) {
        return a - b;
    }

    public static float sub(float a, float b) {
        return a - b;
    }

    public static double sub(float a, double b) {
        return a - b;
    }

    public static BigDecimal sub(float a, BigInteger b) {
        return BigDecimal.valueOf(a).subtract(new BigDecimal(b));
    }

    public static BigDecimal sub(float a, BigDecimal b) {
        return BigDecimal.valueOf(a).subtract(b);
    }

    public static Complex sub(float a, Complex b) {
        return Complex.valueOf(a).sub(b);
    }

    public static Expr sub(float a, Expr b) {
        return Complex.valueOf(a).sub(b);
    }

    // double sub

    public static double sub(double a, byte b) {
        return a - b;
    }

    public static double sub(double a, short b) {
        return a - b;
    }

    public static double sub(double a, int b) {
        return a - b;
    }

    public static double sub(double a, long b) {
        return a - b;
    }

    public static double sub(double a, float b) {
        return a - b;
    }

    public static double sub(double a, double b) {
        return a - b;
    }

    public static BigDecimal sub(double a, BigInteger b) {
        return BigDecimal.valueOf(a).subtract(new BigDecimal(b));
    }

    public static BigDecimal sub(double a, BigDecimal b) {
        return BigDecimal.valueOf(a).subtract(b);
    }

    public static Complex sub(double a, Complex b) {
        return Complex.valueOf(a).sub(b);
    }

    public static Expr sub(double a, Expr b) {
        return Complex.valueOf(a).sub(b);
    }

    // BigInteger sub

    public static BigInteger sub(BigInteger a, byte b) {
        return a.subtract(BigInteger.valueOf(b));
    }

    public static BigInteger sub(BigInteger a, short b) {
        return a.subtract(BigInteger.valueOf(b));
    }

    public static BigInteger sub(BigInteger a, int b) {
        return a.subtract(BigInteger.valueOf(b));
    }

    public static BigInteger sub(BigInteger a, long b) {
        return a.subtract(BigInteger.valueOf(b));
    }

    public static BigDecimal sub(BigInteger a, float b) {
        return new BigDecimal(a).subtract(BigDecimal.valueOf(b));
    }

    public static BigDecimal sub(BigInteger a, double b) {
        return new BigDecimal(a).subtract(BigDecimal.valueOf(b));
    }

    public static BigDecimal sub(BigInteger a, BigInteger b) {
        return new BigDecimal(a).subtract(new BigDecimal(b));
    }

    public static BigDecimal sub(BigInteger a, BigDecimal b) {
        return new BigDecimal(a).subtract(b);
    }

//    public static Complex sub(BigInteger a, Complex b) {
//        throw new IllegalArgumentException("Unsupported");
//    }
//
//    public static Expr sub(BigInteger a, Expr b) {
//        throw new IllegalArgumentException("Unsupported");
//    }


    // BigDecimal sub

    public static BigDecimal sub(BigDecimal a, byte b) {
        return a.subtract(BigDecimal.valueOf(b));
    }

    public static BigDecimal sub(BigDecimal a, short b) {
        return a.subtract(BigDecimal.valueOf(b));
    }

    public static BigDecimal sub(BigDecimal a, int b) {
        return a.subtract(BigDecimal.valueOf(b));
    }

    public static BigDecimal sub(BigDecimal a, long b) {
        return a.subtract(BigDecimal.valueOf(b));
    }

    public static BigDecimal sub(BigDecimal a, float b) {
        return a.subtract(BigDecimal.valueOf(b));
    }

    public static BigDecimal sub(BigDecimal a, double b) {
        return a.subtract(BigDecimal.valueOf(b));
    }

    public static BigDecimal sub(BigDecimal a, BigInteger b) {
        return a.subtract(new BigDecimal(b));
    }

    public static BigDecimal sub(BigDecimal a, BigDecimal b) {
        return a.subtract(b);
    }
//
//    public static Complex sub(BigDecimal a, Complex b) {
//        throw new IllegalArgumentException("Unsupported");
//    }
//
//    public static Expr sub(BigDecimal a, Expr b) {
//        throw new IllegalArgumentException("Unsupported");
//    }


    // byte mul

    public static int mul(byte a, byte b) {
        return a * b;
    }

    public static int mul(byte a, short b) {
        return a * b;
    }

    public static int mul(byte a, int b) {
        return a * b;
    }

    public static long mul(byte a, long b) {
        return a * b;
    }

    public static float mul(byte a, float b) {
        return a * b;
    }

    public static double mul(byte a, double b) {
        return a * b;
    }

    public static BigInteger mul(byte a, BigInteger b) {
        return BigInteger.valueOf(a).multiply(b);
    }

    public static BigDecimal mul(byte a, BigDecimal b) {
        return BigDecimal.valueOf(a).multiply(b);
    }

    public static Complex mul(byte a, Complex b) {
        return Complex.valueOf(a).mul(b);
    }

    public static Expr mul(byte a, Expr b) {
        return Complex.valueOf(a).mul(b);
    }

    // short mul

    public static int mul(short a, byte b) {
        return a * b;
    }

    public static int mul(short a, short b) {
        return a * b;
    }

    public static int mul(short a, int b) {
        return a * b;
    }

    public static long mul(short a, long b) {
        return a * b;
    }

    public static float mul(short a, float b) {
        return a * b;
    }

    public static double mul(short a, double b) {
        return a * b;
    }

    public static BigInteger mul(short a, BigInteger b) {
        return BigInteger.valueOf(a).multiply(b);
    }

    public static BigDecimal mul(short a, BigDecimal b) {
        return BigDecimal.valueOf(a).multiply(b);
    }

    public static Complex mul(short a, Complex b) {
        return Complex.valueOf(a).mul(b);
    }

    public static Expr mul(short a, Expr b) {
        return Complex.valueOf(a).mul(b);
    }

    // int mul

    public static int mul(int a, byte b) {
        return a * b;
    }

    public static int mul(int a, short b) {
        return a * b;
    }

    public static int mul(int a, int b) {
        return a * b;
    }

    public static long mul(int a, long b) {
        return a * b;
    }

    public static float mul(int a, float b) {
        return a * b;
    }

    public static double mul(int a, double b) {
        return a * b;
    }

    public static BigInteger mul(int a, BigInteger b) {
        return BigInteger.valueOf(a).multiply(b);
    }

    public static BigDecimal mul(int a, BigDecimal b) {
        return BigDecimal.valueOf(a).multiply(b);
    }

    public static Complex mul(int a, Complex b) {
        return Complex.valueOf(a).mul(b);
    }

    public static Expr mul(int a, Expr b) {
        return Complex.valueOf(a).mul(b);
    }

    // long mul

    public static long mul(long a, byte b) {
        return a * b;
    }

    public static long mul(long a, short b) {
        return a * b;
    }

    public static long mul(long a, int b) {
        return a * b;
    }

    public static long mul(long a, long b) {
        return a * b;
    }

    public static float mul(long a, float b) {
        return a * b;
    }

    public static double mul(long a, double b) {
        return a * b;
    }

    public static BigInteger mul(long a, BigInteger b) {
        return BigInteger.valueOf(a).multiply(b);
    }

    public static BigDecimal mul(long a, BigDecimal b) {
        return BigDecimal.valueOf(a).multiply(b);
    }

    public static Complex mul(long a, Complex b) {
        return Complex.valueOf(a).mul(b);
    }

    public static Expr mul(long a, Expr b) {
        return Complex.valueOf(a).mul(b);
    }

    // float mul

    public static float mul(float a, byte b) {
        return a * b;
    }

    public static float mul(float a, short b) {
        return a * b;
    }

    public static float mul(float a, int b) {
        return a * b;
    }

    public static float mul(float a, long b) {
        return a * b;
    }

    public static float mul(float a, float b) {
        return a * b;
    }

    public static double mul(float a, double b) {
        return a * b;
    }

    public static BigDecimal mul(float a, BigInteger b) {
        return BigDecimal.valueOf(a).multiply(new BigDecimal(b));
    }

    public static BigDecimal mul(float a, BigDecimal b) {
        return BigDecimal.valueOf(a).multiply(b);
    }

    public static Complex mul(float a, Complex b) {
        return Complex.valueOf(a).mul(b);
    }

    public static Expr mul(float a, Expr b) {
        return Complex.valueOf(a).mul(b);
    }

    // double mul

    public static double mul(double a, byte b) {
        return a * b;
    }

    public static double mul(double a, short b) {
        return a * b;
    }

    public static double mul(double a, int b) {
        return a * b;
    }

    public static double mul(double a, long b) {
        return a * b;
    }

    public static double mul(double a, float b) {
        return a * b;
    }

    public static double mul(double a, double b) {
        return a * b;
    }

    public static BigDecimal mul(double a, BigInteger b) {
        return BigDecimal.valueOf(a).multiply(new BigDecimal(b));
    }

    public static BigDecimal mul(double a, BigDecimal b) {
        return BigDecimal.valueOf(a).multiply(b);
    }

    public static Complex mul(double a, Complex b) {
        return Complex.valueOf(a).mul(b);
    }

    public static Expr mul(double a, Expr b) {
        return Complex.valueOf(a).mul(b);
    }

    // BigInteger mul

    public static BigInteger mul(BigInteger a, byte b) {
        return a.multiply(BigInteger.valueOf(b));
    }

    public static BigInteger mul(BigInteger a, short b) {
        return a.multiply(BigInteger.valueOf(b));
    }

    public static BigInteger mul(BigInteger a, int b) {
        return a.multiply(BigInteger.valueOf(b));
    }

    public static BigInteger mul(BigInteger a, long b) {
        return a.multiply(BigInteger.valueOf(b));
    }

    public static BigDecimal mul(BigInteger a, float b) {
        return new BigDecimal(a).multiply(BigDecimal.valueOf(b));
    }

    public static BigDecimal mul(BigInteger a, double b) {
        return new BigDecimal(a).multiply(BigDecimal.valueOf(b));
    }

    public static BigDecimal mul(BigInteger a, BigInteger b) {
        return new BigDecimal(a).multiply(new BigDecimal(b));
    }

    public static BigDecimal mul(BigInteger a, BigDecimal b) {
        return new BigDecimal(a).multiply(b);
    }

//    public static Complex mul(BigInteger a, Complex b) {
//        throw new IllegalArgumentException("Unsupported");
//    }
//
//    public static Expr mul(BigInteger a, Expr b) {
//        throw new IllegalArgumentException("Unsupported");
//    }


    // BigDecimal mul

    public static BigDecimal mul(BigDecimal a, byte b) {
        return a.multiply(BigDecimal.valueOf(b));
    }

    public static BigDecimal mul(BigDecimal a, short b) {
        return a.multiply(BigDecimal.valueOf(b));
    }

    public static BigDecimal mul(BigDecimal a, int b) {
        return a.multiply(BigDecimal.valueOf(b));
    }

    public static BigDecimal mul(BigDecimal a, long b) {
        return a.multiply(BigDecimal.valueOf(b));
    }

    public static BigDecimal mul(BigDecimal a, float b) {
        return a.multiply(BigDecimal.valueOf(b));
    }

    public static BigDecimal mul(BigDecimal a, double b) {
        return a.multiply(BigDecimal.valueOf(b));
    }

    public static BigDecimal mul(BigDecimal a, BigInteger b) {
        return a.multiply(new BigDecimal(b));
    }

    public static BigDecimal mul(BigDecimal a, BigDecimal b) {
        return a.multiply(b);
    }
//
//    public static Complex mul(BigDecimal a, Complex b) {
//        throw new IllegalArgumentException("Unsupported");
//    }
//
//    public static Expr mul(BigDecimal a, Expr b) {
//        throw new IllegalArgumentException("Unsupported");
//    }


    // byte div

    public static int div(byte a, byte b) {
        return a / b;
    }

    public static int div(byte a, short b) {
        return a / b;
    }

    public static int div(byte a, int b) {
        return a / b;
    }

    public static long div(byte a, long b) {
        return a / b;
    }

    public static float div(byte a, float b) {
        return a / b;
    }

    public static double div(byte a, double b) {
        return a / b;
    }

    public static BigInteger div(byte a, BigInteger b) {
        return BigInteger.valueOf(a).multiply(b);
    }

    public static BigDecimal div(byte a, BigDecimal b) {
        return BigDecimal.valueOf(a).multiply(b);
    }

    public static Complex div(byte a, Complex b) {
        return Complex.valueOf(a).div(b);
    }

    public static Expr div(byte a, Expr b) {
        return Complex.valueOf(a).div(b);
    }

    // short  div

    public static int div(short a, byte b) {
        return a / b;
    }

    public static int div(short a, short b) {
        return a / b;
    }

    public static int div(short a, int b) {
        return a / b;
    }

    public static long div(short a, long b) {
        return a / b;
    }

    public static float div(short a, float b) {
        return a / b;
    }

    public static double div(short a, double b) {
        return a / b;
    }

    public static BigInteger div(short a, BigInteger b) {
        return BigInteger.valueOf(a).multiply(b);
    }

    public static BigDecimal div(short a, BigDecimal b) {
        return BigDecimal.valueOf(a).multiply(b);
    }

    public static Complex div(short a, Complex b) {
        return Complex.valueOf(a).div(b);
    }

    public static Expr div(short a, Expr b) {
        return Complex.valueOf(a).div(b);
    }

    // int  div

    public static int div(int a, byte b) {
        return a / b;
    }

    public static int div(int a, short b) {
        return a / b;
    }

    public static int div(int a, int b) {
        return a / b;
    }

    public static long div(int a, long b) {
        return a / b;
    }

    public static float div(int a, float b) {
        return a / b;
    }

    public static double div(int a, double b) {
        return a / b;
    }

    public static BigInteger div(int a, BigInteger b) {
        return BigInteger.valueOf(a).multiply(b);
    }

    public static BigDecimal div(int a, BigDecimal b) {
        return BigDecimal.valueOf(a).multiply(b);
    }

    public static Complex div(int a, Complex b) {
        return Complex.valueOf(a).div(b);
    }

    public static Expr div(int a, Expr b) {
        return Complex.valueOf(a).div(b);
    }

    // long  div

    public static long div(long a, byte b) {
        return a / b;
    }

    public static long div(long a, short b) {
        return a / b;
    }

    public static long div(long a, int b) {
        return a / b;
    }

    public static long div(long a, long b) {
        return a / b;
    }

    public static float div(long a, float b) {
        return a / b;
    }

    public static double div(long a, double b) {
        return a / b;
    }

    public static BigInteger div(long a, BigInteger b) {
        return BigInteger.valueOf(a).multiply(b);
    }

    public static BigDecimal div(long a, BigDecimal b) {
        return BigDecimal.valueOf(a).multiply(b);
    }

    public static Complex div(long a, Complex b) {
        return Complex.valueOf(a).div(b);
    }

    public static Expr div(long a, Expr b) {
        return Complex.valueOf(a).div(b);
    }

    // float  div

    public static float div(float a, byte b) {
        return a / b;
    }

    public static float div(float a, short b) {
        return a / b;
    }

    public static float div(float a, int b) {
        return a / b;
    }

    public static float div(float a, long b) {
        return a / b;
    }

    public static float div(float a, float b) {
        return a / b;
    }

    public static double div(float a, double b) {
        return a / b;
    }

    public static BigDecimal div(float a, BigInteger b) {
        return BigDecimal.valueOf(a).multiply(new BigDecimal(b));
    }

    public static BigDecimal div(float a, BigDecimal b) {
        return BigDecimal.valueOf(a).multiply(b);
    }

    public static Complex div(float a, Complex b) {
        return Complex.valueOf(a).div(b);
    }

    public static Expr div(float a, Expr b) {
        return Complex.valueOf(a).div(b);
    }

    // double  div

    public static double div(double a, byte b) {
        return a / b;
    }

    public static double div(double a, short b) {
        return a / b;
    }

    public static double div(double a, int b) {
        return a / b;
    }

    public static double div(double a, long b) {
        return a / b;
    }

    public static double div(double a, float b) {
        return a / b;
    }

    public static double div(double a, double b) {
        return a / b;
    }

    public static BigDecimal div(double a, BigInteger b) {
        return BigDecimal.valueOf(a).multiply(new BigDecimal(b));
    }

    public static BigDecimal div(double a, BigDecimal b) {
        return BigDecimal.valueOf(a).multiply(b);
    }

    public static Complex div(double a, Complex b) {
        return Complex.valueOf(a).div(b);
    }

    public static Expr div(double a, Expr b) {
        return Complex.valueOf(a).div(b);
    }

    // BigInteger  div

    public static BigInteger div(BigInteger a, byte b) {
        return a.multiply(BigInteger.valueOf(b));
    }

    public static BigInteger div(BigInteger a, short b) {
        return a.multiply(BigInteger.valueOf(b));
    }

    public static BigInteger div(BigInteger a, int b) {
        return a.multiply(BigInteger.valueOf(b));
    }

    public static BigInteger div(BigInteger a, long b) {
        return a.multiply(BigInteger.valueOf(b));
    }

    public static BigDecimal div(BigInteger a, float b) {
        return new BigDecimal(a).multiply(BigDecimal.valueOf(b));
    }

    public static BigDecimal div(BigInteger a, double b) {
        return new BigDecimal(a).multiply(BigDecimal.valueOf(b));
    }

    public static BigDecimal div(BigInteger a, BigInteger b) {
        return new BigDecimal(a).multiply(new BigDecimal(b));
    }

    public static BigDecimal div(BigInteger a, BigDecimal b) {
        return new BigDecimal(a).multiply(b);
    }

//    public static Complex  div(BigInteger a, Complex b) {
//        throw new IllegalArgumentException("Unsupported");
//    }
//
//    public static Expr  div(BigInteger a, Expr b) {
//        throw new IllegalArgumentException("Unsupported");
//    }


    // BigDecimal  div

    public static BigDecimal div(BigDecimal a, byte b) {
        return a.multiply(BigDecimal.valueOf(b));
    }

    public static BigDecimal div(BigDecimal a, short b) {
        return a.multiply(BigDecimal.valueOf(b));
    }

    public static BigDecimal div(BigDecimal a, int b) {
        return a.multiply(BigDecimal.valueOf(b));
    }

    public static BigDecimal div(BigDecimal a, long b) {
        return a.multiply(BigDecimal.valueOf(b));
    }

    public static BigDecimal div(BigDecimal a, float b) {
        return a.multiply(BigDecimal.valueOf(b));
    }

    public static BigDecimal div(BigDecimal a, double b) {
        return a.multiply(BigDecimal.valueOf(b));
    }

    public static BigDecimal div(BigDecimal a, BigInteger b) {
        return a.multiply(new BigDecimal(b));
    }

    public static BigDecimal div(BigDecimal a, BigDecimal b) {
        return a.multiply(b);
    }
//
//    public static Complex  div(BigDecimal a, Complex b) {
//        throw new IllegalArgumentException("Unsupported");
//    }
//
//    public static Expr  div(BigDecimal a, Expr b) {
//        throw new IllegalArgumentException("Unsupported");
//    }


}
