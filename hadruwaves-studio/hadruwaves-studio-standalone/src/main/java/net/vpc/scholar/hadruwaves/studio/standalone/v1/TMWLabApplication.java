package net.vpc.scholar.hadruwaves.studio.standalone.v1;

import net.vpc.common.io.FileUtils;
import net.vpc.lib.pheromone.application.*;
import net.vpc.lib.pheromone.application.loaders.SplashScreenObserver;

import java.io.File;
import java.io.IOException;
import java.util.*;
import net.vpc.common.swings.RecentFilesMenu;

/**
 * Created by IntelliJ IDEA. User: taha Date: 7 juil. 2003 Time: 08:14:50 To
 * change this template use Options | File Templates.
 */
public class TMWLabApplication extends DefaultApplication {

    private RecentFilesMenu recentFilesMenu;

    public TMWLabApplication() {
        DefaultAppLicence lic = new DefaultAppLicence();
        lic.setProductId("tmwlab");
        lic.setEdition("free");
        lic.setLicenseId("tmwlabfree");
        //super("tmwlab", "TMWLab", "net/vpc/research", null, "0.8", "15",false, (AppLicense)null);
        setLicense(lic);
        setApplicationProperty("product-group", "net/vpc/research");
        setApplicationProperty("release-version", "0.8");
        setApplicationProperty("build-version", "15");
        setApplicationProperty("product-copyrights", "2012");
        setApplicationProperty("author-name", "Taha BEN SALAH");
        clearComponentsConfigurators();
        addComponentsConfigurator(new TMWLabApplicationConfigurator(this));
    }

    public static void main(String args[]) {
        ApplicationLoader.init(new SplashScreenObserver(7, "/net/vpc/scholar/hadruwaves/studio/standalone/v1/images/splashscreen.gif"));
        ApplicationLoader.start();
        TMWLabApplication tmwlab = new TMWLabApplication();
        DefaultApplicationRenderer r = new TMWLabApplicationRenderer(tmwlab);
        ApplicationLoader.moduleLoaded("Application loaded Successfully.");
        r.createFrame().setVisible(true);
        ApplicationLoader.end();
    }

    public static String openInShell(File file, String cmd) throws IOException {
        if (cmd == null || cmd.length() == 0) {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("windows")) {
                String wcmd = null;
                if (os.contains("95") || os.contains("98")) {
                    wcmd = "command.com";
                } else {
                    wcmd = "cmd.exe";
                }
                if (file.isDirectory()) {
                    cmd = wcmd + " /C explorer %f";
                } else {
                    cmd = wcmd + " /C %f";
                }
            } else {
                cmd = "kfmclient exec %f";
            }
        }
        StringTokenizer st = new StringTokenizer(cmd);
        String[] cmdarray = new String[st.countTokens()];
        for (int i = 0; st.hasMoreTokens(); i++) {
            cmdarray[i] = st.nextToken();
            if (cmdarray[i].equals("%f")) {
                cmdarray[i] = file.getAbsolutePath();
            }
        }
        Runtime.getRuntime().exec(cmdarray);
        return cmd;
    }

    @Override
    protected void setupComponents() {
        super.setupComponents();
    }

    public RecentFilesMenu getRecentFilesMenu() {
        return recentFilesMenu;
    }

    public void setRecentFilesMenu(RecentFilesMenu recentFilesMenu) {
        this.recentFilesMenu = recentFilesMenu;
    }

    @Override
    protected void setupStartup() {
        super.setupStartup();
        getResources().loadBundle("net.vpc.scholar.hadruwaves.studio.standalone.v1.messages.TMWLabApplication");
    }



    @Override
    public void saveSharedConfig() {
        super.saveSharedConfig();
        if (getRecentFilesMenu() != null) {
            File[] files = getRecentFilesMenu().getRecentFilesModel().getFiles();
            String[] filePaths = new String[files.length];
            for (int i = 0; i < filePaths.length; i++) {
                filePaths[i] = FileUtils.getFilePath(files[i]);
            }
            getConfigurationManager().getSharedConfiguration().setStringArray("RecentFiles", filePaths, '\n');
        }
    }

    @Override
    public void loadSharedConfig() {
        super.loadSharedConfig();
        if (getRecentFilesMenu().getRecentFilesModel() != null) {
            String[] filePaths = getConfigurationManager().getSharedConfiguration().getStringArray("RecentFiles", new String[0]);
            File[] files = new File[filePaths.length];
            for (int i = 0; i < filePaths.length; i++) {
                files[i] = new File(filePaths[i]);
            }
            getRecentFilesMenu().getRecentFilesModel().setFiles(files);
        }
    }



    //    public static TMWLabApplication getApplication() {
//        return (TMWLabApplication) Application.getApplication();
//    }
    @Override
    protected void installApplicationResources(AppInstallInfo install) throws InstallationException {
        super.installApplicationResources(install);
        File dir = install.getInstallDir();
        (new File(dir, "config")).mkdir();
        (new File(dir, "log")).mkdir();
    }
}
