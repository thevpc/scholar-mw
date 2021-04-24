package net.thevpc.scholar.hadrumaths.test.parse;

import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadrumaths.ExpressionRewriterFactory;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadrumaths.transform.ExprRewriteLogger;
import org.junit.jupiter.api.Assertions;

public class TestParseExpr {
//    @Test
//    public void dotit(){
//        testThis("X+Y*Z","X+(Y*Z)");
//        testThis("X+domain(1->2,3->4)","X+domain(1.0->2.0,3.0->4.0)");
//    }

    public static void main(String[] args) {
        Maths.Config.setCacheEnabled(true);
        Expr e= Maths.expr("(((((domain(0.0->7.86E-4, (-0.001406)->0.001406) * 672.6374890636885) ** (cos((((2 * 0) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995)) * cos((((2 * 0) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995) + (((domain(0.0->7.86E-4, (-0.001406)->0.001406) * 672.6374890636885) ** (cos((((2 * 1) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995)) * cos((((2 * 1) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995) + (((domain(0.0->7.86E-4, (-0.001406)->0.001406) * 672.6374890636885) ** (cos((((2 * 2) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995)) * cos((((2 * 2) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995) + (((domain(0.0->7.86E-4, (-0.001406)->0.001406) * 672.6374890636885) ** (cos((((2 * 3) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995)) * cos((((2 * 3) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995) + (((domain(0.0->7.86E-4, (-0.001406)->0.001406) * 672.6374890636885) ** (cos((((2 * 4) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995)) * cos((((2 * 4) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995) + (((domain(0.0->7.86E-4, (-0.001406)->0.001406) * 672.6374890636885) ** (cos((((2 * 5) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.11419919429943)) * cos((((2 * 5) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.11419919429943) + (((domain(0.0->7.86E-4, (-0.001406)->0.001406) * 672.6374890636885) ** (cos((((2 * 6) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995)) * cos((((2 * 6) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995) + (((domain(0.0->7.86E-4, (-0.001406)->0.001406) * 672.6374890636885) ** (cos((((2 * 7) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.11419919429943)) * cos((((2 * 7) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.11419919429943) + (((domain(0.0->7.86E-4, (-0.001406)->0.001406) * 672.6374890636885) ** (cos((((2 * 8) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995)) * cos((((2 * 8) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995) + (((domain(0.0->7.86E-4, (-0.001406)->0.001406) * 672.6374890636885) ** (cos((((2 * 9) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995)) * cos((((2 * 9) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995) + (((domain(0.0->7.86E-4, (-0.001406)->0.001406) * 672.6374890636885) ** (cos((((2 * 10) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995)) * cos((((2 * 10) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995) + (((domain(0.0->7.86E-4, (-0.001406)->0.001406) * 672.6374890636885) ** (cos((((2 * 11) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995)) * cos((((2 * 11) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995) + (((domain(0.0->7.86E-4, (-0.001406)->0.001406) * 672.6374890636885) ** (cos((((2 * 12) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995)) * cos((((2 * 12) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995) + (((domain(0.0->7.86E-4, (-0.001406)->0.001406) * 672.6374890636885) ** (cos((((2 * 13) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995)) * cos((((2 * 13) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995) + (((domain(0.0->7.86E-4, (-0.001406)->0.001406) * 672.6374890636885) ** (cos((((2 * 14) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995)) * cos((((2 * 14) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995) + (((domain(0.0->7.86E-4, (-0.001406)->0.001406) * 672.6374890636885) ** (cos((((2 * 15) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995)) * cos((((2 * 15) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995) + (((domain(0.0->7.86E-4, (-0.001406)->0.001406) * 672.6374890636885) ** (cos((((2 * 16) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995)) * cos((((2 * 16) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995) + (((domain(0.0->7.86E-4, (-0.001406)->0.001406) * 672.6374890636885) ** (cos((((2 * 17) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995)) * cos((((2 * 17) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995) + (((domain(0.0->7.86E-4, (-0.001406)->0.001406) * 672.6374890636885) ** (cos((((2 * 18) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995)) * cos((((2 * 18) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995) + (((domain(0.0->7.86E-4, (-0.001406)->0.001406) * 672.6374890636885) ** (cos((((2 * 19) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995)) * cos((((2 * 19) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995) + (((domain(0.0->7.86E-4, (-0.001406)->0.001406) * 672.6374890636885) ** (cos((((2 * 20) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995)) * cos((((2 * 20) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995) + (((domain(0.0->7.86E-4, (-0.001406)->0.001406) * 672.6374890636885) ** (cos((((2 * 21) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995)) * cos((((2 * 21) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995) + (((domain(0.0->7.86E-4, (-0.001406)->0.001406) * 672.6374890636885) ** (cos((((2 * 22) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995)) * cos((((2 * 22) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995) + (((domain(0.0->7.86E-4, (-0.001406)->0.001406) * 672.6374890636885) ** (cos((((2 * 23) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995)) * cos((((2 * 23) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995) + (((domain(0.0->7.86E-4, (-0.001406)->0.001406) * 672.6374890636885) ** (cos((((2 * 24) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995)) * cos((((2 * 24) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995) + (((domain(0.0->7.86E-4, (-0.001406)->0.001406) * 672.6374890636885) ** (cos((((2 * 25) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995)) * cos((((2 * 25) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995) + (((domain(0.0->7.86E-4, (-0.001406)->0.001406) * 672.6374890636885) ** (cos((((2 * 26) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995)) * cos((((2 * 26) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995) + (((domain(0.0->7.86E-4, (-0.001406)->0.001406) * 672.6374890636885) ** (cos((((2 * 27) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995)) * cos((((2 * 27) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995) + (((domain(0.0->7.86E-4, (-0.001406)->0.001406) * 672.6374890636885) ** (cos((((2 * 28) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995)) * cos((((2 * 28) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995) + (((domain(0.0->7.86E-4, (-0.001406)->0.001406) * 672.6374890636885) ** (cos((((2 * 29) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995)) * cos((((2 * 29) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995) + (((domain(0.0->7.86E-4, (-0.001406)->0.001406) * 672.6374890636885) ** (cos((((2 * 30) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995)) * cos((((2 * 30) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995) + (((domain(0.0->7.86E-4, (-0.001406)->0.001406) * 672.6374890636885) ** (cos((((2 * 31) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995)) * cos((((2 * 31) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995) + (((domain(0.0->7.86E-4, (-0.001406)->0.001406) * 672.6374890636885) ** (cos((((2 * 32) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995)) * cos((((2 * 32) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995) + (((domain(0.0->7.86E-4, (-0.001406)->0.001406) * 672.6374890636885) ** (cos((((2 * 33) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995)) * cos((((2 * 33) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995) + (((domain(0.0->7.86E-4, (-0.001406)->0.001406) * 672.6374890636885) ** (cos((((2 * 34) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995)) * cos((((2 * 34) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995) + (((domain(0.0->7.86E-4, (-0.001406)->0.001406) * 672.6374890636885) ** (cos((((2 * 35) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995)) * cos((((2 * 35) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995) + (((domain(0.0->7.86E-4, (-0.001406)->0.001406) * 672.6374890636885) ** (cos((((2 * 36) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.11419919429943)) * cos((((2 * 36) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.11419919429943) + (((domain(0.0->7.86E-4, (-0.001406)->0.001406) * 672.6374890636885) ** (cos((((2 * 37) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.11419919429943)) * cos((((2 * 37) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.11419919429943) + (((domain(0.0->7.86E-4, (-0.001406)->0.001406) * 672.6374890636885) ** (cos((((2 * 38) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995)) * cos((((2 * 38) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995) + (((domain(0.0->7.86E-4, (-0.001406)->0.001406) * 672.6374890636885) ** (cos((((2 * 39) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995)) * cos((((2 * 39) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995) + (((domain(0.0->7.86E-4, (-0.001406)->0.001406) * 672.6374890636885) ** (cos((((2 * 40) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995)) * cos((((2 * 40) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995) + (((domain(0.0->7.86E-4, (-0.001406)->0.001406) * 672.6374890636885) ** (cos((((2 * 41) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995)) * cos((((2 * 41) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995) + (((domain(0.0->7.86E-4, (-0.001406)->0.001406) * 672.6374890636885) ** (cos((((2 * 42) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995)) * cos((((2 * 42) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995) + (((domain(0.0->7.86E-4, (-0.001406)->0.001406) * 672.6374890636885) ** (cos((((2 * 43) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995)) * cos((((2 * 43) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995) + (((domain(0.0->7.86E-4, (-0.001406)->0.001406) * 672.6374890636885) ** (cos((((2 * 44) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995)) * cos((((2 * 44) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995) + (((domain(0.0->7.86E-4, (-0.001406)->0.001406) * 672.6374890636885) ** (cos((((2 * 45) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995)) * cos((((2 * 45) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995) + (((domain(0.0->7.86E-4, (-0.001406)->0.001406) * 672.6374890636885) ** (sin((1 * 3.141592653589793 * (X - 0.005690000000000001)) / 0.022760000000000002) * cos((1 * 3.141592653589793 * (Y + 0.0029945)) / 0.005989) * domain(0.005690000000000001->0.028450000000000003, (-0.0029945)->0.0029945) * 171.30368691960348)) * sin((1 * 3.141592653589793 * (X - 0.005690000000000001)) / 0.022760000000000002) * cos((1 * 3.141592653589793 * (Y + 0.0029945)) / 0.005989) * domain(0.005690000000000001->0.028450000000000003, (-0.0029945)->0.0029945) * 171.30368691960348) + (((domain(0.0->7.86E-4, (-0.001406)->0.001406) * 672.6374890636885) ** (sin((1 * 3.141592653589793 * (X - 0.005690000000000001)) / 0.022760000000000002) * cos((2 * 3.141592653589793 * (Y + 0.0029945)) / 0.005989) * domain(0.005690000000000001->0.028450000000000003, (-0.0029945)->0.0029945) * 171.30368691960348)) * sin((1 * 3.141592653589793 * (X - 0.005690000000000001)) / 0.022760000000000002) * cos((2 * 3.141592653589793 * (Y + 0.0029945)) / 0.005989) * domain(0.005690000000000001->0.028450000000000003, (-0.0029945)->0.0029945) * 171.30368691960348) + (((domain(0.0->7.86E-4, (-0.001406)->0.001406) * 672.6374890636885) ** (sin((1 * 3.141592653589793 * (X - 0.005690000000000001)) / 0.022760000000000002) * cos((3 * 3.141592653589793 * (Y + 0.0029945)) / 0.005989) * domain(0.005690000000000001->0.028450000000000003, (-0.0029945)->0.0029945) * 171.30368691960348)) * sin((1 * 3.141592653589793 * (X - 0.005690000000000001)) / 0.022760000000000002) * cos((3 * 3.141592653589793 * (Y + 0.0029945)) / 0.005989) * domain(0.005690000000000001->0.028450000000000003, (-0.0029945)->0.0029945) * 171.30368691960348) + (((domain(0.0->7.86E-4, (-0.001406)->0.001406) * 672.6374890636885) ** (sin((1 * 3.141592653589793 * (X - 0.005690000000000001)) / 0.022760000000000002) * cos((4 * 3.141592653589793 * (Y + 0.0029945)) / 0.005989) * domain(0.005690000000000001->0.028450000000000003, (-0.0029945)->0.0029945) * 171.30368691960348)) * sin((1 * 3.141592653589793 * (X - 0.005690000000000001)) / 0.022760000000000002) * cos((4 * 3.141592653589793 * (Y + 0.0029945)) / 0.005989) * domain(0.005690000000000001->0.028450000000000003, (-0.0029945)->0.0029945) * 171.30368691960348) + (((domain(0.0->7.86E-4, (-0.001406)->0.001406) * 672.6374890636885) ** (sin((2 * 3.141592653589793 * (X - 0.005690000000000001)) / 0.022760000000000002) * cos((1 * 3.141592653589793 * (Y + 0.0029945)) / 0.005989) * domain(0.005690000000000001->0.028450000000000003, (-0.0029945)->0.0029945) * 171.30368691960348)) * sin((2 * 3.141592653589793 * (X - 0.005690000000000001)) / 0.022760000000000002) * cos((1 * 3.141592653589793 * (Y + 0.0029945)) / 0.005989) * domain(0.005690000000000001->0.028450000000000003, (-0.0029945)->0.0029945) * 171.30368691960348) + (((domain(0.0->7.86E-4, (-0.001406)->0.001406) * 672.6374890636885) ** (sin((2 * 3.141592653589793 * (X - 0.005690000000000001)) / 0.022760000000000002) * cos((2 * 3.141592653589793 * (Y + 0.0029945)) / 0.005989) * domain(0.005690000000000001->0.028450000000000003, (-0.0029945)->0.0029945) * 171.30368691960348)) * sin((2 * 3.141592653589793 * (X - 0.005690000000000001)) / 0.022760000000000002) * cos((2 * 3.141592653589793 * (Y + 0.0029945)) / 0.005989) * domain(0.005690000000000001->0.028450000000000003, (-0.0029945)->0.0029945) * 171.30368691960348) + (((domain(0.0->7.86E-4, (-0.001406)->0.001406) * 672.6374890636885) ** (sin((2 * 3.141592653589793 * (X - 0.005690000000000001)) / 0.022760000000000002) * cos((3 * 3.141592653589793 * (Y + 0.0029945)) / 0.005989) * domain(0.005690000000000001->0.028450000000000003, (-0.0029945)->0.0029945) * 171.30368691960348)) * sin((2 * 3.141592653589793 * (X - 0.005690000000000001)) / 0.022760000000000002) * cos((3 * 3.141592653589793 * (Y + 0.0029945)) / 0.005989) * domain(0.005690000000000001->0.028450000000000003, (-0.0029945)->0.0029945) * 171.30368691960348) + (((domain(0.0->7.86E-4, (-0.001406)->0.001406) * 672.6374890636885) ** (sin((2 * 3.141592653589793 * (X - 0.005690000000000001)) / 0.022760000000000002) * cos((4 * 3.141592653589793 * (Y + 0.0029945)) / 0.005989) * domain(0.005690000000000001->0.028450000000000003, (-0.0029945)->0.0029945) * 171.30368691960348)) * sin((2 * 3.141592653589793 * (X - 0.005690000000000001)) / 0.022760000000000002) * cos((4 * 3.141592653589793 * (Y + 0.0029945)) / 0.005989) * domain(0.005690000000000001->0.028450000000000003, (-0.0029945)->0.0029945) * 171.30368691960348) + (((domain(0.0->7.86E-4, (-0.001406)->0.001406) * 672.6374890636885) ** (sin((3 * 3.141592653589793 * (X - 0.005690000000000001)) / 0.022760000000000002) * cos((1 * 3.141592653589793 * (Y + 0.0029945)) / 0.005989) * domain(0.005690000000000001->0.028450000000000003, (-0.0029945)->0.0029945) * 171.30368691960348)) * sin((3 * 3.141592653589793 * (X - 0.005690000000000001)) / 0.022760000000000002) * cos((1 * 3.141592653589793 * (Y + 0.0029945)) / 0.005989) * domain(0.005690000000000001->0.028450000000000003, (-0.0029945)->0.0029945) * 171.30368691960348) + (((domain(0.0->7.86E-4, (-0.001406)->0.001406) * 672.6374890636885) ** (sin((3 * 3.141592653589793 * (X - 0.005690000000000001)) / 0.022760000000000002) * cos((2 * 3.141592653589793 * (Y + 0.0029945)) / 0.005989) * domain(0.005690000000000001->0.028450000000000003, (-0.0029945)->0.0029945) * 171.30368691960348)) * sin((3 * 3.141592653589793 * (X - 0.005690000000000001)) / 0.022760000000000002) * cos((2 * 3.141592653589793 * (Y + 0.0029945)) / 0.005989) * domain(0.005690000000000001->0.028450000000000003, (-0.0029945)->0.0029945) * 171.30368691960348) + (((domain(0.0->7.86E-4, (-0.001406)->0.001406) * 672.6374890636885) ** (sin((3 * 3.141592653589793 * (X - 0.005690000000000001)) / 0.022760000000000002) * cos((3 * 3.141592653589793 * (Y + 0.0029945)) / 0.005989) * domain(0.005690000000000001->0.028450000000000003, (-0.0029945)->0.0029945) * 171.30368691960354)) * sin((3 * 3.141592653589793 * (X - 0.005690000000000001)) / 0.022760000000000002) * cos((3 * 3.141592653589793 * (Y + 0.0029945)) / 0.005989) * domain(0.005690000000000001->0.028450000000000003, (-0.0029945)->0.0029945) * 171.30368691960354) + (((domain(0.0->7.86E-4, (-0.001406)->0.001406) * 672.6374890636885) ** (sin((3 * 3.141592653589793 * (X - 0.005690000000000001)) / 0.022760000000000002) * cos((4 * 3.141592653589793 * (Y + 0.0029945)) / 0.005989) * domain(0.005690000000000001->0.028450000000000003, (-0.0029945)->0.0029945) * 171.30368691960348)) * sin((3 * 3.141592653589793 * (X - 0.005690000000000001)) / 0.022760000000000002) * cos((4 * 3.141592653589793 * (Y + 0.0029945)) / 0.005989) * domain(0.005690000000000001->0.028450000000000003, (-0.0029945)->0.0029945) * 171.30368691960348) + (((domain(0.0->7.86E-4, (-0.001406)->0.001406) * 672.6374890636885) ** (sin((4 * 3.141592653589793 * (X - 0.005690000000000001)) / 0.022760000000000002) * cos((1 * 3.141592653589793 * (Y + 0.0029945)) / 0.005989) * domain(0.005690000000000001->0.028450000000000003, (-0.0029945)->0.0029945) * 171.30368691960348)) * sin((4 * 3.141592653589793 * (X - 0.005690000000000001)) / 0.022760000000000002) * cos((1 * 3.141592653589793 * (Y + 0.0029945)) / 0.005989) * domain(0.005690000000000001->0.028450000000000003, (-0.0029945)->0.0029945) * 171.30368691960348) + (((domain(0.0->7.86E-4, (-0.001406)->0.001406) * 672.6374890636885) ** (sin((4 * 3.141592653589793 * (X - 0.005690000000000001)) / 0.022760000000000002) * cos((2 * 3.141592653589793 * (Y + 0.0029945)) / 0.005989) * domain(0.005690000000000001->0.028450000000000003, (-0.0029945)->0.0029945) * 171.30368691960348)) * sin((4 * 3.141592653589793 * (X - 0.005690000000000001)) / 0.022760000000000002) * cos((2 * 3.141592653589793 * (Y + 0.0029945)) / 0.005989) * domain(0.005690000000000001->0.028450000000000003, (-0.0029945)->0.0029945) * 171.30368691960348) + (((domain(0.0->7.86E-4, (-0.001406)->0.001406) * 672.6374890636885) ** (sin((4 * 3.141592653589793 * (X - 0.005690000000000001)) / 0.022760000000000002) * cos((3 * 3.141592653589793 * (Y + 0.0029945)) / 0.005989) * domain(0.005690000000000001->0.028450000000000003, (-0.0029945)->0.0029945) * 171.30368691960348)) * sin((4 * 3.141592653589793 * (X - 0.005690000000000001)) / 0.022760000000000002) * cos((3 * 3.141592653589793 * (Y + 0.0029945)) / 0.005989) * domain(0.005690000000000001->0.028450000000000003, (-0.0029945)->0.0029945) * 171.30368691960348) + (((domain(0.0->7.86E-4, (-0.001406)->0.001406) * 672.6374890636885) ** (sin((4 * 3.141592653589793 * (X - 0.005690000000000001)) / 0.022760000000000002) * cos((4 * 3.141592653589793 * (Y + 0.0029945)) / 0.005989) * domain(0.005690000000000001->0.028450000000000003, (-0.0029945)->0.0029945) * 171.30368691960348)) * sin((4 * 3.141592653589793 * (X - 0.005690000000000001)) / 0.022760000000000002) * cos((4 * 3.141592653589793 * (Y + 0.0029945)) / 0.005989) * domain(0.005690000000000001->0.028450000000000003, (-0.0029945)->0.0029945) * 171.30368691960348)) - (domain(0.0->7.86E-4, (-0.001406)->0.001406) * 672.6374890636885))");
        ExpressionRewriterFactory.getComputationSimplifier().addRewriteListener(new ExprRewriteLogger());
        e.simplify();
//        System.out.println(e);
    }
    public void testThis(String exprString){
        testThis(exprString,exprString);
    }

    public void testThis(String exprString,String expectedAfterParse){
        Expr e = Maths.expr(exprString);
        String s=e.toString().replace(" ","");
        System.out.println(exprString);
        System.out.println("\t\t"+s);
        String e0=expectedAfterParse.replace(" ","");
        String s0=s.replace(" ","");
        Assertions.assertEquals(e0,s0);

    }
}