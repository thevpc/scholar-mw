
package net.vpc.scholar.hadruplot.backends.calc3d.mathparser;

/**
 * Exception for invalid expressions
 */
public class UnparsableExpressionException extends Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * construct a new {@link UnparsableExpressionException}
	 * 
	 * @param c
	 *            the character which could not be parsed
	 * @param pos
	 *            the position of the character in the expression
	 */
	public UnparsableExpressionException(char c, int pos) {
		super("Unable to parse character at position " + pos + ": '" + String.valueOf(c) + "'");
	}
	/**
	 * construct a new {@link UnparsableExpressionException}
	 * 
	 * @param msg 
	 *            the error message
	 */
	public UnparsableExpressionException(String msg) {
		super(msg);
	}
}
