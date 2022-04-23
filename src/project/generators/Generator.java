package project.generators;

import project.Rule;

import java.util.List;

public abstract class Generator {
    private final float length;
    private final float lengthDivisor;

    private final String axiom;

    private final List<Rule> rules;

    private final int angle;
    private final int rotAngle;

    private final int maxGen;

    public Generator(int angle, int rotAngle, float length, float lengthDivisor,
                     String axiom, List<Rule> rules, int maxGen) {
        this.angle = angle;
        this.rotAngle = rotAngle;
        this.length = length;
        this.lengthDivisor = lengthDivisor;
        this.axiom = axiom;
        this.rules = rules;
        this.maxGen = maxGen;
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

    public int getMaxGen() {
        return maxGen;
    }
}
