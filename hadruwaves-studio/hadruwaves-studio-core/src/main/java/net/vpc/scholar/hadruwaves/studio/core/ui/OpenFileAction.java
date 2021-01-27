/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwaves.studio.core.ui;

/**
 *
 * @author vpc
 */
import java.awt.event.ActionListener;
import java.io.File;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.cookies.OpenCookie;
import org.openide.filesystems.FileChooserBuilder;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;

@ActionID(category = "File", id = "net.thevpc.scholar.hadruwaves.studio.core.ui.OpenFileAction")
@ActionRegistration(displayName = "#CTL_OpenFileAction")
@ActionReference(path = "Menu/File", position = 10)
@Messages("CTL_OpenFileAction=Open File")
public class OpenFileAction implements ActionListener {

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
//The default dir to use if no value is stored
        File home = new File(System.getProperty("user.home"));
        //Now build a file chooser and invoke the dialog in one line of code
        //"user-dir" is our unique key
        File toAdd = new FileChooserBuilder("user-dir").setTitle("Open File").
                setDefaultWorkingDirectory(home).setApproveText("Open").showOpenDialog();
        //Result will be null if the user clicked cancel or closed the dialog w/o OK
        if (toAdd != null) {
            try {
                DataObject.find(FileUtil.toFileObject(toAdd)).
                        getLookup().lookup(OpenCookie.class).open();
            } catch (DataObjectNotFoundException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }

}
