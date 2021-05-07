/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwavesstudio.standalone.v2.components;

import net.thevpc.echo.Application;
import net.thevpc.common.swing.layout.GBC;
import net.thevpc.common.swing.layout.GridBagLayout2;
import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadrumaths.units.ParamUnit;
import net.thevpc.scholar.hadrumaths.units.UnitType;
import net.thevpc.scholar.hadruwaves.project.EvalExprResult;
import net.thevpc.scholar.hadruwaves.project.configuration.HWConfigurationRun;
import net.thevpc.scholar.hadruwaves.project.parameter.HWParameterValue;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.HadruwavesStudio;
import org.fife.ui.autocomplete.AutoCompletion;
import org.fife.ui.autocomplete.BasicCompletion;
import org.fife.ui.autocomplete.CompletionProvider;
import org.fife.ui.autocomplete.DefaultCompletionProvider;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.jdesktop.swingx.JXDialog;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;

import static org.jdesktop.swingx.JXDialog.EXECUTE_ACTION_COMMAND;

/**
 * @author vpc
 */
public class NumberExpressionDialog {

    AreaCharTableListener areaCharTableListener = new AreaCharTableListener();
    boolean _renderLatex = false;
    boolean _renderLatexAgain = false;
    JLabel latexLabel = new JLabel();
    JTextField valueLabel = new JTextField();
    private JXDialog dialog;
    private HWTextEditor messagesArea;
    private String returnActionCommand;
    private HadruwavesStudio studio;
    private HWConfigurationRun configuration;
    private UnitType unitType;
    private ParamUnit unit;

