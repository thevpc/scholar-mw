package net.vpc.scholar.hadrumaths.expeval;

class IdentityUnaryOp extends UnaryOp {
    private final Object arg;

    public IdentityUnaryOp(String name, Object arg) {
        super(name, arg.getClass());
        this.arg = arg;
    }

    @Override
    public Object evaluate(Object a) {
        return a;
    }
}
