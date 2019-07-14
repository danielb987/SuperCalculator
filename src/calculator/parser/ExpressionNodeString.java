package calculator.parser;

/**
 * A parsed expression
 */
public class ExpressionNodeString implements ExpressionNode {

    private final Token _token;
    
    ExpressionNodeString(Token token) {
        _token = token;
    }
    
    @Override
    public Object calculate() {
        return _token._string;
    }
    
    /** {@inheritDoc} */
    @Override
    public String getDefinitionString() {
        return "String:"+_token._string;
    }
    
}
