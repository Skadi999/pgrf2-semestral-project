package project;

import project.patterns.Pattern;

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

    private float length;
    private float lengthDivisor;

    private RotationManager rotationManager;
    private int rotAngle;

    private String lSystem;
    private int generationCount;

    private List<Rule> rules = new ArrayList<>();

    private final Stack<State> states = new Stack<>();


    public void run(int generationCount, Pattern pattern) {
        initializeLSystem(generationCount, pattern);
        convertLSystemByRuleWithGenerations();
        drawLSystem();
    }

    private void initializeLSystem(int generationCount, Pattern pattern) {
        startingX = pattern.getStartingX();
        startingY = pattern.getStartingY();
        length = pattern.getLength();
        lengthDivisor = pattern.getLengthDivisor();
        rules = pattern.getRules();
        rotAngle = pattern.getRotAngle();

        this.generationCount = generationCount;
        rotationManager = new RotationManager(pattern.getAngle());

        lSystem = pattern.getAxiom();
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

    //todo consider changing + to rotateLeft
    //todo check why BetterTree is affected so much by lengthDivisor
    //todo rewrite in modern OpenGL
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
    }


    //-
    private void rotateLeft(int angle) {
        rotationManager.setAngle((rotationManager.getAngle() + angle));
    }

    //+
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

    private void drawLine() {
        glBegin(GL_LINES);
        glColor3f(0f, 1f, 0f);

        glVertex2f(x1, y1);
        glVertex2f(x2, y2);

        glEnd();
    }
}
