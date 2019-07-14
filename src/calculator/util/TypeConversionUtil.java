package calculator.util;

import java.text.ParseException;
import java.util.Collection;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Converts between java types, for example String to Double and double to boolean.
 * 
 * @author Daniel Bergqvist Copyright 2019
 */
public final class TypeConversionUtil {
    
    /**
     * Is this object a Boolean?
     * @param object the object to check
     * @return true if the object is a Boolean, false otherwise
     */
    public static boolean isBoolean(Object object) {
        return object instanceof Boolean;
    }
    
    /**
     * Is this object an integer number?
     * <P>
     * The method returns true if the object is any of these classes:
     * <ul>
     *   <li>AtomicInteger</li>
     *   <li>AtomicLong</li>
     *   <li>BigInteger</li>
     *   <li>Byte</li>
     *   <li>Short</li>
     *   <li>Integer</li>
     *   <li>Long</li>
     * </ul>
     * @param object the object to check
     * @return true if the object is an object that is an integer, false otherwise
     */
    public static boolean isIntegerNumber(Object object) {
        return (object instanceof java.util.concurrent.atomic.AtomicInteger)
                || (object instanceof java.util.concurrent.atomic.AtomicLong)
                || (object instanceof java.math.BigInteger)
                || (object instanceof Byte)
                || (object instanceof Short)
                || (object instanceof Integer)
                || (object instanceof Long);
    }
    
    /**
     * Is this object an integer number?
     * <P>
     * The method returns true if the object is any of these classes:
     * <ul>
     *   <li>AtomicInteger</li>
     *   <li>AtomicLong</li>
     *   <li>BigInteger</li>
     *   <li>Byte</li>
     *   <li>Short</li>
     *   <li>Integer</li>
     *   <li>Long</li>
     *   <li>BigDecimal</li>
     *   <li>Float</li>
     *   <li>Double</li>
     * </ul>
     * @param object the object to check
     * @return true if the object is an object that is either an integer or a
     * float, false otherwise
     */
    public static boolean isFloatingNumber(Object object) {
        return isIntegerNumber(object)
                || (object instanceof java.math.BigDecimal)
                || (object instanceof Float)
                || (object instanceof Double);
    }
    
    /**
     * Is this object a String?
     * @param object the object to check
     * @return true if the object is a String, false otherwise
     */
    public static boolean isString(Object object) {
        return object instanceof String;
    }
    
    
    /**
     * Convert a value to a boolean.
     * <P>
     * Rules:
     * null is converted to false
     * empty string is converted to false
     * "0" string is converted to false
     * "0.000" string is converted to false, if the number of decimals is &gt; 0
     * empty map is converted to false
     * empty collection is converted to false
     * An integer number is converted to false if the number is 0
     * A floating number is converted to false if the number is -0.5 &lt; x &lt; 0.5
     * Everything else is converted to true
     * 
     * @param value the value to convert
     * @param do_i18n true if internationalization should be done, false otherwise
     * @return the boolean value
     */
    public static boolean convertToBoolean(Object value, boolean do_i18n) {
        if (value == null) {
            return false;
        }
        
        if (value instanceof Map) {
            Map map = ((Map)value);
            return !map.isEmpty();
        }
        
        if (value instanceof Collection) {
            Collection collection = ((Collection)value);
            return !collection.isEmpty();
        }
        
        if (value instanceof Number) {
            double number = ((Number)value).doubleValue();
            return ! ((-0.5 < number) && (number < 0.5));
        } else if (value instanceof Boolean) {
            return (Boolean)value;
        } else {
            String str = value.toString();
            
            // try to parse the string as a number
            try {
                double number;
                if (do_i18n) {
                    number = doubleValue(str);
                } else {
                    number = Double.parseDouble(str);
                }
//                System.err.format("The string: '%s', result: %1.4f%n", str, (float)number);
                return ! ((-0.5 < number) && (number < 0.5));
            } catch (NumberFormatException | ParseException ex) {
//                log.debug("The string '{}' cannot be parsed as a number", str);
            }
            
//            System.err.format("The string: %s, %s%n", str, value.getClass().getName());
            String patternString = "^0(\\.0+)?$";
            Pattern pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(str);
            if (matcher.matches()) {
//                System.err.format("The string: '%s', result: %b%n", str, false);
                return false;
            }
//            System.err.format("The string: '%s', result: %b%n", str, !str.isEmpty());
            return !str.isEmpty();
        }
    }
    
