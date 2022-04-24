package project;

/**
 * Represents a rule for converting a character in the L-System string to another string.
 *
 * @author Vyacheslav Novak
 */
public class Rule {
    /**
     * The character which is transformed to a string
     */
    private final char charToConvert;
    /**
     * The string that the character will be transformed into.
     */
    private final String conversion;

    public Rule(char charToConvert, String conversion) {
        this.charToConvert = charToConvert;
        this.conversion = conversion;
    }

    public char getCharToConvert() {
        return charToConvert;
    }

    public String getConversion() {
        return conversion;
    }
}
