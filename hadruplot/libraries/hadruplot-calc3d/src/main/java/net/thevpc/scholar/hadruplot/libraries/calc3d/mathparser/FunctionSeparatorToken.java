package net.thevpc.scholar.hadruplot.libraries.calc3d.mathparser;

import java.util.Stack;

public class FunctionSeparatorToken extends Token{
    public FunctionSeparatorToken() {
        super(",");
    }
    @Override
    void mutateStackForInfixTranslation(Stack<Token> operatorStack, StringBuilder output) {
        Token token;
        while (!((token=operatorStack.peek()) instanceof ParenthesisToken) && !token.getValue().equals("(")){
            output.append(operatorStack.pop().getValue()).append(" ");
        }
    }

}
