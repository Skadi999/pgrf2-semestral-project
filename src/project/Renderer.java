package project;

import global.AbstractRenderer;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import project.generators.BetterTree;
import project.generators.KochIsle;
import project.generators.SimpleTree;

import static org.lwjgl.opengl.GL11.*;

public class Renderer extends AbstractRenderer {
    private LSystem lSystem;


    public Renderer() {
        super(800, 800);
        lSystem = new LSystem();
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
            }
        };

        glfwScrollCallback = new GLFWScrollCallback() {
            @Override
            public void invoke(long window, double dx, double dy) {
                glTranslatef(-0.2275f, 0, 0);
//                glScalef(0.3f, 0.3f, 1);
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

        //1:1 scale
        if (width < height) {
            glViewport(0, 0, width, width);
        } else {
            glViewport(0, 0, height, height);
        }

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

        lSystem.run(4, new BetterTree());
    }

}
