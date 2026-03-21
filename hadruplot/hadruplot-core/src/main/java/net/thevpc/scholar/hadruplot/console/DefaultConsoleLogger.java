//package net.thevpc.scholar.hadruplot.console;
//
//
//import net.thevpc.nuts.text.NMsg;
//
//import javax.swing.*;
//import java.awt.*;
//import java.io.ByteArrayOutputStream;
//import java.io.PrintStream;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.logging.Level;
//
///**
// * @author Taha BEN SALAH (taha.bensalah@gmail.com)
// * @creationtime 6 janv. 2007 12:22:39
// */
//class DefaultConsoleLogger implements ConsoleLogger {
//    private boolean enableDialog = true;
//    private PlotConsole plotConsole;
//    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//    public DefaultConsoleLogger(PlotConsole plotConsole) {
//        this.plotConsole = plotConsole;
//    }
//
//    @Override
//    public void log(NMsg msg) {
//        Level level = msg.getLevel();
//        if(level == null) {
//            level = Level.INFO;
//        }
//        logln(level.getName(),msg);
//    }
//
//    private void logln(String type, Object msg) {
//        if (plotConsole.isDisposing()) {
//            return;
//        }
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        PrintStream ps = new PrintStream(baos);
//        ps.print(sdf.format(new Date()));
//        ps.print(" [");
//        ps.print(type);
//        ps.print("] ");
//        if (msg == null) {
//
//        } else if (msg instanceof Throwable) {
//            ((Throwable) msg).printStackTrace(ps);
//        } else {
//            ps.print(String.valueOf(msg));
//        }
//        ps.flush();
//        plotConsole.run(new LogConsoleAction(baos.toString()));
//        if (enableDialog) {
//            if ("error".equals(type)) {
//                JPanel p = new JPanel(new BorderLayout());
//                p.add(new JLabel(baos.toString()), BorderLayout.CENTER);
//                JCheckBox noMoreDialog = new JCheckBox("Do not show this Dialog next time");
//                p.add(noMoreDialog, BorderLayout.CENTER);
//                JOptionPane.showMessageDialog(null, p, "Error", JOptionPane.ERROR_MESSAGE);
//                enableDialog = !noMoreDialog.isSelected();
//            }
//        }
//    }
//
//    @Override
//    public String toString() {
//        return "PlotConsoleLogger";
//    }
//}
