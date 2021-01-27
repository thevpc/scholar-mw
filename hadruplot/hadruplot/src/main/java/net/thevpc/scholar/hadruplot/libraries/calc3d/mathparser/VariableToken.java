package net.thevpc.scholar.hadruplot.libraries.calc3d.mathparser;

import java.util.Map;
import java.util.Stack;

/**
 * A {@link Token} for representing variables

 */
class VariableToken extends CalculationToken {
	/**
	 * construct a new {@link VariableToken}
	 * 
	 * @param value
	 *            the value of the token
	 */
	VariableToken(String value) {
		super(value);
	}

	@Override
	void mutateStackForCalculation(Stack<Double> stack, Map<String, Double> variableValues) {
		double value = variableValues.get(this.getValue());
		stack.push(value);
	}

	@Override
	void mutateStackForInfixTranslation(Stack<Token> operatorStack, StringBuilder output) {
		output.append(this.getValue()).append(" ");
	}
}
