package test;

import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadrumaths.ScalarProductOperatorFactory;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToDouble;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.CosXCosY;

public class Test3 {

    public static void main(String[] args) {
        CosXCosY x1=new CosXCosY(1,0,0,0.03,-1.5708096509060077, Domain.ofPoints(-4.996541666666667E-4,2.220685100472768E-4,4.996541666666667E-4,2.7758563967690645E-4));
        CosXCosY x2=new CosXCosY(1,0,0,0.03,-Math.PI,Domain.ofPoints(-4.996541666666667E-4,2.220685100472768E-4,4.996541666666667E-4,2.7758563967690645E-4));
        System.out.println(1.5708096509060077-(Math.PI/2));
        sp(x1);
        sp(x2);

    }
    private static void sp(CosXCosY x0){
        double v1 = ScalarProductOperatorFactory.formal().evalDD(x0, x0);
        double v2 = ScalarProductOperatorFactory.numeric().evalDD(x0, x0);
        double v3 = ScalarProductOperatorFactory.numeric().evalDD((DoubleToDouble) x0.simplify(), (DoubleToDouble) x0.simplify());
        System.out.println(x0);
        System.out.println("FORMAL       = " + v1);
        System.out.println("NUMERIC      = " + v2);
        System.out.println("NUMERIC_SIMP = " + v3);
    }
}
