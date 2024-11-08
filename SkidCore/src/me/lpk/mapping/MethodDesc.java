package me.lpk.mapping;

/**
 * The MethodDesc class represents a method's description, including
 * its parameter types and return type, typically in a format used for
 * method signatures in bytecode.
 * <p>
 * This class provides parsing capabilities to interpret method descriptions
 * from different formats, such as JAR format or mapping format, and convert
 * them into a standard representation.
 */
public class MethodDesc {

    /**
     * The types of the parameters, represented as a single string in bytecode format.
     */
    public String paramTypes;

    /**
     * The return type of the method, represented as a single string in bytecode format.
     */
    public String returnType;

    /**
     * Constructs a MethodDesc instance with specified parameter and return types.
     *
     * @param paramTypes the parameter types in bytecode format
     * @param returnType the return type in bytecode format
     */
    public MethodDesc(String paramTypes, String returnType) {
        this.paramTypes = paramTypes;
        this.returnType = returnType;
    }

    /**
     * Creates a MethodDesc object from a string description. It determines whether the input
     * string is in JAR or mapping format and calls the appropriate parsing method.
     *
     * @param s the method description as a string
     * @return a MethodDesc instance representing the parsed description
     */
    public static MethodDesc from(String s) {
        if (s.contains(":")) {
            return fromMapping(s);
        } else {
            return fromJar(s);
        }
    }

    /**
     * Parses a method description in JAR format, extracting the parameter and return types.
     *
     * @param s the JAR format method description
     * @return a MethodDesc instance representing the parsed description
     */
    private static MethodDesc fromJar(String s) {
        int typeIndex = s.indexOf(")") + 1; // Index of the closing parenthesis
        return new MethodDesc(s.substring(0, typeIndex), s.substring(typeIndex));
    }

    /**
     * Parses a method description in mapping format, converting it to standard bytecode format.
     * The method extracts parameter types, identifies the return type, and handles conversion
     * of specific types to their bytecode representations.
     *
     * @param s the mapping format method description
     * @return a MethodDesc instance representing the parsed description
     * @throws RuntimeException if the mapping format cannot be parsed
     */
    private static MethodDesc fromMapping(String s) {
        int parIndex = s.indexOf(")"); // Index of the closing parenthesis
        int lineNumberIndex = -1;
        for (int i = parIndex; i < s.length(); i++) {
            if (Character.isDigit(s.charAt(i))) {
                lineNumberIndex = i;
                break;
            }
        }
        if (lineNumberIndex == -1) {
            throw new RuntimeException("Cannot parse: s = " + s);
        }
        int lineNumberEnd = s.lastIndexOf(":");

        // Parameter types
        String paramTypes = s.substring(0, parIndex + 1);
        // Return type
        String returnType = s.substring(parIndex + 1, lineNumberIndex) + s.substring(lineNumberEnd + 1);

        // Convert return type
        switch (returnType) {
            case "Lvoid;":
                returnType = "V";
                break;
            case "Lint;":
                returnType = "I";
                break;
            case "Lboolean;":
                returnType = "Z";
                break;
            case "Lchar;":
                returnType = "C";
                break;
            case "Llong;":
                returnType = "J";
                break;
            case "Lbyte;":
                returnType = "B";
                break;
            case "Ldouble;":
                returnType = "D";
                break;
            case "Lfloat;":
                returnType = "F";
                break;
            case "Lshort;":
                returnType = "S";
                break;
        }
        return new MethodDesc(paramTypes, returnType);
    }

    /**
     * Returns the method description as a concatenation of parameter types and return type.
     *
     * @return the method description as a string
     */
    @Override
    public String toString() {
        return paramTypes + returnType;
    }
}
