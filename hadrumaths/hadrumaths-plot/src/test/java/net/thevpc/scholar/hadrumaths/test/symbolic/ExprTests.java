package net.thevpc.scholar.hadrumaths.test.symbolic;

//package net.thevpc.scholar.hadrumaths.test;
//
//import net.thevpc.common.classpath.ClassPathUtils;
//import net.thevpc.scholar.hadrumaths.Expr;
//import net.thevpc.scholar.hadrumaths.Maths;
//import net.thevpc.scholar.hadrumaths.symbolic.double2double.Linear;
//import net.thevpc.scholar.hadrumaths.util.PlatformUtils;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//
//import java.lang.reflect.Modifier;
//
//import static net.thevpc.scholar.hadrumaths.Maths.*;
//import static net.thevpc.scholar.hadrumaths.Maths.sin;
//
//public class ExprTests {
//    @Test
//    public void testEqualsAndHashCode() {
//        for (Class cls : ClassPathUtils.resolveContextClassesList()) {
//            if (Expr.class.isAssignableFrom(cls) && !Modifier.isAbstract(cls.getModifiers())) {
//                PlatformUtils.requireEqualsAndHashCode(cls);
//            }
//        }
//    }
//
//    @Test
//    public void testNormalize(){
//        Expr e=sin(X)*domain(0,2,0,2);
//        Assertions.assertEquals("sin(X) * domain(0->2, 0->2) * 0.6484215470020839",String.valueOf(normalize(e)));
//    }
//
//    @Test
//    public void testExample(){
//        System.out.println(Linear.castOrConvert(expr(2,xdomain(0,1))));
//    }
//}
