package net.vpc.scholar.hadruplot.console.params;

public class SimpleParamTitleProvider implements ParamTitleProvider {
    private String[] titles;

    public SimpleParamTitleProvider(String[] titles) {
        this.titles = titles;
    }

    public String getTitle(ParamSet param) {
        return titles[param.getIndex()];
    }
}
