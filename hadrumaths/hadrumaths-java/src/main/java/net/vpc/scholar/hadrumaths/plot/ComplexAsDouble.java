package net.vpc.scholar.hadrumaths.plot;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.vpc.scholar.hadrumaths.Complex;

/**
 * Created by IntelliJ IDEA. User: vpc Date: 9 sept. 2005 Time: 22:35:03 To
 * change this template use File | Settings | File Templates.
 */
public interface ComplexAsDouble {
//    ABS, REAL, IMG, DB, DB2, ARG, COMPLEX

    public static final ComplexAsDouble ABS = new AbstractComplexAsDouble("ABS") {
        @Override
        public double toDouble(Complex c) {
            return c.absdbl();
        }
    };
    public static final ComplexAsDouble REAL = new AbstractComplexAsDouble("REAL") {
        @Override
        public double toDouble(Complex c) {
            return c.realdbl();
        }
    };
    public static final ComplexAsDouble IMG = new AbstractComplexAsDouble("IMG") {
        @Override
        public double toDouble(Complex c) {
            return c.imagdbl();
        }
    };
    public static final ComplexAsDouble DB = new AbstractComplexAsDouble("DB") {
        @Override
        public double toDouble(Complex c) {
            return c.dbdbl();
        }
    };
    public static final ComplexAsDouble DB2 = new AbstractComplexAsDouble("DB2") {
        @Override
        public double toDouble(Complex c) {
            return c.db2dbl();
        }
    };
    public static final ComplexAsDouble ARG = new AbstractComplexAsDouble("ARG") {
        @Override
        public double toDouble(Complex c) {
            return c.argdbl();
        }
    };

    public static ComplexAsDouble[] values() {
        List<ComplexAsDouble> all = new ArrayList<>();
        for (Field declaredField : ComplexAsDouble.class.getDeclaredFields()) {
            try {
                Object t = declaredField.get(null);
                if (t instanceof ComplexAsDouble) {
                    all.add((ComplexAsDouble) t);
                }
            } catch (Exception ex) {
                throw new IllegalArgumentException("Unsupported");
            }
        }
        return all.toArray(new ComplexAsDouble[all.size()]);
    }

    public static ComplexAsDouble valueOf(String n) {
        for (ComplexAsDouble val : values()) {
            if (val.getName().equals(n)) {
                return val;
            }
        }
        return null;
    }

    public static abstract class AbstractComplexAsDouble implements ComplexAsDouble {

        private String name;

        public AbstractComplexAsDouble(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return String.valueOf(name);
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 23 * hash + Objects.hashCode(this.name);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final AbstractComplexAsDouble other = (AbstractComplexAsDouble) obj;
            if (!Objects.equals(this.name, other.name)) {
                return false;
            }
            return true;
        }

    }

    public String getName();

    public double toDouble(Complex c);
//            public static double toDouble(Complex c) {
//        if (d == null) {
//            return c.absdbl();
//        }
//        switch (d) {
//            case ABS:
//                return c.absdbl();
//            case REAL:
//                return c.realdbl();
//            case IMG:
//                return c.imagdbl();
//            case DB:
//                return db(c.absdbl());
//            case DB2:
//                return db2(c.absdbl());
//            case ARG:
//                return c.arg().getReal();
//            case COMPLEX:
//                return c.absdbl();
//        }
//        return Double.NaN;
//    }
}
