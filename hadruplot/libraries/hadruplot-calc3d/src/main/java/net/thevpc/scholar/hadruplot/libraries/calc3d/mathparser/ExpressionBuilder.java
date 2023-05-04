package net.thevpc.scholar.hadruplot.libraries.calc3d.mathparser;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * This is Builder implementation for the mathparser API used to create a
 * Calculable instance for the user
 * 
 * 
 */
public class ExpressionBuilder {
	private final Map<String, Double> variables = new LinkedHashMap<String, Double>();
	private final Set<CustomFunction> customFunctions = new HashSet<CustomFunction>();

	private String expression;

	/**
	 * Create a new ExpressionBuilder
	 * 
	 * @param expression
	 *            the expression to evaluate
	 */
	public ExpressionBuilder(String expression) {

		this.expression = expression;
		//System.out.println(expression);

	}

	/**
	 * build a new {@link Calculable} from the expression using the supplied
	 * variables
	 * 
	 * @return the {@link Calculable} which can be used to evaluate the
	 *         expression
	 * @throws UnknownFunctionException
	 *             when an unrecognized function name is used in the expression
	 * @throws UnparsableExpressionException
	 *             if the expression could not be parsed
	 */
	public Calculable build() throws UnknownFunctionException,
			UnparsableExpressionException {
		/*
		 * Add Pi and e as variables;
		 */
		variables.put("Pi", Math.PI);
		variables.put("PI", Math.PI);
		variables.put("e", Math.E);	
		expression=PreProcessedExpression(expression);		

		try {
			CustomFunction maxFunc = new CustomFunction("max",2) {
			    public double applyFunction(double[] values) {
			              return Math.max(values[0],values[1]);
			        }
			};
			customFunctions.add(maxFunc);
			CustomFunction minFunc = new CustomFunction("min",2) {
			    public double applyFunction(double[] values) {
			    	      return Math.min(values[0],values[1]);
			        }
			};
			customFunctions.add(minFunc);
		} catch (InvalidCustomFunctionException e) {
			e.printStackTrace();
		}
		
		if (expression.indexOf('=') == -1 && !variables.isEmpty()) {

			// User supplied an expression without leading "f(...)="
			// so we just append the user function to a proper "f()="
			// for PostfixExpression.fromInfix()
			StringBuilder function = new StringBuilder("f(");
			for (String var : variables.keySet()) {
				function.append(var).append(',');
			}
			expression = function.deleteCharAt(function.length() - 1)
					.toString() + ")=" + expression;
		}
		// create the PostfixExpression and return it as a Calculable
		PostfixExpression delegate = PostfixExpression.fromInfix(expression,
				customFunctions);
		for (String var : variables.keySet()) {
			if (variables.get(var) != null) {
				delegate.setVariable(var, variables.get(var));
			}
			for (CustomFunction custom : customFunctions) {
				if (custom.getValue().equals(var)) {
					throw new UnparsableExpressionException(
							"variable '"
									+ var
									+ "' cannot have the same name as a custom function "
									+ custom.getValue());
				}
			}
		}
		return delegate;
	}

	/**
	 * removes [ ] by floor(), { } by frac(), | | by abs()
	 * 
	 * @param string
	 *            string representing expression
	 * @return String representing expression after replacing [],{} and ||
	 */
	private String PreProcessedExpression(String str) {
		String exp;
		StringBuilder strB = new StringBuilder(str);
		/*
		int firstIndex, lastIndex;
		firstIndex = strB.indexOf("|");
		while (firstIndex >= 0) {
			strB.replace(firstIndex, firstIndex + 1, "(abs(");
			lastIndex = strB.lastIndexOf("|");
			if (lastIndex > 0)
				strB.replace(lastIndex, lastIndex + 1, "))");
			firstIndex = strB.indexOf("|");
		}
		*/

		// System.out.println(strB);
		exp = strB.toString();
		exp = exp.replace("[", "(floor(");
		exp = exp.replace("]", "))");
		exp = exp.replace("{", "(frac(");
		exp = exp.replace("}", "))");

		exp=removeMod(exp);
		return exp;

	}

	private String removeMod(String str) {
	
		StringBuilder strB = new StringBuilder(str);
	
		//replace Mod symbol by < if occurs at first index
		if (strB.charAt(0) == '|')	
			strB.replace(0, 1, "<");
		//replace Mod symbol by > if occurs at last index
		if (strB.charAt(strB.length()-1) == '|')
			strB.replace(strB.length()-1, strB.length(), ">");
 
        //check for more mod symbols if any
		//if mod Symbol is preceded by operator it must be starting mod [
		//else it must be ending mod (HOPE SO........)
		for (int i = 1; i < strB.length() - 1; i++) {
			if (strB.charAt(i)=='|'){
				//check if char left to | is operator
				if (isOperator(strB.charAt(i - 1))) {
					//replace | by <
                    strB.replace(i, i+1, "<");
				} else {
					//replace | by >, since char to right of it is operator
					strB.replace(i, i+1, ">");
				}
			}	
		}		
		str=strB.toString();
		//replace open mode < by "abs(" and closing mod > by ")"
		str=str.replace("<","abs((");
		str=str.replace(">","))");
		//System.out.println(strB + "==" + str);
		return str;
		
	}

	private boolean isOperator(char c) {

		switch (c) {
		case '/':
			return true;
		case '+':
			return true;
		case '-':
			return true;
		case '*':
			return true;
		case '(':
			return true;
		case ')':
			return true;
		case '<':
			return true;
		case ',':
			return true;
		default:
			return false;
		}

	}

	/**
	 * add a custom function instance for the evaluator to recognize
	 * 
	 * @param function
	 *            the {@link CustomFunction} to add
	 * @return the {@link ExpressionBuilder} instance
	 */
	public ExpressionBuilder withCustomFunction(CustomFunction function) {
		customFunctions.add(function);
		return this;
	}

	public ExpressionBuilder withCustomFunctions(
			Collection<CustomFunction> functions) {
		customFunctions.addAll(functions);
		return this;
	}

	/**
	 * set the value for a variable
	 * 
	 * @param variableName
	 *            the variable name e.g. "x"
	 * @param value
	 *            the value e.g. 2.32d
	 * @return the {@link ExpressionBuilder} instance
	 */
	public ExpressionBuilder withVariable(String variableName, double value) {
		variables.put(variableName, value);
		return this;
	}

	/**
	 * set the variables names used in the expression without setting their
	 * values
	 * 
	 * @param variableNames
	 *            vararg {@link String} of the variable names used in the
	 *            expression
	 * @return the ExpressionBuilder instance
	 */
	public ExpressionBuilder withVariableNames(String... variableNames) {
		for (String variable : variableNames) {
			variables.put(variable, null);
		}
		return this;
	}

	/**
	 * set the values for variables
	 * 
	 * @param variableMap
	 *            a map of variable names to variable values
	 * @return the {@link ExpressionBuilder} instance
	 */
	public ExpressionBuilder withVariables(Map<String, Double> variableMap) {
		for (Entry<String, Double> v : variableMap.entrySet()) {
			variables.put(v.getKey(), v.getValue());
		}
		return this;
	}
}
