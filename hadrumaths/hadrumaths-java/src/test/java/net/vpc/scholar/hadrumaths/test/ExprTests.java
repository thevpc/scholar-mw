package net.vpc.scholar.hadrumaths.test;

import net.vpc.common.classpath.ClassPathUtils;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.symbolic.Linear;
import net.vpc.scholar.hadrumaths.util.PlatformUtils;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Modifier;

import static net.vpc.scholar.hadrumaths.Maths.*;
import static net.vpc.scholar.hadrumaths.Maths.sin;

public class ExprTests {
    @Test
    public void testEqualsAndHashCode() {
        for (Class cls : ClassPathUtils.resolveContextClassesList()) {
            if (Expr.class.isAssignableFrom(cls) && !Modifier.isAbstract(cls.getModifiers())) {
                PlatformUtils.requireEqualsAndHashCode(cls);
            }
        }
    }

    @Test
    public void testNormalize(){
        Expr e=sin(X)*xydomain(0,2,0,2);
        Assert.assertEquals("sin(X) * domain(0->2, 0->2) * 0.6484215470020839",String.valueOf(normalize(e)));
    }

    @Test
    public void testExample(){
        System.out.println(Linear.castOrConvert(expr(2,xdomain(0,1))));
    }
}
