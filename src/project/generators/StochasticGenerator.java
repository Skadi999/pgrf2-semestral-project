package project.generators;

import project.Rule;

import java.util.List;

public abstract class StochasticGenerator extends Generator {
    private final int rotAngleDelta;
    private final float lengthMultiplierDelta;

    public StochasticGenerator(int angle, int rotAngle, float length, float lengthDivisor,
                               String axiom, List<Rule> rules, int maxGen, int rotAngleDelta, float lengthMultiplierDelta) {
        super(angle, rotAngle, length, lengthDivisor, axiom, rules, maxGen);
        this.rotAngleDelta = rotAngleDelta;
        this.lengthMultiplierDelta = lengthMultiplierDelta;
    }

    public int getRotAngleDelta() {
        return rotAngleDelta;
    }

    public float getLengthMultiplierDelta() {
        return lengthMultiplierDelta;
    }
}
