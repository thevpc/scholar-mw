/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package net.thevpc.vr.core.toolbox.hadruwavesbox;

import net.thevpc.nuts.lib.template.DefaultProjectTemplate;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import net.thevpc.nuts.NutsApplicationContext;
import net.thevpc.nuts.NutsId;
import net.thevpc.nuts.lib.template.IOUtils;
import net.thevpc.nuts.lib.template.JavaUtils;
import net.thevpc.nuts.lib.template._StringUtils;
import net.thevpc.common.strings.MessageNameFormat;
import net.thevpc.common.strings.StringToObject;
import net.thevpc.common.strings.StringUtils;
import net.thevpc.common.strings.format.AbstractFunction;
import net.thevpc.nuts.lib.template.ProjectTemplate;
import net.thevpc.nuts.lib.template.ProjectTemplateListener;

/**
 *
 * @author vpc
 */
public class HBoxProject extends DefaultProjectTemplate {

    private HValidatorFactory vf;

    public HBoxProject(NutsApplicationContext appContext) {
        super(appContext);
        this.vf = new HValidatorFactory(appContext.getWorkspace());
        getConfigListeners().add(new ProjectTemplateListener() {
            @Override
            public void onSetProperty(String propertyName, String value, ProjectTemplate project) {
                switch (propertyName) {
                    case "ProjectName": {
                        project.getConfigProperty("ProjectShortTitle").setDefaultValue(StringUtils.isBlank(value) ? "my-project" : JavaUtils.toIdFormat(value));
                        project.getConfigProperty("ProjectLongTitle").setDefaultValue(StringUtils.isBlank(value) ? "My Project" : JavaUtils.toNameFormat(value));
                        break;
                    }
                }
            }
        });
        registerDefaultsFunctions();
        registerFunction("vrMavenModelDependency", new AbstractFunction() {
            @Override
            public Object evalArgs(Object[] args, MessageNameFormat format, StringToObject provider) {
                String module = String.valueOf(args[0]);
                return vrMavenModelDependency(module, HBoxProject.this);
            }
        });
        registerFunction("vrMavenServiceDependency", new AbstractFunction() {
            @Override
            public Object evalArgs(Object[] args, MessageNameFormat format, StringToObject provider) {
                String module = String.valueOf(args[0]);
                return vrMavenServiceDependency(module, HBoxProject.this);
            }
        });
        registerFunction("vrMavenWebDependency", new AbstractFunction() {
            @Override
            public Object evalArgs(Object[] args, MessageNameFormat format, StringToObject provider) {
                String module = String.valueOf(args[0]);
                return vrMavenWebDependency(module, HBoxProject.this);
            }
        });

        setConfigProperty("ObjectName", "MyObject", vf.JAVA_NAME, "Scala Object Name", true);
        setConfigProperty("ModuleName", "my-module", vf.DASHED_NAME, "Module Artifact Id", true);
        setConfigProperty("ModuleVersion", "1.0.0", vf.VERSION, "Module Version", false);
        setConfigProperty("ProjectName", "my-project", vf.DASHED_NAME, "Project Artifact Id", true);
        setConfigProperty("ProjectShortTitle", "my-project", vf.NAME, "Project Short Title", false);
        setConfigProperty("ProjectLongTitle", "my project name", vf.LABEL, "Project Title", false);
        setConfigProperty("ProjectVersion", "1.0.0", vf.VERSION, "Project Version", false);
        setConfigProperty("ProjectGroup", "com.mycompany", vf.PACKAGE, "Project Maven Group Id", true);
        setConfigProperty("ProjectRootFolder", System.getProperty("user.dir"), vf.FOLDER, "Project Root Folder", true);
        setConfigProperty("ConfigCompany", "My Company", vf.LABEL, "Project Default Company", false);
        setConfigProperty("ConfigCountry", "Tunisia", vf.LABEL, "Project Default Country", false);
        setConfigProperty("ConfigIndustry", "Services", vf.LABEL, "Project Default Industry", false);
        setConfigProperty("ConfigAuthor", System.getProperty("user.name"), vf.STRING, "Author (you)", false);
        setConfigProperty("ConfigAuthorUrl", "http://" + System.getProperty("user.name") + "-company.com", vf.URL, "Author Url (your website)", false);
        setConfigProperty("ConfigAuthorCompany", System.getProperty("user.name") + " company", vf.LABEL, "Author Company (your company)", false);
        setConfigProperty("FwkCoreVersion", "1.13.15", vf.VERSION, "VR Framework Version", false);
        getConfigProperty("ModuleName").setValue("main");
        //"vrEnableModule_"
        for (VrModule vrModule : VrModules.getAll()) {
            if (isAskAll() || vrModule.isProminent()) {
                setConfigProperty("EnableModule_" + vrModule.getBaseArtifactId(), "true", vf.BOOLEAN,
                        "Activate " + vrModule.getBaseArtifactId() + " Module (" + vrModule.getTitle() + ")", true
                );
            }

        }
    }

