/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwaves.project.templates;

import net.vpc.scholar.hadrumaths.CharactersTable;
import net.vpc.scholar.hadrumaths.units.FrequencyUnit;
import net.vpc.scholar.hadrumaths.units.LengthUnit;
import net.vpc.scholar.hadrumaths.units.UnitType;
import net.vpc.scholar.hadruwaves.Boundary;
import net.vpc.scholar.hadruwaves.mom.solver.HWSolverTemplateMoM;
import net.vpc.scholar.hadruwaves.mom.solver.test.MomSolverTestTemplateSeq;
import net.vpc.scholar.hadruwaves.mom.solver.test.ParamSeqTemplate;
import net.vpc.scholar.hadruwaves.project.DefaultHWProject;
import net.vpc.scholar.hadruwaves.project.DefaultHWcene;
import net.vpc.scholar.hadruwaves.project.HWProject;
import net.vpc.scholar.hadruwaves.project.scene.HWMaterialTemplate;
import net.vpc.scholar.hadruwaves.project.scene.HWPlanarPort;
import net.vpc.scholar.hadruwaves.project.scene.HWProjectBrick;
import net.vpc.scholar.hadruwaves.project.scene.HWProjectPolygon;
import net.vpc.scholar.hadruwaves.project.scene.HWProjectScene;
import net.vpc.scholar.hadruwaves.project.scene.SceneHelper;

/**
 *
 * @author vpc
 */
public class MicrostripProjectTemplate {

    public HWProject createProject() {
        HWProject p = new DefaultHWProject();
        load(p);
        return p;
    }

