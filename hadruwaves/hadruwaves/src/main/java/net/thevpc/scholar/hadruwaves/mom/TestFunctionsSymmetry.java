package net.thevpc.scholar.hadruwaves.mom;

import net.thevpc.nuts.elem.NElement;
import net.thevpc.nuts.log.NLog;
import net.thevpc.nuts.text.NMsg;
import net.thevpc.nuts.util.NNameFormat;
import net.thevpc.nuts.util.NOptional;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
* @creationtime 24 oct. 2006 22:55:55
*/
public enum TestFunctionsSymmetry {
    /**
     * No symmetry is known
     */
    NO_SYMMETRY,
    
    /**
     * Structure has a Vertical symmetry axis so each gp is sum of 2 gps in the 2 sides
     */
    SUM_X_SYMMETRY,
    
    
    /**
     * Structure has a Vertical symmetry axis so each gp has its symmetric in the other side
     */
    EACH_X_SYMMETRY,

    /**
     * Structure has a Horizontal symmetry axis so each gp is sum of 2 gps in the 2 sides
     */
    SUM_Y_SYMMETRY,
    
    /**
     * Structure has a Horizontal symmetry axis so each gp has its symmetric in the other side
     */
    EACH_Y_SYMMETRY;


    public static NOptional<TestFunctionsSymmetry> parse(NElement value) {
        if(value==null) {
            return NOptional.<TestFunctionsSymmetry>ofNamedEmpty(NMsg.ofC("symmetry")).withDefault(()->TestFunctionsSymmetry.NO_SYMMETRY);
        }
        NOptional<String> v = value.asStringValue();
        if (v.isPresent()) {
            String i = v.get();
            switch (NNameFormat.LOWER_KEBAB_CASE.format(i)) {
                case "no":
                case "none": {
                    return NOptional.of(TestFunctionsSymmetry.NO_SYMMETRY);
                }
                case "each-x":
                case "eachx": {
                    return NOptional.of(TestFunctionsSymmetry.EACH_X_SYMMETRY);
                }
                case "each-y":
                case "eachy": {
                    return NOptional.of(TestFunctionsSymmetry.EACH_Y_SYMMETRY);
                }
                case "sum-x":
                case "sumx": {
                    return NOptional.of(TestFunctionsSymmetry.SUM_X_SYMMETRY);
                }
                case "sum-y":
                case "sumy": {
                    return NOptional.of(TestFunctionsSymmetry.SUM_Y_SYMMETRY);
                }
            }
        }
        NLog.ofScoped(TestFunctionsSymmetry.class).log(NMsg.ofC("invalid TestFunctionsSymmetry %s", value).asError());
        return NOptional.<TestFunctionsSymmetry>ofError(NMsg.ofC("invalid TestFunctionsSymmetry %s", value)).withDefault(NO_SYMMETRY);
    }
}
