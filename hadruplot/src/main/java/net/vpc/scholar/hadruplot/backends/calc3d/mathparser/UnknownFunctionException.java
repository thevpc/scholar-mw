
package net.vpc.scholar.hadruplot.backends.calc3d.mathparser;

/**
 * Exception for handling unknown Functions.
 * 
  */
public class UnknownFunctionException extends Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * construct a new {@link UnknownFunctionException}
	 * 
	 * @param functionName
	 *            the function name which could not be found
	 */
	public UnknownFunctionException(String functionName) {
		super("Unknown function: " + functionName);
	}
}
