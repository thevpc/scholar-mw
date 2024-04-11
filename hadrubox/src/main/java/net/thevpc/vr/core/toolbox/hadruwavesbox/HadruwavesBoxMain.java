package net.thevpc.vr.core.toolbox.hadruwavesbox;

import java.io.File;
import java.io.UncheckedIOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
import net.thevpc.nuts.NApplication;
import net.thevpc.nuts.NSession;
import net.thevpc.nuts.cmdline.NArg;
import net.thevpc.nuts.cmdline.NCmdLine;
import net.thevpc.nuts.io.NPrintStream;
import net.thevpc.nuts.util.NMsg;

public class HadruwavesBoxMain implements NApplication{

    private static final Logger LOG = Logger.getLogger(HadruwavesBoxMain.class.getName());
    public HBoxProject project;

    public static void main(String[] args) {
        new HadruwavesBoxMain().runAndExit(args);
    }

    @Override
    public void run(NSession session) {
        this.project = new HBoxProject(session);
        NCmdLine cmd = session.getAppCmdLine().setCommandName("hadruwaves-box");
        NPrintStream out = session.out();

        if (cmd.isExecMode()) {
            out.println(NMsg.ofC("==Hadruwaves Toolbox== v##%s##", session.getAppId().getVersion()));
            out.println(NMsg.ofC("(c) Taha Ben Salah (==%s==) 2018-2019 - ==%s==\n", "@vpc", "http://github.com/thevpc"));
        }

        NArg a;
        while (cmd.hasNext()) {
            if (session.configureFirst(cmd)) {

            } else if ((a = cmd.next("new", "n").orNull()) != null) {
                newProject(cmd);
                return;
            } else if ((a = cmd.next(
                    "generate scala object", 
                    "generate object", 
                    "g s o",
                    "g o"
            ).orNull()) != null) {
                generateScalaObject(cmd);
                return;
            } else {
                cmd.throwUnexpectedArgument();
            }
        }
        cmd.throwUnexpectedArgument();
    }

    public HadruwavesBoxMain() {
    }

    public void generateScalaObject(NCmdLine commandLine) throws UncheckedIOException {
        File loadPropertiesFrom = null;
        NArg a;
        String name = null;
        while (commandLine.hasNext()) {
            if (project.getSession().configureFirst(commandLine)) {
                //
            }else if ((a = commandLine.nextEntry("--name").orNull()) != null) {
                name = a.getStringValue().get();
            } else if ((a = commandLine.nextFlag("--load").orNull()) != null) {
                loadPropertiesFrom = new File(a.getStringValue().get());
            } else if (commandLine.peek().get().isNonOption() && name == null) {
                name = commandLine.next().get().getImage();
            } else {
                commandLine.throwUnexpectedArgument();
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

    public void newProject(NCmdLine commandLine) {
        File storePropertiesTo = null;
        File loadPropertiesFrom = null;
        NArg a;
        String projectName = null;
        Set<String> archetypes = new HashSet<String>();
        while (commandLine.hasNext()) {
            if (project.getSession().configureFirst(commandLine)) {
                //
            } else if ((a = commandLine.nextEntry("--name").orNull()) != null) {
                projectName = a.getStringValue().get();
            } else if ((a = commandLine.nextEntry("--save").orNull()) != null) {
                storePropertiesTo = new File(a.getStringValue().get());
            } else if ((a = commandLine.nextEntry("--load").orNull()) != null) {
                loadPropertiesFrom = new File(a.getStringValue().get());
            } else if ((a = commandLine.nextFlag("--edu", "--equip", "--exp", "--all").orNull()) != null) {
                if (a.getBooleanValue().get()) {
                    archetypes.remove("all");
                    archetypes.remove("none");
                    archetypes.add("basic");
                    archetypes.add(a.getStringKey().get().substring(2));
                }
            } else if ((a = commandLine.nextFlag("--all").orNull()) != null) {
                if (a.getBooleanValue().get()) {
                    archetypes.clear();
                    archetypes.add("all");
                }
            } else if ((a = commandLine.nextFlag("--none").orNull()) != null) {
                if (a.getBooleanValue().get()) {
                    archetypes.clear();
                    archetypes.add("none");
                }
            } else if (commandLine.peek().get().isNonOption() && projectName == null) {
                projectName = commandLine.next().get().getImage();
            } else {
                commandLine.throwUnexpectedArgument();
            }
        }
        if (!commandLine.isExecMode()) {
            return;
        }
        project.createProject(projectName, storePropertiesTo, loadPropertiesFrom, archetypes.toArray(new String[0]));
    }

}
