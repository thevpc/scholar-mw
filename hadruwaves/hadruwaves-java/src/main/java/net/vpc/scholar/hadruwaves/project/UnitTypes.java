package net.vpc.scholar.hadruwaves.project;

public class UnitTypes {

    public interface UTypeBase {
        UType type();
    }

    public enum UType {
        Dimension,
        Frequency,
        Number,
    }

    public enum Number implements UTypeBase{
        Double,
        Int;

        @Override
        public UType type() {
            return UType.Number;
        }
    }

    public enum Dimension implements UTypeBase{
        m(1L),
        mm(1E-3),
        um(1E-6),
        nm(1E-9),
        pm(1E-12);
        private double multiplier;

        private Dimension(double multiplier) {
            this.multiplier = multiplier;
        }

        public double getMultiplier() {
            return multiplier;
        }
        @Override
        public UType type() {
            return UType.Dimension;
        }
    }

    public enum Frequency implements UTypeBase{
        Hz(1L),
        KHz(1000L),
        MHz(1_000_000L),
        GHz(1_000_000_000L),
        TH(1_000_000_000_000L);
        private long multiplier;

        private Frequency(long multiplier) {
            this.multiplier = multiplier;
        }

        public long getMultiplier() {
            return multiplier;
        }
        @Override
        public UType type() {
            return UType.Frequency;
        }
    }
}
