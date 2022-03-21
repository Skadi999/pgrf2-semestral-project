package project;

//todo add javadocs
public class Rule {
    private char charToConvert;
    private String conversion;

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
