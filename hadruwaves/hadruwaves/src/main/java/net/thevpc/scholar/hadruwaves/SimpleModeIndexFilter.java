package net.thevpc.scholar.hadruwaves;


import net.thevpc.nuts.elem.NElement;



public abstract class SimpleModeIndexFilter implements ModeIndexFilter {
    private boolean frequencyDependent;

    public SimpleModeIndexFilter(boolean frequencyDependent) {
        this.frequencyDependent = frequencyDependent;
    }

    @Override
    public boolean isFrequencyDependent() {
        return false;
    }

    @Override
    public NElement toElement() {
        NObjectElementBuilder sb = NElement.ofObjectBuilder(getClass().getSimpleName());
        sb.add("frequencyDependent", NElementHelper.elem(frequencyDependent));
        return sb.build();
    }
//    public Dumper getDumpStringHelper() {
//        Dumper h = new Dumper(this);
//        h.add("frequencyDependent", frequencyDependent);
//        return h;
//    }
//    @Override
//    public String dump() {
//        return getDumpStringHelper().toString();
//    }
}
