package project;

import global.AbstractRenderer;
import lwjglutils.OGLTextRenderer;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import project.generators.*;

import java.util.Arrays;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Renderer extends AbstractRenderer {
    private LSystem lSystem;
    private int genCount = 0;
    private Generator generator = new StochasticBetterTree();
    private final List<Generator> generators =
            Arrays.asList(new SimpleTree(), new BetterTree(), new KochIsle(),
                    new Bush(), new HillbertCurve(), new QuadraticSnowflakeVariant(), new SierpinskiTriangle(),
                    new SnowflakeCurve(), new StochasticBush(), new StochasticBetterTree());
    private int generatorIndex = 0;

    public boolean isStop = false;


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

        glfwMouseButtonCallback = null;

        glfwCursorPosCallback = new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double x, double y) {
            }
        };

        glfwScrollCallback = new GLFWScrollCallback() {
            @Override
            public void invoke(long window, double dx, double dy) {
            }
        };

        glfwKeyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (action == GLFW_PRESS) {
                    switch (key) {
                        case GLFW_KEY_UP -> { // increment generation
                            if (genCount == generator.getMaxGen()) break;

                            glLoadIdentity();
                            lSystem = new LSystem();
                            genCount++;

                            isStop = false;
                        }
                        case GLFW_KEY_DOWN -> { // decrement generation
                            if (genCount == 0) break;

                            glLoadIdentity();
                            lSystem = new LSystem();
                            genCount--;

                            isStop = false;
                        }
                        case GLFW_KEY_RIGHT -> { //change generator type
                            glLoadIdentity();
                            lSystem = new LSystem();
                            setNextGenerator();

                            isStop = false;
                        }
                    }
                }
            }
        };
    }

    @Override
    public void init() {
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        this.textRenderer = new OGLTextRenderer(width, height);
    }

    //part of game loop
    @Override
    public void display() {
        //1:1 scale
        if (width < height) {
            glViewport(0, 0, width, width);
        } else {
            glViewport(0, 0, height, height);
        }

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        lSystem.run(genCount, generator);

        textRenderer.addStr2D(3, 20, "Vytvořeno v rámci předmětu PGRF2 na UHK. Autor: Vyacheslav Novak");

    }

    private void setNextGenerator() {
        if (generatorIndex + 1 == generators.size()) {
            generatorIndex = 0;
        } else {
            generatorIndex++;
        }
        generator = generators.get(generatorIndex);
        genCount = 0;
    }
}
