package net.thevpc.scholar.hadrumaths.format.params;

import net.thevpc.scholar.hadrumaths.format.ObjectFormatParam;

import java.util.Objects;

/**
 * Created by IntelliJ IDEA. User: vpc Date: 23 juil. 2005 Time: 11:21:16 To
 * change this template use File | Settings | File Templates.
 */
public class ProductObjectFormatParam implements ObjectFormatParam {

    private final String op;

    public ProductObjectFormatParam(String op) {
        this.op = op;
    }

    public String getOp() {
        return op;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductObjectFormatParam that = (ProductObjectFormatParam) o;
        return Objects.equals(op, that.op);
    }

    @Override
    public int hashCode() {
        return Objects.hash(op);
    }
}
