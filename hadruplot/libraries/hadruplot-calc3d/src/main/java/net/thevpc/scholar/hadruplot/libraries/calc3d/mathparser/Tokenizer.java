
package net.thevpc.scholar.hadruplot.libraries.calc3d.mathparser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.thevpc.scholar.hadruplot.libraries.calc3d.mathparser.FunctionToken.Function;



/**
 * Class for tokenizing mathematical expressions by breaking an expression up
 * into multiple different {@link Token}s
  */
class Tokenizer {
	private String[] variableNames;
	private final Set<String> functionNames = new HashSet<String>();
	private final Set<CustomFunction> customFunctions;

	{
		functionNames.add("abs");
		functionNames.add("acos");
		functionNames.add("asin");
		functionNames.add("atan");
		functionNames.add("cbrt");
		functionNames.add("ceil");
		functionNames.add("cos");
		functionNames.add("cosh");
		functionNames.add("exp");
		functionNames.add("expm1");
		functionNames.add("floor");
		functionNames.add("log");
		functionNames.add("sin");
		functionNames.add("sinh");
		functionNames.add("sqrt");
		functionNames.add("tan");
		functionNames.add("tanh");
		functionNames.add("sinh");
		functionNames.add("frac");
		functionNames.add("asec");
		functionNames.add("acosec");
		functionNames.add("acot");
			
	}

	Tokenizer() {
		super();
		customFunctions = null;
	}

	/**
	 * construct a new Tokenizer that recognizes variable names
	 * 
	 * @param variableNames
	 *            the variable names in the expression
	 * @throws IllegalArgumentException
	 *             if a variable has the name as a function
	 * @param customFunctions
	 *            the CustomFunction implementations used if the variableNames
	 *            are not valid
	 */
	Tokenizer(String[] variableNames, Set<CustomFunction> customFunctions) throws IllegalArgumentException {
		super();
		this.variableNames = variableNames;
		if (variableNames != null) {
			for (String varName : variableNames) {
				if (functionNames.contains(varName.toLowerCase())) {
					throw new IllegalArgumentException("Variable '" + varName + "' can not have the same name as a function");
				}
			}
		}
		this.customFunctions = customFunctions;
	}

	private Token getCustomFunctionToken(String name) throws UnknownFunctionException {
		for (CustomFunction func : customFunctions) {
			if (func.getValue().equals(name)) {
				return func;
			}
		}
		throw new UnknownFunctionException(name);
	}

	private boolean isCustomFunction(String name) {
		if (customFunctions == null) {
			return false;
		}
		for (CustomFunction func : customFunctions) {
			if (func.getValue().equals(name)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * check if a char is part of a number
	 * 
	 * @param c
	 *            the char to be checked
	 * @return true if the char is part of a number
	 */
	private boolean isDigit(char c) {
		return Character.isDigit(c) || c == '.';
	}

	private boolean isFunction(String name) {
		for (Function fn : Function.values()) {
			if (fn.name().equals(name.toUpperCase())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * check if a String is a variable name
	 * 
	 * @param name
	 *            the variable name which is checked to be valid the char to be
	 *            checked
	 * @return true if the char is a variable name (e.g. x)
	 */
	private boolean isVariable(String name) {
		if (variableNames != null) {
			for (String var : variableNames) {
				if (name.equals(var)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * tokenize an infix expression by breaking it up into different
	 * {@link Token} that can represent operations,functions,numbers,
	 * parenthesis or variables
	 * 
	 * @param infix
	 *            the infix expression to be tokenized
	 * @return the {@link Token}s representing the expression
	 * @throws UnparsableExpressionException
	 *             when the expression is invalid
	 * @throws UnknownFunctionException
	 *             when an unknown function name has been used.
	 */
	Token[] tokenize(String infix) throws UnparsableExpressionException, UnknownFunctionException {
		final List<Token> tokens = new ArrayList<Token>();
		final char[] chars = infix.toCharArray();
		// iterate over the chars and fork on different types of input
		Token lastToken;
		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			if (c == ' ')
				continue;
			if (isDigit(c)) {
				final StringBuilder valueBuilder = new StringBuilder(1);
				// handle the numbers of the expression
				valueBuilder.append(c);
				int numberLen = 1;
				while (chars.length > i + numberLen && isDigit(chars[i + numberLen])) {
					valueBuilder.append(chars[i + numberLen]);
					numberLen++;
				}
				i += numberLen - 1;
				lastToken = new NumberToken(valueBuilder.toString());
			} else if (Character.isLetter(c) || c == '_') {
				// can be a variable or function
				final StringBuilder nameBuilder = new StringBuilder();
				nameBuilder.append(c);
				int offset = 1;
				while (chars.length > i + offset && (Character.isLetter(chars[i + offset]) || Character.isDigit(chars[i + offset]) || chars[i + offset] == '_')) {
					nameBuilder.append(chars[i + offset++]);
				}
				String name = nameBuilder.toString();
				if (this.isVariable(name)) {
					// a variable
					i += offset - 1;
					lastToken = new VariableToken(name);
				} else if (this.isFunction(name)) {
					// might be a function
					i += offset - 1;
					lastToken = new FunctionToken(name);
				} else if (this.isCustomFunction(name)) {
					// a custom function
					i += offset - 1;
					lastToken = getCustomFunctionToken(name);
				} else {
					// an unknown symbol was encountered
					throw new UnparsableExpressionException(c, i);
				}
			}else if (c == ',') {
			    // a function separator, hopefully
			    lastToken=new FunctionSeparatorToken();
			} else if (OperatorToken.isOperator(c)) {
				lastToken = new OperatorToken(String.valueOf(c), OperatorToken.getOperation(c));
			} else if (c == '(' || c == ')' || c == '[' || c == ']' || c == '{' || c == '}') {
				lastToken = new ParenthesisToken(String.valueOf(c));
			} else {
				// an unknown symbol was encountered
				throw new UnparsableExpressionException(c, i);
			}
			tokens.add(lastToken);
		}
		return tokens.toArray(new Token[tokens.size()]);
	}
}
