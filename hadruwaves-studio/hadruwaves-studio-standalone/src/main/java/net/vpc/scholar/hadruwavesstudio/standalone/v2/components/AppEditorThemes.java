package net.vpc.scholar.hadruwavesstudio.standalone.v2.components;

import net.vpc.common.props.Props;
import net.vpc.common.props.WritablePLMap;
import net.vpc.common.props.WritablePList;
import net.vpc.common.props.WritablePValue;

public class AppEditorThemes {
    private WritablePLMap<String, HWTextEditorTheme> editorThemes = Props.of("editorThemes").lmapOf(String.class, HWTextEditorTheme.class,
            x -> x.getId()
    );
    private WritablePValue<String> editorTheme = Props.of("editorTheme").valueOf(String.class, "Default");
    private WritablePValue<Boolean> usePlaf = Props.of("usePlaf").valueOf(Boolean.class, false);

    public AppEditorThemes() {
        editorThemes.add(new HWTextEditorTheme("default", "Default", "org/fife/ui/rsyntaxtextarea/themes/default.xml"));
        editorThemes.add(new HWTextEditorTheme("default-alt", "Alternative", "org/fife/ui/rsyntaxtextarea/themes/default-alt.xml"));
        editorThemes.add(new HWTextEditorTheme("dark", "Dark", "org/fife/ui/rsyntaxtextarea/themes/dark.xml"));
        editorThemes.add(new HWTextEditorTheme("eclipse", "Eclipse", "org/fife/ui/rsyntaxtextarea/themes/eclipse.xml"));
        editorThemes.add(new HWTextEditorTheme("idea", "Idea", "org/fife/ui/rsyntaxtextarea/themes/idea.xml"));
        editorThemes.add(new HWTextEditorTheme("vs", "Visual Studio", "org/fife/ui/rsyntaxtextarea/themes/vs.xml"));
    }

    public WritablePLMap<String, HWTextEditorTheme> editorThemes() {
        return editorThemes;
    }

    public WritablePValue<String> id() {
        return editorTheme;
    }

    public WritablePValue<Boolean> usePlaf() {
        return usePlaf;
    }
}
