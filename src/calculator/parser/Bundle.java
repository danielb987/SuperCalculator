package calculator.parser;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class Bundle {

	private static final Map<String, String> map = new HashMap<>();
	
	static {
		map.put("InvalidSyntax", "Invalid syntax error");
		map.put("InvalidSyntaxAtIndex", "Invalid syntax at index {0}");
		map.put("InvalidSyntaxNotFullyParsed", "Invalid syntax error. The expression is not fully parsed.");

		map.put("FunctionNotExists", "The function \"{0}\" does not exists");
		map.put("IdentifierNotExists", "The identifier \"{0}\" does not exists");

		map.put("ArithmeticModuloFloatingError", "Modulo cannot be done between floating point operands \"{0}\" and \"{1}\"");
		map.put("ArithmeticNotNumberError", "Arithmetic operations cannot be done on the operand \"{0}\" sinse it's not a number");
		map.put("ArithmeticNotIntegerNumberError", "Arithmetic operations cannot be done on the operand \"{0}\" sinse it's not an integer number");
		map.put("ArithmeticNotCompatibleOperands", "The two operands \"{0}\" and \"{1}\" have different types");
		
		map.put("WrongNumberOfParameters1", "Function \"{0}\" has wrong number of parameters");
		map.put("WrongNumberOfParameters2", "Function \"{0}\" has wrong number of parameters. \"{1}\" parameters expected");
		map.put("IllegalParameter", "Parameter \"{0}\" with value \"{1}\" for function \"{2}\" is invalid");
	}
	
	
    public static String getMessage(String key, Object... subs) {
		String message = map.get(key);
        return MessageFormat.format(message, subs);
    }

}
