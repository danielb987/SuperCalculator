package calculator.parser;

import java.util.List;

/**
 * Definition of a function used in expressions.
 * 
 * @author Daniel Bergqvist 2019
 */
public interface Function {

    public String getName();
    
    public Object calculate(List<ExpressionNode> parameterList) throws ParserException;
    
}
