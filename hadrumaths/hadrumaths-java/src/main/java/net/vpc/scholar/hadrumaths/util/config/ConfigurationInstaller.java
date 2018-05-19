package net.vpc.scholar.hadrumaths.util.config;

public class ConfigurationInstaller {
    private static boolean installed;

    public static void install() {
        if (!installed) {
            installed = true;
        }
    }
}
