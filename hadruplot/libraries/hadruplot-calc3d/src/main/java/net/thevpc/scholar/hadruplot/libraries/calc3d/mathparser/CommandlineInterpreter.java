
package net.thevpc.scholar.hadruplot.libraries.calc3d.mathparser;

/**
 * Simple CmdLine interpreter for mathematical expressions the interpreter
 * takes a mathematical expressions as a {@link String} argument, evaluates it
 * and prints out the result.
 * 
 * 
 * <pre>
 * java de.congrace.exp4j.CommandlineInterpreter "2 * log(2.2223) - ((2-3.221) * 14.232^2)"
 * > 248.91042049521056
 * </pre>
 * 
 * @author fas@congrace.de
 * 
 */
public class CommandlineInterpreter {
	private static void calculateExpression(String string) {
		try {
			final PostfixExpression pe = PostfixExpression.fromInfix(string);
			System.out.println(pe.calculate());
		} catch (UnparsableExpressionException e) {
			e.printStackTrace();
		} catch (UnknownFunctionException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		if (args.length != 1) {
			printUsage();
		} else {
			calculateExpression(args[0]);
		}
	}

	private static void printUsage() {
		final StringBuilder usage = new StringBuilder();
		usage.append("CmdLine Expression Parser\n\n").append("Example: ").append("\n").append("java -jar exp4j.jar \"2.12 * log(23) * (12 - 4)\"\n\n")
				.append("written by fas@congrace.de");
		System.err.println(usage.toString());
	}
}
