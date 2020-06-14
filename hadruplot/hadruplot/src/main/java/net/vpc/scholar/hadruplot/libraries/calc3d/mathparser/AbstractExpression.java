/*


 */
package net.vpc.scholar.hadruplot.libraries.calc3d.mathparser;

import java.text.NumberFormat;
import java.util.List;

/**
 * Abstract base class for mathematical expressions
 * 
 * 
 */
abstract class AbstractExpression {
	private final String expression;
	private final Token[] tokens;
	private final String[] variableNames;
	private final NumberFormat numberFormat = NumberFormat.getInstance();

	/**
	 * Construct a new {@link AbstractExpression}
	 * 
	 * @param expression
	 *            the mathematical expression to be used
	 * @param tokens
	 *            the {@link Token}s in the expression
	 * @param variableNames
	 *            an array of variable names which are used in the expression
	 */
	AbstractExpression(String expression, Token[] tokens, String[] variableNames) {
		this.expression = expression;
		this.tokens = tokens;
		this.variableNames = variableNames;
	}

	/**
	 * get the mathematical expression {@link String}
	 * 
	 * @return the expression
	 */
	public String getExpression() {
		return expression;
	}

	/**
	 * get the used {@link NumberFormat}
	 * 
	 * @return the used {@link NumberFormat}
	 */
	public NumberFormat getNumberFormat() {
		return numberFormat;
	}

	/**
	 * get the {@link Token}s
	 * 
	 * @return the array of {@link Token}s
	 */
	Token[] getTokens() {
		return tokens;
	}

	/**
	 * get the variable names
	 * 
	 * @return the {@link List} of variable names
	 */
	String[] getVariableNames() {
		return variableNames;
	}

}