    public NumberExpressionDialog(HadruwavesStudio studio, UnitType unitType, ParamUnit unit, HWConfigurationRun configuration) {
        this.studio = studio;
        this.configuration = configuration;
        this.unitType = unitType;
        this.unit = unit;
        messagesArea = new HWTextEditor(studio,20, 80);
        messagesArea.setEditable(true);

//        // A CompletionProvider is what knows of all possible completions, and
//        // analyzes the contents of the text area at the caret position to
//        // determine what completion choices should be presented. Most instances
//        // of CompletionProvider (such as DefaultCompletionProvider) are designed
//        // so that they can be shared among multiple text components.
//        CompletionProvider provider = createCompletionProvider(unitType, configuration);
//
//        // An AutoCompletion acts as a "middle-man" between a text component
//        // and a CompletionProvider. It manages any options associated with
//        // the auto-completion (the popup trigger key, whether to display a
//        // documentation window along with completion choices, etc.). Unlike
//        // CompletionProviders, instances of AutoCompletion cannot be shared
//        // among multiple text components.
//        AutoCompletion ac = new AutoCompletion(provider);
//        ac.install(messagesArea);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Code", messagesArea);


        JPanel panel = new JPanel(new BorderLayout());
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        for (CharactersTableComponent.Family fam : CharactersTableComponent.Family.values()) {
            CharactersTableComponent.addSupport(toolbar, areaCharTableListener, fam);
        }

        panel.add(toolbar, BorderLayout.PAGE_START);
        panel.add(tabbedPane, BorderLayout.CENTER);

        JPanel status = new JPanel(new GridBagLayout());

        status.add(new JLabel("Expr."), GBC.of(0,0).anchorW().insetsx(0,5).build());
        JScrollPane comp = new JScrollPane(latexLabel);
        comp.setBorder(null);
        status.add(comp, GBC.of(1,0).fillBoth().weight(5,5).anchorW().build());
        status.add(new JLabel("Value"), GBC.of(0,1).anchorW().insetsx(0,5).build());
        status.add(valueLabel, GBC.of(1,1).fillBoth().anchorW().insetsx(10,0).build());
        valueLabel.setEditable(false);
        valueLabel.setBorder(null);
        panel.add(status, BorderLayout.PAGE_END);
        panel.getActionMap().put(EXECUTE_ACTION_COMMAND, new AbstractAction("Save") {
            @Override
            public void actionPerformed(ActionEvent e) {
                returnActionCommand = "save";
                dialog.setVisible(false);
            }
        });
        messagesArea.getTextArea().getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                renderLatex();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                renderLatex();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                renderLatex();
            }
        });

        this.dialog = new JXDialog(
                (JFrame) studio.appComponent(),
                panel);
        dialog.setTitle("Expression");
        dialog.setLocationByPlatform(true);
    }

    protected void renderLatex() {
        if (_renderLatex) {
            _renderLatexAgain = true;
        }
        _renderLatex = true;
        _renderLatexAgain = false;
        do {
            renderLatexImpl();
        } while (_renderLatexAgain);
    }

    protected void renderLatexImpl() {
//        String latex = "\\begin{array}{lr}\\mbox{\\textcolor{Blue}{Russian}}&\\mbox{\\textcolor{Melon}{Greek}}\\\\";
//        latex += "\\mbox{" + "привет мир".toUpperCase() + "}&\\mbox{" + "γειά κόσμο".toUpperCase()
//                + "}\\\\";
//        latex += "\\mbox{привет мир}&\\mbox{γειά κόσμο}\\\\";
//        latex += "\\mathbf{\\mbox{привет мир}}&\\mathbf{\\mbox{γειά κόσμο}}\\\\";
//        latex += "\\mathit{\\mbox{привет мир}}&\\mathit{\\mbox{γειά κόσμο}}\\\\";
//        latex += "\\mathsf{\\mbox{привет мир}}&\\mathsf{\\mbox{γειά κόσμο}}\\\\";
//        latex += "\\mathtt{\\mbox{привет мир}}&\\mathtt{\\mbox{γειά κόσμο}}\\\\";
//        latex += "\\mathbf{\\mathit{\\mbox{привет мир}}}&\\mathbf{\\mathit{\\mbox{γειά κόσμο}}}\\\\";
//        latex += "\\mathbf{\\mathsf{\\mbox{привет мир}}}&\\mathbf{\\mathsf{\\mbox{γειά κόσμο}}}\\\\";
//        latex += "\\mathsf{\\mathit{\\mbox{привет мир}}}&\\mathsf{\\mathit{\\mbox{γειά κόσμο}}}\\\\";
//        latex += "&\\\\";
//        latex += "\\mbox{\\textcolor{Salmon}{Bulgarian}}&\\mbox{\\textcolor{Tan}{Serbian}}\\\\";
//        latex += "\\mbox{здравей свят}&\\mbox{Хелло уорлд}\\\\";
//        latex += "&\\\\";
//        latex += "\\mbox{\\textcolor{Turquoise}{Bielorussian}}&\\mbox{\\textcolor{LimeGreen}{Ukrainian}}\\\\";
//        latex += "\\mbox{прывітаньне Свет}&\\mbox{привіт світ}\\\\";
//        latex += "\\end{array}";
        try {
            TeXFormula formula = new TeXFormula(toLatex(messagesArea.getText()));

            // Note: Old interface for creating icons:
            // TeXIcon icon = formula.createTeXIcon(TeXConstants.STYLE_DISPLAY, 20);
            // Note: New interface using builder pattern (inner class):
            TeXIcon icon = formula.new TeXIconBuilder().setStyle(TeXConstants.STYLE_DISPLAY).setSize(20)
                    .build();

            icon.setInsets(new Insets(5, 5, 5, 5));

//        BufferedImage image = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(),
//                BufferedImage.TYPE_INT_ARGB);
//        Graphics2D g2 = image.createGraphics();
//        g2.setColor(Color.white);
//        g2.fillRect(0, 0, icon.getIconWidth(), icon.getIconHeight());
            latexLabel.setForeground(new Color(0, 0, 0));
            latexLabel.setBackground(Color.WHITE);
            latexLabel.setIcon(icon);
            latexLabel.setText("");
//        icon.paintIcon(latexLabel, g2, 0, 0);
        } catch (Exception ex) {
            latexLabel.setIcon(null);
            latexLabel.setText("Error:"+ex.getMessage());
            latexLabel.setForeground(Color.RED.darker());
            //ignore
        }
        try {
            valueLabel.setForeground(Color.BLACK);
            EvalExprResult e = configuration.evalResult(messagesArea.getText(),unitType,unit, null);
            if(e.isError()){
                valueLabel.setForeground(Color.RED.darker());
                valueLabel.setText("Error:"+e.getErrorMessage());
            }else{
                valueLabel.setForeground(Color.BLACK.darker());
                String s=e.formattedString;
                if(unit!=null){
                    s+=unit.toString();
                }
                valueLabel.setText(s);
            }
        } catch (Exception ex) {
            valueLabel.setForeground(Color.RED.darker());
            valueLabel.setText("Error:"+ex.getMessage());
            //ignore
        }
    }



    private String toLatex(String text) {
        Expr e = Maths.parseExpression(text);
        return e==null?"":e.toLatex();
    }

    /**
     * Create a simple provider that adds some Java-related completions.
     */
    private CompletionProvider createCompletionProvider(UnitType type, HWConfigurationRun configuration) {

        // A DefaultCompletionProvider is the simplest concrete implementation
        // of CompletionProvider. This provider has no understanding of
        // language semantics. It simply checks the text entered up to the
        // caret position for a match against known completions. This is all
        // that is needed in the majority of cases.
        DefaultCompletionProvider provider = new DefaultCompletionProvider();
        provider.addCompletion(new BasicCompletion(provider, "NaN"));
        provider.addCompletion(new BasicCompletion(provider, "Infinity"));
        provider.addCompletion(new BasicCompletion(provider, "PI"));
        provider.addCompletion(new BasicCompletion(provider, "sin"));
        provider.addCompletion(new BasicCompletion(provider, "cos"));
        provider.addCompletion(new BasicCompletion(provider, "sqrt"));
        if (configuration.project() != null) {
            for (HWParameterValue value : configuration.project().get().parameters().toMap().values()) {
                provider.addCompletion(new BasicCompletion(provider, value.name().get()));
            }
        }
//        // Add a couple of "shorthand" completions. These completions don't
//        // require the input text to be the same thing as the replacement text.
//        provider.addCompletion(new ShorthandCompletion(provider, "sin",
//                "System.out.println(", "System.out.println("));
//        provider.addCompletion(new ShorthandCompletion(provider, "syserr",
//                "System.err.println(", "System.err.println("));

        return provider;

    }

    public void setTitle(String title) {
        dialog.setTitle(title);
    }

    public String getExpression() {
        return messagesArea.getText();
    }

    public void setExpression(String expression) {
        messagesArea.setText(expression);
    }

    public boolean show() {
        returnActionCommand = null;
        dialog.setModal(true);
        dialog.setMinimumSize(new Dimension(400, 400));
        dialog.pack();
        dialog.setVisible(true);
        return "save".equals(returnActionCommand);
    }

    private class AreaCharTableListener implements CharTableListener {
        @Override
        public void onChar(CharEvent event) {
            messagesArea.insertOrReplaceSelection(event.getCharCommand().getText());
        }
    }
}
