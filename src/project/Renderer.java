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
    private Generator generator = new SimpleTree();
    //a list of available generators, you can switch the displayed generator using left and right arrow keys.
    private final List<Generator> generators =
            Arrays.asList(new SimpleTree(), new BetterTree(), new KochIsle(),
                    new Bush(), new HillbertCurve(), new QuadraticSnowflakeVariant(), new SierpinskiTriangle(),
                    new SnowflakeCurve(), new StochasticBush(), new StochasticBetterTree());

    private int generatorIndex = 0;
    /*
    A boolean for stopping/resuming the loop when necessary.
    The rendering does not constantly refresh itself in the loop, because it's not necessary.
    The rendering code (in LwjglWindow) IS located inside the loop, but is controlled by this variable
    This is necessary because otherwise stochastic L-Systems do not render correctly. It also improves performance,
    and does not cause any problems because the L-Systems are static images, there is no movement involved.
    See LwjglWindow to see how the variable controls the rendering code in the loop
    */
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
                        case GLFW_KEY_UP -> { // increment generation number and redraw
                            if (genCount == generator.getMaxGen()) break;

                            glLoadIdentity();
                            lSystem = new LSystem();
                            genCount++;

                            isStop = false;
                        }
                        case GLFW_KEY_DOWN -> { // decrement generation number and redraw
                            if (genCount == 0) break;

                            glLoadIdentity();
                            lSystem = new LSystem();
                            genCount--;

                            isStop = false;
                        }
                        case GLFW_KEY_LEFT -> { //change generator type to the previous one (in the "generators" variable)
                            glLoadIdentity();
                            lSystem = new LSystem();
                            setPreviousGenerator();

                            isStop = false;
                        }
                        case GLFW_KEY_RIGHT -> { //change generator type to the next one (in the "generators" variable)
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

    //see LwjglWindow loop
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

        textRenderer.addStr2D(3, 20, "Semestr??ln?? Projekt L-Syst??my: Vytvo??eno v r??mci p??edm??tu PGRF2 na UHK. " +
                "Autor: Vyacheslav Novak Posledn?? ??prava: 25.04.2022");
        textRenderer.addStr2D(3, 780, "N??pov??da: Left/Right Arrow: P??edchoz??/Dal???? Generator. " +
                "Up/Down Arrow: Inkrementace/Dekrementace Generace ");

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

    private void setPreviousGenerator() {
        if (generatorIndex == 0) {
            generatorIndex = generators.size() - 1;
        } else {
            generatorIndex--;
        }
        generator = generators.get(generatorIndex);
        genCount = 0;
    }
}
