package project.generators;

import project.Rule;

import java.util.List;

/**
 * Represents a Stochastic generator. Stochastic generators are generators that have randomness applied to some
 * of their variables. In this program, the stochastic generator only has randomness applied to the rotation angle
 * and the length. See also {@link Generator}
 *
 * @author Vyacheslav Novak
 */
public abstract class StochasticGenerator extends Generator {
    /**
     * This field is used to apply randomness to the rotation angle of the L-System. The range of the applied random
     * value is [-rotAngleDelta, rotAngleDelta]. See {@link project.LSystem}
     */
    private final int rotAngleDelta;
    /**
     * This field is used to apply randomness to the length of the L-System. The range of the applied random
     * value is [-lengthMultiplierDelta, lengthMultiplierDelta]. See {@link project.LSystem}
     */
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
