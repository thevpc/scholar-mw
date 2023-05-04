package net.thevpc.scholar.hadruwavesstudio.standalone.v1.panels;

//package org.vpc.momlib.application.panels;
//
//import org.vpc.lib.application.Application;
//import org.vpc.lib.io.Files;
//import org.vpc.lib.swing.JFolderChooser;
//import org.vpc.lib.swing.JOptionPane2;
//import org.vpc.lib.util.Resources;
//import org.vpc.lib.util.Utils;
//import jxl.Workbook;
//import jxl.write.*;
//import org.vpc.math.*;
//import org.vpc.math.plot.Plot;
//
//import javax.print.PrintException;
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.PrintStream;
//import java.util.Date;
//
///**
// * User: taha
// * Date: 9 aout 2003
// * Time: 10:20:41
// */
//public class ComplexPlot extends JPanel {
//    Plot p;
//    Plot pr;
//    Plot pi;
//    Plot db;
//    double[] x;
//    double[] y;
//    Object z;
//    String title;
//    File folder;
//    JTabbedPane jTabbedPane;
//
//    public ComplexPlot(File folder, String title, double[] x, Complex[] z) {
//        super(new BorderLayout());
//        this.folder = folder;
//        this.title = title;
//        this.x = x;
//        this.z = z;
//        JToolBar bar = new JToolBar(JToolBar.HORIZONTAL);
//        bar.add(new SaveAllAction());
//        bar.add(new PrintAllDataAction());
//        jTabbedPane = new JTabbedPane();
//        jTabbedPane.addTab("Module", p = new Plot(title + ", en module", x, CArrays.abs(z)));
//        jTabbedPane.addTab(
//                Application.getResources().get("complex.real")
//                , pr = new Plot(title + ", en partie r\u00E9elle", x, CArrays.getReal(z)));
//        jTabbedPane.addTab(Application.getResources().get("complex.img"), pi = new Plot(title + ", en partie imaginaire", x, CArrays.getImag(z)));
//        jTabbedPane.addTab("DB", db = new Plot(title + ", en module (DB)", x, Math2.db(CArrays.abs(z))));
//        add(bar, BorderLayout.PAGE_START);
//        add(jTabbedPane, BorderLayout.CENTER);
//    }
//
//    public ComplexPlot(File folder, String title, double[] x, double[] y, Complex[][] z) {
//        super(new BorderLayout());
//        this.folder = folder;
//        this.title = title;
//        this.x = x;
//        this.y = y;
//        this.z = z;
//        JToolBar bar = new JToolBar(JToolBar.HORIZONTAL);
//        bar.add(new SaveAllAction());
//        bar.add(new PrintAllDataAction());
//        jTabbedPane = new JTabbedPane();
//        jTabbedPane.addTab("Module3D", p = Plot.surface(title + ", en module", x, y, CArrays.abs(z),true));
//        jTabbedPane.addTab("Module", p = Plot.surface(title + ", en module", x, y, CArrays.abs(z),false));
//        jTabbedPane.addTab(Application.getResources().get("complex.real"), pr = Plot.surface(title + ", en partie r\u00E9elle", x, y, CArrays.getReal(z),false));
//        jTabbedPane.addTab(Application.getResources().get("complex.img"), pi = Plot.surface(title + ", en partie imaginaire", x, y, CArrays.getImag(z),false));
//        jTabbedPane.addTab("DB", db = Plot.surface(title + ", en module (DB)", x, y, Math2.db(CArrays.abs(z)),false));
//        add(bar, BorderLayout.PAGE_START);
//        add(jTabbedPane, BorderLayout.CENTER);
//    }
//
//    public void saveAllData(File folder,String format) throws IOException {
//        saveData(new File(folder, title + "."+format));
////        p.saveImage(new File(folder, title + "-MODULE."+format));
////        pr.saveImage(new File(folder, title + "-REAL."+format));
////        pi.saveImage(new File(folder, title + "-IMAG."+format));
////        db.saveImage(new File(folder, title + "-DB."+format));
//    }
//
//    public void saveData(File file) throws IOException {
//        String extension=Files.getFileExtension(file);
//        if("xls".equals(extension)){
//           WritableWorkbook w=Workbook.createWorkbook(file);
//            WritableSheet[] sheets = new WritableSheet[]{
//                w.createSheet("z_complex",0),
//                w.createSheet("z_real",1),
//                w.createSheet("z_imag",2),
//                w.createSheet("z_module",3),
//                w.createSheet("z_module_db",4)
//            };
//
//            try {
//                for(int s=0;s<sheets.length;s++){
//                    for (int i = 0; i < x.length; i++) {
//                        double v = x[i];
//                        sheets[s].addCell(new jxl.write.Number(0,i+1,v));
//                    }
//                    if(y!=null){
//                        for (int i = 0; i < y.length; i++) {
//                            double v = y[i];
//                            sheets[s].addCell(new jxl.write.Number(1+i,0,v));
//                        }
//                    }
//
//                    Complex[][] matrix = null;
//                    if (z instanceof Complex[][]) {
//                        matrix = (Complex[][]) z;
//                        for (int i = 0; i < x.length; i++) {
//                            for (int j = 0; j < y.length; j++) {
//                                Complex c=matrix[i][j];
//                                WritableCell cs=null;
//                                switch(s){
//                                    case 0:{cs=c.getImag()==0? new jxl.write.Number(1+j,1+i,c.getReal()):(WritableCell) new jxl.write.Label(1+j,1+i,c.toString());break;}
//                                    case 1:{cs=new jxl.write.Number(1+j,1+i,c.getReal());break;}
//                                    case 2:{cs=new jxl.write.Number(1+j,1+i,c.getImag());break;}
//                                    case 3:{cs=new jxl.write.Number(1+j,1+i,c.abs());break;}
//                                    case 4:{cs=new jxl.write.Number(1+j,1+i,Math2.db(c.abs()));break;}
//                                }
//                                sheets[s].addCell(cs);
//                            }
//                        }
//                    } else if (z instanceof Complex[]) {
//                        matrix = new Complex[][]{(Complex[]) z};
//                        int j=0;
//                        for (int i = 0; i < x.length; i++) {
//                                Complex c=matrix[0][i];
//                                WritableCell cs=null;
//                                switch(s){
//                                    case 0:{cs=c.getImag()==0? new jxl.write.Number(1+j,1+i,c.getReal()):(WritableCell) new jxl.write.Label(1+j,1+i,c.toString());break;}
//                                    case 1:{cs=new jxl.write.Number(1+j,1+i,c.getReal());break;}
//                                    case 2:{cs=new jxl.write.Number(1+j,1+i,c.getImag());break;}
//                                    case 3:{cs=new jxl.write.Number(1+j,1+i,c.abs());break;}
//                                    case 4:{cs=new jxl.write.Number(1+j,1+i,Math2.db(c.abs()));break;}
//                                }
//                                sheets[s].addCell(cs);
//                        }
//                    }
//
//                }
//                w.write();
//                w.close();
//            } catch (WriteException e) {
//                e.printStackTrace();
//            }
//
//        }else{
//            String commentChar = "#";
//            String endLine = "";
//            // matlab file
//            if (Files.getFileExtension(file).equals("m")) {
//                commentChar = "%";
//                endLine = ";";
//            }
//            PrintStream printStream = null;
//            try {
//                printStream = new PrintStream(new FileOutputStream(file), true);
//                String lineSep = System.getProperty("line.separator");
//                printStream.println(commentChar + " Plot data");
//                printStream.println(commentChar + " Last modified " + Utils.UNIVERSAL_DATE_TIME_FORMAT.format(new Date()));
//                printStream.println(commentChar + " " + title);
//                printStream.println();
//                printStream.println();
//                printStream.println((x == null ? ("x=" + lineSep) : (new DMatrix(new double[][]{x}).toString(commentChar, "x"))) + endLine);
//                printStream.println();
//                if (y != null) {
//                    printStream.println((y == null ? ("y=" + lineSep) : (new DMatrix(new double[][]{y}).toString(commentChar, "y"))) + endLine);
//                }
//                printStream.println();
//                if (z != null) {
//                    Complex[][] matrix = null;
//                    if (z instanceof Complex[][]) {
//                        matrix = (Complex[][]) z;
//                    } else if (z instanceof Complex[]) {
//                        matrix = new Complex[][]{(Complex[]) z};
//                    }
//                    printStream.println((new Matrix(matrix).toString(commentChar, "z_complex")) + endLine);
//
//                    double[][] m_r = CArrays.getReal(matrix);
//                    printStream.println((new DMatrix(m_r).toString(commentChar, "z_real")) + endLine);
//                    m_r = null;
//
//                    double[][] m_i = CArrays.getImag(matrix);
//                    printStream.println((new DMatrix(m_i).toString(commentChar, "z_imag")) + endLine);
//                    m_i = null;
//
//                    double[][] m_mod = CArrays.abs(matrix);
//                    printStream.println((new DMatrix(m_mod).toString(commentChar, "z_module")) + endLine);
//                    m_mod = null;
//
//                    double[][] m_db = Math2.db(CArrays.abs(matrix));
//                    printStream.println((new DMatrix(m_db).toString(commentChar, "z_module_db")) + endLine);
//                    m_db = null;
//
//                }
//            } finally {
//                if (printStream != null) {
//                    try {
//                        printStream.close();
//                    } catch (Exception e1) {
//                        e1.printStackTrace();
//                    }
//                    printStream = null;
//                }
//            }
//        }
//    }
//
//    public void printAll() throws PrintException {
//        p.printImage();
//        pr.printImage();
//        pi.printImage();
//        db.printImage();
//    }
//
//    protected class SaveAllAction extends AbstractAction {
//        public SaveAllAction() {
//            super("SaveAllAction");
//            this.putValue(AbstractAction.NAME, "");
//            this.putValue(AbstractAction.SHORT_DESCRIPTION, "Enregistrer les images et les donn�es dans un r�pertoire");
//            this.putValue(AbstractAction.SMALL_ICON, Resources.loadImageIcon("/images/net/vpc/swing/Save.gif"));
//        }
//
//        public void actionPerformedImpl(ActionEvent event) {
//            if(true){
//                JFileChooser chooser= new JFileChooser();
//                chooser.setCurrentDirectory(folder);
//                chooser.setSelectedFile(new File(folder,title+".txt"));
//                int returnVal = chooser.showSaveDialog(ComplexPlot.this);
//                if (returnVal == JFileChooser.APPROVE_OPTION) {
//                    File file = chooser.getSelectedFile();
//                    if (file != null) {
//                        try {
//                            saveData(file);
//                        } catch (IOException e) {
//                            JOptionPane2.showErrorDialog(e);
//                        }
//                    }
//                }
//            }else{
//                JFolderChooser chooser = new JFolderChooser();
//                chooser.setCurrentDir(folder);
//                int returnVal = chooser.showChooser(ComplexPlot.this);
//                if (returnVal == JFileChooser.APPROVE_OPTION) {
//                    File file = chooser.getCurrentDir();
//                    if (file != null) {
//                        Object selectedValue=JOptionPane.showInputDialog(ComplexPlot.this,"Format","Save",JOptionPane.QUESTION_MESSAGE,null,new Object[]{
//                            "Texte",
//                            "Matlab",
//                            "Excel",
//                            "Image JPG"
//                        },"Texte");
//                        String extension="txt";
//                        if(selectedValue!=null){
//                            if("Texte".equals(selectedValue)){
//                                extension="txt";
//                            }else if("Matlab".equals(selectedValue)){
//                                extension="m";
//                            }else if("Excel".equals(selectedValue)){
//                                extension="xls";
//                            }else if("Image JPG".equals(selectedValue)){
//                                extension="jpg";
//                            }
//                        }
//                        try {
//                            saveAllData(file,extension);
//                        } catch (IOException e) {
//                            JOptionPane2.showErrorDialog(e);
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    protected class PrintAllDataAction extends AbstractAction {
//        public PrintAllDataAction() {
//            super("PrintAllDataAction");
//            this.putValue(AbstractAction.NAME, "");
//            this.putValue(AbstractAction.SHORT_DESCRIPTION, "Imprimer Tous...");
//            this.putValue(AbstractAction.SMALL_ICON, Resources.loadImageIcon("/images/net/vpc/swing/Print.gif"));
//        }
//
//        public void actionPerformedImpl(ActionEvent event) {
//            try {
//                printAll();
//            } catch (PrintException e) {
//                JOptionPane2.showErrorDialog(e);
//            }
//        }
//    }
//}
