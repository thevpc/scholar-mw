package net.thevpc.scholar.hadruplot.actions;

import net.thevpc.common.swing.TransferableImage;
import net.thevpc.scholar.hadruplot.Plot;
import net.thevpc.scholar.hadruplot.extension.PlotModelProvider;
import net.thevpc.scholar.hadruplot.actions.AbstractPlotAction;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.Serializable;
import java.awt.datatransfer.*;
public class CopyImageToClipboardAction extends AbstractPlotAction implements Serializable {
    private static File staticLastFolder;
    private PlotModelProvider plot;
    private File selectedFile;

    public CopyImageToClipboardAction(PlotModelProvider plot) {
        super("Copy image to Clipboard");
        this.plot = plot;
    }

    public void actionPerformed(ActionEvent e) {
        BufferedImage image = Plot.getImage(plot);
        class MyClipboardOwner implements ClipboardOwner{
            public void lostOwnership( Clipboard clip, Transferable trans ) {

            }
        }
        MyClipboardOwner owner=new MyClipboardOwner();
        TransferableImage trans = new TransferableImage( image );
        Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
        c.setContents( trans, owner );


    }


}
