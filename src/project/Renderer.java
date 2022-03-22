package project;

import global.AbstractRenderer;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import project.patterns.KochIsle;
import project.patterns.Pattern;
import project.patterns.SimpleTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static org.lwjgl.opengl.GL11.*;

//todo refactor L system into its own class.
//Angles: 0=right, 90=up, 180=left, 270=bottom
public class Renderer extends AbstractRenderer {

    private float startingX;
    private float startingY;
    private float x1;
    private float y1;
    private float x2;
    private float y2;

    private float length;
    private int lengthDivisor;

    private RotationManager rotationManager;
    private int rotAngle;

    private String lSystem;
    private int generationCount;

    private List<Rule> rules = new ArrayList<>();

    private final Stack<State> states = new Stack<>();

    public Renderer() {
        super(800, 800);

        glfwWindowSizeCallback = new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long window, int w, int h) {
                if (w > 0 && h > 0) {
                    width = w;
                    height = h;
                }
            }
        };

        glfwMouseButtonCallback = null; //glfwMouseButtonCallback do nothing

        glfwCursorPosCallback = new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double x, double y) {
//                System.out.println("glfwCursorPosCallback");
            }
        };

        glfwScrollCallback = new GLFWScrollCallback() {
            @Override
            public void invoke(long window, double dx, double dy) {
                //do nothing
            }
        };
    }

    @Override
    public void init() {
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
    }

    @Override
    public void display() {
        glViewport(0, 0, width, height);

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

        initializeLSystem();

        convertLSystemByRuleWithGenerations();

        drawLSystem();
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

    private void initializeLSystem() {
        Pattern pattern = new SimpleTree();
//        Pattern pattern = new KochIsle();

        startingX = pattern.getStartingX();
        startingY = pattern.getStartingY();
        length = pattern.getLength();
        lengthDivisor = pattern.getLengthDivisor();
        rules = pattern.getRules();
        rotAngle = pattern.getRotAngle();

        generationCount = 4;
        rotationManager = new RotationManager(pattern.getAngle());

        lSystem = pattern.getAxiom();
    }

    private void drawLSystem() {
        for (int i = 0; i < lSystem.length(); i++) {
            switch (lSystem.charAt(i)) {
                case 'F', 'G' -> stepAndDrawLine();
                case 'f' -> step();
                case '+' -> rotateRight(rotAngle);
                case '-' -> rotateLeft(rotAngle);
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
