/*
 
 */
package net.vpc.scholar.hadruplot.backends.calc3d.mathparser;

import java.util.Stack;

/**
 * Token for parenthesis

 */
class ParenthesisToken extends Token {

	ParenthesisToken(String value) {
		super(value);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ParenthesisToken) {
			final ParenthesisToken t = (ParenthesisToken) obj;
			return t.getValue().equals(this.getValue());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return getValue().hashCode();
	}

	/**
	 * check the direction of the parenthesis
	 * 
	 * @return true if it's a left parenthesis (open) false if it is a right
	 *         parenthesis (closed)
	 */
	boolean isOpen() {
		return getValue().equals("(") || getValue().equals("[") || getValue().equals("{");
	}

	@Override
	void mutateStackForInfixTranslation(Stack<Token> operatorStack, StringBuilder output) {
		if (this.isOpen()) {
			operatorStack.push(this);
		} else {
			Token next;
			while ((next = operatorStack.peek()) instanceof OperatorToken || next instanceof FunctionToken || next instanceof CustomFunction
					|| (next instanceof ParenthesisToken && !((ParenthesisToken) next).isOpen())) {
				output.append(operatorStack.pop().getValue()).append(" ");
			}
			operatorStack.pop();
		}
	}
}
