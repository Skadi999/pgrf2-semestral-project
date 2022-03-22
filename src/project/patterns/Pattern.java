package project.patterns;

import project.Rule;

import java.util.List;

//todo: find a way to refactor the constructor
public abstract class Pattern {
    private float startingX;
    private float startingY;

    private float length;
    private int lengthDivisor;

    private String axiom;

    private List<Rule> rules;

    private int angle;
    private int rotAngle;

    public Pattern(float startingX, float startingY, int angle, int rotAngle, float length, int lengthDivisor, String axiom, List<Rule> rules) {
        this.startingX = startingX;
        this.startingY = startingY;
        this.angle = angle;
        this.rotAngle = rotAngle;
        this.length = length;
        this.lengthDivisor = lengthDivisor;
        this.axiom = axiom;
        this.rules = rules;
    }

    public float getStartingX() {
        return startingX;
    }

    public float getStartingY() {
        return startingY;
    }

    public float getLength() {
        return length;
    }

    public int getLengthDivisor() {
        return lengthDivisor;
    }

    public String getAxiom() {
        return axiom;
    }

    public List<Rule> getRules() {
        return rules;
    }

    public int getAngle() {
        return angle;
    }

    public int getRotAngle() {
        return rotAngle;
    }
}
