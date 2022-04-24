package project;

import project.generators.Generator;
import project.generators.StochasticGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import static org.lwjgl.opengl.GL11.*;
//Angles: 0=right, 90=up, 180=left, 270=bottom

/**
 * The main class responsible for all the L-System operations.
 * This class reads initial values from the used Generator, converts the L-System string according to the rules,
 * Draws the L-System, as well as rescaling and centering it.
 *
 * @author Vyacheslav Novak
 */
public class LSystem {
    //initial coordinates. They're set to 0 at the beginning, but are then used while drawing.
    private float startingX;
    private float startingY;

    private float x1;
    private float y1;
    private float x2;
    private float y2;

    //Maximum and minimum coordinates of the whole L-system
    private float xMax;
    private float yMax;
    private float xMin;
    private float yMin;

    //length lengthDivisor, rotAngle and rules are taken from the used Generator.
    private float length;
    private float lengthDivisor;
    private int rotAngle;
    private List<Rule> rules = new ArrayList<>();

    //stochastic Generator fields.
    private int rotAngleDelta;
    private float lengthDelta;

    private RotationManager rotationManager;

    //The L-System string. Initial value is the axiom, taken from the Generator.
    //Then it's converted in convertLSystemByRuleWithGenerations
    private String lSystem;

    private int generationCount;

    //Used by [ and ] operators.
    private Stack<State> states = new Stack<>();

    private boolean isStochastic;


    public void run(int generationCount, Generator generator) {
        initAndDraw(generationCount, generator);

        fixScaleAndPosition();

        //need to clear, reset (reinitialize) and redraw the L-system after scaling and repositioning it.
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        initAndDraw(generationCount, generator);

    }

    private void initAndDraw(int generationCount, Generator generator) {
        initializeLSystem(generationCount, generator);
        convertLSystemByRuleWithGenerations();

        drawLSystem();
    }

    //This algorithm is used to properly rescale the L-System and fit it into the center.
    //The resulting scale of the L-System will be {Max(LSystemWidth, LSystemHeight) = 1.8f}
    //Unfortunately stochastic L-Systems cannot be properly rescaled/centered, because for this algorithm to work,
    //we first draw the shape, then apply the algorithm, and then draw the shape again, but with stochastic L-Systems,
    //the first drawn shape and the second drawn shape are different, resulting in the second drawn shape scaled
    //and positioned according to the values of the first one.
    private void fixScaleAndPosition() {
        float shapeWidth = Math.abs(xMax - xMin);
        float shapeHeight = Math.abs(yMax - yMin);

        float widthScalingFactor;
        float heightScalingFactor;
        float totalScalingFactor = 1f;

        //downscaling if the shape is too large
        if (shapeWidth >= shapeHeight && shapeWidth > 1.8f) {
            widthScalingFactor = 1 / (shapeWidth / 1.8f);
            totalScalingFactor *= widthScalingFactor;
        } else if (shapeHeight > shapeWidth && shapeHeight > 1.8f) {
            heightScalingFactor = 1 / (shapeHeight / 1.8f);
            totalScalingFactor *= heightScalingFactor;
        } //upscaling if shape is too small
        else if (shapeWidth >= shapeHeight && shapeWidth < 1.8f) {
            widthScalingFactor = 1 / (shapeWidth / 1.8f);
            totalScalingFactor *= widthScalingFactor;
        } else if (shapeHeight > shapeWidth && shapeHeight < 1.8f) {
            heightScalingFactor = 1 / (shapeHeight / 1.8f);
            totalScalingFactor *= heightScalingFactor;
        }

        glScalef(totalScalingFactor, totalScalingFactor, 1);

        //After scaling, we must translate it back to the center again.
        centerShapeWithScalingFactor(totalScalingFactor);
    }

    //Takes into account the scaling factor, calculated by the fixScaleAndPosition function and centers the LSystem.
    private void centerShapeWithScalingFactor(float totalScalingFactor) {
        float xMinDistanceToCenter = Math.abs(0 - xMin);
        float xMaxDistanceToCenter = Math.abs(0 - xMax);
        float yMinDistanceToCenter = Math.abs(0 - yMin);
        float yMaxDistanceToCenter = Math.abs(0 - yMax);

        float scaledXMin = (xMin < 0) ? (xMin + xMinDistanceToCenter * (1 - totalScalingFactor)) :
                (xMin - xMinDistanceToCenter * (1 - totalScalingFactor));
        float scaledXMax = (xMax < 0) ? (xMax + xMaxDistanceToCenter * (1 - totalScalingFactor)) :
                (xMax - xMaxDistanceToCenter * (1 - totalScalingFactor));
        float scaledYMin = (yMin < 0) ? (yMin + yMinDistanceToCenter * (1 - totalScalingFactor)) :
                (yMin - yMinDistanceToCenter * (1 - totalScalingFactor));
        float scaledYMax = (yMax < 0) ? (yMax + yMaxDistanceToCenter * (1 - totalScalingFactor)) :
                (yMax - yMaxDistanceToCenter * (1 - totalScalingFactor));

        float scaledXCenter = (scaledXMin + scaledXMax) / 2;
        float scaledYCenter = (scaledYMin + scaledYMax) / 2;

        glTranslatef((-scaledXCenter) / totalScalingFactor, (-scaledYCenter) / totalScalingFactor, 0);
    }

