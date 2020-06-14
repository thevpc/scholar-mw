package net.vpc.scholar.hadruplot.libraries.simple.curve;

//import com.sun.image.codec.jpeg.JPEGCodec;
//import com.sun.image.codec.jpeg.JPEGEncodeParam;
//import com.sun.image.codec.jpeg.JPEGImageEncoder;

import net.vpc.scholar.hadruplot.actions.AbstractPlotAction;

import javax.imageio.ImageIO;
import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Graph extends JPanel implements Printable {

    private String title;                    // Graphic title

    private Font font;                        // Graphic font

    private Font titleFont;                    // Title font

    private FontMetrics metrics;            // Font metrics

    private Color paperColor = Color.white;    // Background color

    private Color txtColor = Color.black;    // Graph text color

    private int graphWidth = 0;            // Graph Width

    private int graphHeight = 0;            // Graph Height

    private static Color[] preDefColors = new Color[]{
            Color.blue, Color.magenta, Color.cyan, Color.orange, Color.green,
            Color.pink, Color.yellow, Color.red, new Color(0, 102, 204), new Color(102, 0, 204),
            new Color(204, 0, 204), new Color(204, 0, 0), new Color(0, 51, 204),
            new Color(153, 0, 204), new Color(204, 0, 153), new Color(204, 51, 0), new Color(204, 204, 0)
    };

    public Graph() {
    }

    public abstract void paintGraph(Graphics g);

    public static Color getPreDefColors(int index) {
        return preDefColors[(index % 17)];
    }

    public int getGraphWidth() {
        return graphWidth;
    }

    public void setGraphWidth(int wdt) {
        graphWidth = wdt;
    }

    public int getGraphHeight() {
        return graphHeight;
    }

    public void setGraphHeight(int hght) {
        graphHeight = hght;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title != null) {
            this.title = title;
        }
    }

    public Font getTextFont() {
        return font;
    }

    public void setTextFont(Font font) {
        if (font != null) {
            this.font = font;
            metrics = getFontMetrics(font);
        }
    }

    public FontMetrics getMetrics() {
        return metrics == null ? getFontMetrics(getFont()) : metrics;
    }

    public Font getTitleFont() {
        return titleFont == null ? getFont() : titleFont;
    }

    public void setTitleFont(Font font) {
        if (font != null) {
            titleFont = font;
        }
    }

    public Color getPaperColor() {
        return paperColor;
    }

    public void setPaperColor(Color color) {
        if (color != null) {
            paperColor = color;
        }
    }

    public Color getTextColor() {
        return txtColor;
    }

    public void setTextColor(Color color) {
        if (color != null) {
            txtColor = color;
        }
    }

    public BufferedImage getImage() {

        if (graphWidth > 0 && graphHeight > 0) {
            BufferedImage bi = new BufferedImage(graphWidth, graphHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = bi.createGraphics();
            paintGraph(g2d);
            return bi;
        } else {
            BufferedImage bi = new BufferedImage(600, 500, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = bi.createGraphics();
            paintGraph(g2d);
            return bi;
//            return null;
        }
    }

    public int print(Graphics g, PageFormat pageFormat, int pageIndex) {
        if (pageIndex > 0) {
            return Printable.NO_SUCH_PAGE;
        } else {
            paintGraph(g);
            return Printable.PAGE_EXISTS;
        }
    }

    public JPanel addSavePrintToolBar() {
        //print
        JButton printBtn = new JButton("Print");
        printBtn.setToolTipText("Print Graphic");
        printBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                PrintService printService = PrintServiceLookup.lookupDefaultPrintService();
                if (printService == null) {
                    Logger.getLogger(Graph.class.getName()).log(Level.SEVERE, null, "Graphics.Graph.noPrinter");
                } else {
                    DocPrintJob printerJob = PrintServiceLookup.lookupDefaultPrintService().createPrintJob();
                    DocFlavor flavor = javax.print.DocFlavor.SERVICE_FORMATTED.PRINTABLE;
                    PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
                    aset.add(new MediaPrintableArea(2, 5, 205, 280, MediaPrintableArea.MM));
                    javax.print.Doc doc = new SimpleDoc(Graph.this, flavor, null);
                    try {
                        printerJob.print(doc, aset);
                        //Log.trace(Swings.getResources().get("Graphics.Graph.PrintSucceeded", Graph.this.getTitle()));
                    } catch (Exception ee) {
                        //Log.error(Swings.getResources().get("Graphics.Graph.PrintError"));
                        //Log.error(ee);
                        ee.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Graphics.Graph.PrintError");
                    }
                }
            }
        });

        //save
        JButton saveBtn = new JButton("Save");
        saveBtn.setToolTipText("Graphics.Graph.ExportGraph");
        saveBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                try {
                    BufferedImage img = Graph.this.getImage();
                    if (img != null) {
                        JFileChooser chooser = new JFileChooser();
                        FileFilter filter = new FileFilter() {

                            public boolean accept(File f) {
                                if (f.isDirectory() || f.getName().toLowerCase().endsWith(".jpeg")) {
                                    return true;
                                } else {
                                    return false;
                                }
                            }

                            public String getDescription() {
                                return ("Graphics.Graph.jpegImages");
                            }
                        };
                        chooser.setFileFilter(filter);
                        int returnVal = chooser.showSaveDialog(Graph.this);
                        if (returnVal == JFileChooser.APPROVE_OPTION) {
                            File file = chooser.getSelectedFile();
                            FileOutputStream out = new FileOutputStream(file);
                            ImageIO.write(img, "jpeg", file);
                            //Log.trace(Swings.getResources().get("Graphics.Graph.SaveSucceeded", Graph.this.getTitle(), file.getCanonicalPath()));
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Graphics.Graph.NullImage");
                    }
                } catch (Exception eee) {
                    //Log.error(Swings.getResources().get("Graphics.Graph.SaveError"));
                    //Log.error(eee);
                    eee.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Graphics.Graph.SaveError");
                }
            }
        });

        JToolBar toolBar = new JToolBar();
        toolBar.add(saveBtn);
        toolBar.add(printBtn);
        JPanel graphPanel = new JPanel(new BorderLayout());
        graphPanel.add(toolBar, BorderLayout.PAGE_START);
        graphPanel.add(this, BorderLayout.CENTER);
        return graphPanel;
    }

    public Action createSaveImageAction() {
        return new SaveImageAction();
    }

    public Action createPrintImageAction() {
        return new PrintImageAction();
    }

    public boolean saveImage(File file) throws IOException {
        BufferedImage img = Graph.this.getImage();
        if (img != null) {
            ImageIO.write(img, "jpeg", file);
            return true;
        }
        return false;
    }

    public boolean printImage() throws PrintException {
        PrintService printService = PrintServiceLookup.lookupDefaultPrintService();
        if (printService == null) {
            return false;
        } else {
            DocPrintJob printerJob = PrintServiceLookup.lookupDefaultPrintService().createPrintJob();
            DocFlavor flavor = javax.print.DocFlavor.SERVICE_FORMATTED.PRINTABLE;
            PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
            aset.add(new MediaPrintableArea(2, 5, 205, 280, MediaPrintableArea.MM));
            javax.print.Doc doc = new SimpleDoc(Graph.this, flavor, null);
            printerJob.print(doc, aset);
            return true;
        }
    }

    protected class SaveImageAction extends AbstractPlotAction {

        public SaveImageAction() {
            super("SaveImageAction");
            this.putValue(AbstractAction.NAME, "");
            this.putValue(AbstractAction.SHORT_DESCRIPTION, "Graphics.Graph.ExportGraph");
            //this.putValue(AbstractAction.SMALL_ICON,"/images/net/vpc/swing/Save.gif");
        }

        public void actionPerformed(ActionEvent event) {
            try {
                BufferedImage img = Graph.this.getImage();
                if (img != null) {
                    JFileChooser chooser = new JFileChooser();
                    FileFilter filter = new FileFilter() {

                        public boolean accept(File f) {
                            if (f.isDirectory() || f.getName().toLowerCase().endsWith(".jpeg") || f.getName().toLowerCase().endsWith(".jpg")) {
                                return true;
                            } else {
                                return false;
                            }
                        }

                        public String getDescription() {
                            return ("Graphics.Graph.jpegImages");
                        }
                    };
                    chooser.setFileFilter(filter);
                    int returnVal = chooser.showSaveDialog(Graph.this);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        File file = chooser.getSelectedFile();
                        if (file.getName().indexOf('.') < 0) {
                            file = new File(file.getParentFile(), file.getName() + ".jpeg");
                        }
                        saveImage(file);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, ("Graphics.Graph.NullImage"));
                }
            } catch (Exception eee) {
                eee.printStackTrace();
                JOptionPane.showMessageDialog(null, ("Graphics.Graph.SaveError"));
            }
        }
    }

    protected class PrintImageAction extends AbstractPlotAction {

        public PrintImageAction() {
            super("PrintImageAction");
            this.putValue(AbstractAction.NAME, "");
            this.putValue(AbstractAction.SHORT_DESCRIPTION, ("Graphics.Graph.PrintGraph"));
            //this.putValue(AbstractAction.SMALL_ICON,Resources.loadImageIcon("/images/net/vpc/report/Print.gif"));
        }

        public void actionPerformed(ActionEvent event) {
            PrintService printService = PrintServiceLookup.lookupDefaultPrintService();
            if (printService == null) {
                Logger.getLogger(Graph.class.getName()).log(Level.SEVERE, null, "Graphics.Graph.noPrinter");
            } else {
                DocPrintJob printerJob = PrintServiceLookup.lookupDefaultPrintService().createPrintJob();
                DocFlavor flavor = javax.print.DocFlavor.SERVICE_FORMATTED.PRINTABLE;
                PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
                aset.add(new MediaPrintableArea(2, 5, 205, 280, MediaPrintableArea.MM));
                javax.print.Doc doc = new SimpleDoc(Graph.this, flavor, null);
                try {
                    printerJob.print(doc, aset);
                } catch (Exception ee) {
                    ee.printStackTrace();
                    JOptionPane.showMessageDialog(null, ("Graphics.Graph.PrintError"));
                }
            }
        }
    }
}