    /**
     * Convert a value to a long.
     * <P>
     * Rules:
     * null is converted to 0
     * empty string is converted to 0
     * empty collection is converted to 0
     * an instance of the interface Number is converted to the number
     * a string that can be parsed as a number is converted to that number.
     * a string that doesn't start with a digit is converted to 0
     * 
     * @param value the value to convert
     * @return the long value
     */
    public static long convertToLong(Object value) {
        if (value == null) {
//            log.warn("the object is null and the returned number is therefore 0.0");
            return 0;
        }
        
        if (value instanceof Number) {
//            System.err.format("Number: %1.5f%n", ((Number)value).doubleValue());
            return ((Number)value).longValue();
        } else if (value instanceof Boolean) {
            return ((Boolean)value) ? 1 : 0;
        } else {
            String str = value.toString();
            String patternString = "(\\-?\\d+)";
            Pattern pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(str);
            // Only look at the beginning of the string
            if (matcher.lookingAt()) {
                String theNumber = matcher.group(1);
                long number = Long.parseLong(theNumber);
//                System.err.format("Number: %1.5f%n", number);
//                log.debug("the string {} is converted to the number {}", str, number);
                return number;
            } else {
//                log.warn("the string \"{}\" cannot be converted to a number", str);
                return 0;
            }
        }
    }
    
    /**
     * Convert a value to a double.
     * <P>
     * Rules:
     * null is converted to 0
     * empty string is converted to 0
     * empty collection is converted to 0
     * an instance of the interface Number is converted to the number
     * a string that can be parsed as a number is converted to that number.
     * if a string starts with a number AND do_i18n is false, it's converted to that number
     * a string that doesn't start with a digit is converted to 0
     * 
     * @param value the value to convert
     * @param do_i18n true if internationalization should be done, false otherwise
     * @return the double value
     */
    public static double convertToDouble(Object value, boolean do_i18n) {
        if (value == null) {
//            log.warn("the object is null and the returned number is therefore 0.0");
            return 0.0d;
        }
        
        if (value instanceof Number) {
//            System.err.format("Number: %1.5f%n", ((Number)value).doubleValue());
            return ((Number)value).doubleValue();
        } else if (value instanceof Boolean) {
            return ((Boolean)value) ? 1 : 0;
        } else {
            if (do_i18n) {
                // try to parse the string as a number
                try {
                    double number = doubleValue(value.toString());
//                    System.err.format("The string: '%s', result: %1.4f%n", value, (float)number);
                    return number;
                } catch (ParseException ex) {
//                    log.debug("The string '{}' cannot be parsed as a number", value);
                }
            }
            String str = value.toString();
            String patternString = "(\\-?\\d+(\\.\\d+)?(e\\-?\\d+)?)";
            Pattern pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(str);
            // Only look at the beginning of the string
            if (matcher.lookingAt()) {
                String theNumber = matcher.group(1);
                double number = Double.parseDouble(theNumber);
//                System.err.format("Number: %1.5f%n", number);
//                log.debug("the string {} is converted to the number {}", str, number);
                return number;
            } else {
//                log.warn("the string \"{}\" cannot be converted to a number", str);
                return 0.0d;
            }
        }
    }
    
    /**
     * Convert a value to a String.
     * 
     * @param value the value to convert
     * @param do_i18n true if internationalization should be done, false otherwise
     * @return the String value
     */
    public static String convertToString(Object value, boolean do_i18n) {
        if (value == null) {
            return "";
        }
        
        if (value instanceof Number) {
            if (do_i18n) {
                return valueOf(((Number)value).doubleValue());
            }
        }
        
        return value.toString();
    }
    
	
	
	
    static private float floatValue(String val) throws java.text.ParseException {
        return java.text.NumberFormat.getInstance().parse(val).floatValue();
    }
	
    static private double doubleValue(String val) throws java.text.ParseException {
        return java.text.NumberFormat.getInstance().parse(val).doubleValue();
    }
	
    static private String valueOf(double val) {
        return java.text.NumberFormat.getInstance().format(val);
    }
	
    static private String valueOf(int val) {
        return java.text.NumberFormat.getInstance().format(val);
    }
	
}