    //Sets many initial values needed by the L-System, the values are either taken from the used Generator, or
    //set to an initial value (f.e. startingX = 0)
    private void initializeLSystem(int generationCount, Generator generator) {
        if (generator instanceof StochasticGenerator) {
            isStochastic = true;
            rotAngleDelta = ((StochasticGenerator) generator).getRotAngleDelta();
            lengthDelta = ((StochasticGenerator) generator).getLengthMultiplierDelta();
        }

        startingX = 0;
        startingY = 0;
        xMin = startingX;
        yMin = startingY;
        xMax = startingX;
        yMax = startingY;
        length = generator.getLength();
        lengthDivisor = generator.getLengthDivisor();
        rules = generator.getRules();
        rotAngle = generator.getRotAngle();
        this.generationCount = generationCount;

        rotationManager = new RotationManager(generator.getAngle());

        states = new Stack<>();

        lSystem = generator.getAxiom();
    }

    //Converts each character of an L-System to a string n times (n = generationCount) according to
    //the used Generator's rules.
    private void convertLSystemByRuleWithGenerations() {
        for (int i = 0; i < generationCount; i++) {
            convertLSystemByRule();
        }
    }

    private void convertLSystemByRule() {
        StringBuilder newLSystem = new StringBuilder();

        for (int i = 0; i < lSystem.length(); i++) {
            for (int j = 0; j < rules.size(); j++) {
                if (lSystem.charAt(i) == rules.get(j).getCharToConvert()) {
                    newLSystem.append(rules.get(j).getConversion());
                    break;
                } else if (j == rules.size() - 1) {
                    newLSystem.append(lSystem.charAt(i));
                }
            }
        }
        lSystem = newLSystem.toString();

        divideLength();
    }

    private void drawLSystem() {
        if (isStochastic) drawStochasticLSystem();
        else drawDeterministicLSystem();
    }

    //Note: The characters F, G, X, Y are responsible for the exact same function.
    private void drawDeterministicLSystem() {
        for (int i = 0; i < lSystem.length(); i++) {
            switch (lSystem.charAt(i)) {
                case 'F', 'G', 'X', 'Y' -> stepAndDrawLine();
                case 'f' -> step();
                case '+' -> rotateLeft(rotAngle);
                case '-' -> rotateRight(rotAngle);
                case '[' -> saveState();
                case ']' -> restoreState();
            }
        }
    }

    private void drawStochasticLSystem() {
        for (int i = 0; i < lSystem.length(); i++) {
            switch (lSystem.charAt(i)) {
                case 'F', 'G', 'X', 'Y' -> stepAndDrawLineRandom();
                case 'f' -> step();
                case '+' -> rotateLeftRandom(rotAngle);
                case '-' -> rotateRightRandom(rotAngle);
                case '[' -> saveState();
                case ']' -> restoreState();
            }
        }
    }

    //F, G, X, Y
    private void stepAndDrawLine() {
        step();
        drawLine();
    }

    //Applies randomness to the length. Used by stochastic L-Systems.
    private void stepAndDrawLineRandom() {
        Random rnd = new Random();
        double rndLength = rnd.nextDouble(lengthDelta * 2) - lengthDelta;
        length += length * rndLength;
        step();
        drawLine();
    }

    //f
    private void step() {
        x1 = startingX;
        y1 = startingY;
        x2 = x1 + rotationManager.getCosTheta() * length;
        y2 = y1 + rotationManager.getSinTheta() * length;

        startingX = x2;
        startingY = y2;

        setMaxCoordinates();
        setMinCoordinates();
    }


    //+
    private void rotateLeft(int angle) {
        rotationManager.setAngle((rotationManager.getAngle() + angle));
    }

    //Applies randomness to the rotation angle. Used by stochastic L-Systems.
    private void rotateLeftRandom(int angle) {
        Random rnd = new Random();
        int rndAngleBonus = rnd.nextInt(rotAngleDelta * 2) - rotAngleDelta;
        rotationManager.setAngle((rotationManager.getAngle() + angle + rndAngleBonus));
    }

    //-
    private void rotateRight(int angle) {
        rotationManager.setAngle((rotationManager.getAngle() - angle));
    }

    //Applies randomness to the rotation angle. Used by stochastic L-Systems.
    private void rotateRightRandom(int angle) {
        Random rnd = new Random();
        int rndAngleBonus = rnd.nextInt(rotAngleDelta * 2) - rotAngleDelta;
        rotationManager.setAngle((rotationManager.getAngle() - angle - rndAngleBonus));
    }

    //[
    private void saveState() {
        State state = new State(startingX, startingY, x2, y2, rotationManager.getAngle(), length);
        states.push(state);
    }

    //]
    private void restoreState() {
        State state = states.pop();
        startingX = state.getX1();
        startingY = state.getY1();
        x2 = state.getX2();
        y2 = state.getY2();
        rotationManager.setAngle(state.getAngle());
        length = state.getLength();
    }

    private void divideLength() {
        length /= lengthDivisor;
    }

    private void setMaxCoordinates() {
        if (x1 > xMax) xMax = x1;
        if (y1 > yMax) yMax = y1;
        if (x2 > xMax) xMax = x2;
        if (y2 > yMax) yMax = y2;
    }

    private void setMinCoordinates() {
        if (x1 < xMin) xMin = x1;
        if (y1 < yMin) yMin = y1;
        if (x2 < xMin) xMin = x2;
        if (y2 < yMin) yMin = y2;
    }

    private void drawLine() {
        glBegin(GL_LINES);
        glColor3f(1f, 1f, 0f);

        glVertex2f(x1, y1);
        glVertex2f(x2, y2);

        glEnd();
    }
}
