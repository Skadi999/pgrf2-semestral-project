package project;

import project.generators.Generator;
import project.generators.StochasticGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import static org.lwjgl.opengl.GL11.*;

//Angles: 0=right, 90=up, 180=left, 270=bottom
public class LSystem {
    private float startingX;
    private float startingY;
    private float x1;
    private float y1;
    private float x2;
    private float y2;

    private float xMax;
    private float yMax;
    private float xMin;
    private float yMin;

    private float length;
    private float lengthDivisor;

    private RotationManager rotationManager;
    private int rotAngle;

    private String lSystem;
    private int generationCount;

    private List<Rule> rules = new ArrayList<>();

    private Stack<State> states = new Stack<>();

    private boolean isScaled;

    private boolean isStochastic;
    private int rotAngleDelta;
    private float lengthDelta;

    public void run(int generationCount, Generator generator) {
        resetAndDraw(generationCount, generator);

        if (!isScaled) {
            fixScaleAndPosition();

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            resetAndDraw(generationCount, generator);

            isScaled = true;
        }
    }

    private void resetAndDraw(int generationCount, Generator generator) {
        initializeLSystem(generationCount, generator);
        convertLSystemByRuleWithGenerations();

        drawLSystem();
    }

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

    //F
    private void stepAndDrawLine() {
        step();
        drawLine();
    }

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

        setMaxCoords();
        setMinCoords();
    }


    //+
    private void rotateLeft(int angle) {
        rotationManager.setAngle((rotationManager.getAngle() + angle));
    }

    private void rotateLeftRandom(int angle) {
        Random rnd = new Random();
        int rndAngleBonus = rnd.nextInt(rotAngleDelta * 2) - rotAngleDelta;
        rotationManager.setAngle((rotationManager.getAngle() + angle + rndAngleBonus));
    }

    //-
    private void rotateRight(int angle) {
        rotationManager.setAngle((rotationManager.getAngle() - angle));
    }

    private void rotateRightRandom(int angle) {
        Random rnd = new Random();
        int rndAngleBonus = rnd.nextInt(rotAngleDelta * 2) - rotAngleDelta;
        rotationManager.setAngle((rotationManager.getAngle() - angle - rndAngleBonus));
    }

    private void saveState() {
        State state = new State(startingX, startingY, x2, y2, rotationManager.getAngle(), length);
        states.push(state);
    }

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

    private void setMaxCoords() {
        if (x1 > xMax) xMax = x1;
        if (y1 > yMax) yMax = y1;
        if (x2 > xMax) xMax = x2;
        if (y2 > yMax) yMax = y2;
    }

    private void setMinCoords() {
        if (x1 < xMin) xMin = x1;
        if (y1 < yMin) yMin = y1;
        if (x2 < xMin) xMin = x2;
        if (y2 < yMin) yMin = y2;
    }

    private void drawLine() {
        glBegin(GL_LINES);
        glColor3f(0f, 1f, 0f);

        glVertex2f(x1, y1);
        glVertex2f(x2, y2);

        glEnd();
    }
}
