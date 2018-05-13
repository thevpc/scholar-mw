package net.vpc.scholar.hadrumaths;

/**
 * Created by vpc on 4/17/14.
 */
public interface OutBoolean {


    static OutBoolean create() {
        return new DefaultOutBoolean();
    }

    static ReadableOutBoolean createReadable() {
        return ReadableOutBoolean.create();
    }

    static OutBoolean none() {
        return NoneOutBoolean.INSTANCE;
    }


//    boolean get();

    void set(boolean t);

    void set();

//    boolean isSet();

    class DefaultOutBoolean implements OutBoolean {
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

        public boolean isSet() {
            return value;
        }
    }



}
