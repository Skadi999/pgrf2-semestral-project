package project;

import global.AbstractRenderer;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;

import static org.lwjgl.opengl.GL11.*;

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

    private String axiom;
    private String lSystem;
    private int generationCount;

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

        setInitialStartingCoords();

        axiom = "F+F+F+F";
        lengthDivisor = 5;

        generationCount = 2;

        lSystem = axiom;
        convertCharsByRule();

        drawLSystem(90);
    }

    //todo: don't forget rules will be stored in a LIST
    //todo: can try doing replaceAll but maybe it will cause problems
    private void convertCharsByRule() {
        Rule ruleOne = new Rule('F', "F+F-F-FF+F+F-F");
        StringBuilder newLSystem = new StringBuilder();

        for (int i = 0; i < generationCount; i++) {
            for (int j = 0; j < lSystem.length(); j++) {
                if (lSystem.charAt(j) == ruleOne.getCharToConvert()) {
                    newLSystem.append(ruleOne.getConversion());
                } else {
                    newLSystem.append(lSystem.charAt(j));
                }
            }
            lSystem = newLSystem.toString();
            newLSystem = new StringBuilder();
            divideLength();
        }
    }

    private void setInitialStartingCoords() {
        startingX = 0.3f;
        startingY = 0.3f;
        length = 1f;
        rotationManager = new RotationManager(270);
    }

    private void drawLSystem(int angleToRotate) {
        for (int i = 0; i < lSystem.length(); i++) {
            switch (lSystem.charAt(i)) {
                case 'F' -> stepAndDrawLine();
                case 'f' -> step();
                case '+' -> rotateRight(angleToRotate);
                case '-' -> rotateLeft(angleToRotate);
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

    //+
    private void rotateRight(int angle) {
        rotationManager.setAngle((rotationManager.getAngle() + angle + 180) % 360);
    }

    //-
    private void rotateLeft(int angle) {
        rotationManager.setAngle((rotationManager.getAngle() + angle) % 360);
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
