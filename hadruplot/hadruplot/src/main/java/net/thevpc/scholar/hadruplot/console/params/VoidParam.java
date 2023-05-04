package net.thevpc.scholar.hadruplot.console.params;


public class VoidParam extends AbstractCParam implements Cloneable {

    public VoidParam(String name) {
        super(name);
    }

    @Override
    public void configure(Object source, Object value) {
        //do nothing
    }
}
