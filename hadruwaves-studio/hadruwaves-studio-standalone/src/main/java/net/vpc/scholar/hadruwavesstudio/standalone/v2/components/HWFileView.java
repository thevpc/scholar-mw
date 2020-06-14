package net.vpc.scholar.hadruwavesstudio.standalone.v2.components;

import net.vpc.scholar.hadruwavesstudio.standalone.v2.HadruwavesStudio;

import javax.swing.*;
import javax.swing.filechooser.FileView;
import java.io.File;

public class HWFileView extends FileView {
    private HadruwavesStudio studio;
    public HWFileView(HadruwavesStudio studio) {
        this.studio=studio;
    }
    /**
     * The name of the file. Normally this would be simply
     * <code>f.getName()</code>.
     */
    public String getName(File f) {
        return null;
    };

    /**
     * A human readable description of the file. For example,
     * a file named <i>jag.jpg</i> might have a description that read:
     * "A JPEG image file of James Gosling's face".
     */
    public String getDescription(File f) {
        return null;
    }

    /**
     * A human readable description of the type of the file. For
     * example, a <code>jpg</code> file might have a type description of:
     * "A JPEG Compressed Image File"
     */
    public String getTypeDescription(File f) {
        return null;
    }

    /**
     * The icon that represents this file in the <code>JFileChooser</code>.
     */
    public Icon getIcon(File f) {
        if(f==null){
            return null;
        }
        return studio.app().iconSet().iconForFile(f,false,false).get();
    }

    /**
     * Whether the directory is traversable or not. This might be
     * useful, for example, if you want a directory to represent
     * a compound document and don't want the user to descend into it.
     */
    public Boolean isTraversable(File f) {
        return null;//base.isTraversable(f);
    }

}
