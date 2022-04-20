package project.generators;

import project.Rule;

import java.util.List;

public abstract class Generator {
    private final float startingX;
    private final float startingY;

    private final float length;
    private final float lengthDivisor;

    private final String axiom;

    private final List<Rule> rules;

    private final int angle;
    private final int rotAngle;

    public Generator(float startingX, float startingY, int angle, int rotAngle, float length, float lengthDivisor, String axiom, List<Rule> rules) {
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

    public float getLengthDivisor() {
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
