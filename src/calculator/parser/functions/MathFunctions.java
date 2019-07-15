package calculator.parser.functions;

import calculator.parser.Bundle;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import calculator.parser.CalculateException;
import calculator.parser.ExpressionNode;
import calculator.parser.Function;
import calculator.parser.FunctionFactory;
import calculator.parser.ParserException;
import calculator.parser.WrongNumberOfParametersException;
import calculator.util.TypeConversionUtil;
import org.openide.util.lookup.ServiceProvider;

/**
 * Implementation of mathematical functions.
 * 
 * @author Daniel Bergqvist 2019
 */
@ServiceProvider(service = FunctionFactory.class)
public class MathFunctions implements FunctionFactory {

    @Override
    public Set<Function> getFunctions() {
		if (1==1) throw new RuntimeException("Hej");
        Set<Function> functionClasses = new HashSet<>();
        functionClasses.add(new IntFunction());
        functionClasses.add(new RandomFunction());
        functionClasses.add(new SinFunction());
        return functionClasses;
    }
    
    
    
    public static class IntFunction implements Function {

        @Override
        public String getName() {
            return "int";
        }

        @Override
        public Object calculate(List<ExpressionNode> parameterList) throws ParserException {
            if (parameterList.size() != 1) {
                throw new WrongNumberOfParametersException(Bundle.getMessage("WrongNumberOfParameters2", getName(), 1));
            }
            return (int) TypeConversionUtil.convertToLong(parameterList.get(0).calculate());
        }
        
    }
    
    public static class LongFunction implements Function {

        @Override
        public String getName() {
            return "long";
        }

        @Override
        public Object calculate(List<ExpressionNode> parameterList) throws ParserException {
            if (parameterList.size() != 1) {
                throw new WrongNumberOfParametersException(Bundle.getMessage("WrongNumberOfParameters2", getName(), 1));
            }
            return TypeConversionUtil.convertToLong(parameterList.get(0).calculate());
        }
        
    }
    
    public static class RandomFunction implements Function {

        @Override
        public String getName() {
            return "random";
        }

        @Override
        public Object calculate(List<ExpressionNode> parameterList) throws CalculateException {
            return Math.random();
        }
        
    }
    
    public static class SinFunction implements Function {

        @Override
        public String getName() {
            return "sin";
        }

        @Override
        public Object calculate(List<ExpressionNode> parameterList) throws ParserException {
            System.err.format("Sin: Num parameters: %d%n", parameterList.size());
            if (parameterList.size() == 1) {
                double param = TypeConversionUtil.convertToDouble(parameterList.get(0).calculate(), false);
                return Math.sin(param);
            } else if (parameterList.size() >= 2) {
                double param0 = TypeConversionUtil.convertToDouble(parameterList.get(0).calculate(), false);
                Object param1 = parameterList.get(1).calculate();
                double result;
                if (param1 instanceof String) {
                    switch ((String)param1) {
                        case "rad":
                            result = Math.sin(param0);
                            break;
                        case "deg":
                            result = Math.sin(Math.toRadians(param0));
                            break;
                        default:
                            throw new CalculateException(Bundle.getMessage("IllegalParameter", 2, param1, getName()));
                    }
                } else if (param1 instanceof Number) {
                    double p1 = TypeConversionUtil.convertToDouble(param1, false);
                    double angle = param0 * p1 / 2 / Math.PI;
                    result = Math.sin(angle);
                } else {
                    throw new CalculateException(Bundle.getMessage("IllegalParameter", 2, param1, getName()));
                }
                
                switch (parameterList.size()) {
                    case 2:
                        return result;
                    case 4:
                        double min = TypeConversionUtil.convertToDouble(parameterList.get(2).calculate(), false);
                        double max = TypeConversionUtil.convertToDouble(parameterList.get(3).calculate(), false);
                        return (result+1) * (max-min)/2 + min;
                    default:
                        throw new WrongNumberOfParametersException(Bundle.getMessage("WrongNumberOfParameters1", getName()));
                }
            }
            throw new WrongNumberOfParametersException(Bundle.getMessage("WrongNumberOfParameters1", getName()));
        }
        
    }
    
}
