package project;

import project.generators.Generator;

import java.util.ArrayList;
import java.util.List;
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

    private float shapeWidth;
    private float shapeHeight;

    private Generator generator;

    private boolean isScaled;

    public void run(int generationCount, Generator generator) {
        initializeLSystem(generationCount, generator);
        convertLSystemByRuleWithGenerations();

        drawLSystem();
        drawBoundsBox();

        if (!isScaled) {
            fixScaleAndPosition();
            isScaled = true;
        }
//    drawLineDebug();

    }

    //for debugging
    private void drawLineDebug() {
        glBegin(GL_LINES);
        glColor3f(0f, 0f, 1f);

        glVertex2f(0.5f, 0.8f);
        glVertex2f(0.7f, 0.8f);

        glVertex2f(0.5f, 0.8f);
        glVertex2f(0.5f, 0.6f);

        glVertex2f(0.5f, 0.6f);
        glVertex2f(0.7f, 0.6f);

        glVertex2f(0.7f, 0.8f);
        glVertex2f(0.7f, 0.6f);

        glEnd();
    }

    //unused
    private void traceShapeAndReset() {
        //trace
        traceLSystem();

        //set width and height based on min and max coords
        shapeWidth = Math.abs(xMax - xMin);
        shapeHeight = Math.abs(yMax - yMin);

        //reset shape parameters
        initializeLSystem(generationCount, generator);
        convertLSystemByRuleWithGenerations();
    }

    //Does not UPSIZE small shapes, only centers and, if a shape is too big, downscales it.
    //todo: implement upscaling & refactor
    private void fixScaleAndPosition() {
        shapeWidth = Math.abs(xMax - xMin);
        shapeHeight = Math.abs(yMax - yMin);

        if (shapeWidth < 1.8f && shapeHeight < 1.8f) {
            float xCenter = xMin + shapeWidth / 2;
            float yCenter = yMin + shapeHeight / 2;
            float distanceToWindowXCenter = 0 - xCenter;
            float distanceToWindowYCenter = 0 - yCenter;
            glTranslatef(distanceToWindowXCenter, distanceToWindowYCenter, 1);
        }

        else if (shapeWidth > 1.8f || shapeHeight > 1.8f) {
            float widthScalingFactor;
            float heightScalingFactor;
            float totalScalingFactor = 1f;

            if (shapeWidth > 1.8f) {
                widthScalingFactor = 1 / (shapeWidth / 1.8f);
                totalScalingFactor *= widthScalingFactor;

                if (shapeHeight * widthScalingFactor > 1.8f) {
                    heightScalingFactor = 1 / (shapeHeight / 1.8f);
                    totalScalingFactor *= heightScalingFactor;
                }
                glScalef(totalScalingFactor, totalScalingFactor, 1);
            } else {
                heightScalingFactor = 1 / (shapeHeight / 1.8f);
                totalScalingFactor *= heightScalingFactor;

                if (shapeWidth * heightScalingFactor > 1.8f) {
                    widthScalingFactor = 1 / (shapeWidth / 1.8f);
                    totalScalingFactor *= widthScalingFactor;
                }
                glScalef(totalScalingFactor, totalScalingFactor, 1);
            }
            //After scaling, we must translate it back to the center again.
            float xMinDistanceToCenter = Math.abs(0 - xMin);
            float xMaxDistanceToCenter = Math.abs(0 - xMax);
            float yMinDistanceToCenter = Math.abs(0 - yMin);
            float yMaxDistanceToCenter = Math.abs(0 - yMax);

            float scaledXMin;
            float scaledXMax;
            float scaledYMin;
            float scaledYMax;

            scaledXMin = (xMin < 0) ? (xMin + xMinDistanceToCenter * (1 - totalScalingFactor)) :
                    (xMin - xMinDistanceToCenter * (1 - totalScalingFactor));
            scaledXMax = (xMax < 0) ? (xMax + xMaxDistanceToCenter * (1 - totalScalingFactor)) :
                    (xMax - xMaxDistanceToCenter * (1 - totalScalingFactor));
            scaledYMin = (yMin < 0) ? (yMin + yMinDistanceToCenter * (1 - totalScalingFactor)) :
                    (yMin - yMinDistanceToCenter * (1 - totalScalingFactor));
            scaledYMax = (yMax < 0) ? (yMax + yMaxDistanceToCenter * (1 - totalScalingFactor)) :
                    (yMax - yMaxDistanceToCenter * (1 - totalScalingFactor));

            float scaledXCenter = (scaledXMin + scaledXMax) / 2;
            float scaledYCenter = (scaledYMin + scaledYMax) / 2;

            glTranslatef((-scaledXCenter)/totalScalingFactor, (-scaledYCenter)/totalScalingFactor, 0);
        }
    }

    private void initializeLSystem(int generationCount, Generator generator) {
        startingX = generator.getStartingX();
        startingY = generator.getStartingY();
        xMax = startingX;
        yMax = startingY;
        length = generator.getLength();
        lengthDivisor = generator.getLengthDivisor();
        rules = generator.getRules();
        rotAngle = generator.getRotAngle();
        this.generator = generator;
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


    //Same as drawLSystem(), but F and G function same as f. needed to find out min/max coords for translations
    //unused
    private void traceLSystem() {
        for (int i = 0; i < lSystem.length(); i++) {
            switch (lSystem.charAt(i)) {
                case 'f', 'F', 'G' -> step();
                case '+' -> rotateLeft(rotAngle);
                case '-' -> rotateRight(rotAngle);
                case '[' -> saveState();
                case ']' -> restoreState();
            }
        }
    }

    //todo stochastic L-systems
    private void drawLSystem() {
        for (int i = 0; i < lSystem.length(); i++) {
            switch (lSystem.charAt(i)) {
                case 'F', 'G' -> stepAndDrawLine();
                case 'f' -> step();
                case '+' -> rotateLeft(rotAngle);
                case '-' -> rotateRight(rotAngle);
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

    //-
    private void rotateRight(int angle) {
        rotationManager.setAngle((rotationManager.getAngle() - angle));
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

    //Draws a bounds box based on x and y min and max coordinates. Used for debugging. todo: delete this method later.
    private void drawBoundsBox() {
        glBegin(GL_LINES);
        glColor3f(1f, 0f, 0f);

        glVertex2f(xMin, yMin);
        glVertex2f(xMax, yMin);

        glVertex2f(xMin, yMin);
        glVertex2f(xMin, yMax);

        glVertex2f(xMin, yMax);
        glVertex2f(xMax, yMax);

        glVertex2f(xMax, yMax);
        glVertex2f(xMax, yMin);

        glEnd();
    }
}
