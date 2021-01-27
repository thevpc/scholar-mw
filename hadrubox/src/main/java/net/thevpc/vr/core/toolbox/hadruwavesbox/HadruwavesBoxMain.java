package net.thevpc.vr.core.toolbox.hadruwavesbox;

import java.io.File;
import java.io.PrintStream;
import java.io.UncheckedIOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
import net.thevpc.nuts.NutsApplication;
import net.thevpc.nuts.NutsApplicationContext;
import net.thevpc.nuts.NutsCommandLine;
import net.thevpc.nuts.NutsArgument;

public class HadruwavesBoxMain extends NutsApplication{

    private static final Logger LOG = Logger.getLogger(HadruwavesBoxMain.class.getName());
    public HBoxProject project;

    public static void main(String[] args) {
        new HadruwavesBoxMain().runAndExit(args);
    }

    @Override
    public void run(NutsApplicationContext appContext) {
        this.project = new HBoxProject(appContext);
        NutsCommandLine cmd = appContext.getCommandLine().setCommandName("hadruwaves-box");
        PrintStream out = appContext.getSession().out();

        if (cmd.isExecMode()) {
            out.printf("==Hadruwaves Toolbox== v##%s##\n", appContext.getAppId().getVersion());
            out.printf("(c) Taha Ben Salah (==%s==) 2018-2019 - ==%s==\n", "@vpc", "http://github.com/thevpc");
        }

        NutsArgument a;
        while (cmd.hasNext()) {
            if (appContext.configureFirst(cmd)) {

            } else if ((a = cmd.next("new", "n")) != null) {
                newProject(cmd);
                return;
            } else if ((a = cmd.next(
                    "generate scala object", 
                    "generate object", 
                    "g s o",
                    "g o"
            )) != null) {
                generateScalaObject(cmd);
                return;
            } else {
                cmd.unexpectedArgument();
            }
        }
        cmd.unexpectedArgument();
    }

    public HadruwavesBoxMain() {
    }

    public void generateScalaObject(NutsCommandLine commandLine) throws UncheckedIOException {
        File loadPropertiesFrom = null;
        NutsArgument a;
        String name = null;
        while (commandLine.hasNext()) {
            if (project.getApplicationContext().configureFirst(commandLine)) {
                //
            }else if ((a = commandLine.nextString("--name")) != null) {
                name = a.getStringValue();
            } else if ((a = commandLine.nextBoolean("--load")) != null) {
                loadPropertiesFrom = new File(a.getStringValue());
            } else if (commandLine.peek().isNonOption() && name == null) {
                name = commandLine.next().getString();
            } else {
                commandLine.unexpectedArgument();
            }
        }
        if (!commandLine.isExecMode()) {
            return;
        }
        if (loadPropertiesFrom != null) {
            project.loadConfigProperties(loadPropertiesFrom);
        }
        project.createScalaObject(name);
    }

    public void newProject(NutsCommandLine commandLine) {
        File storePropertiesTo = null;
        File loadPropertiesFrom = null;
        NutsArgument a;
        String projectName = null;
        Set<String> archetypes = new HashSet<String>();
        while (commandLine.hasNext()) {
            if (project.getApplicationContext().configureFirst(commandLine)) {
                //
            } else if ((a = commandLine.nextString("--name")) != null) {
                projectName = a.getStringValue();
            } else if ((a = commandLine.nextString("--save")) != null) {
                storePropertiesTo = new File(a.getStringValue());
            } else if ((a = commandLine.nextString("--load")) != null) {
                loadPropertiesFrom = new File(a.getStringValue());
            } else if ((a = commandLine.nextBoolean("--edu", "--equip", "--exp", "--all")) != null) {
                if (a.getBooleanValue()) {
                    archetypes.remove("all");
                    archetypes.remove("none");
                    archetypes.add("basic");
                    archetypes.add(a.getStringKey().substring(2));
                }
            } else if ((a = commandLine.nextBoolean("--all")) != null) {
                if (a.getBooleanValue()) {
                    archetypes.clear();
                    archetypes.add("all");
                }
            } else if ((a = commandLine.nextBoolean("--none")) != null) {
                if (a.getBooleanValue()) {
                    archetypes.clear();
                    archetypes.add("none");
                }
            } else if (commandLine.peek().isNonOption() && projectName == null) {
                projectName = commandLine.next().getString();
            } else {
                commandLine.unexpectedArgument();
            }
        }
        if (!commandLine.isExecMode()) {
            return;
        }
        project.createProject(projectName, storePropertiesTo, loadPropertiesFrom, archetypes.toArray(new String[0]));
    }

}