    public void createProject(String projectName, File storePropertiesTo, File loadPropertiesFrom, String[] archetypes) {
        Set<String> archetypesSet = archetypes == null ? new HashSet<>() : new HashSet<>(Arrays.asList(archetypes));
        if (loadPropertiesFrom != null) {
            loadConfigProperties(loadPropertiesFrom);
        }
        if (projectName != null) {
            final NutsId pn = getWorkspace().id().parseRequired(projectName);
            if (pn.getGroup() != null) {
                setConfigValue("ProjectGroup", pn.getGroup());
            }
            if (pn.getName() != null) {
                setConfigValue("ProjectName", pn.getName());
            }
            if (pn.getVersion().getValue() != null) {
                setConfigValue("ProjectVersion", pn.getVersion().getValue());
            }
        }
        if (!archetypesSet.isEmpty()) {
            for (VrModule vrModule : VrModules.getAll()) {
                setConfigValue("vrEnableModule_" + vrModule.getBaseArtifactId(), "false");
                if (!archetypesSet.contains("none")) {
                    for (String archetype : archetypes) {
                        if (vrModule.acceptArchetype(archetype)) {
                            setConfigValue("vrEnableModule_" + vrModule.getBaseArtifactId(), "true");
                        }
                    }
                }
            }
        }
        println("Looking for latest Hadruwaves version...");
        boolean newVersion = false;
        try {
            NutsId v = getWorkspace().search().id("net.thevpc.scholar:hadruwaves-scala")
                    .setLatest(true).getResultIds().required();
            if (v != null) {
                println("Detected hadruwaves-scala version ==%s==", v.getVersion());
                getConfigProperty("HadruwavesVersion").setDefaultValue(v.getVersion().getValue());
//                getConfigProperty("ScholarVersion").setDefaultValue(v.getVersion().getValue());
                newVersion = true;
            }
        } catch (Exception ex) {
            //ignore
        }
        if (!newVersion) {
            println("Using base VR version " + getConfigProperty("vrFwkCoreVersion").getValue());
        }
        println("Generating ==%s==...", "new hadruwaves-scala project");
        setTargetRoot("/");
        copyXml("pom.xml", "/");
        createScalaObject(null);
        println("");
        println("**--------------------------------**");
        println("**PROJECT PROPERTIES**");
        println("**--------------------------------**");
        String sortLines = _StringUtils.sortLines(
                IOUtils.toString(getConfigProperties(), null)
        );
        println(sortLines);
        if (storePropertiesTo != null) {
            try {
                IOUtils.writeString(sortLines, storePropertiesTo, this);
            } catch (IOException ex) {
                throw new UncheckedIOException(ex);
            }
        }
    }

    public void createScalaObject(String name) {
        askForPomAndUpdateConfig();
        getConfigProperty("ObjectName").setValue(name);
        copyScala("src/main/java/Example.scala",
                "src/main/java"
        );
    }


    public static String vrMavenModelDependency(String module, ProjectTemplate config) {
        boolean ok = config.getConfigProperty("vrEnableModule_" + module).getBoolean(true);
        if (ok) {
            VrModule mm = VrModules.get(module);
            if (mm != null && mm.isModel()) {
                return "<dependency>\n"
                        + "            <groupId>" + mm.getGroupId() + "</groupId>\n"
                        + "            <artifactId>" + mm.getBaseArtifactId() + "-model</artifactId>\n"
                        + "            <version>${version.vr}</version>\n"
                        + "        </dependency>";
            }
        }
        return "";
    }

    public static String vrMavenServiceDependency(String module, ProjectTemplate config) {
        boolean ok = config.getConfigProperty("vrEnableModule_" + module).getBoolean(true);
        if (ok) {
            VrModule mm = VrModules.get(module);
            if (mm != null && mm.isService()) {
                return "<dependency>\n"
                        + "            <groupId>" + mm.getGroupId() + "</groupId>\n"
                        + "            <artifactId>" + mm.getBaseArtifactId() + "-service</artifactId>\n"
                        + "            <version>${version.vr}</version>\n"
                        + "        </dependency>";
            }
        }
        return "";
    }

    public static String vrMavenWebDependency(String module, ProjectTemplate config) {
        boolean ok = config.getConfigProperty("vrEnableModule_" + module).getBoolean(true);
        if (ok) {
            VrModule mm = VrModules.get(module);
            if (mm != null && mm.isWeb()) {
                return "<dependency>\n"
                        + "            <groupId>" + mm.getGroupId() + "</groupId>\n"
                        + "            <artifactId>" + mm.getBaseArtifactId() + (mm.isTheme() ? "" : "-web") + "</artifactId>\n"
                        + "            <version>${version.vr}</version>\n"
                        + "        </dependency>";
            }
        }
        return "";
    }

}
