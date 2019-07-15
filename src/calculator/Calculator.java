package calculator;

import calculator.parser.ExpressionNode;
import calculator.parser.ParserException;
import calculator.parser.RecursiveDescentParser;
import calculator.parser.Variable;
import calculator.util.TypeConversionUtil;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class Calculator {

    private static Map<String, Variable> _variables = new HashMap<>();
	
	public static String calculate(String expression) {
		expression = "sin(10)";
		try {
			if (expression.isEmpty()) {
				return "empty expression";
			}
			RecursiveDescentParser t = new RecursiveDescentParser(_variables);
			ExpressionNode exprNode = t.parseExpression(expression);
			return TypeConversionUtil.convertToString(exprNode.calculate(), true);
		} catch (ParserException e) {
			return e.getMessage();
		}
	}
	
	public static void addVariable(Variable variable) {
		_variables.put(variable.getName(), variable);
	}
	
}
