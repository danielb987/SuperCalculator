package calculator.parser;

/**
 * A parsed expression
 */
public class ExpressionNodeFloatingNumber implements ExpressionNode {

    private final Token _token;
    private final double _value;
    
    ExpressionNodeFloatingNumber(Token token) {
        _token = token;
        _value = Double.parseDouble(token._string);
    }
    
    @Override
    public Object calculate() {
        return _value;
    }
    
    /** {@inheritDoc} */
    @Override
    public String getDefinitionString() {
        return "FloatNumber:"+_token._string;
    }
    
}
