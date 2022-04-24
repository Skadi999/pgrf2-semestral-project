package project.generators;

import project.Rule;

import java.util.List;


/**
 * An abstract class that represents a Generator type.
 * This class contains various fields that are used by, and are different for each type of Generator
 * that extends this class.
 * This class represents <b>deterministic</b> generators. For stochastic ones, see {@link StochasticGenerator}
 *
 * @author Vyacheslav Novak
 */
public abstract class Generator {
    /**
     * The length of one step across the L-System. This length is modified by {@link Generator#lengthDivisor}
     */
    private final float length;
    /**
     * Divides the length of an L-System by the specified value. The division happens each time we increment the generation.
     */
    private final float lengthDivisor;

    /**
     * The axiom represents the initial form of the L-System. An axiom always contains at least one character.
     */
    private final String axiom;

    /**
     * A list of rules by which to convert the L-System string into a new one.
     */
    private final List<Rule> rules;

    /**
     * The initial angle of the direction of an L-System.
     */
    private final int angle;
    /**
     * The angle by which to rotate the L-System's direction, using the rotateLeft and rotateRight methods (in LSystem class).
     */
    private final int rotAngle;
    /**
     * The maximum allowed generation number. It is necessary due to performance issues.
     */
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
