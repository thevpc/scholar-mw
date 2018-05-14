package net.vpc.scholar.hadrumaths;

/**
 * Created by vpc on 4/17/14.
 */
public interface ReadableOutBoolean extends OutBoolean {


    static net.vpc.scholar.hadrumaths.ReadableOutBoolean create() {
        return new DefaultReadableOutBoolean();
    }

    boolean get();

    boolean isSet();

    void reset();

    class DefaultReadableOutBoolean implements net.vpc.scholar.hadrumaths.ReadableOutBoolean {
        public boolean value;

        public boolean get() {
            return value;
        }

        public void set(boolean t) {
            value = t;
        }

        public void set() {
            value = true;
        }

        public void reset() {
            value = false;
        }

        public boolean isSet() {
            return value;
        }
    }
}
