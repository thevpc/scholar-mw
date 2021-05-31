package net.thevpc.scholar.hadruwavesstudio.standalone.v2.components;

import net.thevpc.common.props.*;

public class AppEditorThemes {
    private WritableLiMap<String, HWTextEditorTheme> editorThemes = Props.of("editorThemes").lmapOf(String.class, HWTextEditorTheme.class,
            x -> x.getId()
    );
    private WritableString editorTheme = Props.of("editorTheme").stringOf("Default");
    private WritableBoolean usePlaf = Props.of("usePlaf").booleanOf(false);

    public AppEditorThemes() {
        editorThemes.add(new HWTextEditorTheme("default", "Default", "org/fife/ui/rsyntaxtextarea/themes/default.xml"));
        editorThemes.add(new HWTextEditorTheme("default-alt", "Alternative", "org/fife/ui/rsyntaxtextarea/themes/default-alt.xml"));
        editorThemes.add(new HWTextEditorTheme("dark", "Dark", "org/fife/ui/rsyntaxtextarea/themes/dark.xml"));
        editorThemes.add(new HWTextEditorTheme("eclipse", "Eclipse", "org/fife/ui/rsyntaxtextarea/themes/eclipse.xml"));
        editorThemes.add(new HWTextEditorTheme("idea", "Idea", "org/fife/ui/rsyntaxtextarea/themes/idea.xml"));
        editorThemes.add(new HWTextEditorTheme("vs", "Visual Studio", "org/fife/ui/rsyntaxtextarea/themes/vs.xml"));
    }

    public WritableLiMap<String, HWTextEditorTheme> editorThemes() {
        return editorThemes;
    }

    public WritableString id() {
        return editorTheme;
    }

    public WritableBoolean usePlaf() {
        return usePlaf;
    }
}
