package net.vpc.scholar.hadruplot.console;

import net.vpc.common.swings.GridBagLayout2;

import javax.swing.*;
import java.io.IOException;
import java.util.Date;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime 9 janv. 2007 01:39:18
 */
public class PlotConsoleProperties extends JPanel {
    PlotConsole plotConsole;

    public PlotConsoleProperties(PlotConsole plotConsole) {
        super(new GridBagLayout2()
                .addLine("[<filePL       ][<=fileP     ]")
                .addLine("[<fileL        ][<=file      ]")
                .addLine("[<startTimeL   ][<=startTime ]")
        );
        this.plotConsole = plotConsole;
        add(new JLabel("File Pattern"), "filePL");
        add(new JLabel("File"), "fileL");
        add(new JLabel("Start Time"), "startTimeL");
        JTextField file;
        try {
            file = new JTextField(String.valueOf(plotConsole.getCurrentAutoSavingFile().getCanonicalPath()), 50);
        } catch (IOException e) {
            file = new JTextField(String.valueOf(plotConsole.getCurrentAutoSavingFile().getPath()), 50);
        }
        file.setEditable(false);
        add(file, "file");
        JTextField fileP = new JTextField(plotConsole.getAutoSavingFilePattern(), 50);
        fileP.setEditable(false);
        add(fileP, "fileP");
        JTextField startTime = new JTextField(String.valueOf(new Date(plotConsole.getStartTime())));
        startTime.setEditable(false);
        add(startTime, "startTime");
    }

}
