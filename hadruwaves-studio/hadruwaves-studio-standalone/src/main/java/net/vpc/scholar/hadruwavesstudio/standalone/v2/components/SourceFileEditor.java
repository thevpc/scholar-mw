package net.vpc.scholar.hadruwavesstudio.standalone.v2.components;

import net.vpc.scholar.hadruwavesstudio.standalone.v2.HadruwavesStudio;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.explorer.components.HWProjectSourceFile;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import javax.swing.*;
import java.awt.*;

public class SourceFileEditor extends JPanel {
    private HWProjectSourceFile file;
    private String type;
    private HadruwavesStudio studio;
    private HWTextEditor textEditor;

    public SourceFileEditor(HadruwavesStudio studio) {
        super(new BorderLayout());
        this.studio = studio;
        this.textEditor = new HWTextEditor(studio, 120, 60);
        add(textEditor, BorderLayout.CENTER);
    }

    public RSyntaxTextArea getTextArea() {
        return textEditor.getTextArea();
    }


    public HWTextEditor getTextEditor() {
        return textEditor;
    }

    public void saveFile() {
        studio.saveFileObject(file);
    }

    public void newFile() {
        getTextEditor().setText("");
        setEditable(true);
    }

    public void loadFile(HWProjectSourceFile file) {
        setFile(file);
        getTextEditor().setTypeByFileName(file.name().get());
        loadFile();
    }

    public void loadFile() {
        file.load();
        getTextEditor().setText(file.content().get());
        setEditable(file.file.canWrite());
    }

    public void setEditable(boolean b) {
        getTextEditor().setEditable(b);
    }

    public HWProjectSourceFile getFile() {
        return file;
    }

    public SourceFileEditor setFile(HWProjectSourceFile file) {
        this.file = file;
        return this;
    }

    public String getType() {
        return type;
    }


    public SourceFileEditor setType(String type) {
        this.type = type;
        getTextEditor().setType(type);
        return this;
    }
}
