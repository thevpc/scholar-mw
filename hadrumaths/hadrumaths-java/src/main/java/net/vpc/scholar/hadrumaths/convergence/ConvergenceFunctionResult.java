package net.vpc.scholar.hadrumaths.convergence;

/**
 * Created by vpc on 7/14/14.
 */
public class ConvergenceFunctionResult<_ResultType> {
    private final _ResultType value;
    private final Object resultInfo;

    public ConvergenceFunctionResult(_ResultType value, Object resultInfo) {
        this.value = value;
        this.resultInfo = resultInfo;
    }

    public _ResultType getValue() {
        return value;
    }

    public Object getResultInfo() {
        return resultInfo;
    }

    @Override
    public String toString() {
        return "ConvergenceFunctionResult{" +
                "value=" + value +
                ", resultInfo=" + resultInfo +
                '}';
    }
}
