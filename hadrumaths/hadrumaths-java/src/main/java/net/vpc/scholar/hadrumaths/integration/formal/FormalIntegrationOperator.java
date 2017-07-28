package net.vpc.scholar.hadrumaths.integration.formal;

import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToDouble;
import net.vpc.scholar.hadrumaths.util.ClassMapList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vpc on 11/22/16.
 */
public class FormalIntegrationOperator {
    private List<FormalIntegrationOperatorHasher> hashers=new ArrayList<FormalIntegrationOperatorHasher>();
    private ClassMapList<FormalIntegrationOperatorHasher> hashersMap=new ClassMapList<FormalIntegrationOperatorHasher>();

    public interface FormalIntegrationOperatorHasher{
        Class[] typeFilters();
        boolean accept(Expr expr);
    }

    public interface FormalIntegrationOperatorProcessor{
        double evalDD(Domain domain, DoubleToDouble f1);
    }

    public double evalDD(Domain domain, DoubleToDouble f1){
        throw new RuntimeException("Bad Error");
    }

}
