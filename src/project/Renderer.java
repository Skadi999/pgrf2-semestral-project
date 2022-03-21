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
    private int angle;

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

        stepAndDrawLine();

        rotateLeft(90);
        stepAndDrawLine();

        rotateLeft(90);
        stepAndDrawLine();

        rotateLeft(90);
        stepAndDrawLine();
    }

    private void setInitialStartingCoords() {
        startingX = 0.7f;
        startingY = -0.7f;
        length = 1.4f;
        angle = 90;
    }

    //F
    private void stepAndDrawLine() {
        step();
        drawLine();
    }

    //f
    private void step() {
        double angleToRadians = angle * (Math.PI / 180);

        double cosTheta = Math.cos(angleToRadians);
        double sinTheta = Math.sin(angleToRadians);

        x1 = startingX;
        y1 = startingY;
        x2 = (float) (x1 + (cosTheta * length));
        y2 = (float) (y1 + (sinTheta * length));

        startingX = x2;
        startingY = y2;
    }

    private void drawLine() {
        glBegin(GL_LINES);
        glColor3f(0f, 1f, 0f);

        glVertex2f(x1, y1);
        glVertex2f(x2, y2);

        glEnd();
    }

    //+
    private void rotateLeft(int angle) {
        this.angle += angle;
        this.angle = this.angle % 360;
    }

    //-
    private void rotateRight(int angle) {
        this.angle += angle + 180;
        this.angle = this.angle % 360;
    }
}
