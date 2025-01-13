package net.thevpc.scholar.hadruwaves.str;

import net.thevpc.scholar.hadrumaths.ComplexMatrix;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 24 mai 2007 21:47:59
 */
public interface GenericCMatrixEvaluator extends MWStructureEvaluator {
    ComplexMatrix evaluate(MomStructure str);
}