    public void load(HWProject p) {
        p.name().set("Miscrostrip Antenna");

        p.parameters().addAt("/Frequency", "fr", "Frequency", UnitType.Frequency, FrequencyUnit.GHz, "3", false);
        p.parameters().addAt("/Frequency", "\u03BB", "Target Wavelength", UnitType.Length, null, "C/fr", false);
        p.parameters().addAt("/Substrate", "\u03B5_r", "Substrate Relative Permettivity", UnitType.Double, null, "2.2", false);
        p.parameters().addAt("/Substrate", "h", "Substrate Thikness", UnitType.Length, LengthUnit.mm, "0.1", false);
        p.parameters().addAt("/Substrate", "\u03B5_ef", "Substrate Effective Relative Permettivity", UnitType.Double, null, "(\u03B5_r + 1) / 2 + (\u03B5_r - 1) / 2 * (1 / sqrt(1 + 2 * h / a_w))", false);

        p.parameters().addAt("/Antenna", "a_l", "Patch Antenna length (along x)", UnitType.Length, null, "a_leff - 2 * "+"a_"+CharactersTable.DELTA+"l", false);
        p.parameters().addAt("/Antenna", "a_w", "Patch Antenna width (along y)", UnitType.Length, null, "C / (2 * fr) * sqrt(2 / (\u03B5_r + 1))", false);
        p.parameters().addAt("/Antenna", "a_"+CharactersTable.DELTA+"l", "Patch Antenna delta length", UnitType.Length, null, "h * 0.412 * (\u03B5_r + 0.3) * (a_w / h + 0.265) / ((\u03B5_r - 0.258) * (a_w / h + 0.8))", false);
        p.parameters().addAt("/Antenna", "a_leff", "Patch Antenna Effective length", UnitType.Length, null, "C / (2 * fr * sqrt(\u03B5_ef))", false);



        p.parameters().addAt("/FeedLine", "f_l", "FeedLine length (along x)", UnitType.Length, null, "\u03BB / 10", false);
        p.parameters().addAt("/FeedLine", "f_w", "FeedLine width (along y)", UnitType.Length, null, "\u03BB / 40", false);

        p.parameters().addAt("/PlanarSource", "s_l", "Source length (along x)", UnitType.Length, null, "\u03BB / 40", false);
        p.parameters().addAt("/PlanarSource", "s_w", "FeedLine length (along y)", UnitType.Length, null, "\u03BB / 40", false);

        p.parameters().addAt("/Box", "d_l", "Box length (along y)", UnitType.Length, null, "f_l + a_l + \u03BB", false);
        p.parameters().addAt("/Box", "d_w", "Box width (along x)", UnitType.Length, null, "f_w + a_w + \u03BB", false);
        p.parameters().addAt("/MoM/ModeFunctions", "M", "Modes Count", UnitType.Integer, null, "5000", true);
        p.parameters().addAt("/MoM/TestFunctions", "f_m", "Feed Max m", UnitType.Integer, null, "2", false);
        p.parameters().addAt("/MoM/TestFunctions", "f_n", "Feed Max n", UnitType.Integer, null, "1", false);
        p.parameters().addAt("/MoM/TestFunctions", "a_m", "Antenna Max m", UnitType.Integer, null, "4", false);
        p.parameters().addAt("/MoM/TestFunctions", "a_n", "Antenna Max n", UnitType.Integer, null, "4", false);
        HWMaterialTemplate substrate = new HWMaterialTemplate(p);
        substrate.name().set("substrate");
        substrate.permettivity().set("\u03B5_r");
        p.materials().add(substrate);
        HWProjectScene scene = new DefaultHWcene(p);

        HWProjectBrick mb = new HWProjectBrick("Bottom Space", substrate, SceneHelper.createBrickTemplate(
                "0", "d_l", "-d_w / 2", "d_w / 2", "-h", "0"
        ));
        mb.face(HWProjectBrick.Face.NORTH).boundary().set(Boundary.ELECTRIC.name());
        mb.face(HWProjectBrick.Face.EAST).boundary().set(Boundary.ELECTRIC.name());
        mb.face(HWProjectBrick.Face.SOUTH).boundary().set(Boundary.ELECTRIC.name());
        mb.face(HWProjectBrick.Face.WEST).boundary().set(Boundary.ELECTRIC.name());
        mb.face(HWProjectBrick.Face.BOTTOM).boundary().set(Boundary.ELECTRIC.name());
        mb.face(HWProjectBrick.Face.TOP).boundary().set(Boundary.NOTHING.name());
        scene.components().add(mb);

        HWProjectBrick mt = new HWProjectBrick("Top Space", substrate, SceneHelper.createBrickTemplate(
                "0", "d_l", "-d_w / 2", "d_w / 2", "0", "\u221E"
        ));
        mt.face(HWProjectBrick.Face.NORTH).boundary().set(Boundary.ELECTRIC.name());
        mt.face(HWProjectBrick.Face.EAST).boundary().set(Boundary.ELECTRIC.name());
        mt.face(HWProjectBrick.Face.SOUTH).boundary().set(Boundary.ELECTRIC.name());
        mt.face(HWProjectBrick.Face.WEST).boundary().set(Boundary.ELECTRIC.name());
        mt.face(HWProjectBrick.Face.BOTTOM).boundary().set(Boundary.NOTHING.name());
        mt.face(HWProjectBrick.Face.TOP).boundary().set(Boundary.INFINITE.name());
        scene.components().add(mt);

        HWPlanarPort port = new HWPlanarPort("Source",
                SceneHelper.createRectangleXY("0", "s_l", "-s_w / 2", "s_w / 2", "0")
        );
        port.expr().set("1");
        port.impedance().set("50");
        scene.components().add(port);

        scene.components().add(new HWProjectPolygon(
                "FeedLine", p.materials().get("PEC"),
                SceneHelper.createRectangleXY("0", "f_l", "-f_w / 2", "f_w / 2", "0"))
        );

        scene.components().add(new HWProjectPolygon(
                "Patch", p.materials().get("PEC"),
                SceneHelper.createRectangleXY("f_l", "f_l + a_l", "-a_w / 2", "a_w / 2", "0"))
        );

        p.scene().set(scene);
        HWSolverTemplateMoM solver = new HWSolverTemplateMoM();
        p.configurations().activeConfiguration().get()
                .solver().set(solver);
        solver.frequency().set("fr");
        solver.modesCount().set("M");
        
        MomSolverTestTemplateSeq feedTestFunctions = new MomSolverTestTemplateSeq(
                "FeedLine",
                "cos((2 * m + 1) * \u03C0 / 2 * X / f_l) * cos(n * \u03C0 / f_w * (Y + f_w / 2)) * domain(0->f_l, (-f_w / 2)->(f_w / 2))"
        );
        feedTestFunctions.params().add(new ParamSeqTemplate("m","0","f_m"));
        feedTestFunctions.params().add(new ParamSeqTemplate("n","0","f_n"));
        solver.testFunctions().add(feedTestFunctions);
        
        
        MomSolverTestTemplateSeq patchTestFunctions = new MomSolverTestTemplateSeq(
                "PatchAntenna",
                "sin((m * \u03C0) * (X - f_l) / a_l) * cos((n * \u03C0 / a_w) * (Y + a_w / 2)) * domain(f_l->(f_l + a_l), (-a_w / 2)->(a_w / 2))"
        );
        patchTestFunctions.params().add(new ParamSeqTemplate("m","1","a_m"));
        patchTestFunctions.params().add(new ParamSeqTemplate("n","0","a_n"));
        solver.testFunctions().add(patchTestFunctions);
    }
}
